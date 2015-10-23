package net.leejjon.blufpoker.listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import net.leejjon.blufpoker.actors.Dice;
import net.leejjon.blufpoker.stages.GameInputInterface;

/**
 * Created by Leejjon on 23-10-2015.
 */
public class DiceListener extends ActorGestureListener {

    private Dice dice;

    public DiceListener(Dice dice) {
        this.dice = dice;
    }

    @Override
    public void tap (InputEvent event, float x, float y, int count, int button) {
        if (dice.isUnderCup()){
            dice.pullAwayFromCup();
        } else {
            dice.putBackUnderCup();
        }
    }
}
