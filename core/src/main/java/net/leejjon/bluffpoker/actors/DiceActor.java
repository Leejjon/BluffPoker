package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.logic.DiceLocation;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.stages.GameStage;
import net.leejjon.bluffpoker.state.GameState;

import java.util.Random;

import lombok.Getter;

public class DiceActor extends Stack {
    private Texture[] diceTextures;
    private DiceLocation location;
    private Group dicesBeforeCupActors, dicesUnderCupActors;

    private Image diceImage;
    private Image lockImage;

    private SpriteDrawable[] spriteDrawables = new SpriteDrawable[6];

    public DiceActor(Texture[] diceTextures, Texture lockTexture, int initialValue, DiceLocation location, Group dicesBeforeCupActors, Group dicesUnderCupActors) {
        diceImage = new Image(diceTextures[initialValue - 1]);
        lockImage = new Image(lockTexture);
        lockImage.setVisible(false);
        add(diceImage);
        add(lockImage);
        this.diceTextures = diceTextures;
        this.location = location;
        this.dicesBeforeCupActors = dicesBeforeCupActors;
        this.dicesUnderCupActors = dicesUnderCupActors;
        dicesUnderCupActors.addActor(this);

        setWidth(getDiceWidth() / 2);
        setHeight(getDiceHeight() / 2);

        initializeSpriteDrawables();
    }

    private void initializeSpriteDrawables() {
        for (int i = 0; i < diceTextures.length; i++) {
            spriteDrawables[i] = new SpriteDrawable(new Sprite(diceTextures[i]));
        }
    }

    public void updateDice(int newNumber) {
        diceImage.setDrawable(spriteDrawables[newNumber]);
    }

    public void calculateAndSetPosition(int middleYForCup) {
        float x = (GameStage.getMiddleX() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - ((getDiceWidth() / 2) / 2);
        switch (location) {
            case LEFT:
                // This is the left dice, so we place it slightly left of the middle at the same height as the cupActor (with a little dynamic padding based on the dice size).
                x = x - (getDiceWidth() / 2);
                break;
            case MIDDLE:
                // This is the middle dice, so we place it in the middle at the same height as the cupActor (with a little dynamic padding based on the dice size).
                break;
            case RIGHT:
                x = x + (getDiceWidth() / 2);
                break;
        }

        float y = middleYForCup + (getDiceHeight() / (3 + BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()));
        setPosition(x, y);
    }

    private int getDiceWidth() {
        return diceTextures[0].getWidth() / 2;
    }

    private int getDiceHeight() {
        return diceTextures[0].getHeight() / 2;
    }

    public void moveDown() {
        moveBy(0, -getDiceHeight() / 2);
        dicesUnderCupActors.removeActor(this);
        dicesBeforeCupActors.addActor(this);
    }

    public void reset() {
        moveBy(0, getDiceHeight() / 2);
        dicesBeforeCupActors.removeActor(this);
        dicesUnderCupActors.addActor(this);
    }

    public void lock() {
        lockImage.setVisible(true);
    }

    public void unlock() {
        lockImage.setVisible(false);
    }
}
