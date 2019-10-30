package com.rabbit.producer;

import java.io.IOException;

import com.rabbit.until.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
	
	private final static String QUEUE_MESSAGE  = "test_message";
	
	private final static String WORK_MESSAGE = "work模式";
	
	private final static String EXCHAGE_NAME = "交换机名称";
	
	private final static String DIRECT_NAME = "路由器名称";
	
	private final static String TOPIC_NAME ="";
	
	public static void main(String[] args) throws IOException, InterruptedException {
		//Send send = new Send();
		//send.createSimpleConnection();
		//workConnection();//work模式
		//subscribeGetConnection();//订阅模式
		directGetConnection();//交换机模式
	}
	
	/**
	 * 主题模式(通配符模式)
	 * 同一个消息被多个消费者获取，一个消息队列可以有多个消费者实列，其中只有一个消费者实列会收到消息
	 * @throws IOException 
	 * */
	public static void topicGetConnection() throws IOException{
		//连接MQ
		Connection connection = ConnectionUtil.getConnection();
		
		//创建通道
		Channel channel = connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHAGE_NAME, "topic");
		
		//消息内容
		String context = "主题模式";
		
		//发布消息
		channel.basicPublish(EXCHAGE_NAME, "titile.1", null, context.getBytes());
		System.out.println("发布的消息为:"+context);
		channel.close();
		connection.close();
	}
	
	/**
	 * 路由器模式(也是一种交换机模式）direct
	 * 根据路由的key，找到消费队列。
	 *缺陷：路由表必须明确，不能匹配，比如模糊匹配，可以通过topic解决，下篇分析topic模式。)
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 * */
	public static void directGetConnection() throws IOException, InterruptedException{
		
		//连接MQ
		Connection connection = ConnectionUtil.getConnection();
		
		//创建通道
		Channel channel = connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(DIRECT_NAME, "direct");
		
		
		
		for(int i =1;i<100;i++){
			//消息内容
			String message = "路由器模式模式"+i;
			//发布消息
			channel.basicPublish(DIRECT_NAME, "create", null, message.getBytes());			
			System.out.println("路由器模式发送的内容为:" + message);
			Thread.sleep(i*10);
		}
		
		channel.close();
		connection.close();
	}
	
	
	/**
	 * 订阅模式(交换机模式)  subscribe
	 * 交换机将消息送达队列主要有fanout，topic， headers，direct方式，
	 * 队列绑定交换机时需要绑定路由的key，只有绑定对应key的队列能收到来自交换机的消息。
	 * 		-->队列-->消费者
	 * p-->x(交换机)	
	 * 		-->队列-->消费者
	 * a、一个生产者，多个消费者
	 * b、每个消费者都有自己的队列
	 * c、生产者没有将消息直接发送到队列，而是先发送到了交换机
	 * d、每个队列都要绑定到交换机
	 * e、生产者发送的消息，经过交换机，到达队列，实现一个消息被多个消费者获取的目的
	 * 注意:一个消费者队列可以有多个消费者实列，其中只有一个消费者实列会消费
	 * 注意：消息发送到没有队列绑定的交换机时，消息将丢失，因为，交换机没有存储消息的能力，消息只能存在在队列中。
	 * @throws IOException 
	 * @throws InterruptedException 
	 * */
	public static Connection subscribeGetConnection() throws IOException, InterruptedException{
		
		//对mq进行连接
		Connection connection = ConnectionUtil.getConnection();
		
		//创建mq通道
		Channel channel = connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHAGE_NAME, "fanout");
		
		for(int i =1;i<150;i++){
			//消息内容
			String message = "订阅模式"+i;
			//进行消息发布
			channel.basicPublish(EXCHAGE_NAME, "", null, message.getBytes());
			System.out.println("发送的消息内容为:" + message);
			Thread.sleep(i*10);
		}
	
		channel.close();
		connection.close();
	
		
		return connection;
	}
	
	/**
	 * work模式
	 * 一个生产者，两个消费者，一个消息只能被一个生产者获取
	 * 	 		-->消费者1
	 * p-->队列
	 * 			-->消费者2
	 * 
	 *向队列发送一百条消息
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 * */
	public static Connection workConnection() throws IOException, InterruptedException{
		//连接创建MQ
		Connection connection = ConnectionUtil.getConnection();
		
		//创建连接通道
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(WORK_MESSAGE, false, false, false, null);
		
		for(int i = 0;i<100;i++){
			//消息内容
			String message = "第"+i+"条";
			//发布消息
			channel.basicPublish("", WORK_MESSAGE, null, message.getBytes());
			System.out.println("发布的消息为:"+message);
			//进行休眠
			Thread.sleep(i*10);
		
			
		}
		channel.close();
		connection.close();
		return connection;
	}
	
	
	
	/**
	 * 简单队列模式连接
	 * 生产者将消息发送到队列，消费者从队列获取信息
	 * P-->队列-->C
	 * P是生产者
	 * C是消费者
	 * 
	 * */
	public Connection createSimpleConnection() throws IOException{
		//获取到连接以及MQ通道
		Connection connection = ConnectionUtil.getConnection();
		
		//从连接中创建通道
		Channel channel = connection.createChannel();
		
		
		//声明创建队列(资源包里面的队列消息储备有足够的了解消息)
		channel.queueDeclare(QUEUE_MESSAGE,false,false,false,null);
		
		//消息内容
		String message = "hello rabbit";
		
		//消息生产者发送消息给消息队列 (资源包里面的队列消息储备有足够的了解消息)
		channel.basicPublish("", QUEUE_MESSAGE, null, message.getBytes());
		System.out.println("发送的消息为:"+message);
		
		//关闭通道和关闭连接
		channel.close();
		connection.close();
		return connection;
	}

}
