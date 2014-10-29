package com.lz.smarttool.activity;

import com.lz.smarttool.R;
import com.lz.smarttool.service.CompassSensorService;
import com.lz.smarttool.utils.UtilLog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * @Description:指南针
 * @author:LiuZhao
 * @see:
 * @since:
 * @copyright ©lz
 * @Date:2014年7月8日
 */
public class CompassActivity extends Activity {

	private ImageView image_compass; // 指南针图片
	private float currentDegree = 0f; // 指南针图片转过的角度
	private SensorManager sensorManager; // 传感器管理
	// 需要两个Sensor
	private Sensor aSensor;
	private Sensor mSensor;

	// ******************加速度传感器初始化变量*********************************************//
	double gravity[] = new double[3];// 代表3个方向的重力加速度
	double xAcceleration = 0;// 代表3个方向的真正加速度
	double yAcceleration = 0;
	double zAcceleration = 0;
	double currentAcceleration = 0; // 当前的合加速度
	double maxAcceleration = 0; // 最大加速度
	// 接下来定义的数组是为了对加速度传感器采集的加速度加以计算和转化而定义的，转化的目的是为了使数据看上去更符合我们平时的习惯
	/** 磁场传感器 */
	float[] magneticValues = new float[3];
	/** 加速传感器 */
	float[] accelerationValues = new float[3];
	float[] values = new float[3];
	float[] rotate = new float[9];
	// 初始化的三个方位角的值
	float Yaw = 0;
	float Pitch = 0; // values[1]
	float Roll = 0;
	private static final String TAG = CompassActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		image_compass = (ImageView) findViewById(R.id.image_compass);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // 获取管理服务
		aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// Activity中核心代码(在onCreate()方法中创建BroadcastReceiver实例)
		BroadcastReceiver accReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();// 获得 Bundle
				xAcceleration = bundle.getDouble("xAcceleration", 0);
				yAcceleration = bundle.getDouble("yAcceleration", 0);
				zAcceleration = bundle.getDouble("zAcceleration", 0);
				currentAcceleration = bundle.getDouble("currentAcceleration", 0);
				maxAcceleration = bundle.getDouble("maxAcceleration", 0);

				Log.i("-----------accReceiver---------------", String.valueOf(xAcceleration) + "|" + String.valueOf(xAcceleration) + "|" + String.valueOf(yAcceleration) + "|" + String.valueOf(zAcceleration) + "|" + String.valueOf(currentAcceleration) + "|" + String.valueOf(maxAcceleration));
			}
		};

		IntentFilter accFilter = new IntentFilter();
		accFilter.addAction("com.dm.accReceiver");
		registerReceiver(accReceiver, accFilter);

		// 新建并注册广播接收器，用于接收传感器类传递的数据
		BroadcastReceiver magReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();// 获得 Bundle
				Yaw = bundle.getFloat("Yaw", 0);
				Pitch = bundle.getFloat("Pitch", 0);
				Roll = bundle.getFloat("Roll", 0);
				values[0] = (float) Math.toDegrees(Yaw);
				float degree = values[0]; // 获取z转过的角度
				// 旋转动画
				RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				ra.setDuration(100);// 动画持续时间
				image_compass.startAnimation(ra);
				currentDegree = -degree;
				UtilLog.i(TAG, String.valueOf(Yaw) + "|" + String.valueOf(Pitch) + "|" + String.valueOf(Roll));
				UtilLog.i(TAG, "|" + values[0] + "|");
			}
		};

		IntentFilter magFilter = new IntentFilter();
		magFilter.addAction("com.dm.magReceiver");
		registerReceiver(magReceiver, magFilter);

		// 启动注册了传感器监听的 Service
		Intent i = new Intent(this, CompassSensorService.class);
		startService(i);
	}

}
