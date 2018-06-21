package com.software.phone.controller;

import com.software.phone.conf.ResponseEntity;
import com.software.phone.exception.MedicalException;
import com.software.phone.po.LoginPo;
import com.software.phone.po.LoginTokenPo;
import com.software.phone.service.LoginService;
import com.software.phone.utils.JwtComponentUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class LoginController extends BaseController {

    @Autowired
    LoginService loginService;

    @Autowired
    JwtComponentUtil jwtComponentUtil;

    /**
     * 描述：用户点击获取验证码
     *
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(LoginPo loginPo) {
        log.info("用户获取验证码");
        if (loginService.sendMessage(loginPo).split(":")[1].split(",")[0].equals("1")) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }

    /**
     * 描述：用户手机验证码登录
     *
     * @param loginPo
     * @return
     */
    @RequestMapping(value = "/oauth", method = RequestMethod.POST)
    public ResponseEntity oauth(LoginPo loginPo) {
        log.info("用户携带验证码进行登录验证");
        if (loginService.oauth(loginPo)) {
            return this.getSuccessResult();
        }
        return this.getFailResult();
    }

    /**
     * 描述：用户用户名、密码登录
     *
     * @param loginTokenPo
     * @return
     */
    @RequestMapping(value = "/tokenLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity tokenLogin(LoginTokenPo loginTokenPo, @RequestHeader(value = "Authorization") String authorization) throws MedicalException {
        // 首先，验证用户是否已经注册

        // 接着，验证用户是否已经登录
        String token = jwtComponentUtil.createToken(loginTokenPo);
        if (token != "") {
            return this.getSuccessResult("登录成功", token);
        }

        return this.getFailResult("登录失败");
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity test(@RequestHeader(value = "Authorization") String authorization) throws MedicalException {
        if (jwtComponentUtil.getClaims(authorization) == null) {
            return this.getFailResult();
        }
        return this.getSuccessResult("测试成功");
    }
}
