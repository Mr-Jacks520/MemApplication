package pers.hence.memapplication.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.hence.memapplication.dao.UserBlogDao;
import pers.hence.memapplication.dao.UserDao;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.model.entity.UserBlog;
import pers.hence.memapplication.model.vo.UserBlogVO;
import pers.hence.memapplication.service.UserBlogService;
import pers.hence.memapplication.util.UserUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static pers.hence.memapplication.constant.StatusCode.*;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:34
 * @description 用户帖子业务实现层
 */
@Service
public class UserBlogServiceImpl extends ServiceImpl<UserBlogDao, UserBlog> implements UserBlogService {

    @Autowired
    private UserBlogDao userBlogDao;

    @Autowired
    private UserDao userDao;

    /**
     * 发布帖子
     * @param userId 用户ID
     * @param content 内容:富文本
     * @param request request
     */
    @Override
    public void saveBlog(Integer userId, String content, HttpServletRequest request) {
        // 1. 校验
        if (!UserUtils.isLogin(userId, request)) {
            throw new BusinessException(NOT_AUTH, "未登录");
        }
        if (!UserUtils.isAuth(userId, request)) {
            throw new BusinessException(REFUSE, "拒绝访问");
        }
        if ("".equals(content)) {
            throw new BusinessException(ERROR, "参数错误");
        }
        // 2. 构建
        UserBlog userBlog = UserBlog.builder().content(content).userId(userId).build();
        // 3. 保存
        this.save(userBlog);
    }

    /**
     * 获取帖子列表
     * @param userId 用户ID
     * @param request request
     * @return 列表
     */
    @Override
    public List<UserBlogVO> getUserBlogList(Integer userId, HttpServletRequest request) {
        // 1. 校验
        if (!UserUtils.isLogin(userId, request)) {
            throw new BusinessException(NOT_AUTH, "未登录");
        }
        // 2. 构建查询条件
        QueryWrapper<UserBlog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0).orderByDesc("created_time");
        // 3. 查询脱敏
        List<UserBlog> userBlogs = userBlogDao.selectList(queryWrapper);
        List<UserBlogVO> userBlogVos = new ArrayList<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        // 4. 上传用户查询
        for (UserBlog userBlog : userBlogs) {
            userQueryWrapper.clear();
            userQueryWrapper
                    .eq("id", userBlog.getUserId())
                    .eq("is_delete", 0)
                    .select("user_name", "user_avatar");
            User user = userDao.selectOne(userQueryWrapper);
            UserBlogVO userBlogVO = UserBlogVO.builder()
                    .userName(user.getUserName())
                    .userAvatar(user.getUserAvatar())
                    .createdTime(DateUtil.format(userBlog.getCreatedTime(), "yyyy-MM-dd"))
                    .id(userBlog.getId())
                    .content(userBlog.getContent())
                    .thumbs(userBlog.getThumbs()).build();
            userBlogVos.add(userBlogVO);
        }
        return  userBlogVos;
    }
}
