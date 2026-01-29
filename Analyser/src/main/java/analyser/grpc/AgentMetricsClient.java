package analyser.grpc;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc.MetricsServiceStub;
import analyser.kafka.producer.MetricsProducer;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AgentMetricsClient {
    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsProducer producer;
    private final MetricsServiceStub metricsStub;
    private boolean streaming = false;

    public AgentMetricsClient(MetricsProducer producer, MetricsServiceStub agentClient) {
        this.producer = producer;
        this.metricsStub = agentClient;
    }

    public void startStream() {
        if (streaming)
            return;

        log.info("Iniciando stream gRPC nativo...");
        streaming = true;

        var request = MetricRequest.newBuilder().setHost("analyser").build();

        metricsStub.streamMetrics(request, new StreamObserver<MetricResponse>() {
            @Override
            public void onNext(MetricResponse metric) {
                producer.sendUsage(metric);
                if (metric.hasProcessors()) {
                    producer.sendProcessInfo(metric);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("Erro no stream gRPC: {}", t.getMessage());
                streaming = false;
            }

            @Override
            public void onCompleted() {
                log.info("Stream gRPC finalizado pelo servidor.");
                streaming = false;
            }
        });
    }

    public boolean isStreaming() {
        return streaming;
    }
}