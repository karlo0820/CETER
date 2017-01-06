package com.jiale.util;

import java.util.UUID;

public class GetCodeUtil {
	// 子服务系统从cookie中获取验证码
	public static String getCode(String cookie) {
		String code = null;
		if (cookie.startsWith("L")) {
			code = cookie.substring(cookie.indexOf("=") + 1, cookie.indexOf(";"));
		} else {
			code = cookie.substring(cookie.lastIndexOf("=") + 1);
		}
		System.out.println("-----------------------------------------------验证码：" + code
				+ "------------------------------------------------------------------------");
		return code;
	}

	// 整合获取请求登录需要的cookie
	public static String getCookie(String cookie) {
		String newCookie = null;
		UUID uuid = java.util.UUID.randomUUID();
		newCookie = "NGID=" + uuid + "; " + uuid + "=http://jwc.wyu.edu.cn/student/body.htm; " + cookie;
		System.out.println("-----------------------------------------------新Cookie：" + newCookie
				+ "------------------------------------------------------------------------");
		return newCookie;
	}
}
