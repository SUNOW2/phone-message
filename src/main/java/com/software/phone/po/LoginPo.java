package com.software.phone.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 徐旭
 * @data 2018/6/6 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPo {
    /**
     * 联系方式
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 短信模版
     */
    private String content;
}
