package com.software.phone.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PhoneUserDeleteForm
 * @Description
 * @Author 徐旭
 * @Date 2018/7/17 11:08
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneUserDeleteForm {
    /**
     * 编号集合
     */
    private List<Integer> list;
}
