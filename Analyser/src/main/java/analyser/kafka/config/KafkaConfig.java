package analyser.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import analyser.dto.MetricDTO;
import analyser.dto.ProcessInfoDTO;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaTemplate<String, MetricDTO> usageKafkaTemplate(ProducerFactory<String, MetricDTO> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public KafkaTemplate<String, ProcessInfoDTO> infoKafkaTemplate(ProducerFactory<String, ProcessInfoDTO> pf) {
        return new KafkaTemplate<>(pf);
    }
}