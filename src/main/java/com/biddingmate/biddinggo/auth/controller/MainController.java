package com.biddingmate.biddinggo.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String mainApi() {

        return "main route";
    }
}
