package kafka;

import kafka.Consumer;
import org.junit.Test;

import java.util.Properties;

public class ConsumerTest {

    @Test
    public void test() {
        Consumer consumer = new Consumer.Builder()
                .bootstrapServers(KafkaProperties.KAFKA_SERVER)
                .topic(KafkaProperties.TOPIC)
                .groupId("DemoConsumer")
                .autoCommit("false")
                .autoOffsetReset("earliest")
                .autoCommitIntervalMs("1000")
                .sessionTimeoutMs("30000")
                .keyDeserializer("org.apache.kafka.common.serialization.IntegerDeserializer")
                .valueDeserializer("org.apache.kafka.common.serialization.StringDeserializer")
                .build();

        consumer.run();
        consumer.close();
    }
}
