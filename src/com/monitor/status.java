package com.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import com.monitor.common.util.parseXML;
import com.monitor.pojo.connectorPojo;
import com.monitor.pojo.statusPojo;

public class status {
    public static statusPojo getStatus(String url,String name,String pass) throws IOException{
        statusPojo status = null;
        String result = getHtmlContext(url,name,pass);
        status = parseXML.getStatus(result);
        return status;
    }
   public static String getHtmlContext(String tempurl, String username, String password) throws IOException {
       URL url = null;
       BufferedReader breader = null;
       InputStream is = null;
       StringBuffer resultBuffer = new StringBuffer();
       try {
           url = new URL(tempurl);
           String userPassword = username + ":" + password;
           String encoding = new sun.misc.BASE64Encoder().encode (userPassword.getBytes());//在classpath中添加rt.jar包，在%java_home%/jre/lib/rt.jar

           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestProperty("Authorization", "Basic " + encoding);
           is = conn.getInputStream();
           breader = new BufferedReader(new InputStreamReader(is));
           String line = "";
           while ((line = breader.readLine()) != null) {
               resultBuffer.append(line);
           }
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } finally {
           if(breader != null)
               breader.close();
           if(is != null)
               is.close();
       }
       return resultBuffer.toString();
   }
   public static connectorPojo getConnector(statusPojo tomcat_status,String port){
       connectorPojo con = null;
       for (connectorPojo connector : tomcat_status.getConnectorPojos()) {
           if(connector.getName().contains(port)){
               con = connector;
               break;
           }
       }
       return con;
   }
   /**
    * 
    * @param url
    * @param port
    * @param user status的用户名
    * @param pass status打密码
    * @return 元素0，根据status所得的吞吐率，-1时为无程序运行 0时认为宕机
    *         元素1，程序自身计算所得的吞吐率
    *         单位均为 个/秒
    * @author ygz 下午4:44:31
    */
   public static float[] getThoughput(String url,String port,String user,String pass) {
       float[] result = new float[2];
       try {
           Long Rstart,Rend;//请求数
           Long start_time,end_time,spend_time;//时间记录
           spend_time=System.currentTimeMillis();
           statusPojo tomcat_status = status.getStatus(url,user,pass);
           spend_time=System.currentTimeMillis()-spend_time;
           connectorPojo connector = status.getConnector(tomcat_status, port);
           Rstart = connector.getRequest_requestCount();
           start_time = connector.getRequest_processingTime();
           Thread.sleep(3000);//3 秒检测
           spend_time=System.currentTimeMillis()-spend_time;
           tomcat_status = status.getStatus(url,user,pass);
           spend_time=System.currentTimeMillis()-spend_time;
           connector = status.getConnector(tomcat_status, port);
           Rend = connector.getRequest_requestCount();
           end_time=connector.getRequest_processingTime();
           float total_time = (end_time-start_time)/1000f;
           result[0] = total_time/(Rend-Rstart);//平均处理时间
           result[1] = spend_time/2000f;//程序监控时间吞吐率
           if(connector.getThread_currentThreadBusy()<=1)
               result[0]=-1;//没有线程在运行
       }
       catch(Exception e){
           System.err.println(e);
           result[0]=0;
           result[1]=0;
       }
       return result;
   }
}
