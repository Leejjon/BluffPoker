package net.leejjon.bluffpoker.android;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Point;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.Display;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import net.leejjon.bluffpoker.BluffPokerGame;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.leejjon.bluffpoker.android.keyboard.BluffPokerAndroidInput;
import net.leejjon.bluffpoker.interfaces.PlatformSpecificInterface;
import net.leejjon.bluffpoker.interfaces.ZoomFactorInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

public class AndroidLauncher extends AndroidApplication implements SensorEventListener, PlatformSpecificInterface {
    private BluffPokerGame game;

    private static final int READ_CONTACTS_FOR_PLAYER_NAME = 1;

    private ModifyPlayerListener playerModifier = null;
    private Set<String> alreadyExistingPlayers = new TreeSet<>();
    private AtomicLong lastUpdate;
    private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
    private volatile float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        System.out.println("Density: " + getResources().getDisplayMetrics().densityDpi);

        SensorManager sensorManager = getSystemService(SensorManager.class);
        Sensor acceleroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, acceleroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        game = new BluffPokerGame(this);

        initialize(game, config);
//        initialize(new MyGdxGame(), config);

        input = new BluffPokerAndroidInput(this, this, graphics.getView(), config);
    }

    @Override
    public int getZoomFactor() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // 2 Is a nice default isn't it?
        int zoomFactor = 2;
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomFactor = 1;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomFactor = 2;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                // If the screen is square, make it a smaller.
                // For BlackBerry Classic, Q10 and Q5
                zoomFactor = (size.x == size.y) ? 3 : 2;
                break;
            // Robert's Galaxy S6 Edge had DENSITY_XXHIGH with 2560x1440
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:
            // Michel's Moto X style had DENSITY_560. With 2560x1440.
            case DisplayMetrics.DENSITY_560:
                // If the screen is square, make it smaller.
                // For BlackBerry Passport.
                zoomFactor = (size.x == size.y) ? 5 : 3;
                break;
        }
        return zoomFactor;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

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
        Integer displayNameColumn = null;
        String deviceOwnerName = null;

        if (hasContactPermissions()) {
            try (Cursor c = getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)) {
                c.moveToFirst();
                displayNameColumn = c.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME);
                deviceOwnerName = c.getString(displayNameColumn);
            } catch (CursorIndexOutOfBoundsException e) {
                Gdx.app.log("bluffpoker", "Can't retrieve the display name of the device owner because your phone sucks. CursorIndexOutOfBoundsException for cursor index  " + displayNameColumn + " " + e.getMessage());
            }
        }

        return deviceOwnerName != null ? deviceOwnerName : "Player 1";
    }

    private boolean hasContactPermissions() {
        return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void initiateSelectContacts(ModifyPlayerListener playerModifier, Set<String> alreadyExistingPlayers) {
        this.alreadyExistingPlayers = alreadyExistingPlayers;
        if (this.playerModifier == null) {
            this.playerModifier = playerModifier;
        }

        if (hasContactPermissions()) {
            startSelectingContacts();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_FOR_PLAYER_NAME);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        startSelectingContacts();
    }

    public void startSelectingContacts() {
        if (playerModifier != null) {
            Set<String> players = new TreeSet<>();

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            try (Cursor contacts = getApplication().getContentResolver().query(uri, projection, null, null, null)) {
                final int indexName = contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                if (contacts.moveToFirst()) {
                    do {
                        String name = contacts.getString(indexName);
                        if (!players.contains(name) && !alreadyExistingPlayers.contains(name)) {
                            players.add(name);
                        }
                    } while (contacts.moveToNext());
                }
            }

            playerModifier.showPhonebookDialog();
            playerModifier.loadFromPhonebook(players.toArray(new String[players.size()]));
        }
    }
}
