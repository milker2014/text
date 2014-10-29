package com.lz.smarttool.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.lz.smarttool.R;
import com.lz.smarttool.utils.UtilLog;

public class CompassActivity2 extends Activity {

	private ImageView image_compass; // 指南针图片
	private float currentDegree = 0f; // 指南针图片转过的角度
	// 声明加速度传感器对象
	private SensorManager sm = null; // 获取SensorManager对象，通过它可以获得距离，加速度等传感器对象
	private Sensor accelerationSensor = null; // 加速度传感器
	private Sensor magneticSensor = null; // 磁力传感器
	// ******************加速度传感器初始化变量*********************************************//
	double gravity[] = new double[3];// 代表3个方向的重力加速度
	double xAcceleration = 0;// 代表3个方向的真正加速度
	double yAcceleration = 0;
	double zAcceleration = 0;
	double currentAcceleration = 0; // 当前的合加速度
	double maxAcceleration = 0; // 最大加速度
	// 接下来定义的数组是为了对加速度传感器采集的加速度加以计算和转化而定义的，转化的目的是为了使数据看上去更符合我们平时的习惯
	float[] magneticValues = new float[3];
	float[] accelerationValues = new float[3];
	float[] values = new float[3];
	float[] rotate = new float[9];
	// 初始化的三个方位角的值
	float Yaw = 0;
	float Pitch = 0; // values[1]
	float Roll = 0;

	final static int CMD_STOP = 0;
	final static int CMD_UPDATAE = 1;
	private static final String TAG = CompassActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		image_compass = (ImageView) findViewById(R.id.image_compass);
		/**
		 * 设置加速度传感器
		 */
		sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		accelerationSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(accelerationListener, accelerationSensor, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(magneticListener, accelerationSensor, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(magneticListener, magneticSensor, SensorManager.SENSOR_DELAY_UI);

	}

	// **************************加速度测试传感器部分*************************
	// 加速度传感器监听器，当获取的传感器数据发生精度要求范围内的变化时，监听器会调用onSensorChanged函数
	SensorEventListener accelerationListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			final double alpha = 0.8;
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			xAcceleration = event.values[0] - gravity[0];
			yAcceleration = event.values[1] - gravity[1];
			zAcceleration = event.values[2] - gravity[2];

			// 计算三个方向上的和加速度
			double G = Math.sqrt(Math.pow(xAcceleration, 2) + Math.pow(zAcceleration, 2) + Math.pow(yAcceleration, 2));
			currentAcceleration = G;
			if (currentAcceleration > maxAcceleration)
				maxAcceleration = currentAcceleration;

			Intent i = new Intent();
			i.setAction("com.dm.accReceiver");
			i.putExtra("xAcceleration", xAcceleration);
			i.putExtra("yAcceleration", yAcceleration);
			i.putExtra("zAcceleration", zAcceleration);
			i.putExtra("currentAcceleration", currentAcceleration);
			i.putExtra("maxAcceleration", maxAcceleration);
			sendBroadcast(i);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

	};

	// ************************方向传感器**************************
	// 手机方位传感器监听器，当获取的加速度或者磁力传感器数据发生精度要求范围内的变化时，监听器会调用onSensorChanged函数
	SensorEventListener magneticListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			// 如果是加速度传感器的值发生了变化
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelerationValues = event.values;
			}
			// 如果是磁力传感器的值发生了变化
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticValues = event.values;
			}
			boolean sen = SensorManager.getRotationMatrix(rotate, null, accelerationValues, magneticValues);
			SensorManager.getOrientation(rotate, values);
//			for (int i = 0; i < 3; i++) {
//				values[i] = (float) Math.toDegrees(values[i]);
//				UtilLog.i("方位检测", "values[" + i + "]=" + values[i]);
//			}
			UtilLog.i("getRotationMatrix", sen + "");
			UtilLog.i("rotate", rotate + "");
			Yaw = values[0];
			Pitch = values[1];
			Roll = values[2];
			float degree = (float)Math.toDegrees(values[0]); // 获取z转过的角度
			// 旋转动画
			RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(100);// 动画持续时间
			image_compass.startAnimation(ra);
			currentDegree = -degree;
			UtilLog.i(TAG, String.valueOf(Yaw) + "|" + String.valueOf(Pitch) + "|" + String.valueOf(Roll));
			UtilLog.i(TAG, "|" + degree + "|");
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	// 重写 onDestroy 方法
	public void onDestroy() {
		sm.unregisterListener(accelerationListener);
		currentAcceleration = 0;
		maxAcceleration = 0;
		xAcceleration = yAcceleration = zAcceleration = 0;

		// 注销监听器
		sm.unregisterListener(magneticListener);
		sm = null;

		super.onDestroy();
	}
}
