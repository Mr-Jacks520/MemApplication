package pers.hence.memapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.model.vo.Result;
import pers.hence.memapplication.service.MemContentService;

import javax.servlet.http.HttpServletRequest;

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
}
