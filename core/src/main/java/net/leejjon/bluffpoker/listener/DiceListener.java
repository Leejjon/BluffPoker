package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.state.Dice;
import net.leejjon.bluffpoker.state.GameState;

public class DiceListener extends ActorGestureListener {

    private final Dice dice;
    private final UserInterface userInterface;

    public DiceListener(Dice dice, UserInterface userInterface) {
        this.dice = dice;
        this.userInterface = userInterface;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (dice.isUnderCup()) {
            // You've tapped while the dice was under the cup, so you probably meant to swipe it down. We'll do that anyway for you.
            dice.pullAwayFromCup();
            if (GameState.state().isAllowedToLock()) {
                dice.lock();
                userInterface.showLockMessage();
            }
        } else {
            if (GameState.state().isAllowedToLock()) {
                // If it is a blind pass the dices outside of the cup will be locked by default. The hasToThrow boolean is false, but the user is allowed to throw, so it may unlockWithSave the dices and throw.
                if (dice.isLocked()) {
                    dice.unlockWithSave();
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
                if (GameState.state().isAllowedToLock()) {
                    dice.lock();
                    userInterface.showLockMessage();
                }
            }
        }
    }
}
