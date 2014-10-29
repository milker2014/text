package com.lz.smarttool.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.lz.smarttool.utils.UtilLog;

public class CompassSensorService extends Service {

	// �������ٶȴ���������
	private SensorManager sm = null; // ��ȡSensorManager����ͨ�������Ի�þ��룬���ٶȵȴ���������
	private Sensor accelerationSensor = null; // ���ٶȴ�����
	private Sensor magneticSensor = null; // ����������
	// ******************���ٶȴ�������ʼ������*********************************************//
	double gravity[] = new double[3];// ����3��������������ٶ�
	double xAcceleration = 0;// ����3��������������ٶ�
	double yAcceleration = 0;
	double zAcceleration = 0;
	double currentAcceleration = 0; // ��ǰ�ĺϼ��ٶ�
	double maxAcceleration = 0; // �����ٶ�
	// �����������������Ϊ�˶Լ��ٶȴ������ɼ��ļ��ٶȼ��Լ����ת��������ģ�ת����Ŀ����Ϊ��ʹ���ݿ���ȥ����������ƽʱ��ϰ��
	float[] magneticValues = new float[3];
	float[] accelerationValues = new float[3];
	float[] values = new float[3];
	float[] rotate = new float[9];
	// ��ʼ����������λ�ǵ�ֵ
	float Yaw = 0;
	float Pitch = 0; // values[1]
	float Roll = 0;

	final static int CMD_STOP = 0;
	final static int CMD_UPDATAE = 1;

	public void onCreate() {
		super.onCreate();

		/**
		 * ���ü��ٶȴ�����
		 */
		sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);
		accelerationSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magneticSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		sm.registerListener(accelerationListener, accelerationSensor, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(magneticListener, accelerationSensor, SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(magneticListener, magneticSensor, SensorManager.SENSOR_DELAY_UI);

	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i("-----------SensorService---------------", "��������");
	}

	// ��д onDestroy ����
	public void onDestroy() {
		sm.unregisterListener(accelerationListener);
		currentAcceleration = 0;
		maxAcceleration = 0;
		xAcceleration = yAcceleration = zAcceleration = 0;

		// ע��������
		sm.unregisterListener(magneticListener);
		sm = null;

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// **************************���ٶȲ��Դ���������*************************
	// ���ٶȴ�����������������ȡ�Ĵ��������ݷ�������Ҫ��Χ�ڵı仯ʱ�������������onSensorChanged����
	SensorEventListener accelerationListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			final double alpha = 0.8;
			gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
			gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

			xAcceleration = event.values[0] - gravity[0];
			yAcceleration = event.values[1] - gravity[1];
			zAcceleration = event.values[2] - gravity[2];

			// �������������ϵĺͼ��ٶ�
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

	// ************************���򴫸���**************************
	// �ֻ���λ������������������ȡ�ļ��ٶȻ��ߴ������������ݷ�������Ҫ��Χ�ڵı仯ʱ�������������onSensorChanged����
	SensorEventListener magneticListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent event) {
			// ����Ǽ��ٶȴ�������ֵ�����˱仯
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelerationValues = event.values;
			}
			// ����Ǵ�����������ֵ�����˱仯
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticValues = event.values;
			}
			SensorManager.getRotationMatrix(rotate, null, accelerationValues, magneticValues);
			SensorManager.getOrientation(rotate, values);
			for (int i = 0; i < 3; i++) {
				values[i] = (float) Math.toDegrees(values[i]);
				UtilLog.i("��λ���", "values[" + i + "]=" + values[i]);
			}
			Yaw = values[0];
			Pitch = values[1];
			Roll = values[2];

			Intent i = new Intent();
			i.setAction("com.dm.magReceiver");
			i.putExtra("Yaw", Yaw);
			i.putExtra("Pitch", Pitch);
			i.putExtra("Roll", Roll);
			sendBroadcast(i);
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}
