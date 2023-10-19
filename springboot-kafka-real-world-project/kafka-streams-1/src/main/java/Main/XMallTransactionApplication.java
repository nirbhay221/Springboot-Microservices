package Main;

import model.Transaction;
import model.TransactionKey;
import model.TransactionPattern;
import model.TransactionReward;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serdes.JsonSerdes;

import javax.naming.Name;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class XMallTransactionApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(XMallTransactionApplication.class);
    private final static String APP_ID = "Xmall_Application";
    private final static String BOOTSTRAP_SERVER= "localhost:9092";
    private final static String XMALL_TRANSACTION_SOURCE_TOPIC = "xmall.transaction";
    private final static String XMALL_TRANSACTION_PATTERN_TOPIC = "xmall.pattern.transaction";
    private final static String XMALL_TRANSACTION_REWARDS_TOPIC = "xmall.rewards.transaction";
    private final static String XMALL_TRANSACTION_PURCHASES_TOPIC= "xmall.purchases.transaction";
    private final static String XMALL_TRANSACTION_COFFEE_TOPIC = "xmall.coffee.transaction";
    private final static String XMALL_TRANSACTION_ELECT_TOPIC = "xmall.elect.transaction";
    public static void main (String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,APP_ID);
        StreamsBuilder builder = new StreamsBuilder();
        // Consume the DATA from the SOURCE TOPIC "Transaction".
        KStream<String, Transaction> ks0 = builder.stream(
          XMALL_TRANSACTION_SOURCE_TOPIC,
                Consumed.with(Serdes.String(), JsonSerdes.Transaction())
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));
        // Masking Credit Card Pin
        KStream<String, Transaction > ks1=  ks0.mapValues(v-> Transaction.newBuilder().maskCreditCard().build(), Named.as("transaction-masking-pin"));
        // Extracting Transaction Pattern
        ks1.mapValues(v-> TransactionPattern.builder(v).build(),Named.as("transaction-pattern"))
                .to(XMALL_TRANSACTION_PATTERN_TOPIC,Produced.with(Serdes.String(), JsonSerdes.TransactionPattern()));
//        ks1.mapValues(v -> TransactionReward.builder(v).build(),Named.as("transaction-reward"))
//                .to(XMALL_TRANSACTION_REWARDS_TOPIC, Produced.<String, Transaction>with(Serdes.String(),JsonSerdes.TransactionReward()));
        ks1.filter((k,v) -> v.getPrice()>5.00)
                .selectKey((k,v)-> new TransactionKey(v.getCustomerID(),v.getPurchaseDate()))
                .to(XMALL_TRANSACTION_PURCHASES_TOPIC,Produced.with(JsonSerdes.TransactionKey(),JsonSerdes.Transaction()));
        ks1.split(Named.as("transaction-split"))
                .branch((k,v)-> v.getDepartment().equalsIgnoreCase("COFFEE"),
                        Branched.withConsumer(ks-> ks.to(XMALL_TRANSACTION_COFFEE_TOPIC,Produced.with(Serdes.String(),JsonSerdes.Transaction()))))
                .branch((k,v)-> v.getDepartment().equalsIgnoreCase("ELECT"),
                        Branched.withConsumer(ks->ks.to(XMALL_TRANSACTION_ELECT_TOPIC,Produced.with(Serdes.String(),JsonSerdes.Transaction()))));
        ks1.foreach((k,v)-> LOGGER.info("Simulation located the transaction record(masked) to the data lake, the value : {}",v));


        Topology topology = builder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology,properties);
        final CountDownLatch latch  = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            kafkaStreams.close();
            latch.countDown();
            LOGGER.info("Kafka streams application closed gracefully.");
        }));
        kafkaStreams.start();
        LOGGER.info("Kafka streams started");
        latch.await();

    }

}
