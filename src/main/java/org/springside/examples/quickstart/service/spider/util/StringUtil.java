package org.springside.examples.quickstart.service.spider.util;

public class StringUtil {

	public static boolean isNull(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

}
