package api.kafka.consumer;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import api.dto.ProcessInfoDTO;

@Service
public class GraphQLInfoConsumer {
    private final AtomicReference<ProcessInfoDTO> lastInfo = new AtomicReference<>();

    @KafkaListener(topics = "metrics-info-topic", groupId = "graphql-api-info")
    public void consume(ProcessInfoDTO dto) {
        lastInfo.set(dto);
    }

    public ProcessInfoDTO getLatestInfo() {
        return lastInfo.get();
    }
}