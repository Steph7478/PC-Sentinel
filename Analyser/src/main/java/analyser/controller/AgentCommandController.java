package analyser.controller;

import analyser.grpc.AgentMetricsClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/agent")
public class AgentCommandController {
    private final AgentMetricsClient metricsClient;

    public AgentCommandController(AgentMetricsClient metricsClient) {
        this.metricsClient = metricsClient;
    }

    @PostMapping("/start")
    public void start() {
        metricsClient.startStream();
    }
}