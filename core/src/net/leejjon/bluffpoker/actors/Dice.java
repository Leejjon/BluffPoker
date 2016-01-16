package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.bluffpoker.BlufPokerGame;
import net.leejjon.bluffpoker.DiceLocation;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.stages.GameStage;

import java.util.Random;

/**
 * Created by Leejjon on 3-10-2015.
 */
public class Dice extends Stack implements Lockable {
    private Cup cup;
    private Texture[] diceTextures;
    private int diceValue;
    private DiceLocation location;
    private boolean underCup = true;
    private Group dicesBeforeCupActors, dicesUnderCupActors;
    private Image diceImage;
    private Image lockImage;
    private boolean lock = false;

    public Dice(Cup cup, Texture[] diceTextures, Texture lockTexture, int initialValue, DiceLocation location, Group dicesBeforeCupActors, Group dicesUnderCupActors) {
        diceImage = new Image(diceTextures[initialValue - 1]);
        lockImage = new Image(lockTexture);
        lockImage.setVisible(false);
        add(diceImage);
        add(lockImage);
        this.cup = cup;
        this.diceTextures = diceTextures;
        diceValue = initialValue;
        this.location = location;
        this.dicesBeforeCupActors = dicesBeforeCupActors;
        this.dicesUnderCupActors = dicesUnderCupActors;

        dicesUnderCupActors.addActor(this);

        setWidth(getDiceWidth() / 2);
        setHeight(getDiceHeight() / 2);
    }

    public void throwDice() {
        if ((isUnderCup() && !cup.isLocked()) || (!isUnderCup() && !isLocked())) {
            generateRandomNumber();
        } else {
            if (diceValue != 6) {
                unlock();
            }
        }
    }

    private void generateRandomNumber() {
        Random randomDiceNumber = new Random();
        int randomNumber = randomDiceNumber.nextInt(6);
        diceImage.setDrawable(new SpriteDrawable(new Sprite(diceTextures[randomNumber])));
        diceValue = randomNumber;
    }

    public void calculateAndSetPosition() {
        float x = (GameStage.getMiddleX() / BlufPokerGame.getDivideScreenByThis()) - ((getDiceWidth() / 2) / 2);
        switch (location) {
            case LEFT:
                // This is the left dice, so we place it slightly left of the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                x = x - (getDiceWidth() / 2);
                break;
            case MIDDLE:
                // This is the middle dice, so we place it in the middle at the same height as the cup (with a little dynamic padding based on the dice size).
                break;
            case RIGHT:
                x = x + (getDiceWidth() / 2);
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
        if ((cup.isBelieving() || cup.isWatchingOwnThrow()) && isUnderCup()) {
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
            if (cup.isLocked()) {
                lock();
            } else {
                unlock();
            }
            underCup = true;
            moveBy(0, getDiceHeight() / 2);
            dicesBeforeCupActors.removeActor(this);
            dicesUnderCupActors.addActor(this);
        }
    }

    public int getDiceValue() {
        return diceValue + 1;
    }

    @Override
    public void lock() {
        if (!isUnderCup() || cup.isLocked()) {
            lock = true;
            lockImage.setVisible(true);
        }
    }

    @Override
    public void unlock() {
        lock = false;
        lockImage.setVisible(false);
    }

    @Override
    public boolean isLocked() {
        return lock;
    }
}
