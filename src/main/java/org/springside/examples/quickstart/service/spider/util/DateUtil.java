package org.springside.examples.quickstart.service.spider.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

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
