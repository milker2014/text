package com.lz.smarttool.utils;

import com.lz.smarttool.R;
import com.lz.smarttool.SmartToolApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Aos自定义toast
 * 
 * @description:
 * @author cuiwei
 * @version 1.0
 * @created 2012-09-06
 */
public class AosToast extends Toast {

	private AosToast() {
		super(SmartToolApp.getSelf());
	}

	/**
	 * 提示吐司
	 * 
	 * @param text
	 * @param duration
	 * @return
	 * @see:
	 * @since:
	 * @author: cuiwei
	 * @date:2013-12-5
	 */
	public static Toast makeText(CharSequence text, int duration) {
		Toast result = new Toast(SmartToolApp.getSelf());

		LayoutInflater inflate = (LayoutInflater) SmartToolApp.getSelf().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.toast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast);
		tv.setText(text);

		result.setView(view);
		result.setDuration(duration);

		return result;
	}

	/**
	 * 提示吐司
	 * 
	 * @param resId
	 *            getResourcesid
	 * @param duration
	 * @return
	 * @see:
	 * @since:
	 * @author: cuiwei
	 * @date:2013-12-5
	 */
	public static Toast makeText(int resId, int duration) {
		Toast result = new Toast(SmartToolApp.getSelf());

		LayoutInflater inflate = (LayoutInflater) SmartToolApp.getSelf().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.toast, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast);
		tv.setText(SmartToolApp.getSelf().getResources().getText(resId));

		result.setView(view);
		result.setDuration(duration);

		return result;
	}

}
