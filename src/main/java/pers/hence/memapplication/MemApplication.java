package pers.hence.memapplication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hencejacki
 */
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "pers.hence.memapplication.dao")
public class MemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemApplication.class, args);
    }

}
