package net.leejjon.blufpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Texture[] diceTextures;
    private int diceValue;
    private boolean underCup = true;

    public Dice(Texture[] diceTextures, int initialValue) {
        super(diceTextures[initialValue-1]);
        this.diceTextures = diceTextures;
        diceValue = initialValue;

        setWidth(getDiceWidth() / BlufPokerGame.getDivideScreenByThis());
        setHeight(getDiceHeight() / BlufPokerGame.getDivideScreenByThis());
    }

    public void throwDice() {
        Random randomDiceNumber = new Random();
        int randomNumber = randomDiceNumber.nextInt(6);
        setDrawable(new SpriteDrawable(new Sprite(diceTextures[randomNumber])));
        diceValue = randomNumber++;
    }

    public void calculateAndSetPosition(DiceLocation diceLocation, int middleHeightForCup) {
        float x = ((GameStage.getMiddleX() - (getDiceWidth() / 2)) / 2);

        switch (diceLocation) {
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

        float y = middleHeightForCup + (getDiceHeight() / (3 + BlufPokerGame.getDivideScreenByThis()));
        super.setPosition(x, y);
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
        underCup = false;
    }

    public int getDiceValue() {
        return diceValue;
    }
}
