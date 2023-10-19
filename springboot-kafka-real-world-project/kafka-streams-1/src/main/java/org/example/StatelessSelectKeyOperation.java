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

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class StatelessSelectKeyOperation {
    private final static Logger LOGGER= LoggerFactory.getLogger(StatelessSelectKeyOperation.class);
    private final static String APP_ID= "stateless_select_key_operation";
    private final static String BOOTSTRAP_SERVER= "localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";
    public static void main(String[] args) throws InterruptedException {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String()).withName("source-producer")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
                .selectKey((nokey,v) -> v, Named.as("select-key-processor"))
                .print(Printed.<String,String>toSysOut().withLabel("selectKey"));

        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("Kafka Streams closed gracefully");

        }));
        kafkaStreams.start();
        LOGGER.info("Kafka Streams started ...");
        latch.await();


    }
}
