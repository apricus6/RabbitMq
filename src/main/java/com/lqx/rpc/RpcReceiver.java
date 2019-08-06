package com.lqx.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("all")
class RpcReceiver {
    public static final String QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接
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
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //定义消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String num = new String(body,"utf-8");
                System.out.println("接收的消息是:-->"+num);
                //获取计算结果
                int result = fib(Integer.parseInt(num));
                System.out.println("计算的结果:"+result);
                //获取返回结果的队列
                String replyToQueueName = properties.getReplyTo();
                //声明replyToqueueName
                channel.queueDeclare(replyToQueueName,false,false,false,null);
                //发送消息到replyToQueueName队列
                channel.basicPublish("",replyToQueueName,properties,(result+"").getBytes());
            }
        };
        //监听消息队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
    private static int fib(int n){
        if (n == 0) return  0;
        if (n == 1) return  1;
        return fib(n-1)+fib(n-2);
    }
}
