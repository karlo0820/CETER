package com.jiale.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jiale.bean.Achievement;
import com.jiale.bean.Course;
import com.jiale.bean.Syllabus;
import com.jiale.bean.WyuNew;

import android.util.Log;

public class JxHtml {
	/**
	 * ����У԰����ҳ��
	 * 
	 * @param jwhtml
	 * @return List
	 */
	public static List<WyuNew> jiaowu(String jwhtml) {
		System.out.println("----------------------------------- ����ҳ�������ʼ");
		String newsHref = null;
		List<WyuNew> list = new ArrayList<WyuNew>();
		Log.i("����", jwhtml);
		Document jwdoc = Jsoup.parse(jwhtml);
		Elements el = jwdoc.select("[width=406]");
		Elements elAuthor = jwdoc.select("[width=74]");
		Elements elTime = jwdoc.select("[width=82]");
		for (int i = 0; i < el.size(); i++) {
			WyuNew wNew = new WyuNew();
			Element link = el.select("a").get(i);
			newsHref = "http://jwc.wyu.edu.cn/www/" + link.attr("href");
			wNew.setHref(newsHref);
			wNew.setPublicAuthor(elAuthor.get(i).text());
			wNew.setTitle(link.text());
			wNew.setPublicTime(elTime.get(i).text());
			list.add(wNew);
		}
		Log.i("����", "��������");
		return list;
	}

	public static String[] getLibPostData(String libHtml) {
		System.out.println("-----------������¼ҳ����post����-----------------------");
		System.out.println(libHtml);
		String[] string = null;
		if (libHtml != null) {
			Document lib = Jsoup.parse(libHtml);
			Element dation = lib.getElementById("__EVENTVALIDATION");
			Element state = lib.getElementById("__VIEWSTATE");
			Log.i("__EVENTVALIDATION", dation.val());
			Log.i("__VIEWSTATE", state.val());
			string = new String[] { dation.val(), state.val() };
			return string;
		} else {
			Log.i("��������ҳ��--->", "null");
		}
		return null;
	}

	/**
	 * ����ͼ���ҳ��
	 * 
	 * @param libHtml
	 * @return List<String>
	 */
	public static List<String> wyuLib(String libHtml) {
		System.out.println("-----------������ҳ��-----------------------");
		System.out.println(libHtml);
		Document lib = Jsoup.parse(libHtml);
		Elements borrowed = lib.select("#borrowedcontent");
		Elements title = borrowed.select("tr");
		Element dation = lib.getElementById("__EVENTVALIDATION");
		Element state = lib.getElementById("__VIEWSTATE");
		Log.i("__EVENTVALIDATION", dation.val());
		Log.i("__VIEWSTATE", state.val());
		Log.i("��������", String.valueOf(title.size()));
		List<String> data = new ArrayList<String>();
		data.add(dation.val());// ��������ʱ��Ҫpost���������ݣ��洢��data��0,1λ��
		data.add(state.val());
		// ����data�ȴ洢����title,��������
		for (int i = 1; i < title.size(); i++) {
			Elements type = title.get(i).select("[width=5%]");
			Elements time = title.get(i).select("[width=10%]");
			String name = title.get(i).select("[width=35%]").text();
			String flat = type.get(0).text();
			String last = time.get(0).text();
			String lease = time.get(1).text();
			data.add(flat + "=" + last + "=" + name + "=" + lease);
		}
		return data;
	}

