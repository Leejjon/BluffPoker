package net.leejjon.bluffpoker.ios;

import apple.coremotion.CMAccelerometerData;
import apple.coremotion.CMMotionManager;
import apple.foundation.NSError;
import apple.foundation.NSOperationQueue;
import apple.uikit.UIDevice;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSInput;
import net.leejjon.bluffpoker.BluffPokerGame;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * For detecting shakes (the only use of the accelero meter within the bluffpoker app) the IOSInput class in libgdx is not specific enough.
 *
 * So in this class we extend and overwrite methods that access it to modify it into what we need.
 */
public class BluffPokerIOSInput extends IOSInput {
    private CMMotionManager motionManager;
    private AtomicLong lastUpdate;
    private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
    private volatile double last_x, last_y, last_z;
    private BluffPokerGame game;

    public BluffPokerIOSInput(IOSApplication app, BluffPokerGame game) {
        super(app);
        this.game = game;
    }

    void setupPeripherals () {
        motionManager = CMMotionManager.alloc().init();
        setupAccelerometer();
    }

    private void setupAccelerometer () {
        motionManager.setAccelerometerUpdateInterval(0.01d);
        CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler handler = new CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler() {
            @Override
            public void call_startAccelerometerUpdatesToQueueWithHandler (CMAccelerometerData data, NSError nsError) {
                final int SHAKE_THRESHOLD = 500;

                long currentTime = System.currentTimeMillis();

                if (lastUpdate == null) {
                    lastUpdate = new AtomicLong(currentTime);
                }

                if ((currentTime - lastUpdate.get()) > 100) {
                    long diffTime = (currentTime - lastUpdate.get());
                    lastUpdate.set(currentTime);

                    double x, y, z;
                    x = data.acceleration().x();
                    y = data.acceleration().y();
                    z = data.acceleration().z();

                    double xyz = x + y + z;
                    double xyzMinusLastXYZ = xyz - last_x - last_y - last_z;
                    double speed = Math.abs(xyzMinusLastXYZ) / diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        if (numberOfTimesShaked.incrementAndGet() == 3) {
                            Gdx.app.log("bluffpoker", "shake detected: ");
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
        };
        motionManager.startAccelerometerUpdatesToQueueWithHandler(NSOperationQueue.alloc().init(), handler);
    }

}
