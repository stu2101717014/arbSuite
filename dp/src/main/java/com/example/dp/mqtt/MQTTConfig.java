package com.example.dp.mqtt;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTConfig {
    @Value("${ttee.rabbitmq.queue}")
    private String tteeQueueName;


    @Value("${ttee.rabbitmq.exchange}")
    private String tteeExchange;


    @Value("${ttee.rabbitmq.routingkey}")
    private String tteeRoutingKey;

    @Bean
    Queue tteeQueue() {
        return new Queue(tteeQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange tteeTopicExchange() {
        return new TopicExchange(tteeExchange);
    }

    @Bean
    Binding tteeQueueBinding(@Qualifier("tteeQueue") final Queue tteeQu, @Qualifier("tteeTopicExchange") final TopicExchange topicExchange) {
        return BindingBuilder.bind(tteeQu).to(topicExchange).with(tteeRoutingKey);
    }


    @Value("${ns.rabbitmq.queue}")
    private String nsQueueName;

    @Value("${ns.rabbitmq.exchange}")
    private String nsExchange;

    @Value("${ns.rabbitmq.routingkey}")
    private String nsRoutingKey;

    @Bean
    Queue nsQueue() {
        return new Queue(nsQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsTopicExchange() {
        return new TopicExchange(nsExchange);
    }

    @Bean
    Binding nsQueueBinding(@Qualifier("nsQueue") final Queue nsQu, @Qualifier("nsTopicExchange") final TopicExchange nsTopicExchange) {
        return BindingBuilder.bind(nsQu).to(nsTopicExchange).with(nsRoutingKey);
    }



    @Value("${htte.rabbitmq.queue}")
    private String htteQueueName;

    @Value("${htte.rabbitmq.exchange}")
    private String htteExchange;

    @Value("${htte.rabbitmq.routingkey}")
    private String htteRoutingKey;

    @Bean
    Queue htteQueue() {
        return new Queue(htteQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange htteTopicExchange() {
        return new TopicExchange(htteExchange);
    }

    @Bean
    Binding htteQueueBinding(@Qualifier("htteQueue") final Queue htteQu, @Qualifier("htteTopicExchange") final TopicExchange htteTopicExchange) {
        return BindingBuilder.bind(htteQu).to(htteTopicExchange).with(htteRoutingKey);
    }
}
