package com.software.phone.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PhoneUserQueryForm
 * @Description
 * @Author 徐旭
 * @Date 2018/7/17 13:17
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneUserQueryForm {
    private List<String> list;
}
