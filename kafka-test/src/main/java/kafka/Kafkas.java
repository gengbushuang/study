package kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public abstract class Kafkas {

    abstract static class Builder<B extends Builder<B>> {
        Properties props = new Properties();

        String topic;

        /**
         * 不用设置所有得集群机器，设置3到5个就好，客户端会自动获取集群得元数据
         * @param servers
         * @return
         */
        public B bootstrapServers(String servers) {
            props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, servers);
            return self();
        }

        public B topic(String topic) {
            this.topic = topic;
            return self();
        }

        //返回this
        protected abstract B self();

        abstract Kafkas build();
    }
}
