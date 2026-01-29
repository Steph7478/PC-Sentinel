package api.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import api.dto.ProcessInfoDTO;
import api.grpc.AgentDiscoveryClient;
import api.kafka.consumer.GraphQLInfoConsumer;
import agent.services.MetricResponse;

@Controller
public class ProcessInfoGraphQLController {
    private final GraphQLInfoConsumer infoConsumer;
    private final AgentDiscoveryClient discoveryClient;

    public ProcessInfoGraphQLController(GraphQLInfoConsumer infoConsumer, AgentDiscoveryClient discoveryClient) {
        this.infoConsumer = infoConsumer;
        this.discoveryClient = discoveryClient;
    }

    @QueryMapping
    public ProcessInfoDTO processInfos() {
        ProcessInfoDTO latest = infoConsumer.getLatestInfo();
        if (latest != null)
            return latest;

        MetricResponse res = discoveryClient.getPCIdentification();
        return new ProcessInfoDTO(
                res.getHostName(),
                res.getProcessors().getName(),
                res.getProcessors().getLogicalCores(),
                res.getProcessors().getPhysicalCores());
    }
}