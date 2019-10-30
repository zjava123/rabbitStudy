package com.rabbit.consumer;

import java.io.IOException;

import com.rabbit.until.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * work模式
 * 一个生产者，两个消费者，一个消息只能被一个生产者获取
 * 	 		-->消费者1
 * p-->队列
 * 			-->消费者2
 * 
 * */
public class WorkConsumerGetMessage2 {

	private static final  String test_message_consumer = "work模式";
	public static void main(String[] args) throws ShutdownSignalException, ConsumerCancelledException, IOException, InterruptedException {
		work1Consumer();
		
	}
	
	public static void work1Consumer() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
		String message =  null;
		//获取连接MQ通道
		Connection connection = ConnectionUtil.getConnection();
		//创建连接通道
		Channel channel = connection.createChannel();
		
		//声明队列
		channel.queueDeclare(test_message_consumer, false, false, false, null);
		
		//同一时刻，服务器只会发送一条消息
		channel.basicQos(1);
		
		//定义队列消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		//监听队列，false表示手动返回队列(跟ack相对应)，true表示自动
		channel.basicConsume(test_message_consumer, false,queueingConsumer);
		int i=0;
		while(true){
			QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
			message =new String( delivery.getBody() );
			System.out.println("消费者2获取的消息为:"+message);
			//休眠
			Thread.sleep(1000);
			//返回确定状态 如果没有的话就表示自动确定模式,就 表示如果没有，该消息只有在接收到以后就会停止
			//channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
			
		}
		
		
	}
}
