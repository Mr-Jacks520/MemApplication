package pers.hence.memapplication.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.constant.UserConstant;
import pers.hence.memapplication.dao.MemContentDao;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.vo.UserVO;
import pers.hence.memapplication.service.MemContentService;
import pers.hence.memapplication.util.OSSUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

import static pers.hence.memapplication.constant.GlobalConstant.HTTP_PREFIX;
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
    private OSSUtil ossUtil;

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
        MemContent memContent = MemContent.builder()
                .title(fileName)
                .type(OSSUtil.getFileType(suffix))
                .size(fileSize)
                .storagePath(HTTP_PREFIX + path)
                // 复习时间
                .userId(currentUser)
                .build();
        // 5. 保存至数据库
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
}
