package com.jiale.util;

import java.util.UUID;

public class GetCodeUtil {
	// �ӷ���ϵͳ��cookie�л�ȡ��֤��
	public static String getCode(String cookie) {
		String code = null;
		if (cookie.startsWith("L")) {
			code = cookie.substring(cookie.indexOf("=") + 1, cookie.indexOf(";"));
		} else {
			code = cookie.substring(cookie.lastIndexOf("=") + 1);
		}
		System.out.println("-----------------------------------------------��֤�룺" + code
				+ "------------------------------------------------------------------------");
		return code;
	}

	// ���ϻ�ȡ�����¼��Ҫ��cookie
	public static String getCookie(String cookie) {
		String newCookie = null;
		UUID uuid = java.util.UUID.randomUUID();
		newCookie = "NGID=" + uuid + "; " + uuid + "=http://jwc.wyu.edu.cn/student/body.htm; " + cookie;
		System.out.println("-----------------------------------------------��Cookie��" + newCookie
				+ "------------------------------------------------------------------------");
		return newCookie;
	}
}
