package net.leejjon.bluffpoker.ios;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;

public class BluffPokerIOSApplication extends IOSApplication {
    private BluffPokerIOSInput input;

    public BluffPokerIOSApplication(ApplicationListener listener, IOSApplicationConfiguration config) {
        super(listener, config);
    }

//    @Override
//    public Input getInput () {
//        return input;
//    }

    public void overwriteInput(BluffPokerGame game) {
        input = new BluffPokerIOSInput(this, game);
        input.setupPeripherals();
    }
}
