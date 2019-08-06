package com.lqx.fair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
    //设置队列名称
    public static final String QUEUE_NAME = "fair";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        //设置主机地址
        factory.setHost("127.0.0.1");
        //设置端口
        factory.setPort(5672);
        //设置虚拟主机
        factory.setVirtualHost("/shsxt");
        //设置用户名
        factory.setUsername("shsxt");
        //设置登录密码
        factory.setPassword("123456");

        //获取连接
        Connection connection = factory.newConnection();

        //获取通道对象
        Channel channel = connection.createChannel();
        //设置消息队列一次只能发送一条消息
        channel.basicQos(1);
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i=1;i<=30;i++){
            //发送消息到消息队列     交换机       路由件
            channel.basicPublish("",QUEUE_NAME,null,("hello_mq"+i).getBytes("utf-8"));
            System.out.println("生产者产生消息-->"+("hello mq"+i));
            Thread.sleep(1000L);
        }
        channel.close();
        connection.close();
    }
}
