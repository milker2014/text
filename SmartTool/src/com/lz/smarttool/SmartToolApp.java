package com.lz.smarttool;

import android.app.Application;


public class SmartToolApp extends Application{
	private static SmartToolApp mApp;

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
	}
	public static SmartToolApp getSelf() {
		return mApp;
	}
}
