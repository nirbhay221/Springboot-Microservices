package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StatelessForEachOperation {
    private final static Logger LOGGER = LoggerFactory.getLogger(StatelessForEachOperation.class);
    private final static String APP_ID= "stateless_foreach_operation";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String SOURCE_TOPIC = "input.words";
    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String())
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST)
                .withName("source-processor"))
                .foreach((k,v)-> LOGGER.info("record : key : {} , value : {}",k,v), Named.as("foreach-operation"));

        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("Kafka stream stopped gracefully");

        }));
        kafkaStreams.start();
        latch.await();
        LOGGER.info("Kafka stream started...");

    }
}
