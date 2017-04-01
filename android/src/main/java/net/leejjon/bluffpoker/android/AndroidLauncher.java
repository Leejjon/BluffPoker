package net.leejjon.bluffpoker.android;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Point;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;
import net.leejjon.bluffpoker.BluffPokerGame;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.leejjon.bluffpoker.android.keyboard.BluffPokerInput;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;


public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks {
    private GameFragment gameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameFragment = new GameFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, gameFragment);
        trans.commit();
    }

    @Override
    public void exit() {
        gameFragment.exit();
    }

    public static class GameFragment extends AndroidFragmentApplication implements SensorEventListener, ContactsRequesterInterface {
        private BluffPokerGame game;

        private static final int READ_CONTACTS_FOR_PLAYER_NAME = 1;
        private static final int SELECT_CONTACTS = 2;


        private ModifyPlayerListener playerModifier = null;
        private AtomicLong lastUpdate;
        private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
        private volatile float last_x, last_y, last_z;

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

            input = new BluffPokerInput(this, getActivity(), graphics.getView(), config);

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
                    zoomfactor = 7;
                    break;
            }
            return zoomfactor;
        }

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
            Integer displayNameColumn = null;
            String deviceOwnerName = null;

            if (hasContactPermissions()) {
                try (Cursor c = getActivity().getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)) {
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
            return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public Collection<String> getAllPhonebookContacts() {
            return null;
        }

        @Override
        public void initiateSelectContacts(ModifyPlayerListener playerModifier) {
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

        public void startSelectingContacts() {
            Intent intent = new Intent(getActivity(), ContactPickerActivity.class)
                    .putExtra(ContactPickerActivity.EXTRA_THEME, R.style.ContactPicker_Theme_Dark)
                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                    .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
            startActivityForResult(intent, SELECT_CONTACTS);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (playerModifier != null) {
                if (requestCode == SELECT_CONTACTS && resultCode == Activity.RESULT_OK &&
                        data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {
                    java.util.List<Contact> contacts = (java.util.List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);

                    Set<String> players = new TreeSet<>();
                    for (Contact contact : contacts) {
                        // TODO: Figure out if we can trust the first/last name.
                        players.add(contact.getDisplayName());
                    }
                    playerModifier.addNewPlayer(players.toArray(new String[players.size()]));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        gameFragment.startSelectingContacts();
    }
}
