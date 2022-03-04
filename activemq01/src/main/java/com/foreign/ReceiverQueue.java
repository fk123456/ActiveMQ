package com.foreign;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

import javax.jms.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fangke
 * @Description: 消息接收者
 * @Package
 * @date: 2022/3/3 5:53 下午
 * <p>
 */
public class ReceiverQueue {
    public static void main(String[] args) throws Exception {
        // 1. 建立工厂对象，
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
//                "admin",
//                "admin",
                "tcp://172.16.13.147:61616"
        );
        //1.1 信任序列化对象的包
        List<String> list = new ArrayList<>();
        String name = Girl.class.getPackage().getName();
        list.add(name);
        activeMQConnectionFactory.setTrustedPackages(new ArrayList<String>(
                Arrays.asList(
                        new String[]{
                                Girl.class.getPackage().getName()
                        }

                )
        ));
        //2 从工厂里拿一个连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3 从连接中获取Session(会话)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 从会话中获取目的地(Destination)消费者会从这个目的地取消息
        Queue queue = session.createQueue("user");

        //从会话中创建消息消费者
        MessageConsumer consumer = session.createConsumer(queue);

        //从会话中创建文本消息(也可以创建其它类型的消息体)
        //死循环的模式，大量并发进来，会导致内存系统等等问题
//        while (true) {
//            //其实这个在底层就是 socket.accept() 阻塞等数据到达，一旦I/O大，就会造成大量的服务阻塞执行。
//            TextMessage receive = (TextMessage)consumer.receive();
//            System.out.println("TextMessage:" + receive.getText());
//
//        }

        //采用异步 监听器的方式，不阻塞代码的执行
        consumer.setMessageListener(message -> {
            if(message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    System.out.println("TextMessage:" + text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(message instanceof ActiveMQObjectMessage) {
                try {
                    Girl girl = (Girl)((ActiveMQObjectMessage)message).getObject();

                    System.out.println(girl);
                    System.out.println(girl.getName());
                } catch (JMSException e) {
                    e.printStackTrace();
                }

            }

            if(message instanceof BytesMessage) {
                BytesMessage bm = (BytesMessage)message;

                byte[] b = new byte[1024];
                int len = -1;
                while (true) {
                    try {
                        if (!((len = bm.readBytes(b)) != -1)) break;
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    System.out.println(new String(b, 0, len));
                }
            }

        });
    }
}
