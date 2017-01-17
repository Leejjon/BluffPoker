package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WoodenTable extends Image {
    private Texture woodenTableTexture;

    public WoodenTable(Texture woodenTableTexture) {
        super(woodenTableTexture);
        this.woodenTableTexture = woodenTableTexture;
    }

    private int getCallBoardWidth() {
        return woodenTableTexture.getWidth() / 2;
    }

    private int getCallBoardHeight() {
        return woodenTableTexture.getHeight() / 2;
    }
}
