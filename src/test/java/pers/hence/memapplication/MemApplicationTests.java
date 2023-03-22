package pers.hence.memapplication;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.hence.memapplication.dao.MemContentDao;
import pers.hence.memapplication.dao.UserDao;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.entity.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@SpringBootTest
class MemApplicationTests {

    @Autowired
    private MemContentDao memContentDao;

    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        MemContent memContent = memContentDao.selectById(4);
        int ret = memContentDao.updateById(memContent);
        System.out.println(ret + " 更新成功!");
    }

    @Test
    void testUserDao() {
        User user = User.builder()
                .userName("hencejacki")
                .userPass(SecureUtil.md5("123456789"))
                .userSex(1)
                .userMail("hencejacki@gmail.com")
                .build();
        int ret = userDao.insert(user);
        System.out.println(ret);
    }

}
