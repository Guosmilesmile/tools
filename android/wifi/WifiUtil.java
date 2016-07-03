package com.example.wifiinfo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtil{
	private WifiManager wifiManager;
	private Context context;
	private WifiInfo wifiInfo;
	public WifiUtil(Context context){
		this.context = context;
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
	}
	/**
	 * 获取wifi名称
	 * @return
	 */
	public String GetWifiName(){
		return wifiInfo.getSSID();
	}
	/**
	 * 获取wifi状态
	 * @return
	 */
	public boolean GetWifiStatus(){
		if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取wifi ip
	 * @return
	 */
	public String GetWifiIp(){
		return intToIp(wifiInfo.getIpAddress());
	}
	/**
	 * 获取wifi速度
	 * @return
	 */
	public int GetWifiSpeed(){
		return wifiInfo.getLinkSpeed();
	}
	/**
	 * 获取wifi的ssid
	 * @return
	 */
	public String GetWifiSsid(){
		return wifiInfo.getSSID();
	}
	/**
	 * 获取所有wifi信息
	 * @return
	 */
	public String GetInfo(){
		String maxText = wifiInfo.getMacAddress();
		String ipText = intToIp(wifiInfo.getIpAddress());
		String status = "";
		if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			status = "WIFI_STATE_ENABLED";
		}
		String ssid = wifiInfo.getSSID();
		int networkID = wifiInfo.getNetworkId();
		int speed = wifiInfo.getLinkSpeed();
		return "mac：" + maxText + "\n\r" + "ip：" + ipText + "\n\r"
				+ "wifi status :" + status + "\n\r" + "ssid :" + ssid + "\n\r"
				+ "net work id :" + networkID + "\n\r" + "connection speed:"
				+ speed + "\n\r";
	}
	
	private String intToIp(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
				+ ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
	}
}
