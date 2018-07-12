package com.software.phone.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 徐旭
 * @data 2018/7/12 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CentreListResponse<T> {
    /**
     * 不分页的数据
     */
    private List<T> list;
}
