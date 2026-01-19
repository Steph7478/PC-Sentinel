package gRPC.services;

import java.net.InetAddress;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import org.springframework.stereotype.Service;

@Service
public class OshiService {
    private SystemInfo systemInfo = new SystemInfo();

    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private final GlobalMemory memory = systemInfo.getHardware().getMemory();
    private long[] prevTicks = processor.getSystemCpuLoadTicks();

    protected float getCpu() {
        float load = (float) (processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100);
        prevTicks = processor.getSystemCpuLoadTicks();
        return Math.round(load * 100f) / 100f;
    }

    protected float getRam() {
        long total = memory.getTotal();
        long available = memory.getAvailable();
        float usage = (float) (((double) (total - available) / total) * 100);
        return Math.round(usage * 100f) / 100f;
    }

    protected String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
