package api.controller;

import api.dto.MetricDTO;
import org.springframework.beans.factory.annotation.Autowired;
import api.kafka.consumer.GraphQLUsageConsumer;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class UsageGraphQLController {

    @Autowired
    private GraphQLUsageConsumer usageConsumer;

    @SubscriptionMapping
    public Flux<MetricDTO> streamUsage() {
        return usageConsumer.getUsageFlux();
    }
}
