package pers.hence.memapplication;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.model.vo.MemContentVO;
import pers.hence.memapplication.util.AlgorithmUtils;
import pers.hence.memapplication.util.RedisUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Test
    void serializer() {
        Map<String, String> map = new ConcurrentHashMap<>();
        MemContentVO memContentVO1 = new MemContentVO();
        memContentVO1.setId(1);
        memContentVO1.setTitle("你好");
        memContentVO1.setSize("0.9M");
        memContentVO1.setType(1);
        MemContentVO memContentVO2 = new MemContentVO();
        memContentVO2.setId(2);
        memContentVO2.setTitle("你好");
        memContentVO2.setSize("0.9M");
        memContentVO2.setType(2);
        List<MemContentVO> list = new ArrayList<>();
        list.add(memContentVO1);
        list.add(memContentVO2);
        Gson gson = new Gson();
        String s = gson.toJson(list);
        map.put("1", s);
        map.put("2", s);
        redisUtil.add("TEST", map);
    }

    @Test
    void deserializer() {
        Map<String, Object> test = redisUtil.getHashEntries("TEST");
        Gson gson = new Gson();
        String s = (String) test.get("1");
        List<MemContentVO> f = gson.fromJson(s, new TypeToken<List<MemContentVO>>(){}.getType());
        for (MemContentVO g : f) {
            System.out.println(g.getTitle());
        }
    }

    /**
     * 测试艾宾浩斯算法生成
     */
    @Test
    void testAlgorithm() {
        System.out.println(AlgorithmUtils.ebenhausCurve(DateUtil.today()));
    }

}
