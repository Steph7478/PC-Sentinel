package analyser.dto;

import java.util.List;

public class MetricDTO {
    private float cpuUsage;
    private float ramUsage;
    private String hostName;
    private List<ProcessorDTO> processors;

    public MetricDTO() {}

    public MetricDTO(float cpuUsage, float ramUsage, String hostName, List<ProcessorDTO> processors) {
        this.cpuUsage = cpuUsage;
        this.ramUsage = ramUsage;
        this.hostName = hostName;
        this.processors = processors;
    }

    public float getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(float cpuUsage) { this.cpuUsage = cpuUsage; }

    public float getRamUsage() { return ramUsage; }
    public void setRamUsage(float ramUsage) { this.ramUsage = ramUsage; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }

    public List<ProcessorDTO> getProcessors() { return processors; }
    public void setProcessors(List<ProcessorDTO> processors) { this.processors = processors; }
}
