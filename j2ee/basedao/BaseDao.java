package cn.edu.xmu.dao;

import java.util.Map;

public interface BaseDao<T> {

	/**
	 * 查数据
	 * 
	 * @param tablename
	 *            表名
	 * @param tablefield
	 *            待获取的字段
	 * @param params
	 * @return sql语句的结果 1为成功, 0为失败
	 */
	public Object getData(String tablename, String tablefield,
			Map<String, String> params);

	/**
	 * 删除数据
	 * 
	 * @param tablename
	 *            表名
	 * @param params
	 *            条件参数
	 * @return sql语句的结果 1为成功, 0为失败
	 */
	public int deleteRecord(String tablename, Map<String, String> params);

	/**
	 * 添加数据
	 * 
	 * @param tablename
	 *            表名
	 * @param queryParams
	 *            添加的字段名及值
	 * @return sql语句的结果 1为成功, 0为失败
	 */
	public int addRecord(String tablename, Map queryParams);

	/**
	 * 修改数据
	 * 
	 * @param tablename
	 *            表名
	 * @param param1
	 *            待修改的字段
	 * @param param2
	 *            条件语句
	 * @return sql语句的结果 1为成功, 0为失败
	 */
	public int updateRecord(String tablename, Map param1, Map param2);

	/**
	 * 获取纪录数量
	 * 
	 * @param tablename
	 *            表名
	 * @param queryParams
	 *            条件参数
	 * @return 符合条件的记录数
	 */
	public int getEntityCount(String tablename, Map queryParams);

	/**
	 * 
	 * @param tablename
	 *            表名
	 * @param start
	 *            开始的ID
	 * @param end
	 *            结束的ID
	 * @param sort
	 *            排序的字段
	 * @param order
	 *            排序的规则
	 * @param params1
	 *            待获取的字段
	 * @param params1
	 *            查询条件的字段
	 * @return list<类>
	 */
	// public List<T> findAll(String tablename,
	// int start, int end, String sort,String order, Map params1, Map params2);
}
