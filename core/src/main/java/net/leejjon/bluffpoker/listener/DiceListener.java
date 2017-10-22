package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.leejjon.bluffpoker.actors.Dice;
import net.leejjon.bluffpoker.interfaces.GameStatusInterface;
import net.leejjon.bluffpoker.interfaces.UserInterface;

public class DiceListener extends ActorGestureListener {

    private final Dice dice;
    private final GameStatusInterface gameStatusInterface;
    private final UserInterface userInterface;

    public DiceListener(Dice dice, GameStatusInterface gameStatusInterface, UserInterface userInterface) {
        this.dice = dice;
        this.gameStatusInterface = gameStatusInterface;
        this.userInterface = userInterface;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (dice.isUnderCup()) {
            // You've tapped while the dice was under the cup, so you probably meant to swipe it down. We'll do that anyway for you.
            dice.pullAwayFromCup();
            if (gameStatusInterface.isAllowedToLock()) {
                dice.lock();
                userInterface.showLockMessage();
            }
        } else {
            if (gameStatusInterface.isAllowedToLock()) {
                // If it is a blind pass the dices outside of the cup will be locked by default. The hasToThrow boolean is false, but the user is allowed to throw, so it may unlock the dices and throw.
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
                if (gameStatusInterface.isAllowedToLock()) {
                    dice.lock();
                    userInterface.showLockMessage();
                }
            }
        }
    }
}
