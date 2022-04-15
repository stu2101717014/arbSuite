package com.example.hs.mqtt;

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

    @Value("${nsu.rabbitmq.queue}")
    private String nsuQueueName;

    @Value("${nsu.rabbitmq.exchange}")
    private String nsuExchange;

    @Value("${nsu.rabbitmq.routingkey}")
    private String nsuRoutingKey;

    @Bean
    Queue nsuQueue() {
        return new Queue(nsuQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsuTopicExchange() {
        return new TopicExchange(nsuExchange);
    }

    @Bean
    Binding nsuQueueBinding(@Qualifier("nsuQueue") final Queue nsuQu, @Qualifier("nsuTopicExchange") final TopicExchange nsuTopicExchange) {
        return BindingBuilder.bind(nsuQu).to(nsuTopicExchange).with(nsuRoutingKey);
    }

    @Value("${nsd.rabbitmq.queue}")
    private String nsdQueueName;

    @Value("${nsd.rabbitmq.exchange}")
    private String nsdExchange;

    @Value("${nsd.rabbitmq.routingkey}")
    private String nsdRoutingKey;

    @Bean
    Queue nsdQueue() {
        return new Queue(nsdQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsdTopicExchange() {
        return new TopicExchange(nsdExchange);
    }

    @Bean
    Binding nsdQueueBinding(@Qualifier("nsdQueue") final Queue nsdQu, @Qualifier("nsdTopicExchange") final TopicExchange nsdTopicExchange) {
        return BindingBuilder.bind(nsdQu).to(nsdTopicExchange).with(nsdRoutingKey);
    }
}
