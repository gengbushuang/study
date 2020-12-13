package kafka;

import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.Map;

public class ConsumerTopicInfoTest {

    @Test
    public void test() {
        String groupId = "DemoConsumer";
        String serverInfo = KafkaProperties.KAFKA_SERVER;
        Map<TopicPartition, Long> map = ConsumerTopicInfo.lagOf(groupId, serverInfo);
        System.out.println(map);
    }

    @Test
    public void testOffsetTop(){
        String groupId = "DemoConsumer";
        int index = Math.abs(groupId.hashCode()) % 50;
        System.out.println(index);
    }
}
