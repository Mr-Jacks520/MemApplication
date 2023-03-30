package pers.hence.memapplication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.vo.HistoryFileVO;
import pers.hence.memapplication.model.vo.MemContentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/12 13:52
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

    /**
     *
     * @param userId 请求用户ID
     * @param request request
     * @return 当天记忆内容列表
     */
    List<MemContentVO> getReviewContent(Integer userId, HttpServletRequest request);

    /**
     * 用户请求查看历史上传文件
     * @param userId 请求用户ID
     * @param request request
     * @return 历史文件列表
     */
    List<HistoryFileVO> getHistoryFiles(Integer userId, HttpServletRequest request);

    /**
     *
     * @param keywords 搜索关键词
     * @param userId 请求搜索用户ID
     * @param request request
     * @return 搜索结果列表
     */
    List<HistoryFileVO> doHistoryFileSearch(String keywords, Integer userId, HttpServletRequest request);

    /**
     *  @param userId 请求用户ID
     * @param completeId 完成的复习内容
     * @param request request
     */
    void updateNextReviewTime(Integer userId, Integer completeId, HttpServletRequest request);
}
