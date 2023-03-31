package pers.hence.memapplication.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import pers.hence.memapplication.constant.UserConstant;
import pers.hence.memapplication.dao.UserDao;
import pers.hence.memapplication.exception.BusinessException;
import pers.hence.memapplication.model.entity.User;
import pers.hence.memapplication.model.vo.UserVO;
import pers.hence.memapplication.service.UserService;
import pers.hence.memapplication.util.BeanCopyUtils;
import pers.hence.memapplication.util.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pers.hence.memapplication.constant.MailConfig.REDIS_MAIL_KEY_PREFIX;
import static pers.hence.memapplication.constant.RabbitMQPrefix.MAIL_QUEUE;
import static pers.hence.memapplication.constant.StatusCode.*;
import static pers.hence.memapplication.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/23 17:51
 * @description 用户业务层
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "hj";

    /**
     * 默认用户名前缀
     */
    private static final String USER_NAME_PREFIX = "kz-";


    /**
     * 密码最短长度
     */
    private static final int PASS_MIN = 8;

    /**
     * 用户注册
     * @param userMail 用户邮箱
     * @param userPassword 用户密码
     * @param mailCode 验证码
     * @return 用户ID
     */
    @Override
    public Integer userRegister(String userMail, String userPassword, String mailCode) {
        // 校验
        if (StringUtils.isAnyBlank(userMail, userPassword, mailCode)) {
            throw new BusinessException(ERROR, "参数为空");
        }
        if (userPassword.length() < PASS_MIN) {
            throw new BusinessException(ERROR, "密码过短");
        }
        String validPattern = "/^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$/";
        Matcher matcher = Pattern.compile(validPattern).matcher(userMail);
        if (matcher.find()) {
            throw new BusinessException(ERROR, "邮箱不合法");
        }

        // 校验验证码
        String codeFromRedis = (String) redisUtil.get(REDIS_MAIL_KEY_PREFIX + userMail);
        if (!StringUtils.equals(mailCode, codeFromRedis)) {
            throw new BusinessException(ERROR, "验证码错误");
        }

        // 保证账户不重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_mail", userMail);
        long count = userDao.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ERROR, "邮箱重复");
        }

        // 密码加密
        String encryptPass = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));

        // 数据插入
        User user = User.builder()
                .userName(USER_NAME_PREFIX + RandomUtil.randomString(10))
                .userMail(userMail)
                .userPass(encryptPass)
                .userSex(1)
                .build();
        boolean isSave = this.save(user);
        if (!isSave) {
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     * @param userMail 用户邮箱
     * @param userPassword 用户密码
     * @param request 请求
     * @return 脱敏对象
     */
    @Override
    public UserVO userLogin(String userMail, String userPassword, HttpServletRequest request) {
        // 检验
        if (StringUtils.isAnyBlank(userMail, userPassword)) {
            return null;
        }
        if (userPassword.length() < PASS_MIN) {
            return null;
        }
        // 邮箱应当合法
        String validPattern = "/^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$/";
        Matcher matcher = Pattern.compile(validPattern).matcher(userMail);
        if (matcher.find()) {
            throw new BusinessException(ERROR, "邮箱格式非法");
        }

        // 加密
        String encryptPass = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_mail", userMail).eq("user_pass", encryptPass);
        User user = userDao.selectOne(queryWrapper);
        if (null == user) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }
        // 脱敏
        UserVO safetyUser = BeanCopyUtils.copyObject(user, UserVO.class);
        // 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }


    /**
     * 发送验证码
     * @param mailTo 发送对象
     */

    @Override
    public void userSendCode(String mailTo) {
        // 邮箱应当合法
        String validPattern = "/^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$/";
        Matcher matcher = Pattern.compile(validPattern).matcher(mailTo);
        if (matcher.find()) {
            throw new BusinessException(ERROR, "邮箱格式非法");
        }
        Map<String, String> map = new HashMap<>(2);
        map.put("mail", mailTo);
        map.put("code", String.valueOf(RandomUtil.randomInt(1000, 9999)));
        // 异步发送
        rabbitTemplate.convertAndSend(MAIL_QUEUE, map);
    }

    /**
     * 获取登录用户
     * @param request request
     * @return 登录用户
     */
    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        Object user = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (null == user) {
            throw new BusinessException(NOT_AUTH, "未被认证");
        }
        return (UserVO) user;
    }

    /**
     * 用户请求更改自己的信息
     * @param userVO 用户更新视图
     * @param request request
     */
    @Override
    public void updateUserInfo(UserVO userVO, HttpServletRequest request) {
        // 1. 校验
        if (null == userVO) {
            return;
        }
        UserVO loginUser = getLoginUser(request);
        if (!loginUser.getId().equals(userVO.getId())) {
            throw new BusinessException(REFUSE, "授权失败");
        }
        User oldUser = userDao.selectById(userVO.getId());
        if (null == oldUser) {
            throw new BusinessException(ERROR, "空");
        }
        User user = BeanCopyUtils.copyObject(userVO, User.class);
        userDao.updateById(user);
    }

    /**
     * 用户登出
     * @param request request
     */
    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }
}
