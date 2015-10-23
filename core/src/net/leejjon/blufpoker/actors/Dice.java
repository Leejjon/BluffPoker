package net.leejjon.blufpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.blufpoker.BlufPokerGame;
import net.leejjon.blufpoker.DiceLocation;
import net.leejjon.blufpoker.stages.GameStage;

import java.util.Random;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Dice extends Image {
    private Cup cup;
    private Texture[] diceTextures;
    private int diceValue;
    private DiceLocation location;
    private boolean underCup = true;
    private Group dicesBeforeCupActors, dicesUnderCupActors;

    public Dice(Cup cup, Texture[] diceTextures, int initialValue, DiceLocation location,  Group dicesBeforeCupActors, Group dicesUnderCupActors) {
        super(diceTextures[initialValue-1]);
        this.cup = cup;
        this.diceTextures = diceTextures;
        diceValue = initialValue;
        this.location = location;
        this.dicesBeforeCupActors = dicesBeforeCupActors;
        this.dicesUnderCupActors = dicesUnderCupActors;

        dicesUnderCupActors.addActor(this);

        setWidth(getDiceWidth() / BlufPokerGame.getDivideScreenByThis());
        setHeight(getDiceHeight() / BlufPokerGame.getDivideScreenByThis());
    }

    public void throwDice() {
        if (underCup) {
            Random randomDiceNumber = new Random();
            int randomNumber = randomDiceNumber.nextInt(6);
            setDrawable(new SpriteDrawable(new Sprite(diceTextures[randomNumber])));
            diceValue = randomNumber++;
        }
    }

    public void calculateAndSetPosition() {
        float x = ((GameStage.getMiddleX() - (getDiceWidth() / 2)) / 2);

        switch (location) {
            case LEFT:
                // This is the left dice, so we place it slightly left of the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                x = x - (getDiceWidth() / BlufPokerGame.getDivideScreenByThis());
                break;
            case MIDDLE:
                // This is the middle dice, so we place it in the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                break;
            case RIGHT:
                x = x + (getDiceWidth() / BlufPokerGame.getDivideScreenByThis());
                break;
        }

        float y = cup.getMiddleHeightForCup() + (getDiceHeight() / (3 + BlufPokerGame.getDivideScreenByThis()));
        setPosition(x, y);
    }

    private int getDiceWidth() {
        return diceTextures[0].getWidth() / 2;
    }

    private int getDiceHeight() {
        return diceTextures[0].getHeight() / 2;
    }

    public boolean isUnderCup() {
        return underCup;
    }

    public void pullAwayFromCup() {
        if (cup.isBelieving() || cup.isWatchingOwnThrow()) {
            underCup = false;
            moveBy(0, -getDiceHeight() / 2);
            dicesUnderCupActors.removeActor(this);
            dicesBeforeCupActors.addActor(this);
        }
    }

    public void putBackUnderCup() {
        if (cup.isBelieving() || cup.isWatchingOwnThrow()) {
            reset();
        }
    }

    public void reset() {
        if (!underCup) {
            underCup = true;
            moveBy(0, getDiceHeight() / 2);
            dicesBeforeCupActors.removeActor(this);
            dicesUnderCupActors.addActor(this);
        }
    }

    public int getDiceValue() {
        return diceValue + 1;
    }
}
