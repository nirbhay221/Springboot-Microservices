package serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

class JsonDeserialization<T> implements Deserializer<T> {
    private Class<T> Deserializeclass;
    public JsonDeserialization(Class<T> deserializeclass){
        this.Deserializeclass = deserializeclass;

    }


    @Override
    public T deserialize(String topic, byte[] data) {
        try{
            return JsonSerialization.MAPPER.readValue(data,Deserializeclass);
        }
        catch(IOException e){

        }
        return null;
    }
}
