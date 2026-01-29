package api.grpc;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc.MetricsServiceBlockingStub;
import org.springframework.stereotype.Service;

@Service
public class AgentDiscoveryClient {
    private final MetricsServiceBlockingStub blockingStub;

    public AgentDiscoveryClient(MetricsServiceBlockingStub agentClient) {
        this.blockingStub = agentClient;
    }

    public MetricResponse getPCIdentification() {
        var iter = blockingStub.streamMetrics(MetricRequest.newBuilder().setHost("api").build());
        return iter.hasNext() ? iter.next() : null;
    }
}