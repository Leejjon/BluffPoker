package net.leejjon.bluffpoker.assets;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

public enum TextureKey {
    BLACK_PIXEL() {
        @Override
        public Texture get() {
            Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            backgroundPixmap.setColor(0.25f,0.25f,0.25f, 1f);
            backgroundPixmap.fill();
            Texture backgroundTexture = new Texture(backgroundPixmap);
            backgroundPixmap.dispose();
            return backgroundTexture;
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

    private String fileName = null;

    TextureKey() {}

    TextureKey(String fileName) {
        this.fileName = fileName;
    }

    protected Texture get() {
        if (fileName != null) {
            return new Texture(fileName);
        } else {
            throw new IllegalAccessError("Texture not found.");
        }
    }

    public static ObjectMap<TextureKey, Texture> getAllTextures() {
        ObjectMap<TextureKey, Texture> textureMap = new ObjectMap<>();
        for (TextureKey t : TextureKey.values()) {
            textureMap.put(t, t.get());
        }
        return textureMap;
    }
}
