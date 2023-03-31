package pers.hence.memapplication.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.hence.memapplication.model.request.BlogPublishRequest;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.model.vo.UserBlogVO;
import pers.hence.memapplication.service.UserBlogService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 21:21
 * @description 广场控制层
 */
@Api(tags = "广场模块")
@RestController
@RequestMapping("/square")
public class UserBlogController {

    @Autowired
    private UserBlogService userBlogService;

    /**
     * 发布帖子
     * @param publishRequest 请求体
     * @param request request
     * @return return
     */
    @PostMapping("/publish")
    public Result<?> publish(@RequestBody BlogPublishRequest publishRequest, HttpServletRequest request) {
        if (null == publishRequest) {
            return Result.fail("参数空");
        }
        if ("".equals(publishRequest.getContent())) {
            return Result.fail("参数空");
        }
        userBlogService.saveBlog(publishRequest.getUserId(), publishRequest.getContent(), request);
        return Result.create();
    }

    /**
     * 获取所有的帖子
     * @param userId 用户请求ID
     * @param request request
     * @return 帖子列表
     */
    @GetMapping("/blogs")
    public Result<?> getBlogs(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return Result.fail("参数空");
        }
        List<UserBlogVO> userBlogList = userBlogService.getUserBlogList(userId, request);
        return Result.sucess("ok", userBlogList);
    }
}
