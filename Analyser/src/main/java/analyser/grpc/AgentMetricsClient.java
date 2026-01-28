package analyser.grpc;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc;
import analyser.kafka.producer.MetricsProducer;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AgentMetricsClient {
    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsProducer producer;

    @GrpcClient("agentClient")
    private MetricsServiceGrpc.MetricsServiceStub metricsStub;

    private boolean streaming = false;

    public AgentMetricsClient(MetricsProducer producer) {
        this.producer = producer;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void startStream() {
        if (streaming)
            return;

        log.info("Iniciando stream gRPC via @GrpcClient...");
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
                log.error("Erro no stream: {}", t.getMessage());
                streaming = false;
            }

            @Override
            public void onCompleted() {
                log.info("Stream finalizado.");
                streaming = false;
            }
        });
    }
}