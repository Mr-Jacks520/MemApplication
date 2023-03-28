package pers.hence.memapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/28 11:09
 * @description OSS配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun-oss")
public class OSSConfig {

    private String endPoint;

    private String accessKey;

    private String accessSecret;

    private String bucketName;

    /**
     * Bucket 域名
     */
    private String bucketUrl;
}
