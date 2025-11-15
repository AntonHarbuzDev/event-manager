package school.sorokin.event_manager.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import school.sorokin.event_manager.model.EventKafka;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Long, EventKafka> kafkaKafkaTemplate(KafkaProperties kafkaProperties) {
        var props = kafkaProperties.buildProducerProperties(new DefaultSslBundleRegistry());
        ProducerFactory<Long, EventKafka> producerFactory = new DefaultKafkaProducerFactory<>(props);
        return new KafkaTemplate<>(producerFactory);
    }
}
