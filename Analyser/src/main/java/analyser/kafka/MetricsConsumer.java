package analyser.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import agent.services.MetricResponse;

@Service
public class MetricsConsumer {

    private static final Logger log = LoggerFactory.getLogger(MetricsConsumer.class);

    @KafkaListener(topics = "metrics-topic", groupId = "analyser")
    public void analyze(MetricResponse metric) {

        if (metric.getCpuUsage() > 90) {
            log.warn("🔥 CPU ALTA [{}]: {}%",
                    metric.getHostName(), metric.getCpuUsage());
        }
    }
}
