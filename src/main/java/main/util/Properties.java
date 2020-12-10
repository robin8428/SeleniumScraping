package main.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;


public class Properties {

	java.util.Properties properties;

	public Properties() {
		properties = new java.util.Properties();
		String propFileName = "config.properties";

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		try {
			properties.load(inputStream);
		} catch (IOException e) {
			ExceptionUtils.rethrow(e);
		}
	}


	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
