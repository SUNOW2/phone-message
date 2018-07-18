package com.software.phone.controller;

import com.software.phone.dao.PhoneUser;
import com.software.phone.service.PhoneUserService;
import com.software.phonemessageapi.po.LoginPo;
import com.software.phonemessageapi.po.ResponseEntity;
import com.software.phonemessageapi.service.PhoneMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @ClassName RefactorLoginController
 * @Description
 * @Author 徐旭
 * @Date 2018/7/18 16:08
 * @Version 1.0
 */
@Slf4j
@RestController
public class RefactorLoginController extends BaseController implements PhoneMessageService {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private PhoneUserService phoneUserService;

    @Override
    public ResponseEntity hello() {
        ServiceInstance serviceInstance = client.getLocalServiceInstance();
        log.info("/hello, host：" + serviceInstance.getHost() + ", service_id：" + serviceInstance.getServiceId());
        return new ResponseEntity("success", 200, "操作成功", "Hello World");
    }

    @Override
    public ResponseEntity hello(@RequestParam("id")  Integer id) {
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setId(id);
        return new ResponseEntity("success", 200, "操作成功", phoneUserService.selectListRecord(phoneUser));
    }

    @Override
    public ResponseEntity hello(@RequestHeader("id") Integer id, @RequestHeader("password") String password) {
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setId(id);
        phoneUser.setPassword(password);
        return new ResponseEntity("success", 200, "操作成功", phoneUserService.selectListRecord(phoneUser));
    }

    @Override
    public ResponseEntity insert(LoginPo loginPo) {
        // 验证是否重名
        System.out.println("loginPo = " + loginPo);
        PhoneUser phoneUser = new PhoneUser();
        phoneUser.setPhone(loginPo.getPhone());
        if(phoneUserService.countRecord(phoneUser) > 0) {
            return new ResponseEntity("fail", 500, "手机号已经注册", null);
        }

        phoneUser.setPassword(loginPo.getPassword());
        phoneUser.setLoginDate(new Date());
        phoneUser.setRegisterDate(new Date());

        /**
         * 将Date转换为LocalDateTime
         */
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("localDateTime = " + localDateTime);

        phoneUserService.saveRecord(phoneUser);
        return new ResponseEntity("success", 200, "操作成功", null);
    }
}
