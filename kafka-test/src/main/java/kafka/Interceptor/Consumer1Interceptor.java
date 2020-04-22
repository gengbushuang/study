package kafka.Interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 消费端拦截器
 */
public class Consumer1Interceptor implements ConsumerInterceptor<Integer, String> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 在Consumer之前调用
     *
     * @param records
     * @return
     */
    @Override
    public ConsumerRecords<Integer, String> onConsume(ConsumerRecords<Integer, String> records) {
        log.info("Consumer1Interceptor onConsume");
        log.info(records.toString());
        //必須要返回
        return records;
    }

    /**
     * 在提交Offset之后调用
     *
     * @param offsets
     */
    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
        log.info("Consumer1Interceptor onCommit");
        log.info(offsets.toString());
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
