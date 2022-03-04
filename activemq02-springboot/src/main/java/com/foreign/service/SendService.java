package com.foreign.service;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * @author fangke
 * @Description:
 * @Package
 * @date: 2022/3/4 4:04 下午
 * <p>
 */
@Service
public class SendService {

    //内部使用JmsTemplate，做了一层封装，使用更加简便
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMsg(String destination, String msg) {
        System.out.println("send...");
        ActiveMQQueue queue = new ActiveMQQueue(destination);
        jmsMessagingTemplate.afterPropertiesSet();

        ConnectionFactory factory = jmsMessagingTemplate.getConnectionFactory();

        try {
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue queue2 = session.createQueue(destination);

            MessageProducer producer = session.createProducer(queue2);

            TextMessage message = session.createTextMessage("hahaha");


            producer.send(message);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        jmsMessagingTemplate.convertAndSend(queue, msg);
    }

//    public void sendMsg2(String destination, String msg) {
//        jmsMessagingTemplate.convertAndSend(destination, msg);
//    }
}
