package kafka;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.List;

public class ProducerTest {

    @Test
    public void test() throws InterruptedException {
        Producer producer = new Producer.Builder()
                .bootstrapServers(KafkaProperties.KAFKA_SERVER)
                .clientId("DemoProducer")
                .isAsync(true)
                .keySerializer(IntegerSerializer.class.getName())
                .valueSerializer(StringSerializer.class.getName())
                .build();

        for (int i = 0; i < 10000; i++) {
            producer.toSend(KafkaProperties.TOPIC, i, "a" + i);
//            Thread.sleep(1000);
        }
        producer.close();
    }
}
