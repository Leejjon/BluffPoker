package net.leejjon.bluffpoker.ios;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IOSMoeLauncher extends IOSApplication.Delegate implements ContactsRequesterInterface {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    private AtomicLong lastUpdate;
    private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
    private volatile double last_x, last_y, last_z;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false;
        BluffPokerGame bluffPokerGame = new BluffPokerGame(this, 2);
//
//        CMMotionManager cmMotionManager = CMMotionManager.alloc();
//
//        CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler movementHandler = new CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler() {
//            @Override
//            public void call_startAccelerometerUpdatesToQueueWithHandler(CMAccelerometerData data, NSError arg1) {
//                final int SHAKE_THRESHOLD = 1600;
//
//                long currentTime = System.currentTimeMillis();
//
//                if (lastUpdate == null) {
//                    lastUpdate = new AtomicLong(currentTime);
//                }
//
//                if ((currentTime - lastUpdate.get()) > 100) {
//                    long diffTime = (currentTime - lastUpdate.get());
//                    lastUpdate.set(currentTime);
//
//                    double x, y, z;
//                    x = data.acceleration().x();
//                    y = data.acceleration().y();
//                    z = data.acceleration().z();
//
//                    double xyz = x + y + z;
//                    double xyzMinusLastXYZ = xyz - last_x - last_y - last_z;
//                    double speed = Math.abs(xyzMinusLastXYZ) / diffTime * 10000;
//
//                    if (speed > SHAKE_THRESHOLD) {
//                        if (numberOfTimesShaked.incrementAndGet() == 3) {
//                            bluffPokerGame.shakePhone();
//
//                            numberOfTimesShaked.set(0);
//                            return;
//                        }
//                    }
//                    last_x = x;
//                    last_y = y;
//                    last_z = z;
//                    // TODO: Calculate whether it was a shake since last time.
//                }
//            }
//        };
//
//        NSOperationQueue nsOperationQueue = NSOperationQueue.mainQueue();
//
//        cmMotionManager.startAccelerometerUpdatesToQueueWithHandler(nsOperationQueue, movementHandler);

        return new IOSApplication(bluffPokerGame, config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public String getDeviceOwnerName() {
        return "Stofkat";
    }

    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {

    }
}
