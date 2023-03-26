package pers.hence.memapplication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static pers.hence.memapplication.constant.RabbitMQPrefix.*;

/**
 * @author https://github.com/Mr-Jacks520
 * @date 2023/3/25 20:31
 * @description RabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue mailQueue() {
        return new Queue(MAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange mailExchange() {
        return new FanoutExchange(MAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingMailFanout() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
