package pers.hence.memapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.hence.memapplication.dao.MemContentDao;
import pers.hence.memapplication.model.entity.MemContent;

import java.time.LocalDateTime;

@SpringBootTest
class MemApplicationTests {

    @Autowired
    private MemContentDao memContentDao;

    @Test
    void contextLoads() {
        MemContent memContent = memContentDao.selectById(4);
        int ret = memContentDao.updateById(memContent);
        System.out.println(ret + " 更新成功!");
    }

}
