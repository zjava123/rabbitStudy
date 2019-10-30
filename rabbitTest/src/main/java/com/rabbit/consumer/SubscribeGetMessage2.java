package com.rabbit.consumer;

import java.io.IOException;

import com.rabbit.until.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class SubscribeGetMessage2 {
	
	private final static String SUBSCRIBE_MESSAGE="订阅模式";
	
	private final static String EXCHAGE_NAME = "交换机名称";
	
	public static void main(String[] args) throws ShutdownSignalException, ConsumerCancelledException, IOException, InterruptedException {
		subscribeConsumer();
	}
	
	public static void subscribeConsumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		
		//进行连接
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		
		//声明队列
		channel.queueDeclare(SUBSCRIBE_MESSAGE,false,false,false,null);
		
		//将队列绑定到交换机
		channel.queueBind(SUBSCRIBE_MESSAGE, EXCHAGE_NAME, "");
		
		//同一时刻，服务器只会发送一条消息给消费者
		channel.basicQos(1);
		
		//定义队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//监听队列，手动返回完成
		channel.basicConsume(SUBSCRIBE_MESSAGE, false,consumer);
		
		//获取消息
	
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody());
		System.out.println("订阅模式2获取的消息为:"+message);
		Thread.sleep(1000);
		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	
		
	}

}
