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

    @Value("${nsUIGetAll.rabbitmq.queue}")
    private String nsUIGetAllQueueName;

    @Value("${nsUIGetAll.rabbitmq.exchange}")
    private String nsUIGetAllExchange;

    @Value("${nsUIGetAll.rabbitmq.routingkey}")
    private String nsUIGetAllRoutingKey;

    @Bean
    Queue nsUIGetAllQueue() {
        return new Queue(nsUIGetAllQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsUIGetAllTopicExchange() {
        return new TopicExchange(nsUIGetAllExchange);
    }

    @Bean
    Binding nsUIGetAllQueueBinding(@Qualifier("nsUIGetAllQueue") final Queue nsUIGetAllQu, @Qualifier("nsUIGetAllTopicExchange") final TopicExchange nsUIGetAllTopicExchange) {
        return BindingBuilder.bind(nsUIGetAllQu).to(nsUIGetAllTopicExchange).with(nsUIGetAllRoutingKey);
    }


    @Value("${nsUISaveAndFlush.rabbitmq.queue}")
    private String nsUISaveAndFlushQueueName;

    @Value("${nsUISaveAndFlush.rabbitmq.exchange}")
    private String nsUISaveAndFlushExchange;

    @Value("${nsUISaveAndFlush.rabbitmq.routingkey}")
    private String nsUISaveAndFlushRoutingKey;

    @Bean
    Queue nsUISaveAndFlushQueue() {
        return new Queue(nsUISaveAndFlushQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsUISaveAndFlushTopicExchange() {
        return new TopicExchange(nsUISaveAndFlushExchange);
    }

    @Bean
    Binding nsUISaveAndFlushQueueBinding(@Qualifier("nsUISaveAndFlushQueue") final Queue nsUISaveAndFlushQu, @Qualifier("nsUISaveAndFlushTopicExchange") final TopicExchange nsUISaveAndFlushTopicExchange) {
        return BindingBuilder.bind(nsUISaveAndFlushQu).to(nsUISaveAndFlushTopicExchange).with(nsUISaveAndFlushRoutingKey);
    }

    @Value("${nsUIDelete.rabbitmq.queue}")
    private String nsUIDeleteQueueName;

    @Value("${nsUIDelete.rabbitmq.exchange}")
    private String nsUIDeleteExchange;

    @Value("${nsUIDelete.rabbitmq.routingkey}")
    private String nsUIDeleteRoutingKey;

    @Bean
    Queue nsUIDeleteQueue() {
        return new Queue(nsUIDeleteQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange nsUIDeleteTopicExchange() {
        return new TopicExchange(nsUIDeleteExchange);
    }

    @Bean
    Binding nsUIDeleteQueueBinding(@Qualifier("nsUIDeleteQueue") final Queue nsUIDeleteQu, @Qualifier("nsUIDeleteTopicExchange") final TopicExchange nsUIDeleteTopicExchange) {
        return BindingBuilder.bind(nsUIDeleteQu).to(nsUIDeleteTopicExchange).with(nsUIDeleteRoutingKey);
    }
}
