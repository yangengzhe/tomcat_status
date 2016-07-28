package com.new_monitor.sql;

import java.awt.Container;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 存储服务，1）判断IP 根据IP筛选主机后；2）根据应用名筛选 找到WebID；3）存储数据
 * @date 2016年7月28日 上午9:57:42
 * @author yangengzhe
 *
 */
public class sqlService {
    static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
  
    public static void sendStatus(String ip,String webapp,float throughput,long responsetime,int concurrent,long timestamp,float error) {
        int id = 0;
        try {  
//            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println("本地IP:"+ip);
            
//            ip = "172.29.131.123";
            sql = "select id from csp_vmitem where IP='"+ip+"'";//SQL语句  
            db1 = new DBHelper(sql);//创建DBHelper对象  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            String condition ="";
          //查找虚拟机
            while (ret.next()) {
                condition = condition + "vm="+ret.getString(1) +" OR "; 
            }
            if(condition == "") return;
            sql = "select id,loadfilename from clp_cloudapp where "+condition+"1=0";
            db1 = new DBHelper(sql);//创建DBHelper对象  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {//查找webID
                String war_name = ret.getString(2);
                war_name = war_name.substring(0,war_name.lastIndexOf("."));
                war_name = war_name.substring(war_name.indexOf('-')+1);
                if(war_name.equals(webapp)){
                    id = Integer.parseInt(ret.getString(1));
                    break;
                }
            }
            if(id == 0) return;
            //插入数据
            sql ="insert into cmp_appstatus values(null,"+id+","+throughput+","+responsetime+","+concurrent+","+timestamp+",null,"+error+")";
            System.out.println(sql);
            db1 = new DBHelper(sql);//创建DBHelper对象  
            db1.pst.executeUpdate();//执行语句，得到结果集
            ret.close();  
            db1.close();//关闭连接  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 

}
