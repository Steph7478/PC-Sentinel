package api.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import api.dto.MetricDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class GraphQLUsageConsumer {
    private final Sinks.Many<MetricDTO> usageSink = Sinks.many().multicast().onBackpressureBuffer();

    @KafkaListener(topics = "metrics-usage-topic", groupId = "graphql-api-usage")
    public void consume(MetricDTO dto) {
        usageSink.tryEmitNext(dto);
    }

    public Flux<MetricDTO> getUsageFlux() {
        return usageSink.asFlux();
    }
}