package com.software.phone.service.impl;

import com.software.phone.po.LoginPo;
import com.software.phone.service.LoginService;
import com.software.phone.utils.HttpRequestUtil;
import com.software.phone.utils.RedisComponentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author 徐旭
 * @data 2018/6/6 15:11
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    HttpRequestUtil httpRequestUtil;

    @Autowired
    RedisComponentUtil redisUtil;

    /**
     * 发送验证码
     * @param loginPo
     * @return
     */
    @Override
    public String sendMessage(LoginPo loginPo) {
        String content = "【签名】您正在注册延长账号，验证码" + getCode(loginPo) + "，如非本人操作请注意账号安全。";
        String url = "https://api.dingdongcloud.com/v2/sms/single_send";
        loginPo.setContent(content);
        return httpRequestUtil.postRequest(url, loginPo);
    }

    /**
     * 获取验证码
     * @param loginPo
     * @return
     */
    @Override
    @Cacheable(value = "oauth")
    public String getCode(LoginPo loginPo) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            sb = sb.append((int)(Math.random() * 8));
        }
        System.out.println("获取到的验证码：" + sb.toString());
//        此处必须设置loginPo.getPhone()为String类型，不然的话，设置的过期时间无效
        redisUtil.set(loginPo.getPhone().toString(), sb.toString(), 60L);
        return sb.toString();
    }

    /**
     * 获取用户体检的验证码与服务器提供的验证码是否一致
     * @param loginPo
     * @return
     */
    @Override
    public Boolean oauth(LoginPo loginPo) {
        Object obj = redisUtil.get(loginPo.getPhone());
        System.out.println("上传的验证码：" + obj.toString());
        if(obj != null && obj.toString().equals(loginPo.getCode())) {
            return true;
        }
        return false;
    }
}