package org.springside.examples.quickstart.service.spider.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemUtil {

	private final String fileName = "config.properties";

	private SystemUtil() {

	}

	public static SystemUtil getInstance() {
		return new SystemUtil();
	}

	public String getSystemConfig(String key) {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(fileName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p.getProperty(key);
	}

}
