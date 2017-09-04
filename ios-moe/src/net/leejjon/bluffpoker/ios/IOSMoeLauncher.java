package net.leejjon.bluffpoker.ios;

import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;

import java.util.Set;

public class IOSMoeLauncher extends BluffPokerIOSApplication.Delegate implements ContactsRequesterInterface {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    public BluffPokerGame bluffPokerGame;

    @Override
    protected BluffPokerIOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = true; // We put it on false so libgdx thinks it's not used. We actually do use it in the BluffPokerIOSApplication.java.

        bluffPokerGame = new BluffPokerGame(this,2);
//        MyGdxGame bluffPokerGame = new MyGdxGame();

        return new BluffPokerIOSApplication(bluffPokerGame, config);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public String getDeviceOwnerName() {
        return "Player 1";
    }

    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {

    }
}
