package com.software.phone.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName SysUser
 * @Description
 * @Author 徐旭
 * @Date 2018/7/16 15:18
 * @Version 1.0
 */
@Data
public class SysUser implements Serializable {
    private static final long serialVersionUID = -4522147554144838970L;
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 邮箱
     */
    private String userEmail;
    /**
     * 简介
     */
    private String userInfo;
    /**
     * 头像
     */
    private byte[] headImg;
    /**
     * 创建时间
     */
    private Date createTime;
    //省略 getter 和 setter
}
