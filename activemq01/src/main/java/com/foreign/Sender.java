package com.foreign;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author fangke
 * @Description: 消息发送者
 * @Package
 * @date: 2022/3/3 5:37 下午
 * <p>
 */
public class Sender {
    public static void main(String[] args) throws Exception {
        // 1. 建立工厂对象，
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
//                ActiveMQConnectionFactory.DEFAULT_USER,
//                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
//                "tcp://172.16.13.147:61616"
//        );
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                "admin",
                "admin",
                "tcp://172.16.13.147:61616"
        );
        //2 从工厂里拿一个连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3 从连接中获取Session(会话)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 从会话中获取目的地(Destination)，消费者会从这个目的地取消息
        Queue queue = session.createQueue("user");


        //从会话中创建消息提供者
        MessageProducer producer = session.createProducer(queue);
        //从会话中创建文本消息(也可以创建其它类型的消息体)

        for (int i = 0; i < 100; i++) {
            TextMessage message = session.createTextMessage("msg: " + i);
            // 通过消息提供者发送消息到ActiveMQ
            Thread.sleep(1000);
            producer.send(message);
        }
        //开启事务的时候，需要commit，才会提交到mq
        //connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//        session.commit();

        // 关闭连接
        connection.close();
        System.out.println("exit");
    }
}
