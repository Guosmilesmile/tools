package com.cnc.roas.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * ssh连接工具类
 * @author guoy1
 *
 */
public class SSHUtil {

	private static String host;//主机名
	private static int port;//端口
	private static String user;//用户名
	private static String password;//密码
	
	private static Session SESSION;//会话
	private static ChannelExec OPENCHANNEL ;//操作通道
	static{
		try {
			Configuration config = new PropertiesConfiguration("SSHConfig.properties");
			host = config.getString("host");
			port = config.getInt("port");
			user = config.getString("user");
			password = config.getString("password");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * 
	 * @param host 主机名
	 * @param port 端口
	 * @param user 用户名
	 * @param password 密码
	 */
	public SSHUtil(String host, int port, String user, String password) {
		super();
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	/**
	 * 关闭连接
	 */
	public static void CloseSession(){
		CloseChannel();
		if(null==SESSION || !SESSION.isConnected()){
			
		}else{
			SESSION.disconnect();
		}
	}
	
	/**
	 * 获取session连接
	 * @return
	 */
	public static Session getSession(){
		try {
			if(null==SESSION || !SESSION.isConnected()){
				JSch jSch = new JSch();
				SESSION = jSch.getSession(user, host, port);
				Properties configProperties = new Properties();
				configProperties.put("StrictHostKeyChecking", "no");
				SESSION.setConfig(configProperties);
				SESSION.setPassword(password);
				SESSION.connect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SESSION;
	}
	
	/**
	 * 关闭操作通道
	 * @return
	 */
	public static void CloseChannel(){
		if(null==OPENCHANNEL){
			
		}else{
			try {
				OPENCHANNEL.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	/**
	 * 获取操作通道
	 * @return
	 */
	public static void excuteChannel(String command,final SSHExectResultCallBack sri){
		SESSION = getSession();
		try {
			if(null==OPENCHANNEL || OPENCHANNEL.isClosed()){
				OPENCHANNEL = (ChannelExec) SESSION.openChannel("exec");
			}
			OPENCHANNEL.setCommand(command);
			OPENCHANNEL.connect();
			InputStream in = OPENCHANNEL.getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			new Thread(){
				public void run() {
					try {
						String buf = null;
						while ((buf = reader.readLine()) != null) {
							String content =  new String(buf.getBytes("gbk"), "UTF-8")+ "\r\n";
							sri.getResult(content);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 无长连接，执行一次性指令
	 * @param command
	 * @return
	 */
	public static String excuteOnce(String command){
		SESSION = getSession();
		String result = "";
		try {
			if(null==OPENCHANNEL || OPENCHANNEL.isClosed()){
				OPENCHANNEL = (ChannelExec) SESSION.openChannel("exec");
			}
			OPENCHANNEL.setCommand(command);
			OPENCHANNEL.connect();
			InputStream in = OPENCHANNEL.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				result += new String(buf.getBytes("gbk"), "UTF-8")+ "\r\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result += e.getMessage();
		}finally{
			if (OPENCHANNEL != null && !OPENCHANNEL.isClosed()) {
				OPENCHANNEL.disconnect();
			}
			if (SESSION != null && SESSION.isConnected()) {
				SESSION.disconnect();
			}
		}
		return result;
	}
	public SSHUtil() {
		super();
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 测试入口
	 * @param args
	 */
	public static void main(String[] args) {
		String excuteOnce = SSHUtil.excuteOnce("cd /usr/local/mongodb/bin;ls;");
		System.out.println(excuteOnce);
	}
}