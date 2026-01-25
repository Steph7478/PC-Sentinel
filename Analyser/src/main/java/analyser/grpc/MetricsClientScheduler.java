package analyser.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MetricsClientScheduler {

    private static final Logger log = LoggerFactory.getLogger(MetricsClientScheduler.class);

    private final AgentMetricsClient client;

    public MetricsClientScheduler(AgentMetricsClient client) {
        this.client = client;
    }

    @Scheduled(fixedDelay = 2000)
    public void tryStartStream() {
        if (!client.isStreaming()) {
            log.debug("Tentativa de iniciar stream gRPC...");
            client.startStream();
        }
    }
}
