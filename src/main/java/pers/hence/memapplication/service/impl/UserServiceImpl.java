package pers.hence.memapplication.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pers.hence.memapplication.dao.UserDao;
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.service.UserService;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:51
 * @description 用户业务层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
}
