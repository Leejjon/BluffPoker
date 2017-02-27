package net.leejjon.bluffpoker.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public enum Textures {
    BACKGROUND("blackbackground") {
        @Override
        public void load(AssetManager assetManager) {
            Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            backgroundPixmap.setColor(0.25f,0.25f,0.25f, 1f);
            backgroundPixmap.fill();
            Texture backgroundTexture = new Texture(backgroundPixmap);
            backgroundPixmap.dispose();
        }
    },
    CALL_BOARD("data/callboard.png"),
    CLOSED_CUP("data/closedCup.png"),
    CUP_LOCK("data/cuplock.png"),
    DICE1("data/dice1.png"),
    DICE2("data/dice2.png"),
    DICE3("data/dice3.png"),
    DICE4("data/dice4.png"),
    DICE5("data/dice5.png"),
    DICE6("data/dice6.png"),
    DICE_LOCK("data/dicelock.png"),
    OPEN_CUP("data/openCup.png");

    private final String fileName;

    Textures(String fileName) {
        this.fileName = fileName;
    }

    public Texture get(AssetManager assetManager) {
        return assetManager.get(fileName);
    }

    public void load(AssetManager assetManager) {
        assetManager.load(fileName, Texture.class);
    }

    public static void loadTextures(AssetManager assetManager) {
        for (Textures t : Textures.values()) {
            t.load(assetManager);
        }

        assetManager.finishLoading();
    }
}
