package net.leejjon.bluffpoker.actors;

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

import java.util.Iterator;

public class Cup extends Stack implements Lockable {
    private Group foregroundActors;
    private Group backgroundActors;

    private TextureRegionDrawable closedCupSpriteDrawable;
    private TextureRegionDrawable openCupSpriteDrawable;

    private Texture closedCupTexture;
    private Image cup;
    private Image lock;

    private int middleYForCup = 0;
    private int middleXForCup = 0;

    private boolean believing = false;
    private boolean watchingOwnThrow = false;
    private boolean locked = false;

    public Cup(Texture closedCupTexture, Texture openCupTexture, Texture cupLockTexture, Group foregroundActors, Group backgroundActors) {
        cup = new Image(closedCupTexture);
        lock = new Image(cupLockTexture);
        lock.setVisible(false);
        add(cup);
        add(lock);

        this.closedCupTexture = closedCupTexture;
        this.foregroundActors = foregroundActors;
        this.backgroundActors = backgroundActors;

        // Calculate the position for the Cup.
        this.middleXForCup = (GameStage.getMiddleX() / BluffPokerGame.getDivideScreenByThis()) - ((getCupWidth() / 2) / 2);
        this.middleYForCup = (GameStage.getMiddleY() / BluffPokerGame.getDivideScreenByThis()) - ((getCupHeight() / 2) / 2);

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

    public void believe() {
        cup.setDrawable(openCupSpriteDrawable);

        foregroundActors.removeActor(this);
        backgroundActors.addActor(this);
        believing = true;
    }

    public void doneBelieving() {
        cup.setDrawable(closedCupSpriteDrawable);

        backgroundActors.removeActor(this);
        foregroundActors.addActor(this);
        believing = false;
    }

    public boolean isBelieving() {
        return believing;
    }

    public void watchOwnThrow() {
        cup.setDrawable(openCupSpriteDrawable);

        foregroundActors.removeActor(this);
        backgroundActors.addActor(this);

        watchingOwnThrow = true;
    }

    public void doneWatchingOwnThrow() {
        cup.setDrawable(closedCupSpriteDrawable);

        backgroundActors.removeActor(this);
        foregroundActors.addActor(this);

        watchingOwnThrow = false;
    }

    public boolean isWatchingOwnThrow() {
        return watchingOwnThrow;
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
    }

    public int getMiddleYForCup() {
        return middleYForCup;
    }

    @Override
    public void lock() {
        locked = true;
        lock.setVisible(true);
    }

    @Override
    public void unlock() {
        locked = false;
        lock.setVisible(false);
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
