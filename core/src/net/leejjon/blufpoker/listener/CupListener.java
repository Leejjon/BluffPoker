package net.leejjon.blufpoker.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.leejjon.blufpoker.actions.LiftCupAction;
import net.leejjon.blufpoker.stages.GameInputInterface;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class CupListener extends ActorGestureListener {
    private GameInputInterface game;

    public CupListener(GameInputInterface game) {
        this.game = game;
    }

    @Override
    public void tap (InputEvent event, float x, float y, int count, int button) {
        game.tapCup();
    }

    /**
     * I had/have no clue what a fling is, but I guess it's some sort of swipe move.
     * @param event Contains event information, for example what actor this event occurred on.
     * @param velocityX I assume this is the speed of the swipe in horizontal direction.
     * @param velocityY I assume this is the speed of the swipe in vertical direction.
     * @param button Not relevant, it is always a touch event as this game is for phones/tablets only!
     */
    @Override
    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
        // I could simplify this code and only detect swipe gestures going up, but I'm just going to
        // keep it here because it's useful to know how to detect all four directions.
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                System.out.println("You've made a swipe gesture on the cup in the direction: Right");
            } else {
                System.out.println("You've made a swipe gesture on the cup in the direction: Left");
            }
        } else {
            if (velocityY > 0) {
                System.out.println("You've made a swipe gesture on the cup in the direction: Up");

                game.swipeCupUp();
            } else {
                System.out.println("You've made a swipe gesture on the cup in the direction: Down");
            }
        }
    }
}
