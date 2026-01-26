package analyser.dto;

public class ProcessorDTO {
    private String name;
    private int logicalCores;
    private int physicalCores;

    public ProcessorDTO() {
    }

    public ProcessorDTO(String name, int logicalCores, int physicalCores) {
        this.name = name;
        this.logicalCores = logicalCores;
        this.physicalCores = physicalCores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogicalCores() {
        return logicalCores;
    }

    public void setLogicalCores(int logicalCores) {
        this.logicalCores = logicalCores;
    }

    public int getPhysicalCores() {
        return physicalCores;
    }

    public void setPhysicalCores(int physicalCores) {
        this.physicalCores = physicalCores;
    }
}
