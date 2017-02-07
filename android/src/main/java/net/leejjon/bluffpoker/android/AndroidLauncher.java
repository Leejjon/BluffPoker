package net.leejjon.bluffpoker.android;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import net.leejjon.bluffpoker.BluffPokerGame;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.leejjon.bluffpoker.android.keyboard.NumberCombinationInput;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;


public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameFragment gameFragment = new GameFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, gameFragment);
        trans.commit();
    }

    @Override
    public void exit() {

    }

    public static class GameFragment extends AndroidFragmentApplication implements SensorEventListener, ContactsRequesterInterface {
        private BluffPokerGame game;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

            System.out.println("Density: " + getResources().getDisplayMetrics().densityDpi);

            SensorManager sensorManager = getActivity().getSystemService(SensorManager.class);
            Sensor acceleroSensor = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            sensorManager.registerListener(this, acceleroSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

            /*
             * We are not going to ask the user for phonebook permissions on the first run just to load his own name,
             * as that might scare users off. If during the selection of players clicks the phonebook button,
             * we will request for contact access. The next time the app is run, we have access and will preload
             * the device owners name.
             */
            game = new BluffPokerGame(this, getZoomFactor());

            View view = initializeForView(game, config);

            input = new NumberCombinationInput(this, getActivity(), graphics.getView(), config);

            return view;
        }

        private int getZoomFactor() {
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
            return zoomfactor;
        }

        private static final int READ_CONTACTS_FOR_PLAYER_NAME = 1;

        private AtomicLong lastUpdate;
        private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
        private volatile float last_x, last_y, last_z;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

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
        public String getDeviceOwnerName() {
            String deviceOwnerName = null;
            if (hasContactPermissions()) {
                try (Cursor c = getActivity().getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)) {
                    c.moveToFirst();
                    deviceOwnerName = c.getString(c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
                }
            }

            return deviceOwnerName != null ? deviceOwnerName : "Player 1";
        }

        @Override
        public boolean hasContactPermissions() {
            return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public void requestContactPermission() {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_FOR_PLAYER_NAME);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Some permission granted.");
    }
}
