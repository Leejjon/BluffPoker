package net.leejjon.blufpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import net.leejjon.blufpoker.actions.LiftCupAction;

import java.util.Iterator;

/**
 * Created by Leejjon on 2-10-2015.
 */
public class Cup extends Image {
    private Texture believeTexture;
    private Texture defaultTexture;

    private boolean believing = false;

    public Cup(Texture defaultTexture, Texture believeTexture) {
        super(defaultTexture);
        this.defaultTexture = defaultTexture;
        this.believeTexture = believeTexture;
    }

    public void believe() {
        setDrawable(new SpriteDrawable(new Sprite(believeTexture)));
        believing = true;
    }

    public void doneBelieving() {
        setDrawable(new SpriteDrawable(new Sprite(defaultTexture)));
        believing = false;
    }

    public boolean isBelieving() {
        return believing;
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
}
