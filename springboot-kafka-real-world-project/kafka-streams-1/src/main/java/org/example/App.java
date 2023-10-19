package org.example;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App 
{

    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(App.class);
    private final static String APP_ID= "app_id";
    private final static String BOOTSTRAP_SERVER= "localhost:9092";
    private final static String SOURCE_TOPIC= "input.words";
    private final static String TARGET_TOPIC= "output.words";

    public static void main( String[] args ) throws InterruptedException {

        // 1. Configurations
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
//        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
//        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // 2. Stream Builder Creation
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(), Serdes.String()).withName("source-processor")
                .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
                .peek((k,v) -> LOGGER.info("[source] records : {}",v), Named.as("pre-transform-peak"))
                .filter((k,v) ->v!=null && v.length()>5,Named.as("filter-processor"))
                .mapValues(v->v.toUpperCase(),Named.as("transform-processor"))
                .peek((k,v)-> LOGGER.info("[source] records: {}",v),Named.as("post-transform-peek"))
                .to(TARGET_TOPIC, Produced.with(Serdes.String(),Serdes.String()).withName("sink-processor"));
        // 3. Topology Creation
        Topology topology = streamsBuilder.build();
        // 4. Create Kafka Streams
        KafkaStreams kafkaStreams= new KafkaStreams(topology,properties);
        final CountDownLatch latch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("The Kafka stream application is graceful shutdown");
        }));
        kafkaStreams.start();
        LOGGER.info(" The kafka stream application is started ... ");
        latch.await();

    }
}
