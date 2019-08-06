package com.lqx.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("all")
public class QueryReceiver {
    public static final String QUEUE_NAME = "ego.query";
    public static final String EXCHANGE_NAME = "topic";

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
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"ego.query.#");
        //消费消息
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                System.out.println("ego商城查询消费方收到消息-->"+msg);
            }
        };
        //监听消息队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
