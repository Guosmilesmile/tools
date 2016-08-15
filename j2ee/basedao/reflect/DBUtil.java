
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * 数据库操作工具类
 * @author guoy1
 *
 */
public class DBUtil {
	private static BoneCP connectionPool = null;
	
	static {
		try {
			Properties prop = new Properties();
			String driver = null;
			String url = null;
			String username = null;
			String password = null;
			
			// System.out.println(DBUtil.class.getClassLoader().getResource(""));
			prop.load(DBUtil.class.getClassLoader().getResourceAsStream("DBConfig.properties"));

			driver = prop.getProperty("driver");

			Class.forName(driver);
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			
			BoneCPConfig config = new BoneCPConfig();  
			//数据库的JDBC URL  
            config.setJdbcUrl(url);   
            //数据库用户名  
            config.setUsername(username);   
            //数据库用户密码  
            config.setPassword(password);  
            //数据库连接池的最小连接数  
            config.setMinConnectionsPerPartition(2);  
            //数据库连接池的最大连接数  
            config.setMaxConnectionsPerPartition(10);  
            //  
            config.setPartitionCount(3);  
            config.setAcquireIncrement(2);
            //设置数据库连接池  
            connectionPool = new BoneCP(config);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	public static void closeConn(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库的连接
	 * @return
	 */
	public static Connection openConnection() {
        Connection connection = null;
        //从数据库连接池获取一个数据库连接  
        try {
			connection = connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} // fetch a connection  
		return connection;
	}

	/**
	 * Execute a query sql and return a ResultSet, such as a SELECT query
	 * 
	 * @param sql
	 *            The sql statement
	 * @return The ResultSet object
	 * @throws SQLException
	 */
	public static ResultSet executeQuery(String sql, Connection conn)
			throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}

	/**
	 * Execute a sql statement without a result, such as a INSERT, UPDATE,
	 * DELETE statement
	 * 
	 * @param sql
	 *            The sql statement
	 * @throws SQLException
	 */
	public static void executeSql(String sql) throws SQLException {
		Connection conn = openConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		pstmt.close();
		closeConn(conn);
	}

	public static int executeUpdate(String sql) throws SQLException {
		Connection conn = openConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int result = pstmt.executeUpdate();
		pstmt.close();
		closeConn(conn);
		return result;
	}
}
