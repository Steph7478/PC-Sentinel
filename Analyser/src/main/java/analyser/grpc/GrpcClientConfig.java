package analyser.grpc;

import agent.services.MetricsServiceGrpc;
import agent.services.MetricsServiceGrpc.MetricsServiceStub;
import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcClientConfig {

    @Bean
    public MetricsServiceStub agentClient(GrpcChannelFactory channelFactory) {
        Channel channel = channelFactory.createChannel("agentClient");
        return MetricsServiceGrpc.newStub(channel);
    }
}