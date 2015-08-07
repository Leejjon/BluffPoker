package net.leejjon.blufpoker.android;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import net.leejjon.blufpoker.BlufPokerGame;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication implements SensorEventListener {
	private BlufPokerGame game;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		int zoomfactor = 0;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_MEDIUM: 
			zoomfactor = 1;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomfactor = 2;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			zoomfactor = 2;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			zoomfactor = 4;
			break;
		case DisplayMetrics.DENSITY_TV:
			zoomfactor = 8;
		}
		
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor acceloMeterSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorManager.registerListener(this, acceloMeterSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		game = new BlufPokerGame(zoomfactor);
		initialize(game, config);
	}
	
	private AtomicLong lastUpdate;
	private AtomicLong timestampOffirstShake;
	private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
	private volatile float last_x, last_y, last_z;
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		final int SHAKE_THRESHOLD = 1600;

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long currentTime = System.currentTimeMillis();

			if (lastUpdate == null) {
				lastUpdate = new AtomicLong(currentTime);
			}
			
			if ((currentTime - lastUpdate.get()) > 100) {
				long diffTime = (currentTime - lastUpdate.get());
				lastUpdate.set(currentTime);

				float x, y, z;
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];

				float xyz = x + y + z;
				float xyzMinusLastXYZ = xyz - last_x - last_y - last_z;
				float speed = Math.abs(xyzMinusLastXYZ) / diffTime * 10000;

				if (speed > SHAKE_THRESHOLD) {
					
					System.out.println("shake detected w/ speed: " + speed
							+ ", x: " + x + ", y=" + y + ", z=" + z);
					
					
					if (numberOfTimesShaked.incrementAndGet() == 3) {
						game.shakePhone();
						
						numberOfTimesShaked.set(0);
						return;
					}
				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
