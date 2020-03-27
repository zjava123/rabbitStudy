package com.rabbit.consumer;

import java.io.IOException;

import com.rabbit.until.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class DirectGetMessage1 {
	
	private static final String QUEUE_MESSAGE = "路由器模式";
	
	private static final String DIRECT_NAME = "路由器名称";
	public static void main(String[] args) throws ShutdownSignalException, ConsumerCancelledException, IOException, InterruptedException {
		directConsumer();
		
	}

	
	
	public static void directConsumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		//连接mq
		Connection connection = ConnectionUtil.getConnection();
		
		//创建通道
		Channel channel = connection.createChannel();
		
		//声明队列
		channel.queueDeclare(QUEUE_MESSAGE,false,false,false,null);
		
		//绑定队列到交换机
		channel.queueBind(QUEUE_MESSAGE, DIRECT_NAME, "update");
		//channel.queueBind(QUEUE_MESSAGE, DIRECT_NAME, "");
		
		//同一时刻，服务器只会发送一条消息给消费者
		channel.basicQos(2);
		//定义队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//监听队列，手动返回完成
		channel.basicConsume(QUEUE_MESSAGE, false,consumer);
		//获取消息
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("路由器模式1获取的消息为:"+message);
			Thread.sleep(1000);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
	public static void resTestGit(){
		System.out.println("测试git是否已经好了!");
	}

}
