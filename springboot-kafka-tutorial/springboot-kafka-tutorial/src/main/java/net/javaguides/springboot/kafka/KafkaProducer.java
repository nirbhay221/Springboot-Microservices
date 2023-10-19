package net.javaguides.springboot.kafka;

import net.javaguides.springboot.config.KafkaTopicConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducer {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(KafkaProducer.class);

    private KafkaTemplate<String,String> kafkaTemplate;
    @Value("${spring.kafka.topic.name}")
    private String topicName;
    public KafkaProducer(KafkaTemplate<String,String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String message){
        LOGGER.info(String.format("Message sent -> %s",message));
        kafkaTemplate.send(topicName,message);

    }

}
