package agent.services;

import java.net.InetAddress;
import java.util.List;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import org.springframework.stereotype.Service;

@Service
public class OshiService {

    private final SystemInfo systemInfo = new SystemInfo();
    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private final GlobalMemory memory = systemInfo.getHardware().getMemory();
    private long[] prevTicks = processor.getSystemCpuLoadTicks();

    public float getCpu() {
        float load = (float) (processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100);
        prevTicks = processor.getSystemCpuLoadTicks();
        return Math.round(load * 100f) / 100f;
    }

    public float getRam() {
        long total = memory.getTotal();
        long available = memory.getAvailable();
        float usage = (total - available) * 100f / total;
        return Math.round(usage * 100f) / 100f;
    }

    public List<ProcessorInfo> getProcessorInfo() {
        int physicalCores = processor.getPhysicalProcessorCount();
        int logicalCores = processor.getLogicalProcessorCount();

        ProcessorInfo info = ProcessorInfo.newBuilder()
                .setName(processor.getProcessorIdentifier().getName())
                .setPhysicalCores(physicalCores)
                .setLogicalCores(logicalCores)
                .build();

        return List.of(info);
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
