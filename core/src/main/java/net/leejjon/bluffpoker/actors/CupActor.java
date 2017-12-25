package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.actions.LiftCupAction;
import net.leejjon.bluffpoker.interfaces.Lockable;
import net.leejjon.bluffpoker.stages.GameStage;
import net.leejjon.bluffpoker.state.Cup;
import net.leejjon.bluffpoker.state.GameState;

import java.util.Iterator;

import lombok.Getter;

public class CupActor extends Stack {
    private Group foregroundActors;
    private Group backgroundActors;

    private TextureRegionDrawable closedCupSpriteDrawable;
    private TextureRegionDrawable openCupSpriteDrawable;

    private Texture closedCupTexture;
    private Image cup;
    @Getter private Image lock;

    private int middleYForCup = 0;
    private int middleXForCup = 0;

    public CupActor(Texture closedCupTexture, Texture openCupTexture, Texture cupLockTexture, Group foregroundActors, Group backgroundActors) {
        cup = new Image(closedCupTexture);
        lock = new Image(cupLockTexture);
        lock.setVisible(false);
        add(cup);
        add(lock);

        this.closedCupTexture = closedCupTexture;
        this.foregroundActors = foregroundActors;
        this.backgroundActors = backgroundActors;

        // Calculate the position for the CupActor.
        this.middleXForCup = (GameStage.getMiddleX() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - ((getCupWidth() / 2) / 2);
        this.middleYForCup = (GameStage.getMiddleY() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - ((getCupHeight() / 2) / 2);

        // Yeah, everything is shown bigger because of the divideScreenByThisValue to prevent buttons and labels from being too small.
        // Because of this the picture itself is also too big, so we divide it by the same number again to end up with a satisfying result.
        setWidth(getCupWidth() / 2);
        setHeight(getCupHeight() / 2);
        setPosition(middleXForCup, middleYForCup);

        closedCupSpriteDrawable = new TextureRegionDrawable(new TextureRegion(closedCupTexture));
        openCupSpriteDrawable = new TextureRegionDrawable(new TextureRegion(openCupTexture));

        setFillParent(false);
        foregroundActors.addActor(this);
    }

    private int getCupWidth() {
        return closedCupTexture.getWidth() / 2;
    }

    private int getCupHeight() {
        return closedCupTexture.getHeight() / 2;
    }

    public void open() {
        cup.setDrawable(openCupSpriteDrawable);

        foregroundActors.removeActor(this);
        backgroundActors.addActor(this);
    }

    public void close() {
        cup.setDrawable(closedCupSpriteDrawable);

        backgroundActors.removeActor(this);
        foregroundActors.addActor(this);
    }

    public boolean isMoving() {
        boolean moving = false;

        Iterator<Action> iterator = getActions().iterator();
        while (iterator.hasNext()) {
            Action action = iterator.next();
            if (action instanceof LiftCupAction) {
                moving = true;
            }
        }
        return moving;
    }

    public void reset() {
        cup.setVisible(true);
        setPosition(middleXForCup, middleYForCup);
        Gdx.app.log("bluffpoker", "CupActor has been reset.");
    }

    public int getMiddleYForCup() {
        return middleYForCup;
    }
}
