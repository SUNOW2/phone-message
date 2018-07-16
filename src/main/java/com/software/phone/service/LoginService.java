package com.software.phone.service;

import com.software.phone.po.LoginPo;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    /**
     * 描述：发送消息，获取验证码
     * @param loginPo
     * @return
     */
    String sendMessage(LoginPo loginPo);

    /**
     * 描述：生成验证码
     * @param loginPo
     * @return
     */
    String getCode(LoginPo loginPo);

    /**
     * 验证手机号及验证码
     * @param loginPo
     * @return
     */
    Boolean oauth(LoginPo loginPo);
}

