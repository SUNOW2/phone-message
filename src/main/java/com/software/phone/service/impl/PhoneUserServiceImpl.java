package com.software.phone.service.impl;

import com.software.phone.dao.PhoneUser;
import com.software.phone.domain.PhoneUserMapper;
import com.software.phone.po.SysUser;
import com.software.phone.service.PhoneUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PhoneUserServiceImpl
 * @Description
 * @Author 徐旭
 * @Date 2018/7/14 17:30
 * @Version 1.0
 */
@Service
public class PhoneUserServiceImpl extends BaseServiceImpl<PhoneUser, PhoneUser, PhoneUserMapper> implements PhoneUserService {

    @Override
    public PhoneUser getUserByPhone(PhoneUser phoneUser) {
        return this.getMapper().getUserByPhone(phoneUser);
    }

    @Override
    public void selectUserById(SysUser user) {
        this.getMapper().selectUserById(user);
    }

    @Override
    public List<SysUser> selectUserPage(Map<String, Object> params) {
        return this.getMapper().selectUserPage(params);
    }

}
