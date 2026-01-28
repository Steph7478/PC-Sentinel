package api.dto;

public record ProcessInfoDTO(String hostName, String processorName, int logicalCores, int physicalCores) {
}
