package pers.hence.memapplication.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pers.hence.memapplication.config.OSSConfig;
import pers.hence.memapplication.constant.MemType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/28 12:19
 * @description OSS工具类
 */
@Component
public class OSSUtil {

    private final static String PDF_TYPE = "pdf";
    private final static String WORD_TYPE = "docx;doc";
    private final static String TXT_TYPE = "txt";
    private final static String IMAGE_TYPE = "png;jpg;jpeg";
    private final static String MUSIC_TYPE = "mp3";

    @Autowired
    private OSSConfig ossConfig;

    /**
     * 获取OSS实例
     * @return OSSClient
     */
    private OSS getOSSClient() {
        return new OSSClientBuilder()
                .build(ossConfig.getEndPoint(),
                        ossConfig.getAccessKey(),
                        ossConfig.getAccessSecret());
    }

    /**
     * 根据文件后缀名生成对应的元信息
     * @param suffix 文件后缀
     * @return ObjectMeta
     */
    public ObjectMetadata getFileMetaInfo(String suffix) {
        if (null == suffix) {
            return null;
        }
        if (StringUtils.isBlank(suffix)) {
            return null;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        if (TXT_TYPE.equals(suffix)) {
            metadata.setContentType("text/plain");
        }else if (PDF_TYPE.equals(suffix)) {
            metadata.setContentType("application/pdf");
        }else if (WORD_TYPE.contains(suffix)) {
            metadata.setContentType("application/msword");
        }else if (IMAGE_TYPE.contains(suffix)) {
            metadata.setContentType("image/" + suffix);
        }else if (MUSIC_TYPE.equals(suffix)) {
            metadata.setContentType("audio/mp3");
        }else {
            return null;
        }
        return metadata;
    }

    /**
     * 获取文件上传路径
     * @param suffix 后缀名
     * @return OSS上传路径
     */
    private String getFilePath(String suffix) {
        if (null == suffix) {
            return null;
        }
        if (StringUtils.isBlank(suffix)) {
            return null;
        }

        if (TXT_TYPE.equals(suffix)) {
            return MemType.TXT.getPath();
        }else if (PDF_TYPE.equals(suffix)) {
            return MemType.PDF.getPath();
        }else if (WORD_TYPE.contains(suffix)) {
            return MemType.WORD.getPath();
        }else if (IMAGE_TYPE.contains(suffix)) {
            return MemType.IMAGE.getPath();
        }else if (MUSIC_TYPE.equals(suffix)) {
            return MemType.MUSIC.getPath();
        }else {
            return null;
        }
    }

    /**
     * 上传文件
     * @param file 文件
     * @return 文件存储路径
     * @throws IOException IO异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 1. 获取OSSClient实例
        OSS client = getOSSClient();
        // 2. 构造上传路径
        String suffix = FileNameUtil.extName(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." +  suffix;
        String uploadPath = getFilePath(suffix) + "/" + DateUtil.today() + "/" + fileName;
        // 3. 获取元信息
        ObjectMetadata metaInfo = getFileMetaInfo(suffix);
        if (null == metaInfo) {
            return null;
        }
        // 4. 上传
        byte[] content = file.getBytes();
        PutObjectRequest request =
                new PutObjectRequest(ossConfig.getBucketName(),
                        uploadPath,
                        new ByteArrayInputStream(content));
        request.setMetadata(metaInfo);
        client.putObject(request);
        client.shutdown();
        return ossConfig.getBucketUrl() + "/" + uploadPath;
    }
}
