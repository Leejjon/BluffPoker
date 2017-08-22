package net.leejjon.bluffpoker.ios;

import apple.coremotion.CMAccelerometerData;
import apple.coremotion.CMMotionManager;
import apple.foundation.NSDictionary;
import apple.foundation.NSError;
import apple.foundation.NSOperationQueue;
import apple.uikit.UIApplication;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.MyGdxGame;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;

import java.util.Set;

public class IOSMoeLauncher extends IOSApplication.Delegate implements ContactsRequesterInterface {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    private BluffPokerIOSApplication bluffPokerIOSApplication;
    public BluffPokerGame bluffPokerGame;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = false; // We put it on false so libgdx thinks it's not used. We actually do use it in the BluffPokerIOSApplication.java.

        bluffPokerGame = new BluffPokerGame(this,2);
//        MyGdxGame game = new MyGdxGame();
        bluffPokerIOSApplication = new BluffPokerIOSApplication(bluffPokerGame, config);
        return bluffPokerIOSApplication;
    }

    @Override
    public boolean applicationDidFinishLaunchingWithOptions (UIApplication application, NSDictionary<?, ?> launchOptions) {
        boolean launch = super.applicationDidFinishLaunchingWithOptions(application, launchOptions);
        bluffPokerIOSApplication.overwriteInput(bluffPokerGame);
        return launch;
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
