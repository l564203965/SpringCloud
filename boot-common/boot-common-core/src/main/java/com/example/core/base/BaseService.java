/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：BaseService.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.example.core.base;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * The class Base service.
 *
 * @param <T> the type parameter
 *
 * @author paascloud.net@gmail.com
 */
public abstract class BaseService<T> implements IService<T> {

	/**
	 * The Logger.
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The Mapper.
	 */
	@Autowired
	protected Mapper<T> baseMapper;

	/**
	 * Gets mapper.
	 *
	 * @return the mapper
	 */
	public Mapper<T> getBaseMapper() {
		return baseMapper;
	}

	/**
	 * Select list.
	 *
	 * @param record the record
	 *
	 * @return the list
	 */
	@Override
	public List<T> select(T record) {
		return baseMapper.select(record);
	}

	/**
	 * Select by key t.
	 *
	 * @param key the key
	 *
	 * @return the t
	 */
	@Override
	public T selectByKey(Object key) {
		return baseMapper.selectByPrimaryKey(key);
	}

	/**
	 * Select all list.
	 *
	 * @return the list
	 */
	@Override
	public List<T> selectAll() {
		return baseMapper.selectAll();
	}

	/**
	 * Select one t.
	 *
	 * @param record the record
	 *
	 * @return the t
	 */
	@Override
	public T selectOne(T record) {
		return baseMapper.selectOne(record);
	}

	/**
	 * Select count int.
	 *
	 * @param record the record
	 *
	 * @return the int
	 */
	@Override
	public int selectCount(T record) {
		return baseMapper.selectCount(record);
	}

	/**
	 * Select by example list.
	 *
	 * @param example the example
	 *
	 * @return the list
	 */
	@Override
	public List<T> selectByExample(Object example) {
		return baseMapper.selectByExample(example);
	}

	/**
	 * Save int.
	 *
	 * @param record the record
	 *
	 * @return the int
	 */
	@Override
	public int save(T record) {
		return baseMapper.insertSelective(record);
	}

	/**
	 * Batch save int.
	 *
	 * @param list the list
	 *
	 * @return the int
	 */
	@Override
	public int batchSave(List<T> list) {
		int result = 0;
		for (T record : list) {
			int count = baseMapper.insertSelective(record);
			result += count;
		}
		return result;
	}

	/**
	 * Update int.
	 *
	 * @param entity the entity
	 *
	 * @return the int
	 */
	@Override
	public int update(T entity) {
		return baseMapper.updateByPrimaryKeySelective(entity);
	}

	/**
	 * Delete int.
	 *
	 * @param record the record
	 *
	 * @return the int
	 */
	@Override
	public int delete(T record) {
		return baseMapper.delete(record);
	}

	/**
	 * Delete by key int.
	 *
	 * @param key the key
	 *
	 * @return the int
	 */
	@Override
	public int deleteByKey(Object key) {
		return baseMapper.deleteByPrimaryKey(key);
	}

	/**
	 * Batch delete int.
	 *
	 * @param list the list
	 *
	 * @return the int
	 */
	@Override
	public int batchDelete(List<T> list) {
		int result = 0;
		for (T record : list) {
			int count = baseMapper.delete(record);
			if (count < 1) {
				logger.error("删除数据失败");
				throw new RuntimeException("删除数据失败!");
			}
			result += count;
		}
		return result;
	}

	/**
	 * Select count by example int.
	 *
	 * @param example the example
	 *
	 * @return the int
	 */
	@Override
	public int selectCountByExample(Object example) {
		return baseMapper.selectCountByExample(example);
	}

	/**
	 * Update by example int.
	 *
	 * @param record  the record
	 * @param example the example
	 *
	 * @return the int
	 */
	@Override
	public int updateByExample(T record, Object example) {
		return baseMapper.updateByExampleSelective(record, example);
	}

	/**
	 * Delete by example int.
	 *
	 * @param example the example
	 *
	 * @return the int
	 */
	@Override
	public int deleteByExample(Object example) {
		return baseMapper.deleteByPrimaryKey(example);
	}

	/**
	 * Select by row bounds list.
	 *
	 * @param record    the record
	 * @param rowBounds the row bounds
	 *
	 * @return the list
	 */
	@Override
	public List<T> selectByRowBounds(T record, RowBounds rowBounds) {
		return baseMapper.selectByRowBounds(record, rowBounds);
	}

	/**
	 * Select by example and row bounds list.
	 *
	 * @param example   the example
	 * @param rowBounds the row bounds
	 *
	 * @return the list
	 */
	@Override
	public List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
		return baseMapper.selectByExampleAndRowBounds(example, rowBounds);
	}
}
