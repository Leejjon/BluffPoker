package net.leejjon.blufpoker.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.leejjon.blufpoker.actors.Dice;
import net.leejjon.blufpoker.interfaces.Throwable;
import net.leejjon.blufpoker.stages.GameInputInterface;

/**
 * Created by Leejjon on 23-10-2015.
 */
public class DiceListener extends ActorGestureListener {

    private Dice dice;
    private Throwable throwable;

    public DiceListener(Dice dice, Throwable throwable) {
        this.dice = dice;
        this.throwable = throwable;
    }

    @Override
    public void tap (InputEvent event, float x, float y, int count, int button) {
        if (dice.isUnderCup()) {
            // You've tapped while the dice was under the cup, so you probably meant to swipe it down. We'll do that anyway for you.
            dice.pullAwayFromCup();
            if (throwable.stillHasToThrow()) {
                dice.lock();
            }
        } else {
            if (throwable.stillHasToThrow()) {
                if (dice.isLocked()) {
                    dice.unlock();
                } else {
                    dice.lock();
                }
            }
        }
    }

    @Override
    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
        // See the code in the CupListener class for more explanation on swipe detection.
        if (!(Math.abs(velocityX) > Math.abs(velocityY))) {
            if (velocityY > 0) {
                // You've made a swipe gesture on the dice in the direction: Up
                dice.putBackUnderCup();
            } else {
                // You've made a swipe gesture on the dice in the direction: Down
                dice.pullAwayFromCup();
                if (throwable.stillHasToThrow()) {
                    dice.lock();
                }
            }
        }
    }
}
