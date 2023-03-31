package pers.hence.memapplication.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hence.memapplication.model.request.FeedBackRequest;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.service.UserFeedBackService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/30 14:50
 * @description 帮助界面控制层
 */
@Api(tags = "帮助中心模块")
@RestController
@RequestMapping("/help")
public class HelpCenterController {

    @Autowired
    private UserFeedBackService userFeedBackService;

    /**
     * 用户反馈
     * @param feedBackRequest request
     * @param request request
     * @return null
     */
    @PostMapping("/feedback")
    public Result<?> feedback(@RequestBody FeedBackRequest feedBackRequest, HttpServletRequest request) {
        // 1. 校验
        if (null == feedBackRequest) {
            return Result.fail("参数为空");
        }
        userFeedBackService.saveFeedback(feedBackRequest.getUserId(), feedBackRequest.getContent(), request);
        return Result.create();
    }
}
