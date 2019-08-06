package com.lqx.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("all")
public class RpcClient {
    //设置队列名称
    public static final String QUEUE_NAME = "rpc_queue";
    public static final String REPLY_QUEUE = "reply_name";

    public static void main(String[] args) throws Exception {
        /**
         * 创建连接
         */
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

        //创建通道并定义队列
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //生成消息的唯一Id
        final String correlationId = UUID.randomUUID().toString();
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                .replyTo(REPLY_QUEUE).correlationId(correlationId).build();
        //发送消息到队列        路由器       路由件        属性
        channel.basicPublish("",QUEUE_NAME,replyProps,"6".getBytes("utf-8"));
        System.out.println("发送成功!");
        channel.queueDeclare(REPLY_QUEUE,false,false,false,null);
        //定义消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
            String corrId = properties.getCorrelationId();
            if (correlationId.equals(corrId)){
                System.out.println("获取服务器的返回结果:"+new String(body,"utf-8"));
            }
            }
        };
        //监听消息队列
        channel.basicConsume(REPLY_QUEUE,true,consumer);
    }
}
