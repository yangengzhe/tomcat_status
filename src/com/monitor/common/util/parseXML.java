package com.monitor.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.monitor.status;
import com.monitor.pojo.connectorPojo;
import com.monitor.pojo.statusPojo;

public class parseXML {
    public static statusPojo getStatus(String result){
        Document document = null;
        statusPojo status = null;
        try {
            document = DocumentHelper.parseText(result);//将字符串转化为XML的Document
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        status = new statusPojo();
        //对jvm结点的操作
        Element rootElement = document.getRootElement();
        Element element=rootElement.element("jvm");
        element=element.element("memory");
        //对属性的操作
        status.setJvm_memory_free(Long.parseLong(element.attribute("free").getText()));
        status.setJvm_memory_total(Long.parseLong(element.attribute("total").getText()));
        status.setJvm_memory_max(Long.parseLong(element.attribute("max").getText()));
        //对connector节点的操作
        List<Element> connectors=rootElement.elements("connector");
        List<connectorPojo> conns = new ArrayList<connectorPojo>();
        for (Element connector : connectors) {
            connectorPojo conn = new connectorPojo();
            conn.setName(connector.attribute("name").getText());
            Element threadInfo = connector.element("threadInfo");
            conn.setThread_currentThreadBusy(Integer.parseInt(threadInfo.attribute("currentThreadsBusy").getText()));
            conn.setThread_currentThreadCount(Integer.parseInt(threadInfo.attribute("currentThreadCount").getText()));
            conn.setThread_maxThread(Integer.parseInt(threadInfo.attribute("maxThreads").getText()));
            Element requestInfo = connector.element("requestInfo");
            conn.setRequest_maxTime(Long.parseLong(requestInfo.attribute("maxTime").getText()));
            conn.setRequest_processingTime(Long.parseLong(requestInfo.attribute("processingTime").getText()));
            conn.setRequest_requestCount(Long.parseLong(requestInfo.attribute("requestCount").getText()));
            conn.setRequest_errorCount(Long.parseLong(requestInfo.attribute("errorCount").getText()));
            conn.setRequest_bytesReceived(Long.parseLong(requestInfo.attribute("bytesReceived").getText()));
            conn.setRequest_bytesSent(Long.parseLong(requestInfo.attribute("bytesSent").getText()));
            conns.add(conn);
        }
        status.setConnectorPojos(conns);
        return status;
    }
}
