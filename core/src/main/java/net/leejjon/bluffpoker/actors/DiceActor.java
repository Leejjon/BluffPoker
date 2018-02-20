package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.logic.DiceLocation;
import net.leejjon.bluffpoker.stages.GameStage;

public class DiceActor extends Stack {
    private Texture[] diceTextures;
    private DiceLocation location;
    private Group dicesBeforeCupActors, dicesUnderCupActors;
    private Image diceImage;
    private Image lockImage;

    private SpriteDrawable[] spriteDrawables = new SpriteDrawable[6];

    public DiceActor(Texture[] diceTextures, Texture lockTexture, int initialValue, DiceLocation location, Group dicesBeforeCupActors, Group dicesUnderCupActors, int middleYForCup) {
        this.diceTextures = diceTextures;
        for (int i = 0; i < diceTextures.length; i++) {
            spriteDrawables[i] = new SpriteDrawable(new Sprite(diceTextures[i]));
        }

        diceImage = new Image(spriteDrawables[initialValue-1]);
        lockImage = new Image(lockTexture);
        lockImage.setVisible(false);
        add(diceImage);
        add(lockImage);

        this.location = location;
        this.dicesBeforeCupActors = dicesBeforeCupActors;
        this.dicesUnderCupActors = dicesUnderCupActors;
        this.dicesUnderCupActors.addActor(this);

        setWidth(getDiceWidth() / 2);
        setHeight(getDiceHeight() / 2);

        calculateAndSetPosition(middleYForCup);
    }

    public void updateDice(int newNumber) {
        diceImage.setDrawable(spriteDrawables[newNumber]);
    }

    public void calculateAndSetPosition(int middleYForCup) {
        float x = (GameStage.getMiddleX() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()) - ((getDiceWidth() / 2) / 2);
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

        float y = middleYForCup + (getDiceHeight() / (3 + BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()));

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

    public void moveUp() {
        moveBy(0, getDiceHeight() / 2);
        reset();
    }

    public void reset() {
        dicesBeforeCupActors.removeActor(this);
        dicesUnderCupActors.addActor(this);
    }

    public void lock() {
        lockImage.setVisible(true);
    }

    public void unlock() {
        lockImage.setVisible(false);
    }

    /**
     * Only use this in test.
     */
    public Image getDiceImage() {
        return diceImage;
    }

    /**
     * Only use this in test.
     */
    public Image getLockImage() {
        return lockImage;
    }

    /**
     * Only use this in tests.
     */
    public SpriteDrawable[] getSpriteDrawables() {
        return spriteDrawables;
    }
}
