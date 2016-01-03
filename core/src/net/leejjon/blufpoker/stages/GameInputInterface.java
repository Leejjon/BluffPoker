package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leejjon on 11-10-2015.
 */
public interface GameInputInterface {
    void tapCup();

    void swipeCupUp();

    /**
     * @return If long tapping isn't allowed in this game phase, we return false so it doesn't blocks other events like
     * swipe or tapping. If it is allowed, we return true to avoid any swipes/taps being activated while attempting a
     * long tap.
     */
    boolean longTapOnCup();
}
