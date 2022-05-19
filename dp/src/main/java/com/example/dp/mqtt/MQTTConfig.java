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


    @Value("${htteGetAll.rabbitmq.queue}")
    private String htteGetAllQueueName;

    @Value("${htteGetAll.rabbitmq.exchange}")
    private String htteGetAllExchange;

    @Value("${htteGetAll.rabbitmq.routingkey}")
    private String htteGetAllRoutingKey;

    @Bean
    Queue htteGetAllQueue() {
        return new Queue(htteGetAllQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange htteGetAllTopicExchange() {
        return new TopicExchange(htteGetAllExchange);
    }

    @Bean
    Binding htteGetAllQueueBinding(@Qualifier("htteGetAllQueue") final Queue htteGetAllQu, @Qualifier("htteGetAllTopicExchange") final TopicExchange htteGetAllTopicExchange) {
        return BindingBuilder.bind(htteGetAllQu).to(htteGetAllTopicExchange).with(htteGetAllRoutingKey);
    }

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

    @Value("${dpMetrics.rabbitmq.queue}")
    private String dpMetricsQueueName;

    @Value("${dpMetrics.rabbitmq.exchange}")
    private String dpMetricsExchange;

    @Value("${dpMetrics.rabbitmq.routingkey}")
    private String dpMetricsRoutingKey;

    @Bean
    Queue dpMetricsQueue() {
        return new Queue(dpMetricsQueueName, Boolean.FALSE);
    }

    @Bean
    TopicExchange dpMetricsTopicExchange() {
        return new TopicExchange(dpMetricsExchange);
    }

    @Bean
    Binding dpMetricsQueueBinding(@Qualifier("dpMetricsQueue") final Queue dpMetricsQu, @Qualifier("dpMetricsTopicExchange") final TopicExchange dpMetricsTopicExchange) {
        return BindingBuilder.bind(dpMetricsQu).to(dpMetricsTopicExchange).with(dpMetricsRoutingKey);
    }
}