	/**
	 * ����ˮ���ѯҳ��
	 * 
	 * @param bthtml
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> battery(String bthtml) {
		// String[] data = new String[3];
		ArrayList<String> list = new ArrayList<String>();
		// int index = 0;
		Log.i("battery", "�ڽ���HTML");
		Document btdoc = Jsoup.parse(bthtml);
		Elements tb = btdoc.select("[width=98%]");
		Elements value = tb.select(".STYLE7");
		Log.i("battery", "��������");
		for (int i = 0; i < value.size(); i++) {
			if (i > 1) {
				list.add(value.get(i).text());
				// data[index] = value.get(i).text();
				// index++;
			}
		}
		return list;
	}

	/**
	 * �����α�ҳ��
	 * 
	 * @param html
	 */
	public static List<Syllabus> kb(String html) {
		Document doc = Jsoup.parse(html);
		List<Syllabus> list = new ArrayList<Syllabus>();
		Elements tab = doc.select("table[borderColorDark=#eeeeee]");
		Elements els = tab.get(0).select("tr");
		System.out.println("��ȡtab:" + els.text());
		Elements tb2 = tab.get(1).select("tr");
		System.out.println("��ȡ��tab2:" + tb2.text());
		List<String> tearcher = new ArrayList<String>();
		for (int tc = 1; tc < tb2.size(); tc++) {
			String[] string = tb2.get(tc).text().split(" ");
			System.out.println("tab2:" + string[3]);
			tearcher.add(string[3]);
		}
		for (int jie = 1; jie < els.size() - 1; jie++) {
			System.out.println("��" + jie + "���");
			Elements el = els.get(jie).select("td[valign=top]");
			System.out.println("��ȡ������tr:" + el.text());
			for (int day = 0; day < el.size(); day++) {
				System.out.println("����" + (day + 1) + "��");
				Element e = el.get(day);
				System.out.println("����td:" + e.text());
				String[] s = e.text().split(" ");

				System.out.println("�ָ��ĳ���" + s.length);

				int index = 0;

				for (int z = 0; z < s.length / 3; z++) {

					System.out.println("��" + (z + 1) + "���γ�");
					Syllabus sl = new Syllabus();
					sl.setJie(jie);
					sl.setDay(day + 1);
					for (int x = 0; x < 3; x++) {
						if (x % 3 == 0) {
							System.out.println("�γ�����" + s[index]);
							sl.setCourseName(s[index++]);
						} else if (x % 3 == 1) {
							// ��2-3,5-10��
							String[] temp = s[index].substring(1, s[index].length() - 1).split(",");
							System.out.println(temp.length);
							for (int g = 0; g < temp.length; g++) {
								String[] all = temp[g].split("-");
								if (g % 2 == 0) {
									for (int time = 0; time < all.length; time++) {
										if (time % 2 == 0) {
											System.out.println("�γ��϶ο�ʼʱ�䣺" + all[time]);
											sl.setStartUP(Integer.parseInt(all[time]));
										} else if (time % 2 == 1) {
											System.out.println("�γ��϶ν���ʱ�䣺" + all[time]);
											sl.setEndUP(Integer.parseInt(all[time]));
										}
									}
								} else if (g % 2 == 1) {
									for (int time = 0; time < all.length; time++) {
										if (time % 2 == 0) {
											System.out.println("�γ��¶ο�ʼʱ�䣺" + all[time]);
											sl.setStartDown(Integer.parseInt(all[time]));
										} else if (time % 2 == 1) {
											System.out.println("�γ��¶ν���ʱ�䣺" + all[time]);
											sl.setEndDown(Integer.parseInt(all[time]));
										}
									}
								}
							}
							index++;
						} else if (x % 3 == 2) {
							for (int t = 0; t < tearcher.size(); t++) {
								int len;
								if ((len = s[index].indexOf(tearcher.get(t))) != -1) {
									System.out.println("��ʦ��" + s[index].substring(len));
									sl.setTearcher(s[index].substring(len));
									System.out.println("�Ͽεص㣺" + s[index].substring(0, len));
									sl.setAddress(s[index].substring(0, len));
									index++;
									System.out.println("------------------------------------------");
									break;
								}

							}
						}
					}
					list.add(sl);
				}
			}
		}

		Iterator<Syllabus> it = list.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().toString());
		}
		return list;
	}

	public static List<String> trainPlan(String html) {
		List<String> plan = new ArrayList<String>();
		Document doc = Jsoup.parse(html);
		Elements tr = doc.select("tr[height=\"20\"]");
		for (int i = 1; i < tr.size(); i++) {
			Elements td = tr.get(i).select("td");
			if (td.size() == 9) {
				plan.add(td.get(1).text());
				Log.i("�����ƻ�", td.get(1).toString());
			} else {
				plan.add(td.get(0).text());
				Log.i("�����ƻ�", td.get(0).toString());
			}
		}
		return plan;
	}

	/**
	 * �����ɼ�ҳ��
	 * 
	 * @param html
	 * @return
	 */
	public static List<Achievement> cj(String html) {
		System.out.println("��ʼ�����ɼ�ҳ��");
		List<Achievement> list = new ArrayList<Achievement>(); // һ��ѧ�ڵ�����

		List<String> semester = new ArrayList<String>(); // ѧ��
		Document doc = Jsoup.parse(html);
		Elements tab = doc.select("table.MsoTableGrid");

		Elements els = doc.select("span[lang=EN-US]");

		Pattern p = Pattern.compile("^\\d{4}-\\d{4}");
		boolean flag = true;
		for (int y = 0; y < els.size(); y++) {
			if ((p.matcher(els.get(y).text()).matches())) {
				if (flag) {
					semester.add(els.get(y).text() + "��");
					flag = false;
				} else {
					semester.add(els.get(y).text() + "��");
					flag = true;
				}
			}
		}
		for (int i = 0; i < tab.size() - 2; i++) {
			Elements tr = tab.get(i).select("tr");
			Achievement ac = new Achievement(); // һ���ɼ�
			ac.setSemester(semester.get(i)); // ���ø�ѧ��
			List<Course> cl = new ArrayList<Course>(); // һѧ�ڵ����гɼ�
			for (int y = 0; y < tr.size(); y++) {
				Elements el = tr.get(y).select("p");
				Course c = new Course();
				for (int z = 0; z < el.size(); z++) {

					if (z % 6 == 0) {
						c.setCourseNumber(el.get(z).text());
					} else if (z % 6 == 1) {
						c.setCourseName(el.get(z).text());
					} else if (z % 6 == 2) {
						c.setCategory(el.get(z).text());
					} else if (z % 6 == 3) {
						c.setCredit(el.get(z).text());
					} else if (z % 6 == 4) {
						c.setScore(el.get(z).text());
					} else if (z % 6 == 5) {
						c.setRemark(el.get(z).text());
					}
				}
				cl.add(c);
			}
			ac.setCourse(cl);
			list.add(ac);
		}
		System.out.println("��ɽ����ɼ�ҳ��");
		return list;
	}
}
