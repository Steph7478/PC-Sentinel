package analyser.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import agent.services.MetricResponse;

@Service
public class MetricsProducer {

    private final KafkaTemplate<String, MetricResponse> kafkaTemplate;

    public MetricsProducer(KafkaTemplate<String, MetricResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MetricResponse metric) {
        kafkaTemplate.send("metrics-topic", metric);
    }
}
