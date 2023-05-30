package ru.gilko.gatewayimpl.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.gilko.gatewayapi.dto.rental.RentalDto;
import ru.gilko.gatewayapi.dto.rental.StatisticDto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String[] bootstrapServers;

    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }


    @Bean
    public ProducerFactory<String, StatisticDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        Arrays.stream(bootstrapServers).forEach(server -> {
            configProps.put(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    server);
        });

        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, StatisticDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public KafkaTemplate<String, Object> instanceKafkaTemplate(
            DefaultKafkaProducerFactory<String, Object> instanceProducerFactory) {
        return new KafkaTemplate<>(instanceProducerFactory);
    }


    @Bean
    public KafkaSendCallback<UUID, RentalDto> organizationSendCallback() {
        return new KafkaSendCallback<>() {
            @Override
            public void onFailure(KafkaProducerException ex) {
                ProducerRecord<UUID, RentalDto> failedProducerRecord = ex.getFailedProducerRecord();
                log.error("Sending message wih key {} was failed cause {}", failedProducerRecord.key().toString(),
                        ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<UUID, RentalDto> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                ProducerRecord<UUID, RentalDto> producerRecord = result.getProducerRecord();
                log.info("Message with key {} and value {} was successfully send to topic {} in partition {} " +
                                "with offset {} and timestamp {}", producerRecord.key().toString(), producerRecord.value(),
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
            }
        };
    }
}
