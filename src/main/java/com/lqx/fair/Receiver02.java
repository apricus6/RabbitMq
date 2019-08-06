package com.lqx.fair;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("all")
public class Receiver02 {
    public static final String QUEUE_NAME = "fair";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/shsxt");
        factory.setUsername("shsxt");
        factory.setPassword("123456");

        //获取连接
        Connection connection = factory.newConnection();
        //获取通道对象
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //消费消息
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                System.out.println("消费方收到消息-->"+msg);
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //手动回执消息给Mq
                //第一个参数:deliveryTag 表示消息的唯一标志符
                //第二个参数:multiple  表示是否为批量发送
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //监听消息队列
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
