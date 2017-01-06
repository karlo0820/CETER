package com.jiale.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {

	/**
	 * 把输入流的内容转化为字符串
	 * 
	 * @param is
	 * @return
	 */

	public static String readInputStream(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];

			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			is.close();
			baos.close();
			byte[] result = baos.toByteArray();
			// 试着解析result中的代码
			String temp = new String(result);
			if (temp.contains("utf-8")) {
				return temp;
			} else if (temp.contains("gb2312")) {

				return new String(result, "gb2312");
			} else if (temp.contains("gbk")) {
				return new String(result, "gbk");
			}
			return temp;
			// return new String(result);//默认的utf-8编码格式，其他格式的会出现返回乱码
		} catch (Exception e) {
			e.printStackTrace();
			return "获取源代码失败";
		}
	}
}