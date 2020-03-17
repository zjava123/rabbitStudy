package com.rabbit.until;

import java.io.IOException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
	
	public static Connection getConnection() throws IOException{
		
		//定义工厂连接
		ConnectionFactory factory = new ConnectionFactory();


		//设置服务地址
		factory.setHost("localhost");
		
		//端口
		factory.setPort(5672);
		//设置账号信息，用户名，密码,vhost
		factory.setVirtualHost("rabbittest");
		factory.setUsername("zhonghui");
		factory.setPassword("a123456");
		
		//通过工程获取连接
		Connection connection = factory.newConnection();
		return connection;
		
	}
	public static void main(String[] args) throws IOException {
		Connection connection = getConnection();
		System.out.println(connection.getAddress());
		connection.close();
	}


}
