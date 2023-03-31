package pers.hence.memapplication.controller;

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
@RestController
@RequestMapping("/square")
public class UserBlogController {

    @Autowired
    private UserBlogService userBlogService;

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

    @GetMapping("/blogs")
    public Result<?> getBlogs(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return Result.fail("参数空");
        }
        List<UserBlogVO> userBlogList = userBlogService.getUserBlogList(userId, request);
        return Result.sucess("ok", userBlogList);
    }
}
