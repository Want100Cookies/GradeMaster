package com.datbois.grademaster.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @RequestMapping(value = "/foo")
    public String foo() {
        return "Bar";
    }
}
