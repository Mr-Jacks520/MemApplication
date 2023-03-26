package pers.hence.memapplication.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.redisson.api.RedissonClient;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 19:48
 * @description Redission配置
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonConfig {

    private String host;

    private String port;

    private String username;

    private String password;

    private Integer database;

    @Bean
    public RedissonClient redissonClient() {
        // 创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer().setAddress(redisAddress).setUsername(username).setPassword(password).setDatabase(database);
        // 创建实例
        return Redisson.create(config);
    }
}
