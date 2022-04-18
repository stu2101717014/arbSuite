package com.example.ui.mqtt;


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

    @Value("${ppttGetLatest.rabbitmq.queue}")
    private String ppttGetLatestQueueName;

    @Value("${ppttGetLatest.rabbitmq.exchange}")
    private String ppttGetLatestExchange;

    @Value("${ppttGetLatest.rabbitmq.routingkey}")
    private String ppttGetLatestRoutingKey;

    @Bean
    Queue ppttGetLatestQueue() {
        return new Queue(ppttGetLatestQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange ppttGetLatestTopicExchange() {
        return new TopicExchange(ppttGetLatestExchange);
    }

    @Bean
    Binding ppttGetLatestQueueBinding(@Qualifier("ppttGetLatestQueue") final Queue ppttGetLatestQu, @Qualifier("ppttGetLatestTopicExchange") final TopicExchange ppttGetLatestTopicExchange) {
        return BindingBuilder.bind(ppttGetLatestQu).to(ppttGetLatestTopicExchange).with(ppttGetLatestRoutingKey);
    }


    @Value("${getPlatformNames.rabbitmq.queue}")
    private String getPlatformNamesQueueName;

    @Value("${getPlatformNames.rabbitmq.exchange}")
    private String getPlatformNamesExchange;

    @Value("${getPlatformNames.rabbitmq.routingkey}")
    private String getPlatformNamesRoutingKey;

    @Bean
    Queue getPlatformNamesQueue() {
        return new Queue(getPlatformNamesQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange getPlatformNamesTopicExchange() {
        return new TopicExchange(getPlatformNamesExchange);
    }

    @Bean
    Binding getPlatformNamesQueueBinding(@Qualifier("getPlatformNamesQueue") final Queue getPlatformNamesQu, @Qualifier("getPlatformNamesTopicExchange") final TopicExchange getPlatformNamesTopicExchange) {
        return BindingBuilder.bind(getPlatformNamesQu).to(getPlatformNamesTopicExchange).with(getPlatformNamesRoutingKey);
    }
}
