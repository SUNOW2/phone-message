package com.software.phone.controller;

import com.software.phone.conf.ResponseEntity;
import com.software.phone.po.LoginPo;
import com.software.phone.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/phone")
public class LoginController extends BaseController {

    @Autowired
    LoginService loginService;

    /**
     * 描述：用户点击获取验证码
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(LoginPo loginPo) {
        log.info("用户获取验证码");
        if(loginService.sendMessage(loginPo).split(":")[1].split(",")[0].equals("1")) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }

    @RequestMapping(value = "/oauth", method = RequestMethod.POST)
    public ResponseEntity oauth(LoginPo loginPo) {
        log.info("用户携带验证码进行登录验证");
        if(loginService.oauth(loginPo)) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }
}