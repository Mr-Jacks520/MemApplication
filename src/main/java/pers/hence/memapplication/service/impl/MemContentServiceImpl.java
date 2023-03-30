package pers.hence.memapplication.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.constant.UserConstant;
import pers.hence.memapplication.dao.MemContentDao;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.vo.HistoryFileVO;
import pers.hence.memapplication.model.vo.MemContentVO;
import pers.hence.memapplication.model.vo.UserVO;
import pers.hence.memapplication.service.MemContentService;
import pers.hence.memapplication.util.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pers.hence.memapplication.constant.GlobalConstant.HTTP_PREFIX;
import static pers.hence.memapplication.constant.GlobalConstant.PREJOB_PREFIX;
import static pers.hence.memapplication.constant.RedisConstant.HISTORY_FILE_PREFIX;
import static pers.hence.memapplication.constant.StatusCode.*;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/12 13:53
 * @description 记忆内容服务业务层
 */

@Service
@Slf4j
public class MemContentServiceImpl extends ServiceImpl<MemContentDao, MemContent> implements MemContentService {

    @Autowired
    private MemContentDao memContentDao;

    @Autowired
    private OSSUtil ossUtil;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 文件上传
     * @param file 文件
     * @param request request
     * @return 是否成功
     */
    @Override
    public int uploadFile(MultipartFile file, HttpServletRequest request) {
        // 1. 校验
        if (null == file) {
            throw new BusinessException(ERROR, "文件不存在");
        }
        // 2. 获取当前用户
        Integer currentUser = getCurrentUser(request);
        // 3. 上传
        String path;
        try {
            path = ossUtil.uploadFile(file);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException(INTERNAL_ERROR, "上传失败");
        }
        // 4. 构造数据库对象
        String fileName = FileNameUtil.mainName(file.getOriginalFilename());
        String suffix = FileNameUtil.extName(file.getOriginalFilename());
        String fileSize = BigDecimal.valueOf((double) file.getSize() / 1024 / 1024)
                .divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP) + "M";
        // 5. 生成复习时间表
        String reviewTimes = AlgorithmUtils.ebenhausCurve(DateUtil.today());
        MemContent memContent = MemContent.builder()
                .title(fileName)
                .type(OSSUtil.getFileType(suffix))
                .size(fileSize)
                .storagePath(HTTP_PREFIX + path)
                // 复习时间表
                .reviewTimes(reviewTimes)
                // 下次复习时间
                .nextReview(AlgorithmUtils.getNextReviewTime(DateUtil.today()))
                .userId(currentUser)
                .build();
        // 6. 保存至数据库
        boolean isSave = this.save(memContent);
        if (!isSave) {
            throw new BusinessException(INTERNAL_ERROR, "文件保存失败");
        }
        return 1;
    }

    /**
     * 获取当前登录用户ID
     * @param request request
     * @return 当前登录用户ID
     */
    @Override
    public Integer getCurrentUser(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        Object user = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (null == user) {
            throw new BusinessException(NOT_AUTH, "未被认证");
        }
        UserVO currentUser = (UserVO) user;
        return currentUser.getId();
    }

    /**
     * 用户请求当天复习内容:from redis
     * @param userId 请求用户ID
     * @param request request
     * @return 记忆内容列表
     */
    @Override
    public List<MemContentVO> getReviewContent(Integer userId, HttpServletRequest request) {
        // 1. 校验
        check(userId, request);
        // 2. 查询redis
        Map<String, Object> cached = redisUtil.getHashEntries(PREJOB_PREFIX);
        if (cached.isEmpty()) {
            return null;
        }
        Gson gson = new Gson();
        String cachedList = (String) cached.getOrDefault(String.valueOf(userId), "");
        // 3.  反序列化
        List<MemContentVO> memContents = gson.fromJson(cachedList, new TypeToken<List<MemContentVO>>() {
        }.getType());
        if (memContents.isEmpty()) {
            return null;
        }
        return BeanCopyUtils.copyList(memContents, MemContentVO.class);
    }

    /**
     * 用户请求查看历史上传文件
     * @param userId 请求用户ID
     * @param request request
     * @return 历史文件列表
     */
    @Override
    public List<HistoryFileVO> getHistoryFiles(Integer userId, HttpServletRequest request) {
        // 1. 校验
        check(userId, request);
        // 2. 查询redis
        List<HistoryFileVO> historyFiles;
        Gson gson = new Gson();
        String cached = (String) redisUtil.get(HISTORY_FILE_PREFIX + userId);
        if (cached != null && !cached.isEmpty()) {
            // =============缓存命中==============
            // 1. 反序列化
            return gson.fromJson(cached, new TypeToken<List<HistoryFileVO>>(){}.getType());
        }else {
            // =============缓存未命中,走数据库查询并缓存至redis=================
            // 1. 构造查询条件
            QueryWrapper<MemContent> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_delete", 0).eq("user_id", userId).orderByDesc("created_time");
            // 2. 查询
            List<MemContent> memContents = memContentDao.selectList(queryWrapper);
            // 3. 序列化
            historyFiles = BeanCopyUtils.copyList(memContents, HistoryFileVO.class);
            String value = gson.toJson(historyFiles);
            // 4. 缓存: 5 min
            redisUtil.set(HISTORY_FILE_PREFIX + userId, value, 60 * 5);
        }
        return historyFiles;
    }

    /**
     * 用户模糊搜索历史文件
      * @param keywords 搜索关键词
     * @param userId 请求搜索用户ID
     * @param request request
     * @return 搜索结果
     */
    @Override
    public List<HistoryFileVO> doHistoryFileSearch(String keywords, Integer userId, HttpServletRequest request) {
        // 1. 校验
        check(userId, request);
        if ("".equals(keywords)) {
            return null;
        }
        // 2. 构造查询条件
        QueryWrapper<MemContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keywords).eq("is_delete", 0).orderByDesc("created_time");
        // 3. 数据库查询
        List<MemContent> memContents = memContentDao.selectList(queryWrapper);
        return BeanCopyUtils.copyList(memContents, HistoryFileVO.class);
    }

    /**
     * 校验
     * @param userId 用户ID
     * @param request request
     */
    private void check(Integer userId, HttpServletRequest request) {
        if (!UserUtils.isLogin(userId, request)) {
            throw new BusinessException(NOT_AUTH, "未登录");
        }
        if (!UserUtils.isAuth(userId, request)) {
            throw new BusinessException(REFUSE, "拒绝访问");
        }
    }

    /**
     * 用户完成今日复习内容，删除Redis中对应复习内容，更新下次复习时间
     * @param userId 请求用户ID
     * @param completeId 完成的复习内容
     * @param request request
     */
    @Override
    public void updateNextReviewTime(Integer userId, Integer completeId, HttpServletRequest request) {
        // 1. 校验
        check(userId, request);
        if (null == completeId) {
            throw new BusinessException(ERROR, "参数空");
        }
        // 2. 删除Redis中的内容
        Map<String, Object> preCached = redisUtil.getHashEntries(PREJOB_PREFIX);
        if (preCached.isEmpty()) {
            return;
        }
        String cached = (String) preCached.get(String.valueOf(userId));
        // 3. 反序列化
        Gson gson = new Gson();
        List<MemContentVO> memContents = gson.fromJson(cached, new TypeToken<ArrayList<MemContentVO>>() {
        }.getType());
        // TODO: 删除的另一种方案:链表删除
        List<MemContentVO> filter = memContents.stream()
                .filter(memContent -> !memContent.getId().equals(completeId))
                .collect(Collectors.toList());
        if (filter.isEmpty()) {
            // 4. 为空则从Redis移除
            redisUtil.delete(PREJOB_PREFIX, String.valueOf(userId));
        }
        // 5. 更新下次复习时间
        MemContent oldMemContent = memContentDao.selectById(userId);
        String reviewTimes = oldMemContent.getReviewTimes();
        String nextReview = oldMemContent.getNextReview();
        // 6. 反序列化
        List<String> timePoints = gson.fromJson(reviewTimes, new TypeToken<List<String>>() {
        }.getType());
        // 7. 获取下次复习时间下标
        int i = timePoints.indexOf(nextReview) + 1;
        if (i > timePoints.size()) {
            // 大于时间范围,则设置为complete
            oldMemContent.setIsComplete(1);
        }else {
            oldMemContent.setNextReview(timePoints.get(i));
        }
        // 8. 更新
        memContentDao.updateById(oldMemContent);
    }
}
