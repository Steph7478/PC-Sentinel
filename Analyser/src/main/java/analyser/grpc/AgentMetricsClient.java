package analyser.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import agent.services.MetricRequest;
import agent.services.MetricResponse;
import agent.services.MetricsServiceGrpc;
import agent.services.MetricsServiceGrpc.MetricsServiceStub;
import analyser.kafka.MetricsProducer;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;

@Service
public class AgentMetricsClient {

    private static final Logger log = LoggerFactory.getLogger(AgentMetricsClient.class);

    private final MetricsServiceStub stub;
    private final MetricsProducer producer;

    public AgentMetricsClient(MetricsProducer producer) {
        this.producer = producer;

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("NODE_IP", 31090)
                .usePlaintext()
                .build();

        this.stub = MetricsServiceGrpc.newStub(channel);
    }

    @PostConstruct
    public void start() {
        MetricRequest request = MetricRequest.newBuilder()
                .setHost("analyser")
                .build();

        stub.streamMetrics(request, new StreamObserver<MetricResponse>() {

            @Override
            public void onNext(MetricResponse metric) {
                producer.send(metric);
                log.debug("Métrica recebida do host {}: CPU {}%",
                        metric.getHostName(), metric.getCpuUsage());
            }

            @Override
            public void onError(Throwable t) {
                log.error("Stream caiu", t);
            }

            @Override
            public void onCompleted() {
                log.info("Stream finalizado");
            }
        });
    }
}
