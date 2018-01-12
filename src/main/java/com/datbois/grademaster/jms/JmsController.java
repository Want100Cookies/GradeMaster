package com.datbois.grademaster.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JmsController {

    @Autowired
    private JmsService service;

    @RequestMapping(value = "/jms/produce")
    public String produce(@RequestParam("msg") String msg){
        service.send(msg);
        return "MESSAGE SENT";
    }

    @RequestMapping(value = "/jms/receive")
    public String receive(){
        return service.receive();
    }
}
