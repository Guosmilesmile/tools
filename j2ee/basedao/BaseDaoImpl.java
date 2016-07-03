package cn.edu.xmu.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.xmu.dao.BaseDao;
import cn.edu.xmu.util.DBUtil;

/**
 * @author Lee
 * @param <T>
 *
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

	@Override
	public Object getData(String tablename, String tablefield,
			Map<String, String> params) {
		Object result = null;

		String sql = "select " + tablefield + " from " + tablename
				+ " where 1=1 and ";
		for (Object object : params.keySet()) {
			String key = object.toString();
			String value = params.get(key).toString();
			sql += String.format(" %s like  '%s%%' ", key, value);
		}
		System.out.println("select语句" + sql);

		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			pstmt = connection.prepareStatement(sql);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				result = resultSet.getInt(tablefield);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				resultSet.close();
				pstmt.close();
				connection.close();
			} catch (Exception e2) {
				result = null;
			}
		}
		return result;
	}

	@Override
	public int deleteRecord(String tablename, Map<String, String> params) {

		int result = 0;
		String sql = "delete from " + tablename + " where 1=1 ";
		for (Object o : params.keySet()) {
			String key = o.toString();
			String value = params.get(key).toString();
			sql += String.format(" and  %s = '%s' ", key, value);
		}
		System.out.println(sql);

		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				pstmt.close();
				connection.close();
			} catch (Exception e2) {
				return 0;
			}
		}
		return result;
	}

	@Override
	public int addRecord(String tablename, Map queryParams) {

		int result = 0;
		String sql = "insert into " + tablename + "(";
		for (Object o : queryParams.keySet()) {
			String key = o.toString() + ",";
			sql += key;
		}
		sql = sql.substring(0, sql.length() - 1);

		sql += ")values(";

		for (Object o : queryParams.keySet()) {
			String key = o.toString();
			String value = queryParams.get(key).toString();
			sql += String.format("'%s',", value);
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += ")";

		System.out.println(sql);

		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				pstmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return result;
	}

	@Override
	public int getEntityCount(String tablename, Map queryParams) {
		int count = 0;

		String sql = "select count(*) from " + tablename + " where 1 = 1";
		for (Object object : queryParams.keySet()) {
			String key = object.toString();
			String value = queryParams.get(key).toString();
			sql += String.format(" and  %s = '%s' ", key, value);
		}
		System.out.println(sql);
		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		try {
			pstmt = connection.prepareStatement(sql);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			count = -1;
		} finally {
			try {
				pstmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				count = -1;
			}
		}

		System.out.println(count);
		return count;
	}

	@Override
	public int updateRecord(String tablename, Map param1, Map param2) {

		int result = 0;

		String sql = "update " + tablename + " set ";
		for (Object object : param1.keySet()) {
			String key = object.toString();
			String value = param1.get(key).toString();
			sql += String.format(" %s = '%s',", key, value);
		}

		sql = sql.substring(0, sql.length() - 1);

		sql += " where 1=1 ";

		for (Object object : param2.keySet()) {
			String key = object.toString();
			String value = param2.get(key).toString();
			sql += String.format(" and %s = '%s'", key, value);
		}

		System.out.println(sql);

		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				pstmt.close();
				connection.close();
			} catch (Exception e2) {
				return 0;
			}
		}
		return result;
	}

	public List<T> findAll(String tablename, int start, int end, String sort,
			String order, Map params1, Map params2) {

		List<T> list = new ArrayList<T>();

		String sql = " select tmp.* from ( select";

		for (Object object : params1.keySet()) {
			String key = object.toString();
			sql += String.format(" %s,", key);
		}
		sql = sql.substring(0, sql.length() - 1);
		sql += " from " + tablename + " where 1=1 ";

		for (Object object : params2.keySet()) {
			String key = object.toString();
			String value = params2.get(key).toString();
			sql += String.format(" and %s = '%s'", key, value);
		}
		sql += " order by " + sort + " " + order + " ) tmp limit " + start
				+ " ," + end;

		System.out.println(sql);

		Connection connection = DBUtil.openConnection();
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			pstmt = connection.prepareStatement(sql);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {

				Object o = resultSet.getObject("");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				resultSet.close();
				pstmt.close();
				connection.close();
			} catch (Exception e2) {
				return null;
			}
		}

		return null;
	}

}
