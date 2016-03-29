package com.monitor.pojo;


public class connectorPojo {
    private String name;
    private long request_maxTime;
    private long request_processingTime;
    private long request_requestCount;
    private long request_errorCount;
    private long request_bytesReceived;
    private long request_bytesSent;
    private int thread_maxThread;
    private int thread_currentThreadCount;
    private int thread_currentThreadBusy;
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    public long getRequest_maxTime() {
        return request_maxTime;
    }

    
    public void setRequest_maxTime(long request_maxTime) {
        this.request_maxTime = request_maxTime;
    }

    
    public long getRequest_processingTime() {
        return request_processingTime;
    }

    
    public void setRequest_processingTime(long request_processingTime) {
        this.request_processingTime = request_processingTime;
    }

    
    public long getRequest_requestCount() {
        return request_requestCount;
    }

    
    public void setRequest_requestCount(long request_requestCount) {
        this.request_requestCount = request_requestCount;
    }

    
    public long getRequest_errorCount() {
        return request_errorCount;
    }

    
    public void setRequest_errorCount(long request_errorCount) {
        this.request_errorCount = request_errorCount;
    }

    
    public long getRequest_bytesReceived() {
        return request_bytesReceived;
    }

    
    public void setRequest_bytesReceived(long request_bytesReceived) {
        this.request_bytesReceived = request_bytesReceived;
    }

    
    public long getRequest_bytesSent() {
        return request_bytesSent;
    }

    
    public void setRequest_bytesSent(long request_bytesSent) {
        this.request_bytesSent = request_bytesSent;
    }

    
    public int getThread_maxThread() {
        return thread_maxThread;
    }

    
    public void setThread_maxThread(int thread_maxThread) {
        this.thread_maxThread = thread_maxThread;
    }

    
    public int getThread_currentThreadCount() {
        return thread_currentThreadCount;
    }

    
    public void setThread_currentThreadCount(int thread_currentThreadCount) {
        this.thread_currentThreadCount = thread_currentThreadCount;
    }

    
    public int getThread_currentThreadBusy() {
        return thread_currentThreadBusy;
    }

    
    public void setThread_currentThreadBusy(int thread_currentThreadBusy) {
        this.thread_currentThreadBusy = thread_currentThreadBusy;
    }
    
    
}
