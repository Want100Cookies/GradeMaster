package com.datbois.grademaster.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JmsServiceImpl implements JmsService {

    @Autowired
    private JmsConsumer consumer;

    @Autowired
    private JmsProducer producer;

    @Override
    public void send(String msg){
        producer.send(msg);
    }

    @Override
    public String receive() {
        return consumer.receive();
    }
}
