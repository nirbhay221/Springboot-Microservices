package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class statelessKafkaSplitOperation {
    private final static Logger LOGGER= LoggerFactory.getLogger(statelessKafkaSplitOperation.class);
    private final static String APP_ID= "stateless_kafka_split_operation";
    private final static String BOOTSTRAP_SERVER="localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";

    public static void main (String[] args) throws InterruptedException {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> ks0= builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String())
                .withName("source-producer")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));

        Map<String,KStream<String,String>> kStreamMap = ks0.split(Named.as("split-stream"))
                .branch((k,v)-> v.contains("apache"), Branched.as("apache"))
                .branch((k,v)-> v.contains("kafka"),Branched.as("kafka"))
                .branch((k,v)-> v.contains("streams"),Branched.withFunction(ks -> ks.mapValues(e-> e.toUpperCase()),"streams"))
                .defaultBranch(Branched.as("default"));
        kStreamMap.get("split-streamapache").print(Printed.<String,String>toSysOut().withLabel("apache"));
        kStreamMap.get("split-streamkafka").print(Printed.<String,String>toSysOut().withLabel("kafka"));
        kStreamMap.get("split-streamstreams").print(Printed.<String,String>toSysOut().withLabel("streams"));
        kStreamMap.get("split-streamdefault").print(Printed.<String,String>toSysOut().withLabel("default"));
        kStreamMap.get("split-streamstreams").merge(kStreamMap.get("split-streamdefault"),Named.as("merge-producer")).print(Printed.<String,String>toSysOut().withLabel("merged"));
        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("kafka stream closed gracefully.");

        }));
        kafkaStreams.start();
        LOGGER.info("kafka stream started.");
        latch.await();
    }

}
