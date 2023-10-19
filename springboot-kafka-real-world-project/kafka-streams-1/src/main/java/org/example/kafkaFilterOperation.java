package org.example;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Name;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class kafkaFilterOperation {
    private final static Logger LOGGER= LoggerFactory.getLogger(kafkaFilterOperation.class);
    private final static String APP_ID= "stateless_filtering_operation";
    private final static String BOOTSTRAP_SERVER="localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";
    public static void main(String[] args) throws InterruptedException {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String,String> ks0 = streamsBuilder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(),Serdes.String()).withName("source-processor")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));
        ks0.filter((k,v)-> v.contains("kafka"), Named.as("filter-processor"))
                .print(Printed.<String,String>toSysOut().withName("Filtering"));
        ks0.filterNot((k,v)->v.contains("kafka"), Named.as("filter-not-processor"))
                .print(Printed.<String,String>toSysOut().withName("Filtering-Not"));


        final Topology topology = streamsBuilder.build();
        final KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->
        {
            kafkaStreams.close();;
            latch.countDown();
            LOGGER.info("Kafka streams application gracefully closed");

        }));
        kafkaStreams.start();;
        LOGGER.info("Kafka Streams application started ...");
        latch.await();
    }
}
