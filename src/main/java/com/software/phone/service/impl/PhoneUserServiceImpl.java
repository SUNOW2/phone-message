package com.software.phone.service.impl;

import com.software.phone.dao.PhoneUser;
import com.software.phone.domain.PhoneUserMapper;
import com.software.phone.po.SysUser;
import com.software.phone.service.PhoneUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public int deleteByList(List<Integer> list) {
        return this.getMapper().deleteByList(list);
    }

    @Override
    public List<PhoneUser> queryBatch(List<String> list) {
        List<PhoneUser> phList = this.getMapper().queryBatch(list);
        // 此处仅仅为测试，无任何意义
        phList.stream().forEach(phoneUser -> {
            String str = "12,13,14,";
            List<String> testList = new ArrayList<>();
            if (StringUtils.isNotEmpty(str)) {
                testList.addAll(Arrays.asList(str.split(",")));
            }
            this.getMapper().queryBatch(testList == null ? null : testList);
        });
        return phList;
    }

    @Override
    public void updateBatch(List<PhoneUser> list) {
        this.getMapper().updateBatch(list);
    }

}
