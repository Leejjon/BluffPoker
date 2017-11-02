package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Logo extends Image {
    private Texture logoTexture;

    public Logo(Texture logoTexture) {
        super(logoTexture);
        this.logoTexture = logoTexture;

        setWidth(logoTexture.getWidth() / 4);
        setHeight(logoTexture.getHeight() / 4);
    }
}
