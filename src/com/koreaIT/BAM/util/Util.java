package com.koreaIT.BAM.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	public static String getDateStr() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
