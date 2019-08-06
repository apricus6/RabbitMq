package com.lqx.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {
    //设置队列名称
    public static final String QUEUE_NAME = "hello";

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

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "hello";

        //发送消息到队列        路由器       路由件        属性
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes("utf-8"));
        channel.close();
        connection.close();
    }
}
