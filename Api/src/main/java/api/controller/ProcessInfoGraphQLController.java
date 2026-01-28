package api.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import api.dto.ProcessInfoDTO;
import api.kafka.consumer.GraphQLInfoConsumer;

@Controller
public class ProcessInfoGraphQLController {

    private final GraphQLInfoConsumer infoConsumer;

    public ProcessInfoGraphQLController(GraphQLInfoConsumer infoConsumer) {
        this.infoConsumer = infoConsumer;
    }

    @QueryMapping
    public ProcessInfoDTO processInfos() {
        return infoConsumer.getLatestInfo();
    }
}