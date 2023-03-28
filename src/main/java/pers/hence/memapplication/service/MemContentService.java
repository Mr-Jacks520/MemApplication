package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author https://github.com/Mr-Jacks520
 * @Date 2023/3/12 13:52
 * @Description 记忆内容父接口
 */

public interface MemContentService extends IService<MemContent> {

    /**
     * 上传文件
     * @param file 文件
     * @param request request
     * @return 是否成功
     */
    int uploadFile(MultipartFile file, HttpServletRequest request);

    /**
     * 获取当前登录用户ID
     * @param request request
     * @return 当前登录用户ID
     */
    Integer getCurrentUser(HttpServletRequest request);
}
