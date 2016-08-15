
import java.util.Map;


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
	 * @returnf
	 */
	public int deleteDatas(T t,long[] ids);
	
	/**
	 * 
	 * 获取所有的数据
	 * @return 数据数量total 数据data
	 * @param T 查询条件，模糊查询
	 * 
	 */
	public Map<String, Object> getDataEntities(T t);
	
	/**
	 * 
	 * 分页查询数据
	 * @return 数据数量total 数据data
	 * @param T 查询条件，模糊查询
	 * 
	 */
	public Map<String, Object> getDataEntities(T t,int start,int pagesize);
}
