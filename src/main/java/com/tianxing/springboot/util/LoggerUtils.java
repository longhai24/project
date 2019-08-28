package com.tianxing.springboot.util;

import org.slf4j.LoggerFactory;

public class LoggerUtils {

	public static void debug(Class cls, String message) {
		LoggerFactory.getLogger(cls).debug(message);
	}

	public static void info(Class cls, String message) {
		LoggerFactory.getLogger(cls).info(message);
	}

	public static void error(Class cls, String message, Throwable ex) {
		LoggerFactory.getLogger(cls).error(message, ex);
	}

}
