package com.lqx.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@SuppressWarnings("all")
public class Sender {
    //设置队列名称
    public static final String EXCHANGE_NAME = "direct";

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
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String msg = "this is error msg";
        //发送消息到队列        路由器       路由件        属性
        channel.basicPublish(EXCHANGE_NAME,"123",null,msg.getBytes("utf-8"));
        channel.close();
        connection.close();
    }
}
