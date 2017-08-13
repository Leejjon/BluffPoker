package com.mygdx.game;

import apple.coremotion.CMAccelerometerData;
import apple.coremotion.CMMotionManager;
import apple.foundation.NSError;
import apple.foundation.NSOperationQueue;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import org.moe.natj.general.Pointer;
import net.leejjon.bluffpoker.MyGdxGame;

import apple.uikit.c.UIKit;

public class IOSMoeLauncher extends IOSApplication.Delegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false;
        return new IOSApplication(new MyGdxGame(), config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());

        CMMotionManager cmMotionManager = CMMotionManager.alloc();

        CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler movementHandler = new CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler() {
            @Override
            public void call_startAccelerometerUpdatesToQueueWithHandler(CMAccelerometerData data, NSError arg1) {
                double xAcceleration = data.acceleration().x();
                double yAcceleration = data.acceleration().y();
                double zAcceleration = data.acceleration().z();

                // TODO: Calculate whether it was a shake since last time.
            }
        };

        NSOperationQueue nsOperationQueue = NSOperationQueue.mainQueue();

        cmMotionManager.startAccelerometerUpdatesToQueueWithHandler(nsOperationQueue, movementHandler);

        // TODO: On tear down kill the handler.
    }
}
