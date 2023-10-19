package net.javaguides.springboot;

import net.javaguides.springboot.Entity.WikimediaData;
import net.javaguides.springboot.repository.WikimediaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
    private WikimediaDataRepository wikimediaDataRepository;
    public KafkaDatabaseConsumer(WikimediaDataRepository wikimediaDataRepository){
        this.wikimediaDataRepository= wikimediaDataRepository;

    }
    @KafkaListener(topics="${spring.kafka.topic.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String eventMessage){
        LOGGER.info(String.format("Event Message Received -> %s",eventMessage));
        String truncatedEventMessage = truncateAndKeepFirstFiveWords(eventMessage);

        WikimediaData wikimediaData=  new WikimediaData();
        wikimediaData.setWikiEventData(truncatedEventMessage);
        wikimediaDataRepository.save(wikimediaData);
    }
    private String truncateAndKeepFirstFiveWords(String inputText) {
        // Truncate the text to a maximum length of 16,777,217 characters
        int maxLength = 16777217;
        if (inputText.length() > maxLength) {
            inputText = inputText.substring(0, maxLength);
        }

        // Split the truncated text into words
        String[] words = inputText.split("\\s+");

        // Keep only the first five words
        int numWordsToKeep = Math.min(5, words.length);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numWordsToKeep; i++) {
            result.append(words[i]).append(" ");
        }

        return result.toString().trim();
    }

}
