package net.leejjon.bluffpoker.actions;

import com.badlogic.gdx.scenes.scene2d.Action;

import java.util.concurrent.atomic.AtomicBoolean;

public class PauseScreenAction extends Action {
    private AtomicBoolean doneShowing = new AtomicBoolean(false);

    @Override
    public boolean act(float delta) {

        return doneShowing.get();
    }
}
