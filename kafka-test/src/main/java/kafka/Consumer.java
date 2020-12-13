package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public final class Consumer extends Kafkas {
    private final String topic;

    private final KafkaConsumer<Integer, String> consumer;

    private Consumer(Builder builder) {

        this.topic = builder.topic;
        this.consumer = new KafkaConsumer<>(builder.props);
    }

    public Map<TopicPartition, Long> getFromOffset() {

        return Collections.EMPTY_MAP;
    }

    public void testShowTopicInfo() {
        Map<String, List<PartitionInfo>> mapListTopics = consumer.listTopics();
        Collection<List<PartitionInfo>> listCollection = mapListTopics.values();
        for (List<PartitionInfo> partitionInfos : listCollection) {
            for (PartitionInfo partitionInfo : partitionInfos) {
                TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                System.out.println(topicPartition);
                Map<TopicPartition, Long> mapBeginningOffsets = consumer.beginningOffsets(Collections.singletonList(topicPartition));
                System.out.println("mapBeginningOffsets--->" + mapBeginningOffsets);
                Map<TopicPartition, Long> mapEndOffsets = consumer.endOffsets(Collections.singletonList(topicPartition));
                System.out.println("mapEndOffsets--->" + mapEndOffsets);
            }
        }
    }


    public void run() {
        consumer.subscribe(Collections.singletonList(this.topic));

        Set<TopicPartition> assignment = new HashSet<>();
        while (assignment.size() == 0) {
            consumer.poll(Duration.ofSeconds(1));
            assignment = consumer.assignment();
        }

        Map<TopicPartition, Long> fromOffset = getFromOffset();

        for (TopicPartition tp : assignment) {
            Long offset = fromOffset.get(tp);
            if (offset == null) {
                continue;
            }
            long kafkaOffset = consumer.position(tp);
            if (kafkaOffset > offset.intValue()) {
                System.out.println("分区 " + tp + " 从 " + offset.longValue() + " 开始消费");
                consumer.seek(tp, offset.longValue());
            }
        }


        try {
            while (true) {
                ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(1));
                if (records.isEmpty()) {
                    Thread.sleep(2000);
                    continue;
                }
                for (ConsumerRecord<Integer, String> record : records) {
                    System.out.println("------>Received message: (" + record.partition() + "," + record.key() + ", " + record.value() + ") at offset " + record.offset());
                }
                //异步提交
                consumer.commitAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                consumer.commitSync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void close() {
        consumer.close();
    }


    public static class Builder extends Kafkas.Builder<Builder> {

        public Builder groupId(String groupId) {
            this.props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            return this;
        }

        /**
         * true为自动提交Offset
         * false为手动提交Offset
         *
         * @param autoCommit
         * @return
         */
        public Builder autoCommit(String autoCommit) {
            this.props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
            return this;
        }

        /**
         * earliest：各个分区从提交得offset开始消费，没有就最早偏移量消费
         * latest：各个分区从提交得offset开始消费，没有就最新偏移量消费
         * none：各个分区从提交得offset开始消费，没有就抛出异常
         *
         * @param autoOffsetReset
         * @return
         */
        public Builder autoOffsetReset(String autoOffsetReset) {
            this.props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
            return this;
        }

        public Builder autoCommitIntervalMs(String autoCommitIntervalMs) {
            this.props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
            return this;
        }

        public Builder sessionTimeoutMs(String sessionTimeoutMs) {
            this.props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
            return this;
        }

        public Builder keyDeserializer(String keyDeserializer) {
            this.props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
            return this;
        }

        public Builder valueDeserializer(String valueDeserializer) {
            this.props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
            return this;
        }

        public Builder interceptors(List<String> interceptors) {
            this.props.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Consumer build() {
            return new Consumer(this);
        }
    }
}
