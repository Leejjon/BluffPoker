package net.leejjon.bluffpoker.android;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import android.database.Cursor;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.view.Display;
import com.badlogic.gdx.Gdx;
import net.leejjon.bluffpoker.BluffPokerGame;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.leejjon.bluffpoker.android.keyboard.NumberCombinationInput;

public class AndroidLauncher extends AndroidApplication implements SensorEventListener {
	private BluffPokerGame game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);



		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

		// 2 Is a nice default isn't it?
		int zoomfactor = 2;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomfactor = 1;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomfactor = 2;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
            // If the screen is square, make it a smaller.
            // For BlackBerry Classic, Q10 and Q5
            zoomfactor = (size.x == size.y) ? 3 : 2;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
            // If the screen is square, make it smaller.
            // For BlackBerry Passport.
            zoomfactor = (size.x == size.y) ? 5 : 4;
			break;
		// Robert's Samsung Galaxy S6 Edge had DENSITY_XXXHIGH and Michel's Moto X style had DENSITY_560. Both had 2560x1440 resolutions.
		case DisplayMetrics.DENSITY_XXXHIGH:
		case DisplayMetrics.DENSITY_560:
			zoomfactor = 6;
			break;
		}

		System.out.println("Zoomfactor: " + getResources().getDisplayMetrics().densityDpi);

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor acceloMeterSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		sensorManager.registerListener(this, acceloMeterSensor,
				SensorManager.SENSOR_DELAY_NORMAL);


		game = new BluffPokerGame(zoomfactor, getDeviceOwnerName());
		initialize(game, config);
		input = new NumberCombinationInput(this, this, graphics.getView(),  config);
	}

	private String getDeviceOwnerName() {

		try (Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)) {
			c.moveToFirst();
			return c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
		} catch (SecurityException e) {
            System.err.print(e.getMessage());
            return "Defaultname";
        }
	}
	
	private AtomicLong lastUpdate;
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
					
//					System.out.println("shake detected w/ speed: " + speed
//							+ ", x: " + x + ", y=" + y + ", z=" + z);
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
