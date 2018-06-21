package com.software.phone.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 徐旭
 * @data 2018/6/20 10:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenPo {
    /**
     * 联系方式
     */
    @NotEmpty(message = "联系方式不可以为空")
    private String phone;
}
