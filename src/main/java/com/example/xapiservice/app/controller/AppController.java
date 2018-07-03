package com.example.xapiservice.app.controller;

import com.example.xapiservice.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addApp() {
        System.out.println("success");
    }


}
