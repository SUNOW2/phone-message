package com.software.phone.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 徐旭
 * @data 2018/7/12 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CentreCutPageResponse<T> extends CentreListResponse<T> {
    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总条数
     */
    private long totalCount;

    public CentreCutPageResponse(int pageNum, int pageSize, long totalCount, List<T> list) {
        super(list);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
    }

}
