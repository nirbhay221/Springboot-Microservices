package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkamapValueOperation {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaMapOperation.class);
    private final static String APP_ID= "stateless_map_operation";
    private final static String BOOTSTRAP_SERVER= "localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";

    public static void main(String[] args) throws InterruptedException {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String,String> ks0= builder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String()).withName("source-processor")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));
        KStream<String,String> ks1= ks0.mapValues(v->v.toUpperCase(),Named.as("map-processor"));
        KStream<String,String> ks2= ks0.mapValues((k,v)-> (k+"---"+v).toUpperCase(),Named.as("map-values-processor"));
        ks1.print(Printed.<String,String>toSysOut().withLabel("mapValues"));
        ks2.print(Printed.<String,String>toSysOut().withLabel("mapValuesWithKeys"));
        final Topology topology = builder.build();
        final KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        final CountDownLatch latch= new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("The kafka streams application gracefully shutdown.");
        }));
        kafkaStreams.start();
        LOGGER.info("The kafka streams application start...");
        latch.await();
    }
}
