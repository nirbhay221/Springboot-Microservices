package serdes;

import model.Transaction;
import model.TransactionKey;
import model.TransactionPattern;
import model.TransactionReward;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerdes {

    public static TransactionWrapSerde Transaction(){
        return new TransactionWrapSerde(new JsonSerialization<>(), new JsonDeserialization<>(Transaction.class));

    }
    public static TransactionPatternWrapSerde TransactionPattern(){
        return new TransactionPatternWrapSerde(new JsonSerialization<>(), new JsonDeserialization<>(TransactionPattern.class));
    }

    public static TransactionRewardWrapSerde TransactionReward(){
        return new TransactionRewardWrapSerde(new JsonSerialization<>(), new JsonDeserialization<>(TransactionReward.class));
    }

    public static TransactionKeySerde TransactionKey(){
        return new TransactionKeySerde(new JsonSerialization<>(), new JsonDeserialization<>(TransactionKey.class));
    }



    public final static class TransactionWrapSerde extends WrapSerde<Transaction>{
        private TransactionWrapSerde(Serializer<Transaction> serializer, Deserializer<Transaction> deserializer){
            super(serializer,deserializer);
        }
    }
    public final static class TransactionPatternWrapSerde extends WrapSerde<TransactionPattern>{
        public TransactionPatternWrapSerde(Serializer<TransactionPattern> serializer, Deserializer<TransactionPattern> deserializer) {
            super(serializer, deserializer);
        }
    }
    public final static class TransactionRewardWrapSerde extends WrapSerde<TransactionReward> {
        private TransactionRewardWrapSerde(Serializer<TransactionReward> serializer,Deserializer<TransactionReward> deserializer){
            super(serializer,deserializer);
        }
    }

    public final static class TransactionKeySerde extends WrapSerde<TransactionKey>{
        private  TransactionKeySerde(Serializer<TransactionKey> serializer, Deserializer<TransactionKey> deserializer ){
            super(serializer,deserializer);
        }
    }


    public static class WrapSerde<T> implements Serde<T>{
        private final Serializer<T> serializer;
        private final Deserializer<T> deserializer;
        private WrapSerde(Serializer<T> serializer,Deserializer<T> deserializer){
            this.serializer = serializer;
            this. deserializer = deserializer;
        }
        @Override
        public Serializer<T> serializer(){
            return serializer;
        }
        @Override
        public Deserializer<T> deserializer(){
            return deserializer;
        }

    }
}
