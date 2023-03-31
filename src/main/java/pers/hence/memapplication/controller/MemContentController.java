package pers.hence.memapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.model.request.MemContentRequest;
import pers.hence.memapplication.model.vo.HistoryFileVO;
import pers.hence.memapplication.model.vo.MemContentVO;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.service.MemContentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/28 15:08
 * @description 记忆内容控制层
 */
@RestController
@RequestMapping("/memcontent")
public class MemContentController {

    @Autowired
    private MemContentService memContentService;

    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return Result.fail("文件不存在");
        }
        int ret = memContentService.uploadFile(file, request);
        return Result.sucess("上传成功", ret);
    }

    /**
     * 获取今日记忆内容
     * @param userId 请求用户ID
     * @param request request
     * @return 统一结果返回
     */
    @GetMapping("/content")
    public Result<?> getMemContent(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return Result.fail("参数空");
        }
        List<MemContentVO> reviewContent = memContentService.getReviewContent(userId, request);
        return Result.sucess("ok", reviewContent);
    }

    /**
     * 获取历史上传文件
     * @param userId 请求用户ID
     * @param request request
     * @return 历史文件列表
     */
    @GetMapping("/history")
    public Result<?> getHistoryFiles(Integer userId, HttpServletRequest request) {
        if (null == userId) {
            return Result.fail("参数空");
        }
        List<HistoryFileVO> historyFiles = memContentService.getHistoryFiles(userId, request);
        return Result.sucess("ok", historyFiles);
    }

    /**
     * 搜索历史上传文件
     * @param memContentRequest 请求体
     * @param request request
     * @return 历史文件列表
     */
    @GetMapping("/search")
    public Result<?> doSearch(MemContentRequest memContentRequest, HttpServletRequest request) {
        if (null == memContentRequest) {
            return Result.fail("参数空");
        }
        if ("".equals(memContentRequest.getKeywords())) {
            return Result.fail("参数空");
        }
        List<HistoryFileVO> historyFileSearch =
                memContentService.doHistoryFileSearch(
                        memContentRequest.getKeywords(),
                        memContentRequest.getUserId(),
                        request);
        return Result.sucess("ok", historyFileSearch);
    }

    /**
     * 完成复习内容更新
     * @param memContentRequest 请求体
     * @param request request
     * @return 统一结果返回
     */
    @PutMapping("/complete")
    public Result<?> complete(@RequestBody MemContentRequest memContentRequest, HttpServletRequest request) {
        if (null == memContentRequest) {
            return Result.fail("参数空");
        }
        memContentService.updateNextReviewTime(memContentRequest.getUserId(), memContentRequest.getCompleteId(), request);
        return Result.create();
    }
}
