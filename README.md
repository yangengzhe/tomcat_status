# Manager status Tomcat中WEB应用的监控

利用Tomcat自带的应用Manager进行应用以及jvm虚拟机的运行监控

使用帮助：http://www.i3geek.com/archives/1126

## 用法

1. 配置tomcat的conf，增加manager的用户权限
2. 利用用户名和密码登陆`http://localhost:8080/manager/status?XML=true`
3. 通过对xml的解析，实现多运行数据的获取
4. 两次查询，通过将总处理时间processingTime差 / 请求数requestCount差，得出平均每个请求处理的时间

## 作用

自动化脚本，检查应用状态时的相关数据，如平均响应时间、传输速度以及JVM虚拟机的使用情况

## 源码说明

	com.monitor
		|-common.util
		|	|-parseXML.java
		|		解析XML的工具类
		|-pojo
		|	|-connectorPojo.java
		|	|	一个容器的Pojo
		|	|-statusPojo.java
		|		整个status的pojo
		|-main.java
		|	主函数
		|-status.java
			读取，操作解析status