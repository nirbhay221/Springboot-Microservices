package net.javaguides.springboot.kafka;

import net.javaguides.springboot.payload.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class JsonKafkaProducer {

    @Value("${spring.kafka.topic-json.name}")
    private String topicName;
    private KafkaTemplate<String, User> kafkaTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(JsonKafkaProducer.class);

    public JsonKafkaProducer(KafkaTemplate<String,User> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(User data){
        LOGGER.info(String.format("Message sent -> %s",data.toString()));
        Message<User> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC,topicName)
                .build();
        kafkaTemplate.send(message);
    }
}
