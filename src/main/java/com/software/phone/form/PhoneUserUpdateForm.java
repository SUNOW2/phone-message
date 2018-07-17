package com.software.phone.form;

import com.software.phone.dao.PhoneUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PhoneUserUpdateForm
 * @Description
 * @Author 徐旭
 * @Date 2018/7/17 15:40
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneUserUpdateForm {

    /**
     * 新增的数据集合
     */
    private List<PhoneUser> list;
}
