package com.monitor;


public class main {

    public static void main(String[] args) {
            float[] thoughput =status.getThoughput("http://192.168.191.1:8080/manager/status?XML=true","8080", "admin", "123456");
            System.out.println(thoughput[0]+" "+thoughput[1]);
    }

}
