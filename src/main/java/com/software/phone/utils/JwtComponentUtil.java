package com.software.phone.utils;

import com.software.phone.exception.MedicalException;
import com.software.phone.po.LoginTokenPo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 徐旭
 * @data 2018/6/20 10:20
 */
@Component
public class JwtComponentUtil {

    @Autowired
    RedisComponentUtil redisComponent;

    /**
     * 描述：根据token获取会话
     * @param authorization
     * @return
     */
    public Claims getClaims(String authorization) throws MedicalException {
        // 没有登录信息
        if(StringUtils.isBlank(authorization)) {
            return null;
        }
        // JWT token认证
        Claims claims = Jwts.parser().setSigningKey(AppConstantsUtil.JWT_SECURITY)
                .parseClaimsJws(authorization).getBody();
        if (redisComponent.get(authorization) == null) {
            throw new MedicalException("当前请求已过期，请重新登录");
        }

        return claims;
    }

    /**
     * 描述：生成token
     * @param loginTokenPo
     * @return
     */
    public String createToken(LoginTokenPo loginTokenPo) {
        Date expDate = new Date(System.currentTimeMillis() + AppConstantsUtil.ExpTime.loginExpTime);

        // setSubject("")：设置主题, setExpiration("")：设置超时时间
        String token = Jwts.builder().setSubject("medical").setExpiration(expDate)
                .claim("phone", loginTokenPo.getPhone())
                .signWith(SignatureAlgorithm.HS256, AppConstantsUtil.JWT_SECURITY)
                .compact();

        // 将token存入数据库，这个步骤没有意义，可能是将
        redisComponent.set(token, 20 * 60 *60);

        return token;
    }
}
