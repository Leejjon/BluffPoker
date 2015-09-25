package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.blufpoker.Settings;

import java.util.List;

public class GameStage extends AbstractStage {
    private int divideScreenByThis;
    private int width = 0;
    private int height = 0;
    private int tw = 0;
    private int th = 0;
    private int middleHeightForCup = 0;
    private int middleWidthForCup = 0;

    private Settings settings = null;
    private Texture cupTexture;
    private SpriteBatch batch;
    private Sound diceRoll;
    private Image cup;

    public GameStage(int divideScreenByThis, Skin uiSkin) {
        super(divideScreenByThis, false);
        this.divideScreenByThis = divideScreenByThis;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        cupTexture = new Texture("data/ingamebeker.png");
        tw = cupTexture.getWidth();
        th = cupTexture.getHeight();

        batch = new SpriteBatch();
        diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

        cup = new Image(cupTexture);
        cup.setFillParent(false);

        // Yeah, everything is show bigger because of the divideScreenByThisValue to prevent buttons and labels from being too small. Because of this the picture itself is also too big, so we divide it by the same number again to end up with a satisfying result.
        cup.setWidth(getCupWidth() / divideScreenByThis);
        cup.setHeight(getCupHeight() / divideScreenByThis);
        middleHeightForCup = (getMiddleY() - (getCupHeight() / 2)) /2;
        middleWidthForCup = (getMiddleX() - (getCupWidth() / 2)) / 2;
        cup.setPosition(middleWidthForCup, middleHeightForCup);

        cup.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("The cup received an import event, " + "Input type: " + event.getType());
            }
        });

        addActor(cup);
    }

    public void startGame(List<String> players, Settings settings) {
        this.settings = settings;
    }

    private int getMiddleX() {
        return (width / (2));
    }

    private int getMiddleY() {
        return (height / (2));
    }

    private int getCupWidth() {
        return tw / 2;
    }

    private int getCupHeight() {
        return th / 2;
    }

    public void playDiceRoll() {
        if (visibility) {
            diceRoll.play(1.0f);
        }
    }

    public void dispose() {
        batch.dispose();
        cupTexture.dispose();
        diceRoll.dispose();
        super.dispose();
    }
}
