package com.datbois.grademaster.jms;

import javax.jms.*;

public class NotificationMessageListener implements MessageListener{

    @Override
    public void onMessage(Message message){
        TextMessage textMessage = (TextMessage) message;
        try{
            System.out.println("Received: " + textMessage.getText());
        }
        catch (JMSException e){
            e.printStackTrace();
        }
    }
}
