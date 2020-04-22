package kafka.Interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 生产端拦截器
 */
public class Producer1Interceptor implements ProducerInterceptor<Integer,String> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * 消息发送之前调用
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<Integer, String> onSend(ProducerRecord<Integer, String> record) {
        log.info("Producer1Interceptor onSend");
        //必須要有返回
        return new ProducerRecord<>(record.topic(), record.key(), record.value() + "-Interceptor");
    }

    /**
     * 消息发送成功或者失败之后调用
     * 早于Callback之前调用
     * @param metadata
     * @param exception
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        log.info("Producer1Interceptor onAcknowledgement");
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
