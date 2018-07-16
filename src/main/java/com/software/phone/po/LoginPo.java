package com.software.phone.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

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
    @NotEmpty(message = "联系方式不可以为空")
    private String phone;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不可以为空")
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 短信模版
     */
    private String content;
}

/**
 * @NotEmpty:不能为null，且size>0
 * @NotNull:不能为null，但可以为empty，没有size的约束
 * @NotBlank:只适用于String，不能为null，且trim()之后size>0
 */