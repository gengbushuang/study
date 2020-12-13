package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.concurrent.ExecutionException;

public final class Producer extends Kafkas {

    private final KafkaProducer<Integer, String> producer;

    private final String topic;

    private final Boolean isAsync;


    public static class Builder extends Kafkas.Builder<Builder> {

        private Boolean isAsync = true;

        private int numRecords;

        @Override
        protected Builder self() {
            return this;
        }

        public Builder isAsync(Boolean isTrue) {
            this.isAsync = isTrue;
            return this;
        }

        public Builder keySerializer(String keyClassName) {
            this.props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keyClassName);
            return this;
        }

        public Builder valueSerializer(String valueClassName) {
            this.props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueClassName);
            return this;
        }

        public Builder transactionTimeoutMs(int transactionTimeoutMs) {
            this.props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, transactionTimeoutMs);
            return this;
        }

        public Builder transactionalId(String transactionalId) {
            this.props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
            return this;
        }

        public Builder enableIdempotency(boolean enableIdempotency) {
            this.props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotency);
            return this;
        }

        public Builder clientId(String clientId) {
            this.props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
            return this;
        }

        /**
         * acks=0
         * acks=1(默认值)
         * acks=all 这个是表明所有得副本得Broker都要接收到消息，才算是已提交
         * @param ack
         * @return
         */
        public Builder acks(String ack){
            this.props.put(ProducerConfig.ACKS_CONFIG, ack);
            return this;
        }

        /**
         * 设置retries>0能够自动重试消息发送，避免消息丢失
         * @param retries
         * @return
         */
        public Builder retries(String retries){
            this.props.put(ProducerConfig.RETRIES_CONFIG, retries);
            return this;
        }

        public Builder interceptors(List<String> interceptors){
            this.props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);
            return this;
        }

        @Override
        public Producer build() {
            return new Producer(this);
        }
    }

    private Producer(Builder builder) {
        this.isAsync = builder.isAsync;
        this.topic = builder.topic;
        this.producer = new KafkaProducer(builder.props);
    }

    public void toSend(String topic, Integer key, String value) {
        if (isAsync) {
            producer.send(new ProducerRecord<>(topic, key, value), new ProducerCallback(System.currentTimeMillis(), key, value));
        } else {
            try {
                producer.send(new ProducerRecord<>(topic, key, value)).get();
                System.out.println("--------->Sent message: (" + key.toString() + ", " + value + ")");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        producer.close();
    }

    class ProducerCallback implements Callback {
        private final long startTime;
        private final Integer key;
        private final String value;

        public ProducerCallback(long startTime, Integer key, String value) {
            this.startTime = startTime;
            this.key = key;
            this.value = value;
        }

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (metadata != null) {
                System.out.println(
                        "message(" + key + ", " + value + ") sent to partition(" + metadata.partition() +
                                "), " +
                                "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
            } else {
                exception.printStackTrace();
            }
        }
    }
}
