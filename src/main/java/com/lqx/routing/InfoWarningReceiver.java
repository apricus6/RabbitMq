package com.lqx.routing;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("all")
public class InfoWarningReceiver {
    public static final String QUEUE_NAME = "warning";
    public static final String EXCHANGE_NAME = "direct";

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
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //绑定交换机   可以绑定多个路由件
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"info");
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"warning");
        //消费消息
        Consumer consumer =new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                System.out.println("info_warning日志消费方收到消息-->"+msg);
            }
        };
        //监听消息队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
