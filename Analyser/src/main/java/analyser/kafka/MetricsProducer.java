package analyser.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import agent.services.MetricResponse;
import analyser.dto.MetricDTO;
import analyser.dto.ProcessorDTO;

import java.util.stream.Collectors;

@Service
public class MetricsProducer {

    private final KafkaTemplate<String, MetricDTO> kafkaTemplate;

    public MetricsProducer(KafkaTemplate<String, MetricDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MetricResponse metric) {
        MetricDTO dto = new MetricDTO(
                metric.getCpuUsage(),
                metric.getRamUsage(),
                metric.getHostName(),
                metric.getProcessorsList().stream()
                        .map(p -> new ProcessorDTO(p.getName(), p.getLogicalCores(), p.getPhysicalCores()))
                        .collect(Collectors.toList()));

        kafkaTemplate.send("metrics-topic", dto);
    }
}
