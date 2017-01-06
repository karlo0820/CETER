package com.jiale.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.jiale.bean.CET;

import android.os.Handler;
import android.util.Log;

public class MyService {
	// 最后一次请求
	public static String get(String url) {
		System.out.println("请求页面");
		String html = "";
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader("Referer", "http://jwc.wyu.edu.cn/student/menu.asp");
		get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Host", "jwc.wyu.edu.cn");
		get.addHeader("DNT", "1");
		get.addHeader("Connection", "Keep-Alive");
		synchronized (client) {
			try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = StreamTools.readInputStream(response.getEntity().getContent());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (get != null) {
					get.abort();
				}
				GetClient.httpClient = null;
			}
		}
		System.out.println("完成请求页面");
		System.out.println(html.length());
		System.out.println(html);
		return html;
	}

	// 请求培养计划页面
	public static String getTrainPlan() {
		String html = "";
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet("http://jwc.wyu.edu.cn/student/f2.asp");
		get.addHeader("Referer", "http://jwc.wyu.edu.cn/student/menu.asp");
		get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Host", "jwc.wyu.edu.cn");
		get.addHeader("DNT", "1");
		get.addHeader("Connection", "Keep-Alive");
		synchronized (client) {

			try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = StreamTools.readInputStream(response.getEntity().getContent());
					Log.i("TrainingPlan", "成绩");
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (get != null) {
					get.abort();
				}
				GetClient.httpClient = null;
			}
		}
		System.out.println("完成请求培养方案页面");
		System.out.println(html.length());
		System.out.println(html);
		return html;
	}

	/**
	 * 请求图书馆登录页面
	 **/
	public static String getLib(String url) {
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("Host", "lib.wyu.edu.cn");
		get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
//		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Referer", " http://lib.wyu.edu.cn/Html/pagetop.asp");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		synchronized (client) {
			try {
				HttpResponse response = client.execute(get);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String html = StreamTools.readInputStream(response.getEntity().getContent());
					System.out.println(html);
					return html ;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (get != null)
					get.abort();
			}
		}
		return null;
	}

	/**
	 * 请求子系统登录页面，获取验证码 请求页面方法 params: url
	 * 
	 * return:String ---cookie
	 */
	public static String getCjLogin(String url) {
		System.out.println("请求登录页面，获取验证码");
		String cookie = "";
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader("Connection", "Keep-Alive");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("Host", "jwc.wyu.edu.cn");
		get.addHeader("Accept", "text/html, application/xhtml+xml, image/jxr, */*");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");

		synchronized (client) {
			try {
				HttpResponse response = client.execute(get);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
					// System.out.println(StreamTools.readInputStream(response.getEntity().getContent()));
					if (cookies.size() > 1) {
						for (Cookie c : cookies) {
							System.out.println("cookie的value" + c.getValue());
							cookie += c.getName() + "=" + c.getValue() + "; ";
						}
						cookie = cookie.substring(0, cookie.length() - 1);
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// if (get != null)
				// get.abort();
			}
		}
		System.out.println("-------------------------------------------------------------------------------Cookie"
				+ cookie + "------------------------------------------------------------------------");
		return cookie;
	}

	/**
	 * 请求子服务系统中的成绩页面 params: url cookie
	 */
	public static String getCj(String url) {
		System.out.println("请求成绩页面");
		String html = "";
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader("Referer", "http://jwc.wyu.edu.cn/student/menu.asp");
		get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Host", "jwc.wyu.edu.cn");
		get.addHeader("DNT", "1");
		get.addHeader("Connection", "Keep-Alive");
		synchronized (client) {
			try {
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = StreamTools.readInputStream(response.getEntity().getContent());
					Log.i("CJ", "成绩");
				} else if (response.getStatusLine().getStatusCode() == 302) {
					html = StreamTools.readInputStream(response.getEntity().getContent());
					Log.i("CJ", "成绩");
					Log.i("重定向", "302");
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (get != null) {
					get.abort();
				}
			}
		}
		System.out.println("完成请求成绩页面");
		System.out.println(html.length());
		System.out.println(html);
		return html;
	}

	/**
	 * 子服务系统请求登陆方法 params: UserCode UserPwd code cookie url
	 */
	public static String post(String UserCode, String UserPwd, String code, String cookie, String url) {
		System.out.println("请求登录");
		String html = "";
		String ck = "";
		HttpClient client = GetClient.getHttpClient();
		HttpResponse response = null;
		HttpEntity entity = null;
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Submit", "提 交"));
		params.add(new BasicNameValuePair("UserCode", UserCode));
		params.add(new BasicNameValuePair("UserPwd", UserPwd));
		params.add(new BasicNameValuePair("Validate", code));
		synchronized (client) {

			try {
				post.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
				post.addHeader("Cookie", cookie);
				post.addHeader("Referer", "http://jwc.wyu.edu.cn/student/body.htm");
				post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
				post.addHeader("Accept-Language", "zh-CN");
				post.addHeader("Content-Type", "application/x-www-form-urlencoded");
				post.addHeader("Accept", "text/html, application/xhtml+xml, */*");
				post.addHeader("Accept-Encoding", "gzip, deflate");
				post.addHeader("Host", "jwc.wyu.edu.cn");
				post.addHeader("Cache-Control", "no-cache");
				post.addHeader("DNT", "1");
				post.addHeader("Connection", "Keep-Alive");
				response = client.execute(post);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
					if (cookies != null) {
						for (Cookie c : cookies) {

							ck += c.getName() + "=" + c.getValue() + ";";
						}
						ck = ck.substring(0, ck.length() - 1);
						System.out.println(ck);
					}
					entity = response.getEntity();
					html = StreamTools.readInputStream(entity.getContent());
					System.out.println(
							"----------------------------------------------------请求登录后页面-------------------------------------------------------------------");
					System.out.println(html.length());
					System.out.println(html);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
		System.out.println("请求登录完成");
		return html;
	}

	// 请求图书续借 index 第几本书 dation state post的数据
	public static String postExBooks(int index, String dation, String state) {
		String html = null;
		HttpClient client = GetClient.getHttpClient();
		HttpPost post = new HttpPost("http://lib.wyu.edu.cn/opac/user/bookborrowed.aspx");
		HttpResponse response = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__EVENTVALIDATION", dation));
		params.add(new BasicNameValuePair("__VIEWSTATE", state));
		params.add(new BasicNameValuePair("ctl00$cpRight$borrowedRep$ctl0" + index + "$cbk", "on"));
		params.add(new BasicNameValuePair("ctl00$cpRight$btnRenew", "续借"));

		post.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		post.addHeader("Accept-Language", "zh-CN");
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Host", "lib.wyu.edu.cn");
		post.addHeader("DNT", "1");
		post.addHeader("Connection", "Keep-Alive");
		post.addHeader("Cache-Control", "no-cache");
		synchronized (client) {

			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = StreamTools.readInputStream(response.getEntity().getContent());
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (post != null) {
					post.abort();
				}
			}
		}
		System.out.println("--------------------------请求图书续借返回页面--------------------------");
		System.out.println(html);
		return html;
	}

	// 请求登录图书馆页面 返回登录后的页面 ，html测试是否成功登录
	public static String postLib(String User, String Pwd, String dation, String state, String Code, String Url) {
		String cookie = "";
		String html = null;
		Log.i("请求登录", "获得client");
		HttpClient client = GetClient.getHttpClient();
		HttpPost post = new HttpPost(Url);
		HttpResponse response = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__EVENTARGUMENT", "                "));
		params.add(new BasicNameValuePair("__EVENTTARGET", "              "));
		params.add(new BasicNameValuePair("__EVENTVALIDATION", dation));
		params.add(new BasicNameValuePair("__VIEWSTATE", state));
		params.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$btnLogin_Lib", "登录"));
		params.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$txtCode", Code));
		params.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$txtlogintype", "0"));
		params.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$txtPas_Lib", Pwd));
		params.add(new BasicNameValuePair("ctl00$ContentPlaceHolder1$txtUsername_Lib", User));

		post.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		post.addHeader("Referer", "http://lib.wyu.edu.cn/opac/login.aspx");
		post.addHeader("Accept-Language", "zh-CN");
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
//		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Host", "lib.wyu.edu.cn");
		post.addHeader("Connection", "Keep-Alive");
		post.addHeader("Cache-Control", "no-cache");
		synchronized (client) {

			try {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				response = client.execute(post);
				Log.i("StatusCode", "" + response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
					for (Cookie c : cookies) {
						cookie += c.getName() + "=" + c.getValue() + ";";
					}
					Log.i("cookie", cookie);
					html = StreamTools.readInputStream(response.getEntity().getContent());
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (post != null) {
					post.abort();
				}
			}
		}
		System.out.println("-------------请求登录返回页面------------------");
		System.out.println(html.length());
		System.out.println(html);
		return html;
	}

	// 请求 图书馆借阅情况的页面
	public static String doGet(String url) {
		String html = "";
		HttpClient client = GetClient.getHttpClient();
		HttpGet get = new HttpGet(url);
		get.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		get.addHeader("Referer", "http://lib.wyu.edu.cn/opac/user/userinfo.aspx");
		get.addHeader("Accept-Language", "zh-CN");
		get.addHeader("User-Agent", "ozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
//		get.addHeader("Accept-Encoding", "gzip, deflate");
		get.addHeader("Host", "lib.wyu.edu.cn");
		get.addHeader("Connection", "Keep-Alive");
		synchronized (client) {
			try {
				HttpResponse response = client.execute(get);
				Log.i("StatusCode", "" + response.getStatusLine().getStatusCode());
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					Log.i("请求借阅页面返回长度", response.getEntity().getContentLength() + "");
					html = StreamTools.readInputStream(response.getEntity().getContent());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (get != null) {
					get.abort();
				}
			}
		}
		return html;
	}

	// 登录用电查询页面
	public static String postBattery(String url, String room, String num) {
		String html = "";
		HttpPost post = new HttpPost(url);
		HttpClient client = GetClient.getHttpClient();
		HttpResponse response = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action", "search"));
		params.add(new BasicNameValuePair("apartID", num));
		params.add(new BasicNameValuePair("meter_room", room));
		params.add(new BasicNameValuePair("Submit", "%CC%E1%BD%BB"));

		post.addHeader("Accept", "text/html, application/xhtml+xml, */*");
		post.addHeader("Referer", "http://202.192.252.140/index.asp");
		post.addHeader("Accept-Language", "zh-CN");
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Accept-Encoding", "gzip, deflate");
		post.addHeader("Host", "202.192.252.140");
		post.addHeader("DNT", "1");
		post.addHeader("Connection", "Keep-Alive");
		post.addHeader("Cache-Control", "no-cache");
		synchronized (client) {
			try {
				post.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
				response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = EntityUtils.toString(response.getEntity());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (post != null) {
					post.abort();
				}
				GetClient.httpClient = null;
			}
		}
		return html;

	}

	public static String postfs(String id, String name) {
		HttpClient client = GetClient.getHttpClient();
		String html = null;
		HttpResponse response = null;
		HttpPost post = new HttpPost("http://cet.99sushe.com/findscore");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("name", name));
		String cookie = "CNZZDATA30023677=cnzz_eid%3D1065784670-1445416365-http%253A%252F%252Fcet.99sushe.com%252F%26ntime%3D1445416365; id="
				+ id;
		synchronized (client) {
			try {
				post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
				post.addHeader("Cookie", cookie);
				post.addHeader("Host", "cet.99sushe.com");
				post.addHeader("Connection", "Keep-Alive");
				post.addHeader("Cache-Control", "no-cache");
				post.addHeader("Referer", "http://cet.99sushe.com/");
				post.addHeader("DNT", "1");
				post.addHeader("Accept", "text/html, application/xhtml+xml, */*");
				post.addHeader("Accept-Encoding", "gzip, deflate");
				post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");

				response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					html = EntityUtils.toString(response.getEntity());
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (post != null) {
					post.abort();
				}
				GetClient.httpClient = null;
			}
		}
		System.out.println(html);
		return html;
	}
}
