package api.grpc;

import agent.services.MetricsServiceGrpc;
import agent.services.MetricsServiceGrpc.MetricsServiceBlockingStub;
import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public MetricsServiceBlockingStub agentBlockingStub(GrpcChannelFactory channelFactory) {
        Channel channel = channelFactory.createChannel("agentClient");
        return MetricsServiceGrpc.newBlockingStub(channel);
    }
}