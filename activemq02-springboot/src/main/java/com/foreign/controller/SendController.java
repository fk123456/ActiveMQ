package com.foreign.controller;

import com.foreign.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangke
 * @Description:
 * @Package
 * @date: 2022/3/4 4:03 下午
 * <p>
 */
@RestController
public class SendController {

    @Autowired
    private SendService sendService;

    @GetMapping("/sendMsg")
    public void sendMsg(String destination, String msg) {
        sendService.sendMsg(destination, msg);
    }
}
