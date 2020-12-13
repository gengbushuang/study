package kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ConsumerTopicInfo {

    public static Map<TopicPartition, Long> lagOf(String groupId, String bootstrapServers) {
        Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        try (AdminClient client = AdminClient.create(properties)) {
            ListConsumerGroupOffsetsResult offsetsResult = client.listConsumerGroupOffsets(groupId);
            try {
                KafkaFuture<Map<TopicPartition, OffsetAndMetadata>> kafkaFuture = offsetsResult.partitionsToOffsetAndMetadata();
                Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap = kafkaFuture.get();
                properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
                properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                try (final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
                    Map<TopicPartition, Long> endOffsets = consumer.endOffsets(offsetAndMetadataMap.keySet());
                    return endOffsets.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(),
                            entry -> entry.getValue() - offsetAndMetadataMap.get(entry.getKey()).offset()));
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return Collections.emptyMap();
            } catch (ExecutionException e) {

                return Collections.emptyMap();
            }
        }
    }

    public static void main(String[] args) {
    }
}
