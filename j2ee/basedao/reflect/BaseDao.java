package com.cnc.roas.base;

import java.util.List;
import java.util.Map;

import com.cnc.roas.sparkjob.confmeta.entity.ConfMetaDataEntity;

public interface BaseDao<T>{
	/**
	 * 插入数据
	 * @return
	 */
	public int insertData(T t);
	
	/**
	 * 更新数据，必须带有id
	 * @param confMetaDataEntity
	 * @return
	 */
	public int updateData(T t);
	
	
	/**
	 * 删除数据，传递id数组
	 * @param ids
	 * @return
	 */
	public int deleteDatas(String[] ids);
	
	/**
	 * 
	 * 获取所有的数据
	 * @return 数据数量total 数据rows
	 * @param T 查询条件，模糊查询
	 * 
	 */
	public Map<String, Object> getDataEntities(T t);
	
	/**
	 * 
	 * 分页查询数据
	 * @return 数据数量total 数据rows
	 * @param T 查询条件，模糊查询
	 * 
	 */
	public List<T> getDataEntities(T t,int start,int pagesize);
	
	/**
	 * 
	 * 分页查询数据
	 * @return 数据数量total 数据rows
	 * @param T 查询条件，模糊查询
	 * 
	 */
	public Integer getDataEntitiesCount(T t);
	
	
	/**
	 * 通过id获取实体
	 * @param id
	 * @return
	 */
	public Object getData(Integer id);
}
