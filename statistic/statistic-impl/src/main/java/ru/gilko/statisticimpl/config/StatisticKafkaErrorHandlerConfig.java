package ru.gilko.statisticimpl.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;

@Configuration
public class StatisticKafkaErrorHandlerConfig {

    @Bean("dltStatistic")
    public NewTopic dltDirectoryTopic() {
        return TopicBuilder.name("statistic.DLT")
                .build();
    }

    @Bean
    public DefaultErrorHandler statisticDefaultErrorHandler(KafkaTemplate<String, StatisticDto> kafkaTemplate) {
        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        return new DefaultErrorHandler(deadLetterPublishingRecoverer, new FixedBackOff(2000L, 2L));
    }
}
