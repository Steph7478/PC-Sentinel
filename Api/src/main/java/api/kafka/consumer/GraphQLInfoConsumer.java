package api.kafka.consumer;

import api.dto.ProcessInfoDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GraphQLInfoConsumer {

    private ProcessInfoDTO lastData;

    @KafkaListener(topics = "metrics-info-topic", groupId = "api-info-group")
    public void consume(ProcessInfoDTO dto) {
        this.lastData = dto;
    }

    public Mono<ProcessInfoDTO> getInfo() {
        return Mono.justOrEmpty(this.lastData);
    }
}