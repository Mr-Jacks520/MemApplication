package pers.hence.memapplication.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.service.LeadingPageService;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:48
 * @description 引导页控制层
 */
@Api(tags = "引导页模块")
@RestController
@RequestMapping("/leading")
public class LeadingPageController {

    @Autowired
    private LeadingPageService leadingPageService;

    /**
     * 获取引导页信息
     * @return result
     */
    @GetMapping("/get")
    public Result<?> getLeadingPage() {
        return Result.sucess("获取成功", leadingPageService.getLeadingPageList());
    }
}
