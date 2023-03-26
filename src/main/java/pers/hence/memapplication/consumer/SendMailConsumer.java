package pers.hence.memapplication.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pers.hence.memapplication.constant.MailConfig;
import pers.hence.memapplication.model.entity.MailMessage;
import pers.hence.memapplication.util.RedisUtil;

import javax.annotation.Resource;
import java.util.Map;

import static pers.hence.memapplication.constant.RabbitMQPrefix.MAIL_QUEUE;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 20:39
 * @description 邮件异步发送
 */
@Component
@Slf4j
@RabbitListener(queues = MAIL_QUEUE)
public class SendMailConsumer {

    @Resource
    private JavaMailSender sender;

    @Autowired
    private MailMessage mailMessage;

    @Autowired
    private RedisUtil redisUtil;

    @RabbitHandler
    public void process(Map<String, String> map) {
        String mail = map.get("mail");
        String code = map.get("code");

        try {
            this.sendMail(mail, code);
            Thread.sleep(6000);
            // 设置redis
            redisUtil.set(MailConfig.REDIS_MAIL_KEY_PREFIX + mail, code, MailConfig.EXPIRED_TIME);
            log.info(mail + "-" + code + "-发送成功");
        } catch (Exception e) {
            log.error(mail + code + "发送失败-" + e.getMessage());
        }
    }

    public synchronized void sendMail(String mail, String code) {
        sender.send(mailMessage
                .create(mail, "邮箱验证码", "邮箱验证码：" + code + "，" + MailConfig.EXPIRED_TIME + "分钟内有效"));
    }
}
