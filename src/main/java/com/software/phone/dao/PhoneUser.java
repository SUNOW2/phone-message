package com.software.phone.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @ClassName PhoneUser
 * @Description
 * @Author 徐旭
 * @Date 2018/7/14 16:53
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneUser extends BaseDao {
    /**
     * 编号
     */
    private int id;

    /**
     * 联系方式
     */
    @NotEmpty(message = "联系方式不可为空")
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 登陆时间
     */
    private Date loginDate;

    /**
     * 注册时间
     */
    private Date registerDate;
}
