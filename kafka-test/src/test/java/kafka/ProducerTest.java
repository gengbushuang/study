package kafka;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

public class ProducerTest {

    @Test
    public void test() {
        Producer producer = new Producer.Builder()
                .bootstrapServers(KafkaProperties.KAFKA_SERVER)
                .clientId("DemoProducer")
                .keySerializer(IntegerSerializer.class.getName())
                .valueSerializer(StringSerializer.class.getName())
                .build();
        for (int i = 20; i < 30; i++) {
            producer.toSend(KafkaProperties.TOPIC, i, "a" + i);
        }
        producer.close();
    }
}
