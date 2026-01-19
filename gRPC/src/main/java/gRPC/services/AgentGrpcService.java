package gRPC.services;

import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

import java.time.Duration;

@GrpcService
public class AgentGrpcService extends MetricsServiceGrpc.MetricsServiceImplBase {

        @Autowired
        private OshiService oshiService;

        @Override
        public void streamMetrics(MetricRequest request, StreamObserver<MetricResponse> responseObserver) {
                Flux.interval(Duration.ofSeconds(1))
                                .map(tick -> MetricResponse.newBuilder()
                                                .setCpuUsage(oshiService.getCpu())
                                                .setRamUsage(oshiService.getRam())
                                                .addAllProcessors(oshiService.getProcessorInfo())
                                                .setHostName(oshiService.getHostName())
                                                .build())
                                .subscribe(
                                                responseObserver::onNext,
                                                responseObserver::onError,
                                                responseObserver::onCompleted);
        }

}
