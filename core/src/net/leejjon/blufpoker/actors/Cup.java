package net.leejjon.blufpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.blufpoker.BlufPokerGame;
import net.leejjon.blufpoker.actions.LiftCupAction;
import net.leejjon.blufpoker.interfaces.Lockable;
import net.leejjon.blufpoker.listener.CupListener;
import net.leejjon.blufpoker.stages.GameInputInterface;
import net.leejjon.blufpoker.stages.GameStage;

import java.util.Iterator;

/**
 * Created by Leejjon on 2-10-2015.
 */
public class Cup extends Image implements Lockable {
    private Group foregroundActors;
    private Group backgroundActors;

    private SpriteDrawable closedCupSpriteDrawable;
    private SpriteDrawable openCupSpriteDrawable;

    private Texture closedCupTexture;

    private int middleHeightForCup = 0;
    private int middleWidthForCup = 0;

    private boolean believing = false;
    private boolean watchingOwnThrow = false;
    private boolean locked = false;

    public Cup(Texture closedCupTexture, Texture openCupTexture, Group foregroundActors, Group backgroundActors) {
        super(closedCupTexture);
        this.closedCupTexture = closedCupTexture;
        this.foregroundActors = foregroundActors;
        this.backgroundActors = backgroundActors;

        // Calculate the position for the Cup.
        this.middleWidthForCup = (GameStage.getMiddleX() - (getCupWidth() / 2)) / BlufPokerGame.getDivideScreenByThis();
        this.middleHeightForCup = (GameStage.getMiddleY() - (getCupHeight() / 2)) / BlufPokerGame.getDivideScreenByThis();

        // Yeah, everything is shown bigger because of the divideScreenByThisValue to prevent buttons and labels from being too small. Because of this the picture itself is also too big, so we divide it by the same number again to end up with a satisfying result.
        setWidth(getCupWidth() / BlufPokerGame.getDivideScreenByThis());
        setHeight(getCupHeight() / BlufPokerGame.getDivideScreenByThis());
        setPosition(middleWidthForCup, middleHeightForCup);

        closedCupSpriteDrawable = new SpriteDrawable(new Sprite(closedCupTexture));
        openCupSpriteDrawable = new SpriteDrawable(new Sprite(openCupTexture));

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
        setDrawable(openCupSpriteDrawable);

        foregroundActors.removeActor(this);
        backgroundActors.addActor(this);

        believing = true;
    }

    public void doneBelieving() {
        setDrawable(closedCupSpriteDrawable);

        backgroundActors.removeActor(this);
        foregroundActors.addActor(this);

        believing = false;
    }

    public boolean isBelieving() {
        return believing;
    }

    public void watchOwnThrow() {
        setDrawable(openCupSpriteDrawable);

        foregroundActors.removeActor(this);
        backgroundActors.addActor(this);

        watchingOwnThrow = true;
    }

    public void doneWatchingOwnThrow() {
        setDrawable(closedCupSpriteDrawable);

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
        setVisible(true);
        setPosition(middleWidthForCup, middleHeightForCup);
    }

    public int getMiddleHeightForCup() {
        return middleHeightForCup;
    }

    @Override
    public void lock() {
        locked = true;
    }

    @Override
    public void unlock() {
        locked = false;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
