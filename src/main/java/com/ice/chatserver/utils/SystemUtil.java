package com.ice.chatserver.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class SystemUtil {
    private static OperatingSystemMXBean operatingSystemMXBean;
    
    static {
        operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    }
    
    public static double getSystemCpuLoad() {
        return operatingSystemMXBean.getSystemCpuLoad();
    }
    
    public static int getSystemCpuCount() {
        return Runtime.getRuntime().availableProcessors();
    }
    
    public static double getSystemMemLoad() {
        double totalMem = operatingSystemMXBean.getTotalPhysicalMemorySize();
        double freeMem = operatingSystemMXBean.getFreePhysicalMemorySize();
        return (totalMem - freeMem) / totalMem;
    }
}