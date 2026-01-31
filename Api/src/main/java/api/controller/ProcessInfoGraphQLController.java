package api.controller;

import api.dto.ProcessInfoDTO;
import api.kafka.consumer.GraphQLInfoConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ProcessInfoGraphQLController {

    @Autowired
    private GraphQLInfoConsumer infoConsumer;

    @QueryMapping
    public Mono<ProcessInfoDTO> processInfo() {
        return infoConsumer.getInfo();
    }
}
