package com.lz.smarttool.utils;

import android.util.Log;

public class UtilLog {

	// 发布时关闭isShowLOG
	private static final boolean isShowLOG = true;// true

	// 用来防止msg为空时的异常
	private static final String NULL_STR = "msg is null!";

	public static void v(String tag, String msg) {
		if (isShowLOG)
			Log.v(tag, msg != null ? msg : NULL_STR);
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (isShowLOG)
			Log.v(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void d(String tag, String msg) {
		if (isShowLOG)
			Log.d(tag, msg != null ? msg : NULL_STR);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (isShowLOG)
			Log.d(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void i(String tag, String msg) {
		if (isShowLOG)
			Log.i(tag, msg != null ? msg : NULL_STR);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (isShowLOG)
			Log.i(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void w(String tag, String msg) {

		if (isShowLOG)
			Log.w(tag, msg != null ? msg : NULL_STR);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (isShowLOG)
			Log.w(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void e(String tag, String msg) {
		if (isShowLOG)
			Log.e(tag, msg != null ? msg : NULL_STR);
	}

}
