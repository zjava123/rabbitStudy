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
public class WorkConsumerGetMessage1 {

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
		/**
		 * RabbitMQ中的概念，channel.basicQos(1)指该消费者在接收到队列里的消息但没有返回确认结果之前,
		 * 队列不会将新的消息分发给该消费者。队列中没有被消费的消息不会被删除，还是存在于队列中。
		 * */
		channel.basicQos(1);
		
		//定义队列消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		
		//监听队列，false表示手动返回完成状态，true表示自动
		channel.basicConsume(test_message_consumer,false ,queueingConsumer);
		while(true){
			QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
			message =new String( delivery.getBody() );
			System.out.println("消费者1获取的消息为:"+message);
			//休眠
			Thread.sleep(100);
			//返回确定状态,就会一直执行下去，因为在baseicQos(1)当中设定了条数，为返回结果之前其他消息会保留在mq 
			//如果没有的话就表示自动确定模式
			//channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			
			
		}
		
	
	}
}
