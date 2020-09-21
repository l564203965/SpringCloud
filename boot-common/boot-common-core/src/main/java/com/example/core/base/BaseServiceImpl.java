package com.example.core.base;

import com.example.core.mybatis.BaseEntity;
import tk.mybatis.mapper.common.BaseMapper;

/**
 * @author: ljy  Date: 2020/1/19.
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseService<T> {
}
