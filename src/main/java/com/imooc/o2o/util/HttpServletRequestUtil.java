package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
	/**
	 * 从request里获得key，将key的值转换为整型，如果失败就返回-1
	 * @param request
	 * @param key
	 * @return
	 */
	public static int getInt(HttpServletRequest request,String key) {
		try {
			return Integer.decode(request.getParameter(key));
		}catch(Exception e){
			return -1;
		}
	}
	/**
	 * 从request里获得key，将key的值转换为长整型，如果失败就返回-1
	 * @param request
	 * @param key
	 * @return
	 */
	public static long getLong(HttpServletRequest request,String key) {
		try {
			return Long.valueOf(request.getParameter(key));
		}catch(Exception e){
			return -1;
		}
	}
	/**
	 * 从request里获得key，将key的值转换为double型，如果失败就返回-1d(double型的-1)
	 * @param request
	 * @param key
	 * @return
	 */
	public static double getDouble(HttpServletRequest request,String key) {
		try {
			return Double.valueOf(request.getParameter(key));
		}catch(Exception e){
			return -1d;
		}
	}
	/**
	 * 从request里获得key，将key的值转换为布尔型，如果失败就返回false
	 * @param request
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(HttpServletRequest request,String key) {
		try {
			return Boolean.valueOf(request.getParameter(key));
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * 从request里获得key，将key的值转换为字符串类型，如果失败就返回null
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getString(HttpServletRequest request,String key) {
		try {
			String result = request.getParameter(key);
			if(result != null) {
				result = result.trim();
			}
			if("".equals(result)) {
				result = null;
			}
			return result;
		}catch(Exception e){
			return null;
		}
	}
}
