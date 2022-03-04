package com.foreign.service.receiver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author fangke
 * @Description:
 * @Package
 * @date: 2022/3/4 4:11 下午
 * <p>
 */
@Service
public class ReceiverService {

    //topic消息接收
    @JmsListener(destination = "springboot-activemq", containerFactory = "jmsListenerContainerTopic")
    public void receiverTopicMsg(String msg) {
        System.out.println("收到Topic消息 : " + msg);
    }

    //queue消息接收
    @JmsListener(destination = "springboot-activemq", containerFactory = "jmsListenerContainerQueue")
    public void receiverQueueMsg(String msg) {
        System.out.println("收到Queue消息 : " + msg);
    }
}
