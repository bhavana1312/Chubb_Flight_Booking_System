package com.flightapp.util;

import java.util.UUID;

public class PnrUtil {
	private PnrUtil() {}
	public static String generate() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
	}
}
