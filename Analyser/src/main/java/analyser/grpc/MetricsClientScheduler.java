package analyser.grpc;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MetricsClientScheduler {

    private final AgentMetricsClient client;

    public MetricsClientScheduler(AgentMetricsClient client) {
        this.client = client;
    }

    @Scheduled(fixedDelay = 2000)
    public void tryStartStream() {
        if (!client.isStreaming() && client.isStubAvailable()) {
            client.startStream();
        }
    }
}
