package analyser.kafka.consumer;

import analyser.dto.ProcessInfoDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProcessInfoConsumer {
    private final AtomicReference<ProcessInfoDTO> infoCache = new AtomicReference<>();

    @KafkaListener(topics = "metrics-info-topic", groupId = "graphql-info")
    public void consumeInfo(ProcessInfoDTO info) {
        infoCache.compareAndSet(null, info);
    }

    public ProcessInfoDTO getInfo() {
        return infoCache.get();
    }
}