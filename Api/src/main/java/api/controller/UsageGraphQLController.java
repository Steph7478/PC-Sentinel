package api.controller;

import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import api.dto.MetricDTO;
import api.kafka.consumer.GraphQLUsageConsumer;

@Controller
public class UsageGraphQLController {

    private final GraphQLUsageConsumer usageConsumer;

    public UsageGraphQLController(GraphQLUsageConsumer usageConsumer) {
        this.usageConsumer = usageConsumer;
    }

    @SubscriptionMapping
    public Flux<MetricDTO> streamUsage() {
        return usageConsumer.getUsageFlux();
    }
}