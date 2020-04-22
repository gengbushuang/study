package kafka.interceptor;

import kafka.Consumer;
import kafka.KafkaProperties;
import kafka.Producer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.Collections;

public class InterceptorTest {

    @Test
    public void testProducer() {
        Producer producer = new Producer.Builder()
                .bootstrapServers(KafkaProperties.KAFKA_SERVER)
                .clientId("DemoProducer")
                .keySerializer(IntegerSerializer.class.getName())
                .valueSerializer(StringSerializer.class.getName())
                .interceptors(Collections.singletonList("kafka.Interceptor.Producer1Interceptor"))
                .build();
        for (int i = 50; i < 60; i++) {
            producer.toSend(KafkaProperties.TOPIC, i, "a" + i);
        }
        producer.close();
    }

    @Test
    public void testConsumer() {
        Consumer consumer = new Consumer.Builder()
                .bootstrapServers(KafkaProperties.KAFKA_SERVER)
                .topic(KafkaProperties.TOPIC)
                .groupId("DemoConsumer")
                .autoCommit("true")
                .autoOffsetReset("earliest")
                .autoCommitIntervalMs("1000")
                .sessionTimeoutMs("30000")
                .keyDeserializer("org.apache.kafka.common.serialization.IntegerDeserializer")
                .valueDeserializer("org.apache.kafka.common.serialization.StringDeserializer")
                .interceptors(Collections.singletonList("kafka.Interceptor.Consumer1Interceptor"))
                .build();

        consumer.run();

        consumer.close();
    }
}
