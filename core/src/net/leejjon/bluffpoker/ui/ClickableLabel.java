package net.leejjon.bluffpoker.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;

public class ClickableLabel extends Label implements Disableable {
    private boolean disabled;

    public ClickableLabel(CharSequence text, Skin skin) {
        super(text, skin /*, "arial32", Color.WHITE*/);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
}
