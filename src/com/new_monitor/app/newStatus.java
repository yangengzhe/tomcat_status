package com.new_monitor.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.new_monitor.sql.sqlService;

/**
 * appmonitor.jar
 * @date 2016年7月28日 上午11:06:31
 * @author yangengzhe
 *
 */
public class newStatus {
    public static void main(String[] args) {
        String webapp_path = null,ip = null;
        try {//读配置文件
            InputStream in = new BufferedInputStream(new FileInputStream("config.properties"));   
            Properties p = new Properties();   
            p.load(in);  
            webapp_path = p.getProperty("webapp_path");
            ip = p.getProperty("ip");
            System.out.println(webapp_path);
            System.out.println(ip);
        } catch (IOException e) { 
            System.out.println("配置文件出错");
        } 
    	getAppStatus gThread = new getAppStatus(webapp_path,ip);
    	gThread.start();
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
    
    
    public static String add(String s1,String s2){
        StringBuffer res=new StringBuffer();
        if(s1.contains("-")&&s2.contains("-")){
            res.append("-");
            s1=s1.replace("-", "");
            s2=s2.replace("-", "");
        }else if((!s1.contains("-")&&s2.contains("-"))){
            return sub(s1,s2.replace("-", ""));
        }else if((s1.contains("-")&&!s2.contains("-"))){
            return sub(s2,s1.replace("-", ""));
        }
        
        int len =Math.abs(s1.length()-s2.length());
        if(s1.length()>s2.length()){
            s2=castSame(s2,len);
        }else{
            s1=castSame(s1,len);
        }
        int n=0;
        for(int i=s1.length()-1;i>=0;i--){
            int temp = (s1.charAt(i)-'0'+s2.charAt(i)-'0'+n);
            if(i==0){
                res.append(temp%10).append(temp/10==0?"":temp/10);
            }else{
                res.append(temp%10);
                n=temp/10;
            }    
        }
        return kickZero(String.valueOf(res.reverse()));
        
    }
    public static String sub(String s1,String s2){
        boolean flag = false;
        StringBuffer res=new StringBuffer();
        if(s1.contains("-")&&s2.contains("-")){
            return sub(s2.replace("-", ""),s1.replace("-", ""));
        
        }else if((!s1.contains("-")&&s2.contains("-"))){
            return add(s1,s2.replace("-", ""));
        }else if((s1.contains("-")&&!s2.contains("-"))){
            return "-"+add(s2,s1.replace("-", ""));
        }
        
        if(!isGreater(s1,s2)){
            flag = true;
            String temp =s1;
            s1=s2;
            s2=temp;
        }
        s2 = castSame( s2,s1.length()-s2.length());
        int n=0;
        for(int i=s1.length()-1;i>=0;i--){
            Integer temp = s1.charAt(i)-'0'-s2.charAt(i)+'0'-n;
            
            temp = i==0?(temp==0?null:temp):temp;
            res.append(temp==null?"":temp>=0?temp:temp+10);
            
            n=temp==null?0:temp<0?1:0;
            
        }
        String r = kickZero(flag?String.valueOf(res.append("-").reverse()):String.valueOf(res.reverse()));  
        if(r.equals("")) return "0";
        return r;
    }
    public static boolean isGreater(String s1,String s2){//890  235
        
        if(s1.length()>s2.length()){
            return true;
        }else if(s1.length()<s2.length()){
            return false;
        }else if(s1.length()==s2.length()){
            int i=0;
            while(i<s1.length()){
                if(s1.charAt(i)<s2.charAt(i)){
                    return false;
                }else if(s1.charAt(i)>s2.charAt(i)){
                    return true;
                }
                i++;
            }
        }
        return true;
        
    }
    public static String castSame(String s,int len){
        StringBuffer sb =new StringBuffer();
        for(int i=0;i<len;i++){
            sb.append("0");
        }
        s=String.valueOf(sb.append(s));
        return s;    
    }
    
    public static String kickZero(String s){
        int i=0;
        while(s.length()>1){
            if('0'==s.charAt(0)){
                s=s.substring(1);
            }else{
                break;
            }
        }
        return s;
        
    }
}
/**
 * 获得列表并检测
 * @date 2016年7月28日 上午9:19:15
 * @author yangengzhe
 *
 */
class getAppStatus extends Thread{
    String Webapp_path="d:/";
    String ip = "";
    ArrayList<String> sysApp=new ArrayList<String>();
    public getAppStatus(String Webapp_path,String ip){
        this.Webapp_path = Webapp_path;
        this.ip = ip;
        sysApp.add("docs");
        sysApp.add("examples");
        sysApp.add("host-manager");
        sysApp.add("manager");
        sysApp.add("probe");
        sysApp.add("ROOT");
    }
    @Override
    public void run() {
        ArrayList<String> apps;
        HashMap<String,Thread> threads = new HashMap<String,Thread>();
        while(true){
            apps = getList();
            for (String app : apps) {
                if(threads.containsKey(app) && threads.get(app)!=null && threads.get(app).getName().equals(app)){
                    continue;
                }
                statusThread st = new statusThread(app,ip);
                st.setName(app);
                st.start();
                threads.put(app,st);
                System.out.println("监控应用："+app);
            }
            try {
                Thread.sleep(60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList<String> getList(){
        ArrayList<String> app = new ArrayList<String>();
        File file=new File(Webapp_path);
        File[] tempList = file.listFiles();
        app.clear();
        for (int i = 0; i < tempList.length; i++) {
         if (tempList[i].isDirectory() && !sysApp.contains(tempList[i].getName())) {
          app.add(""+tempList[i].getName());
         }
        }
        return app;
    }
    
}
/**
 * 
 * 监控状态的线程类
 * @date 2016年7月28日 上午9:18:50
 * @author yangengzhe
 *
 */
class statusThread extends Thread{
    public String webapp = "probe";
    public String ip = "";
    public statusThread(String webapp,String ip){
        this.webapp = webapp;
        this.ip = ip;
    }
    @Override
    public void run() {
        try {
            String last_ResponseTime="0",last_Request="0",last_Error="0";
            Long ResponseTime=0l,Request=0l,Error=0l;
            String online_user="0";
            float perError;
            Long avg_ResponseTime;
            long timestamp;
            //吞吐率(个/分) Request; 平均响应时间(ms) avg_ResponseTime; 错误率 perError; 在线用户 online_user
            while(true){
                timestamp = System.currentTimeMillis();
                String str = newStatus.getHtmlContext("http://127.0.0.1:8080/probe/appprocdetails.ajax?webapp=/"+webapp,"admin","123456");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("\\s*", "");
                str = str.replaceAll("<[^>]+>", "");
                Matcher m =Pattern.compile("[^0-9:]").matcher(str);
                str = m.replaceAll("").trim();
                String[] strs = str.split(":");
              //处理时间
                ResponseTime = Long.valueOf(newStatus.sub(strs[1], last_ResponseTime));
                last_ResponseTime = strs[1];
                
                str = newStatus.getHtmlContext("http://127.0.0.1:8080/probe/appreqdetails.ajax?webapp=/"+webapp,"admin","123456");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("\\s*", "");
                str = str.replaceAll("<[^>]+>", "");
                m =Pattern.compile("[^0-9:]").matcher(str);
                str = m.replaceAll("").trim();
                strs = str.split(":");
              //请求数(吞吐率 个/分)
                Request = Long.valueOf(newStatus.sub(strs[1], last_Request));
                last_Request = strs[1];
              //错误数
                Error = Long.valueOf(newStatus.sub(strs[2], last_Error));
                last_Error = strs[2];
                
                str = newStatus.getHtmlContext("http://127.0.0.1:8080/probe/appruntimeinfo.ajax?webapp=/"+webapp,"admin","123456");
                str = str.replaceAll("&nbsp;", "");
                str = str.replaceAll("\\s*", "");
                str = str.substring(str.indexOf("Datasourceusage")+15);
                str = str.replaceAll("<[^>]+>", "");
                str = str.substring(0, str.indexOf("yes"));
                //在线人数
                online_user = str;
                //平均响应时间
                avg_ResponseTime = (Request == 0?0:(ResponseTime / Request));
                //错误率
                perError = (Request == 0?0:(Error*100 / Request));
                
                //发送到服务器
                System.out.println("应用名"+webapp+"吞吐率"+Request+"响应时间"+avg_ResponseTime+"错误率"+perError+"在线用户"+online_user);
                sqlService.sendStatus(ip,webapp,Request, avg_ResponseTime, Integer.parseInt(online_user), System.currentTimeMillis(), perError);
                while(System.currentTimeMillis() - timestamp <60*1000)
                    Thread.sleep(1000);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
