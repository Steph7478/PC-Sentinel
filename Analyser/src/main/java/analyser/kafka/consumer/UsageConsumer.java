package analyser.kafka.consumer;

import analyser.dto.MetricDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Service
public class UsageConsumer {

    private final Many<MetricDTO> usageSink = Sinks.many().multicast().onBackpressureBuffer();

    @KafkaListener(topics = "metrics-usage-topic", groupId = "graphql-usage")
    public void consumeUsage(MetricDTO metric) {
        usageSink.tryEmitNext(metric);
    }

    public Many<MetricDTO> getUsageSink() {
        return usageSink;
    }
}
