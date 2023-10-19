package org.example;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class KafkaFlatMapOperation {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaFlatMapOperation.class);
    private final static String APP_ID= "stateless_flatmap_operation";
    private final static String BOOTSTRAP_SERVER = "localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";

    public static void main(String[] args) throws InterruptedException {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(), Serdes.String()).withName("source-processor")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
                .flatMap((k,v)-> Arrays.stream(v.split("\\s+")).map(e -> KeyValue.pair(e,e.length())).collect(Collectors.toList()),
                Named.as("flatmap-processor")).print(
                        Printed.<String,Integer>toSysOut().withLabel("flatMap")
                );
        final Topology topology= builder.build();
        final KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("Kafka Stream Application gracefully closed");
        }));
        kafkaStreams.start();
        LOGGER.info("Kafka Stream Application starts ...");
        latch.await();

    }
}
