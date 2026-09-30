package com.duoc.rabbitmqlogs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "logs_direct_exchange";
    public static final String ALL_LOGS_QUEUE = "all_logs_queue";
    public static final String ERRORS_ONLY_QUEUE = "errors_only_queue";
    // Desafío SECURITY
    public static final String SECURITY_LOGS_QUEUE = "security_logs_queue";

    @Bean
    public DirectExchange logsExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue allLogsQueue() {
        return new Queue(ALL_LOGS_QUEUE, true);
    }

    @Bean
    public Queue errorsOnlyQueue() {
        return new Queue(ERRORS_ONLY_QUEUE, true);
    }

    // Desafío SECURITY: nueva cola exclusiva
    @Bean
    public Queue securityLogsQueue() {
        return new Queue(SECURITY_LOGS_QUEUE, true);
    }

    @Bean
    public Binding bindInfo(
            @Qualifier("logsExchange") DirectExchange exchange,
            @Qualifier("allLogsQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("INFO");
    }

    @Bean
    public Binding bindWarning(
            @Qualifier("logsExchange") DirectExchange exchange,
            @Qualifier("allLogsQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("WARNING");
    }

    @Bean
    public Binding bindErrorGeneral(
            @Qualifier("logsExchange") DirectExchange exchange,
            @Qualifier("allLogsQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("ERROR");
    }

    @Bean
    public Binding bindErrorCritical(
            @Qualifier("logsExchange") DirectExchange exchange,
            @Qualifier("errorsOnlyQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("ERROR");
    }

    // Desafío SECURITY: SECURITY -> security_logs_queue
    @Bean
    public Binding bindSecurity(
            @Qualifier("logsExchange") DirectExchange exchange,
            @Qualifier("securityLogsQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("SECURITY");
    }
}
