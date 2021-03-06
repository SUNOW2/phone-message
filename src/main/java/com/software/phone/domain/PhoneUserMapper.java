package com.software.phone.domain;

import com.software.phone.dao.PhoneUser;
import com.software.phone.po.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName PhoneUserMapper
 * @Description
 * @Author 徐旭
 * @Date 2018/7/14 16:56
 * @Version 1.0
 */
@Component
public interface PhoneUserMapper extends BaseMapper<PhoneUser, PhoneUser>{

    /**
     * 根据联系方式获取用户信息，可用于重名验证
     * @param phoneUser
     * @return
     */
    PhoneUser getUserByPhone(PhoneUser phoneUser);

    /**
     * 存储过程的调用
     * @param user
     */
    void selectUserById(SysUser user);

    /**
     * 存储过程实现的分页查询
     * @param params
     * @return
     */
    List<SysUser> selectUserPage(Map<String, Object> params);

    /**
     * 批量删除
     * @param list
     * @return
     */
    int deleteByList(List<Integer> list);

    /**
     * 批量查询
     * @param list
     * @return
     */
    List<PhoneUser> queryBatch(List<String> list);

    /**
     * 批量更新
     * @param list
     */
    void updateBatch(@Param("list") List<PhoneUser> list);
}
