package kafka;

import kafka.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.time.Duration;
import java.util.*;

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



//        consumer.testShowTopicInfo();

        consumer.run();
        consumer.close();
    }
}
