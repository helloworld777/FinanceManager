package com.lu.momeymanager.util;

import java.text.DecimalFormat;

public class FormatUti {
	public static void formatNumber(double d) {
		final DecimalFormat decimalFormat = new DecimalFormat("#.00");
		decimalFormat.format(d);
	}
}
