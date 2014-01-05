package org.springside.examples.quickstart.service.spider.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static String getCurrentDateTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret = format.format(date);
		return toChineseDate(ret);
	}

	public static void main(String[] args) {
		String a = "2001-05-06 12:56:32";
		String[] tmp = a.split(" ");
		String[] ymd = tmp[0].split("-");
		String[] hms = tmp[1].trim().split(":");

		StringBuffer buf = new StringBuffer();
		buf.append(ymd[0]).append("年").append(ymd[1]).append("月")
				.append(ymd[2]).append("日").append(hms[0]).append("时").append(
						hms[1]).append("分").append(hms[2]).append("秒");
		System.out.println(buf.toString());
	}

	private static String toChineseDate(String date) {
		String[] tmp = date.split(" ");
		String[] ymd = tmp[0].split("-");
		String[] hms = tmp[1].trim().split(":");

		StringBuffer buf = new StringBuffer();
		buf.append(ymd[0]).append("年").append(ymd[1]).append("月")
				.append(ymd[2]).append("日").append(hms[0]).append("时").append(
						hms[1]).append("分").append(hms[2]).append("秒");
		return buf.toString();
	}

	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(format.format(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	/**
	 * 取当前日期的不同格式数组
	 * 
	 * @return
	 */
	public static String[] getDateDiffFormat() {
		List<String> lst = new ArrayList<String>();

		Calendar cal = Calendar.getInstance();
		Date curr = cal.getTime();

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		lst.add(format1.format(curr));

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
		lst.add(format2.format(curr));

		return (String[]) lst.toArray(new String[lst.size()]);
	}

	/**
	 * 将制定日期格式化为不同格式的数组
	 * 
	 * @return
	 */
	public static String[] getDateDiffFormat(String date) {
		List<String> lst = new ArrayList<String>();

		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		try {
			lst.add(format1.format(format1.parse(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat format2 = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			lst.add(format2.format(format1.parse(date)));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return (String[]) lst.toArray(new String[lst.size()]);
	}

	public static Date formatDate(String publishDate) {
		if (StringUtil.isNull(publishDate)) {
			return null;
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format1.parse(publishDate.trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String formatDate(Date publishDate) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = null;
		date = format1.format(publishDate);
		return date;
	}
}
