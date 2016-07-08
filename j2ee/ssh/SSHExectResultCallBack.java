package com.cnc.roas.utils;

/**
 * 长连接结果返回
 * @author guoy1
 *
 */
public interface SSHExectResultCallBack {
	/**
	 * 获取主机返回的结果
	 * @param content
	 */
	public void getResult(String content);
}
