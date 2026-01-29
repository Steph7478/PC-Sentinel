package api.kafka.consumer;

import api.dto.ProcessInfoDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class GraphQLInfoConsumer {

    private final AtomicReference<ProcessInfoDTO> lastInfo = new AtomicReference<>();

    @KafkaListener(topics = "metrics-info-topic", groupId = "api-info-group")
    public void consume(ProcessInfoDTO dto) {
        System.out.println("KAFKA: ProcessInfo recebido");
        lastInfo.set(dto);
    }

    public ProcessInfoDTO getLatestInfo() {
        return lastInfo.get();
    }
}
