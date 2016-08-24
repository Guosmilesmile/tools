package com.cnc.roas.base;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnc.roas.annotation.ColumnName;
import com.cnc.roas.annotation.TableName;
import com.cnc.roas.sparkjob.confmeta.entity.ConfMetaDataEntity;
import com.cnc.roas.utils.DBUtil;
import com.sun.jndi.cosnaming.CNCtx;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class BaseDaoImpl<T> implements BaseDao<T>{

	@Override
	public int insertData(T t) {
		Connection con = null ;
		PreparedStatement pre = null;
		int isFinishs = 0;
		try {
			con = DBUtil.openConnection();
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName annotation = (TableName) clz.getAnnotation(TableName.class);
			String tablename = annotation.tablename();
			String sql = "insert into "+tablename+" (";
			
			Method[] declaredMethods = clz.getDeclaredMethods();
			String insertColumn = "";
			String insertValue = "";
			for(Method method : declaredMethods){
				if(method.isAnnotationPresent(ColumnName.class)){
					if(!"getId".equals(method.getName())){
						ColumnName columnName = method.getAnnotation(ColumnName.class);
						insertColumn += columnName.columnName()+",";
						insertValue += "'"+method.invoke(t)+"',";
					}
				}
			}
			sql += insertColumn.substring(0,insertColumn.length()-1)+") values ("+insertValue.substring(0,insertValue.length()-1)+")";
			System.out.println(sql);
			pre = con.prepareStatement(sql);
			pre.execute();
			isFinishs = 1;
		} catch (Exception e) {
			e.printStackTrace();
			isFinishs = 0;
		}finally{
			try {
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isFinishs;
	}

	
	@Override
	public int updateData(T t) {
		if(null==t){
			return 0;
		}
		Connection con = null ;
		PreparedStatement pre =null;
		int isFinish = 0;
		try {
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName anotation = (TableName) clz.getAnnotation(TableName.class);
			String tableName = anotation.tablename();
			String sql = "update "+ tableName +" set ";
			Method[] methods = clz.getMethods();
			int id = -1;
			for(Method method : methods){
				if(method.isAnnotationPresent(ColumnName.class)){
					if(!"getId".equals(method.getName())){
						if(null!=method.invoke(t)){
							ColumnName annotation = method.getAnnotation(ColumnName.class);
							sql += annotation.columnName()+" = '"+method.invoke(t)+"',";
						}
					}else{
						id = (int) method.invoke(t);
					}
				}
			}
			sql = sql.substring(0,sql.length()-1);
			sql += " where id="+id;
			System.out.println(sql);
			con = DBUtil.openConnection();
			pre = con.prepareStatement(sql);
			pre.execute();
			isFinish = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isFinish;
	}

	@Override
	public int deleteDatas(String[] ids) {
		if(0==ids.length || null == ids){
			return 0;
		}
		Connection con = null;
		PreparedStatement pre =  null;
		int isFinish = 0;
		Type genType = getClass().getGenericSuperclass();  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
		Class clz = (Class) params[0];  
		TableName annotation = (TableName) clz.getAnnotation(TableName.class);
		String tablename = annotation.tablename();
		String sql = "delete from "+tablename+" where id=?";
		try {
			con =DBUtil.openConnection();
			for(int i=0;i<ids.length;i++){
				pre = con.prepareStatement(sql);
				pre.setString(1, ids[i]);
				pre.addBatch();
			}
			pre.executeBatch();
			isFinish = 1;
		} catch (Exception e) {
			e.printStackTrace();
			isFinish = 0;
		}finally{
			try {
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return isFinish;
	}

	@Override
	public Map<String, Object> getDataEntities(T t) {
		Map<String, Object> map = new  HashMap<String, Object>();
		List<T> list = new ArrayList<T>();
		Connection con = null ;
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		
		try {
			con = DBUtil.openConnection();
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName annotation = (TableName) clz.getAnnotation(TableName.class);
			String tablename = annotation.tablename();
			String sql = "select * from "+tablename+" where "; 
			Method[] methods = clz.getDeclaredMethods();
			if(null!=t){
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						if("get".equals(method.getName().subSequence(0, 3))){
							ColumnName columnName = method.getAnnotation(ColumnName.class);
							if(null!=method.invoke(t)){
								sql += columnName.columnName() + " like '%" + method.invoke(t)+ "%' and ";
							}
						}
					}
				}
			}
			sql+=" 1=1";
			System.out.println(sql);
			pre = con.prepareStatement(sql);
			resultSet = pre.executeQuery();
			while(resultSet.next()){
				T newT = (T) clz.newInstance();
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						ColumnName columnName = method.getAnnotation(ColumnName.class);
						String str = resultSet.getString(columnName.columnName());//以string的形式获取数据
						if(null==str){
							str = "";
						}
						String getName= method.getName();
						String setName = "set"+getName.substring(3,getName.length());
						Method setMethod = clz.getDeclaredMethod(setName,method.getReturnType());
						Constructor cons = method.getReturnType().getConstructor(String.class);//构造类似 xxx(String)的构造函数 ,xx由get的返回类型决定
						Object setObject = cons.newInstance(str);
						setMethod.invoke(newT,setObject);
					}
				}
				list.add(newT);
			}
			map.put("rows", list);
			map.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try {
				resultSet.close();
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	
	@Override
	public Integer getDataEntitiesCount(T t) {
		Integer total = 0;
		Connection con = null ;
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		
		try {
			con = DBUtil.openConnection();
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName annotation = (TableName) clz.getAnnotation(TableName.class);
			String tablename = annotation.tablename();
			String sql = "select count(id) from "+tablename+" where "; 
			Method[] methods = clz.getDeclaredMethods();
			if(null!=t){
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						if("get".equals(method.getName().subSequence(0, 3))){
							ColumnName columnName = method.getAnnotation(ColumnName.class);
							if(null!=method.invoke(t)){
								sql += columnName.columnName() + " like '%" + method.invoke(t)+ "%' and ";
							}
						}
					}
				}
			}
			sql+=" 1=1";
			System.out.println(sql);
			pre = con.prepareStatement(sql);
			resultSet = pre.executeQuery();
			while(resultSet.next()){
				total = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try {
				resultSet.close();
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;
	}
	
	@Override
	public List<T> getDataEntities(T t,int start,int pagesize) {
		List<T> list = new ArrayList<T>();
		Connection con = null ;
		PreparedStatement pre = null;
		ResultSet resultSet = null;
		
		try {
			con = DBUtil.openConnection();
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName annotation = (TableName) clz.getAnnotation(TableName.class);
			String tablename = annotation.tablename();
			String sql = "select * from "+tablename+" where "; 
			Method[] methods = clz.getDeclaredMethods();
			if(null!=t){
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						if("get".equals(method.getName().subSequence(0, 3))){
							ColumnName columnName = method.getAnnotation(ColumnName.class);
							if(null!=method.invoke(t)){
								sql += columnName.columnName() + " like '%" + method.invoke(t)+ "%' and ";
							}
						}
					}
				}
			}
			sql+=" 1=1";
			sql += " limit "+start+","+pagesize;
			System.out.println(sql);
			pre = con.prepareStatement(sql);
			resultSet = pre.executeQuery();
			while(resultSet.next()){
				T newT = (T) clz.newInstance();
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						ColumnName columnName = method.getAnnotation(ColumnName.class);
						String str = resultSet.getString(columnName.columnName());
						if(null==str){
							str = "";
						}
						String getName= method.getName();
						String setName = "set"+getName.substring(3,getName.length());
						Method setMethod = clz.getDeclaredMethod(setName,method.getReturnType());
						Constructor cons = method.getReturnType().getConstructor(String.class);
						Object setObject = cons.newInstance(str);
						setMethod.invoke(newT,setObject);
					}
				}
				list.add(newT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try {
				resultSet.close();
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}


	@Override
	public Object getData(Integer id) {
		Connection con = null ;
		PreparedStatement pre = null;
		ResultSet resultSet = null ;
		Object result = null;
		try {
			con = DBUtil.openConnection();
			Type genType = getClass().getGenericSuperclass();  
	        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
			Class clz = (Class) params[0];  
			TableName annotation = (TableName) clz.getAnnotation(TableName.class);
			String tablename = annotation.tablename();
			String sql = "select * from "+tablename+" where id="+id; 
			Method[] methods = clz.getDeclaredMethods();
			System.out.println(sql);
			pre = con.prepareStatement(sql);
			resultSet  = pre.executeQuery();
			while(resultSet.next()){
				T newT = (T) clz.newInstance();
				for(Method method : methods){
					if(method.isAnnotationPresent(ColumnName.class)){
						ColumnName columnName = method.getAnnotation(ColumnName.class);
						String str = resultSet.getString(columnName.columnName());
						if(null==str){
							str = "";
						}
						String getName= method.getName();
						String setName = "set"+getName.substring(3,getName.length());
						Method setMethod = clz.getDeclaredMethod(setName,method.getReturnType());
						Constructor cons = method.getReturnType().getConstructor(String.class);
						Object setObject = cons.newInstance(str);
						setMethod.invoke(newT,setObject);
					}
				}
				result = newT;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try {
				resultSet.close();
				pre.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
