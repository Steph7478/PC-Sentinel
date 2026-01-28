package analyser.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc;
import analyser.kafka.producer.MetricsProducer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

@Service
public class AgentMetricsClient {

    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsProducer producer;
    private final String grpcHost;
    private final int grpcPort;

    private ManagedChannel channel;
    private boolean streaming = false;

    public AgentMetricsClient(MetricsProducer producer,
            @Value("${grpc.analyser.host}") String grpcHost,
            @Value("${grpc.analyser.port}") int grpcPort) {
        this.producer = producer;
        this.grpcHost = grpcHost;
        this.grpcPort = grpcPort;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void startStream() {
        if (streaming)
            return;

        log.info("Conectando no gRPC {}:{}", grpcHost, grpcPort);

        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            channel = ManagedChannelBuilder
                    .forAddress(grpcHost, grpcPort)
                    .usePlaintext()
                    .enableRetry()
                    .maxRetryAttempts(5)
                    .build();
        }

        streaming = true;

        MetricsServiceGrpc.newStub(channel)
                .streamMetrics(MetricRequest.newBuilder().setHost("analyser").build(),
                        new StreamObserver<MetricResponse>() {
                            @Override
                            public void onNext(MetricResponse metric) {
                                producer.sendUsage(metric);
                                producer.sendProcessInfo(metric);
                            }

                            @Override
                            public void onError(Throwable t) {
                                streaming = false;
                                log.warn("Stream gRPC caiu, será re-tentado pelo scheduler", t);
                                shutdownChannel();
                            }

                            @Override
                            public void onCompleted() {
                                streaming = false;
                                log.info("Stream gRPC finalizado pelo servidor");
                                shutdownChannel();
                            }
                        });
    }

    private void shutdownChannel() {
        if (channel != null && !channel.isShutdown()) {
            channel.shutdown();
            channel = null;
        }
    }
}
