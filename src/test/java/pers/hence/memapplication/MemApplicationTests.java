package pers.hence.memapplication;

import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import pers.hence.memapplication.constant.MemType;
import pers.hence.memapplication.dao.LeadingPageDao;
import pers.hence.memapplication.dao.MemContentDao;
import pers.hence.memapplication.dao.UserDao;
import pers.hence.memapplication.model.entity.LeadingPage;
import pers.hence.memapplication.model.entity.MailMessage;
import pers.hence.memapplication.model.entity.MemContent;
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.util.RedisUtil;

import javax.annotation.Resource;

@SpringBootTest
class MemApplicationTests {

    @Autowired
    private MemContentDao memContentDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LeadingPageDao leadingPageDao;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private JavaMailSender sender;

    @Autowired
    private MailMessage mailMessage;

    /**
     * 测试记忆内容
     */
    @Test
    void contextLoads() {
//        MemContent memContent = memContentDao.selectById(4);
//        int ret = memContentDao.updateById(memContent);
//        System.out.println(ret + " 更新成功!");
        String a = MemType.IMAGE.getPath();
        System.out.println(a);
    }

    /**
     * 测试用户
     */
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

    /**
     * 测试引导页
     */
    @Test
    void testLeadingPage() {
        LeadingPage leadingPage = LeadingPage.builder()
                .leadingTitle("测试标题2")
                .leadingContent("测试内容2")
                .leadingImage("http://www.jkblogs.site/i/2023/03/23/11c0e7b.jpg")
                .build();

        int ret = leadingPageDao.insert(leadingPage);
        System.out.println(ret);

//        LeadingPage select = leadingPageDao.selectById(1);
//        System.out.println(select);
    }

    @Test
    void testRedis() {
        redisUtil.set("test", "tessssssssssssssssff你是地方");
        System.out.println(redisUtil.get("test"));
    }

    @Test
    void testSendMail() {
        sender.send(mailMessage.create("1286624819@qq.com", "test", "test"));
    }

}
