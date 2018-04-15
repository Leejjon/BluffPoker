package net.leejjon.bluffpoker.enums;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.stages.PauseStage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum TextureKey {
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
    LOGO("data/logo.png"),
    OPEN_CUP("data/openCup.png"),
    BLACK_PIXEL() {
        @Override
        protected Texture get() {
            Pixmap onePixelPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            onePixelPixmap.setColor(0f, 0f, 0f, 1);
            onePixelPixmap.fill();
            Texture onePixelTexture = new Texture(onePixelPixmap);
            onePixelPixmap.dispose();
            return onePixelTexture;
        }
    },
    MENU_TOP_COLOR() {
        @Override
        protected Texture get() {
            Pixmap onePixelPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
//            onePixelPixmap.setColor(0.10f, 0.12f, 0.09f, 1);
            onePixelPixmap.setColor(0.40f, 0.40f, 0.40f, 1);

            onePixelPixmap.fill();
            Texture onePixelTexture = new Texture(onePixelPixmap);
            onePixelPixmap.dispose();
            return onePixelTexture;
        }
    },
    MENU_COLOR() {
        @Override
        protected Texture get() {
            Pixmap onePixelPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
            onePixelPixmap.setColor(0.75f, 0.75f, 0.75f, 1);
//            onePixelPixmap.setColor(0.29f, 0.47f, 0.33f, 1);
            onePixelPixmap.fill();
            Texture onePixelTexture = new Texture(onePixelPixmap);
            onePixelPixmap.dispose();
            return onePixelTexture;
        }
    };

    private String fileName;

    protected Texture get() {
        return new Texture(fileName);
    }

    /**
     * @param relativePath Relative path to assets.
     * @return Texture of enum entry.
     */
    protected Texture get(String relativePath) {
        if (fileName == null) {
            return get();
        } else {
            return new Texture(relativePath + fileName);
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
