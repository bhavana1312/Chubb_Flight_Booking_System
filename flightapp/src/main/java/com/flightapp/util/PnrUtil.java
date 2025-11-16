package com.flightapp.util;

import java.util.UUID;

public class PnrUtil {
	public static String generate() {
		String raw = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
		return raw;
	}
}
