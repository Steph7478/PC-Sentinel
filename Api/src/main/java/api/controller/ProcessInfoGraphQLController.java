package api.controller;

import api.dto.ProcessInfoDTO;
import api.kafka.consumer.GraphQLInfoConsumer;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ProcessInfoGraphQLController {

    private final GraphQLInfoConsumer infoConsumer;

    public ProcessInfoGraphQLController(GraphQLInfoConsumer infoConsumer) {
        this.infoConsumer = infoConsumer;
    }

    @QueryMapping
    public Mono<ProcessInfoDTO> processInfo() {
        return infoConsumer.getInfo();
    }
}
