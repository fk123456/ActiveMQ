package com.foreign;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author fangke
 * @Description: 消息接收者
 * @Package
 * @date: 2022/3/3 5:53 下午
 * <p>
 */
public class ReceiverTopic {
    public static void main(String[] args) throws Exception {
        // 1. 建立工厂对象，
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
//                ActiveMQConnectionFactory.DEFAULT_USER,
//                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "admin",
                "admin",
                "tcp://172.16.13.147:61616"
        );
        //2 从工厂里拿一个连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3 从连接中获取Session(会话)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 从会话中获取目的地(Destination)消费者会从这个目的地取消息
        Destination topic = session.createTopic("user");


        //从会话中创建消息消费者
        MessageConsumer consumer = session.createConsumer(topic);

        //从会话中创建文本消息(也可以创建其它类型的消息体)
        while (true) {
            TextMessage receive = (TextMessage)consumer.receive();
            System.out.println("TextMessage:" + receive.getText());

        }
    }
}
