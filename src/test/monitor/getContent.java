package test.monitor;

import java.io.IOException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.monitor.status;
import com.monitor.common.util.parseXML;
import com.monitor.pojo.statusPojo;

public class getContent {
    @Test
    public void test(){
        //http://blog.csdn.net/redarmy_chen/article/details/12969219
        String result = "";
        Document document = null;//引入org.dom4j包
        try {
            result = status.getHtmlContext("http://192.168.191.1:8080/manager/status?XML=true", "admin", "123456");
            document = DocumentHelper.parseText(result);//将字符串转化为XML的Document
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //对xml结点的操作
        Element rootElement = document.getRootElement();
        Element element=rootElement.element("jvm");
        element=element.element("memory");
        //对属性的操作
        Attribute attribute=element.attribute("max");
        String text=attribute.getText();  

//        System.out.println(text);
        
        statusPojo status = parseXML.getStatus(result);
        System.out.println();
    }
    
}
