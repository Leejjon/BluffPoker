package net.leejjon.bluffpoker.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.state.GameState;

public class DiceListener extends ActorGestureListener {

    private final DiceActor diceActor;
    private final UserInterface userInterface;

    public DiceListener(DiceActor diceActor, UserInterface userInterface) {
        this.diceActor = diceActor;
        this.userInterface = userInterface;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (diceActor.isUnderCup()) {
            // You've tapped while the diceActor was under the cup, so you probably meant to swipe it down. We'll do that anyway for you.
            diceActor.pullAwayFromCup();
            if (GameState.get().isAllowedToLock()) {
                diceActor.lock();
                userInterface.showLockMessage();
            }
        } else {
            if (GameState.get().isAllowedToLock()) {
                // If it is a blind pass the dices outside of the cup will be locked by default. The hasToThrow boolean is false, but the user is allowed to throw, so it may unlock the dices and throw.
                if (diceActor.isLocked()) {
                    diceActor.unlock();
                } else {
                    diceActor.lock();
                }
            }
        }
    }

    @Override
    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
        // See the code in the CupListener class for more explanation on swipe detection.
        if (!(Math.abs(velocityX) > Math.abs(velocityY))) {
            if (velocityY > 0) {
                // You've made a swipe gesture on the diceActor in the direction: Up
                diceActor.putBackUnderCup();
            } else {
                // You've made a swipe gesture on the diceActor in the direction: Down
                diceActor.pullAwayFromCup();
                if (GameState.get().isAllowedToLock()) {
                    diceActor.lock();
                    userInterface.showLockMessage();
                }
            }
        }
    }
}
