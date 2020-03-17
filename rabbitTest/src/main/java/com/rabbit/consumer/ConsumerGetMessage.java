package com.rabbit.consumer;

import java.io.IOException;

import com.rabbit.until.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 
 * 
 * */
public class ConsumerGetMessage {
	private final static String QUEUE_MESSAGE  = "test_message";
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		String message = SimpleConsumerGetMessage();
		System.out.println(message);
		
	}
	/**
	 * 简单模式消费者或者消息
	 * @throws InterruptedException 
	 * @throws ConsumerCancelledException 
	 * @throws ShutdownSignalException 
	 * */
	public static String  SimpleConsumerGetMessage() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		String message = null;
		//获取到连接以及MQ通道
		Connection connection = ConnectionUtil.getConnection();
		
		//从连接中创建通道
		Channel channel = connection.createChannel();
		
		//声明队列
		channel.queueDeclare(QUEUE_MESSAGE, false,false,false,null);
		
		//定义队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//监听队列
		channel.basicConsume(QUEUE_MESSAGE, true,consumer);
		
		//获取消息
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			 message = new String(delivery.getBody());
			System.out.println("消费者获取到的消息为:" + message);
			if( message != null && !"".equals( message.trim() ) ){
				break;
			}
		}
		
		return message;
	}

	
}
