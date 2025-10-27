package net.leejjon.bluffpoker.android

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import net.leejjon.bluffpoker.BluffPokerApp
import net.leejjon.bluffpoker.android.keyboard.BluffPokerAndroidInput
import net.leejjon.bluffpoker.interfaces.PlatformSpecificInterface
import net.leejjon.bluffpoker.listener.ModifyPlayerListener
import java.util.TreeSet
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.abs

/** Launches the Android application. */
class AndroidLauncher : AndroidApplication(), SensorEventListener, PlatformSpecificInterface {
    lateinit var game: BluffPokerApp

    lateinit var lastUpdate: AtomicLong

    val numberOfTimesShaked: AtomicInteger = AtomicInteger(0)

    lateinit var playerModifier: ModifyPlayerListener
    private var alreadyExistingPlayers = emptySet<String>()

    var last_x = 0.0f
    var last_y = 0.0f
    var last_z = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = AndroidApplicationConfiguration().apply { useImmersiveMode = true }

        println("Density: ${resources.displayMetrics.densityDpi}")

        val sensorManager = getSystemService(SensorManager::class.java)
        val acceleroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, acceleroSensor, SensorManager.SENSOR_DELAY_NORMAL)

        game = BluffPokerApp(this)

        initialize(game, config)

        input = BluffPokerAndroidInput(this, this, graphics.view, config)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        val SHAKE_TRESHOLD = 1600

        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()

            if (!this::lastUpdate.isInitialized) {
                lastUpdate = AtomicLong(currentTime)
            }

            if ((currentTime - (lastUpdate.get())) > 100) {
                val diffTime = (currentTime - lastUpdate.get())
                lastUpdate.set(currentTime)

                val (x, y, z) = event.values

                val xyz = x + y + z
                val xyzMinusLastXYZ = xyz - last_x - last_y -last_z
                val speed = abs(xyzMinusLastXYZ) / diffTime * 10000

                if (speed > SHAKE_TRESHOLD) {
                    if (numberOfTimesShaked.incrementAndGet() == 3) {
                        Gdx.app.postRunnable {
                            game.shakePhone()
                        }

                        numberOfTimesShaked.set(0)
                        return
                    }
                }

                last_x = x
                last_y = y
                last_z = z
            }
        }
    }

    override fun getDeviceOwnerName(): String? {
        return if (hasContactPermissions()) {
            application.contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null).use {
                return if (it != null) {
                    it.moveToFirst()
                    val displayNameColumn = it.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME)
                    val deviceOwnerName = it.getString(displayNameColumn)
                    deviceOwnerName ?: "Player 1"
                } else {
                    "Player 1"
                }
            }
        } else {
            "Player 1"
        }
    }

    private fun hasContactPermissions(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    override fun initiateSelectContacts(
        playerModifier: ModifyPlayerListener?,
        alreadyExistingPlayers: Set<String>?
    ) {
        if (alreadyExistingPlayers != null) {
            this.alreadyExistingPlayers = alreadyExistingPlayers
        }

        if (!this::playerModifier.isInitialized && playerModifier != null) {
            this.playerModifier = playerModifier
        }

        if (hasContactPermissions()) {
            startSelectingContacts()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), READ_CONTACTS_FOR_PLAYER_NAME)
        }
    }

    override fun getZoomFactor(): Int {
        val display = this.context.display
        val size = Point()

        display.getSize(size)

        val defaultZoomFactor = 2
        val biggerScreenZoomFactor = if (size.x == size.y) 5 else 3;

        return when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_MEDIUM -> 1
            DisplayMetrics.DENSITY_HIGH -> defaultZoomFactor
            DisplayMetrics.DENSITY_XHIGH -> if (size.x == size.y) 3 else 2
            DisplayMetrics.DENSITY_XXHIGH -> biggerScreenZoomFactor
            DisplayMetrics.DENSITY_420 -> biggerScreenZoomFactor
            DisplayMetrics.DENSITY_440 -> biggerScreenZoomFactor
            DisplayMetrics.DENSITY_XXXHIGH -> biggerScreenZoomFactor
            DisplayMetrics.DENSITY_560 -> biggerScreenZoomFactor
            else -> {
                defaultZoomFactor
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Gdx.app.postRunnable {
            startSelectingContacts()
        }
    }

    private fun startSelectingContacts() {
        if (!this::playerModifier.isInitialized) {
            val players = TreeSet<String>()

            val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

            application.contentResolver.query(uri, projection, null, null, null).use {
                if (it != null) {
                    val indexName = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    if (it.moveToFirst()) {
                        do {
                            val name = it.getString(indexName)
                            if (!players.contains(name) && !alreadyExistingPlayers.contains(name)) {
                                players.add(name)
                            }
                        } while (it.moveToNext())
                    }
                }
            }

            playerModifier.showPhonebookDialog()
            playerModifier.loadFromPhonebook(*players.toTypedArray())
        }
    }

    companion object {
        private val READ_CONTACTS_FOR_PLAYER_NAME: Int = 1
    }
}