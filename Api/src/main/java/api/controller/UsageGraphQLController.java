package api.controller;

import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import api.dto.MetricDTO;
import api.grpc.AgentDiscoveryClient;
import api.kafka.consumer.GraphQLUsageConsumer;

@Controller
public class UsageGraphQLController {
    private final GraphQLUsageConsumer usageConsumer;
    private final AgentDiscoveryClient discoveryClient;

    public UsageGraphQLController(GraphQLUsageConsumer usageConsumer, AgentDiscoveryClient discoveryClient) {
        this.usageConsumer = usageConsumer;
        this.discoveryClient = discoveryClient;
    }

    @SubscriptionMapping
    public Flux<MetricDTO> streamUsage() {
        discoveryClient.getPCIdentification();

        return usageConsumer.getUsageFlux();
    }
}