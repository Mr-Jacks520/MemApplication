package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.hence.memapplication.model.entity.UserBlog;
import pers.hence.memapplication.model.vo.UserBlogVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 13:32
 * @description 用户帖子业务接口层
 */
public interface UserBlogService extends IService<UserBlog> {

    /**
     * 用户发布帖子
     * @param userId 用户ID
     * @param content 内容:富文本
     * @param request request
     */
    void saveBlog(Integer userId, String content, HttpServletRequest request);

    /**
     * 用户查看所有的帖子
     * @param userId 用户ID
     * @param request request
     * @return 帖子脱敏列表
     */
    List<UserBlogVO> getUserBlogList(Integer userId, HttpServletRequest request);

}
