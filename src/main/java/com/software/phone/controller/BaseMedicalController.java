package com.software.phone.controller;

import com.software.phone.exception.MedicalException;
import com.software.phone.po.LoginTokenPo;
import com.software.phone.utils.JwtComponentUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author 徐旭
 * @data 2018/6/20 10:32
 */
public class BaseMedicalController {

    @Autowired
    JwtComponentUtil jwtComponent;

    /**
     * 描述：根据authorization获取会话里面的用户信息,未登录时返回值为空
     * @param authorization
     * @return
     */
    protected LoginTokenPo getLoginTokenPo(String authorization) throws Exception {
        LoginTokenPo loginTokenPo = new LoginTokenPo();

        // 获取会话内的信息
        Claims claims = jwtComponent.getClaims(authorization);
        if(claims == null) {
            return null;
        }

        loginTokenPo.setPhone(claims.get("phone").toString());

        return loginTokenPo;
    }

    /**
     * 描述：检查用户是否登录，如果未登录，报异常
     * @param authorization
     * @return
     * @throws Exception
     */
    protected LoginTokenPo checkLogin(String authorization) throws Exception {
        LoginTokenPo loginTokenPo = getLoginTokenPo(authorization);

        if(loginTokenPo == null) {
            throw new MedicalException("用户未登录");
        }
        return loginTokenPo;
    }
}
