package analyser.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc;
import agent.services.MetricsServiceGrpc.MetricsServiceStub;
import analyser.kafka.MetricsProducer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

@Service
public class AgentMetricsClient {

    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsServiceStub stub;
    private final MetricsProducer producer;

    private boolean streaming = false;

    public AgentMetricsClient(
            MetricsProducer producer,
            @Value("${grpc.analyser.host}") String grpcHost,
            @Value("${grpc.analyser.port}") int grpcPort) {
        this.producer = producer;

        log.info("Conectando no gRPC {}:{}", grpcHost, grpcPort);

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();

        this.stub = MetricsServiceGrpc.newStub(channel);
    }

    public boolean isStubAvailable() {
        return stub != null;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void startStream() {
        if (streaming)
            return;
        streaming = true;

        MetricRequest request = MetricRequest.newBuilder()
                .setHost("analyser")
                .build();

        log.info("Iniciando stream gRPC com o Agent...");

        stub.streamMetrics(request, new StreamObserver<MetricResponse>() {

            @Override
            public void onNext(MetricResponse metric) {
                producer.send(metric);
                log.debug(
                        "Métrica recebida do host {}: CPU {}%, RAM {}%",
                        metric.getHostName(),
                        metric.getCpuUsage(),
                        metric.getRamUsage());
            }

            @Override
            public void onError(Throwable t) {
                streaming = false;
                log.error("Stream gRPC caiu, retry automático", t);
            }

            @Override
            public void onCompleted() {
                streaming = false;
                log.info("Stream gRPC finalizado");
            }
        });
    }
}
