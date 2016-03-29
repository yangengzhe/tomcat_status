package com.monitor.pojo;

import java.util.List;

public class statusPojo {
    
    private long jvm_memory_free;
    private long jvm_memory_total;
    private long jvm_memory_max;
    private List<connectorPojo> connectorPojos;
    
    
    
    public long getJvm_memory_free() {
        return jvm_memory_free;
    }

    
    public void setJvm_memory_free(long jvm_memory_free) {
        this.jvm_memory_free = jvm_memory_free;
    }

    
    public long getJvm_memory_total() {
        return jvm_memory_total;
    }

    
    public void setJvm_memory_total(long jvm_memory_total) {
        this.jvm_memory_total = jvm_memory_total;
    }

    
    public long getJvm_memory_max() {
        return jvm_memory_max;
    }

    
    public void setJvm_memory_max(long jvm_memory_max) {
        this.jvm_memory_max = jvm_memory_max;
    }


    
    public List<connectorPojo> getConnectorPojos() {
        return connectorPojos;
    }


    
    public void setConnectorPojos(List<connectorPojo> connectorPojos) {
        this.connectorPojos = connectorPojos;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("jvm: ");
        sb.append("\n 剩余内存:");
        sb.append(jvm_memory_free);
        sb.append(" 总内存:");
        sb.append(jvm_memory_total);
        sb.append(" 最大内存:");
        sb.append(jvm_memory_max);
        sb.append("\ntomcat: ");
        sb.append("\n thread: ");
        sb.append(" 最大线程数:");
        sb.append(connectorPojos.get(1).getThread_maxThread());
        sb.append(" 当前线程数:");
        sb.append(connectorPojos.get(1).getThread_currentThreadCount());
        sb.append(" 当前繁忙线程数:");
        sb.append(connectorPojos.get(1).getThread_currentThreadBusy());
        sb.append("\n request: ");
        sb.append(" 最大处理时间:");
        sb.append(connectorPojos.get(1).getRequest_maxTime());
        sb.append(" 最短处理时间:");
        sb.append(connectorPojos.get(1).getRequest_processingTime());
        sb.append(" 请求数:");
        sb.append(connectorPojos.get(1).getRequest_requestCount());
        sb.append(" 错误数:");
        sb.append(connectorPojos.get(1).getRequest_errorCount());
        sb.append(" 接收字节:");
        sb.append(connectorPojos.get(1).getRequest_bytesReceived());
        sb.append(" 发送字节:");
        sb.append(connectorPojos.get(1).getRequest_bytesSent());
        return sb.toString();
    }
    
    
}
