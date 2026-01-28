package analyser.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import agent.services.MetricResponse;
import analyser.dto.MetricDTO;
import analyser.dto.ProcessInfoDTO;

@Service
public class MetricsProducer {

    private final KafkaTemplate<String, MetricDTO> usageKafkaTemplate;
    private final KafkaTemplate<String, ProcessInfoDTO> infoKafkaTemplate;

    public MetricsProducer(
            KafkaTemplate<String, MetricDTO> usageKafkaTemplate,
            KafkaTemplate<String, ProcessInfoDTO> infoKafkaTemplate) {
        this.usageKafkaTemplate = usageKafkaTemplate;
        this.infoKafkaTemplate = infoKafkaTemplate;
    }

    public void sendUsage(MetricResponse metric) {
        MetricDTO dto = new MetricDTO(metric.getCpuUsage(), metric.getRamUsage());
        usageKafkaTemplate.send("metrics-usage-topic", dto);
    }

    public void sendProcessInfo(MetricResponse metric) {
        var p = metric.getProcessors();

        ProcessInfoDTO dto = new ProcessInfoDTO(
                metric.getHostName(),
                p.getName(),
                p.getLogicalCores(),
                p.getPhysicalCores());

        infoKafkaTemplate.send("metrics-info-topic", dto);
    }
}