package com.software.phone.service.impl;

import com.software.phone.domain.BaseMapper;
import com.software.phone.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName BaseServiceImpl
 * @Description
 * @Author 徐旭
 * @Date 2018/7/16 10:27
 * @Version 1.0
 */
public abstract class BaseServiceImpl<T, C, M extends BaseMapper<T, C>> implements BaseService<T, C> {
    @Autowired
    private M mapper;

    @Override
    public void saveRecord(T po) {
        mapper.saveRecord(po);
    }

    @Override
    public int deleteRecord(Object id) {
        return mapper.deleteRecord(id);
    }

    @Override
    public void updateRecord(T po) {
        mapper.updateRecord(po);
    }

    @Override
    public T selectRecord(C c) {
        return mapper.selectRecord(c);
    }

    @Override
    public List<T> selectListRecord(C c) {
        return mapper.selectListRecord(c);
    }

    @Override
    public int countRecord(T po) {
        return mapper.countRecord(po);
    }

    @Override
    public List<T> selectPageListRecord(C c) {
        return mapper.selectPageListRecord(c);
    }

    protected M getMapper() {
        return mapper;
    }
}
