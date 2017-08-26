package net.leejjon.bluffpoker.ios;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;

public class BluffPokerIOSApplication extends IOSApplication {
    private BluffPokerIOSInput input;
    private IOSApplicationConfiguration config;

    public BluffPokerIOSApplication(ApplicationListener listener, IOSApplicationConfiguration config) {
        super(listener, config);
        this.config = config;
    }

    public void overwriteInput(BluffPokerGame game) {
        input = new BluffPokerIOSInput(this, config, game);
        input.setupAccelerometer();
    }
}
