package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class KafkaFlatMapValueOperation {
    private final static Logger LOGGER= LoggerFactory.getLogger(KafkaFlatMapValueOperation.class);
    private final static String APP_ID= "stateless_flatMapValue_operation";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String SOURCE_TOPIC = "input.words";
    public static void main (String[] args) throws InterruptedException {
       final Properties properties = new Properties();
       properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
       properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
       StreamsBuilder builder = new StreamsBuilder();
       builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String()).withName("source-producer")
               .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
               .flatMapValues(v-> Arrays.stream(v.split("\\s+")).map(String::toUpperCase).collect(Collectors.toList()),
                       Named.as("flatMap-value-processor"))
               .print(Printed.<String,String>toSysOut().withLabel("flatMapValues"));
       Topology topology = builder.build();
       final KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("Kafka stream closed gracefully.");
        }));
        kafkaStreams.start();
        LOGGER.info("Kafka stream started ..");
        latch.await();
    }
}
