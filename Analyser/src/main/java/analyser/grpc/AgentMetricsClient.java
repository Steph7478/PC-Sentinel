package analyser.grpc;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc.MetricsServiceStub;
import analyser.kafka.producer.MetricsProducer;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AgentMetricsClient {

    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsProducer producer;
    private final MetricsServiceStub metricsStub;
    private volatile boolean streaming = false;

    public AgentMetricsClient(MetricsProducer producer, MetricsServiceStub agentClient) {
        this.producer = producer;
        this.metricsStub = agentClient;
    }

    @PostConstruct
    public void autoStart() {
        log.info("ANALYSER: Pod subiu, iniciando stream gRPC com Agent...");
        startStream();
    }

    public synchronized void startStream() {
        if (streaming) {
            log.info("ANALYSER: Stream já ativo, ignorando start");
            return;
        }

        streaming = true;

        var request = MetricRequest.newBuilder()
                .setHost("analyser")
                .build();

        metricsStub.streamMetrics(request, new StreamObserver<>() {

            @Override
            public void onNext(MetricResponse metric) {
                producer.sendUsage(metric);
                if (metric.hasProcessors()) {
                    producer.sendProcessInfo(metric);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("ANALYSER: Erro no stream gRPC", t);
                streaming = false;
            }

            @Override
            public void onCompleted() {
                log.warn("ANALYSER: Stream gRPC finalizado pelo Agent");
                streaming = false;
            }
        });
    }

    public boolean isStreaming() {
        return streaming;
    }
}
