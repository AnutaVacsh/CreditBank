package ru.vaschenko.deal.queries.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("#{${kafka.init.topics}==true}")
public class KafkaConfig {

    @Value("${kafka.topics.finish_registration}")
    private String finishRegistration;
    @Value("${kafka.topics.create_documents}")
    private String createDocuments;
    @Value("${kafka.topics.send_documents}")
    private String sendDocuments;
    @Value("${kafka.topics.send_ses}")
    private String sendSesCode;
    @Value("${kafka.topics.credit_issued}")
    private String creditIssued;

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder
                .name(finishRegistration)
                .build();
    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return TopicBuilder
                .name(createDocuments)
                .build();
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return TopicBuilder
                .name(sendDocuments)
                .build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder
                .name(sendSesCode)
                .build();
    }

    @Bean
    public NewTopic creditIssuedTopic() {
        return TopicBuilder
                .name(creditIssued)
                .build();
    }
}
