package com.datbois.grademaster.jms;

public interface JmsService {
    void send(String msg);

    String receive();
}
