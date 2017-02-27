package net.leejjon.bluffpoker.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public enum Textures {
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

    public String getFileName() {
        return fileName;
    }

    public Texture get(AssetManager assetManager) {
        return assetManager.get(fileName);
    }

    public static void loadTextures(AssetManager assetManager) {
        for (Textures t : Textures.values()) {
            assetManager.load(t.getFileName(), Texture.class);
        }
        assetManager.finishLoading();
    }
}
