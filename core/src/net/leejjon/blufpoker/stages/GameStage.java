package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.blufpoker.Settings;
import net.leejjon.blufpoker.actors.Cup;
import net.leejjon.blufpoker.listener.CupListener;

import java.util.List;

public class GameStage extends AbstractStage {
    private int divideScreenByThis;
    private int width = 0;
    private int height = 0;
    private int middleHeightForCup = 0;
    private int middleWidthForCup = 0;

    private Settings settings = null;
    private Texture cupTexture;
    private Texture believing;
    private Texture dice1;
    private Texture dice2;
    private Texture dice3;
    private Texture dice4;
    private Texture dice5;
    private Texture dice6;

    private SpriteBatch batch;
    private Sound diceRoll;
    private final Cup cup;
    private Image leftDice;
    private Image middleDice;
    private Image rightDice;

    // Putting certain images at the foreground or background usually goes via z index. However the z index seems broken
    // unless I pull off crazy hacks. What Actor is painted first is simply decided by the order you add them to the stage.
    // So I decided to create two groups and switch the actors between the groups.
    private Group foreGroundActors;
    private Group backgroundActors;

    public GameStage(int divideScreenByThis, Skin uiSkin) {
        super(divideScreenByThis, false);
        this.divideScreenByThis = divideScreenByThis;

        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        cupTexture = new Texture("data/cup.png");
        believing = new Texture("data/believe.png");

        // Load the textures of the dices.
        dice1 = new Texture("data/dice1.png");
        dice2 = new Texture("data/dice2.png");
        dice3 = new Texture("data/dice3.png");
        dice4 = new Texture("data/dice4.png");
        dice5 = new Texture("data/dice5.png");
        dice6 = new Texture("data/dice6.png");

        batch = new SpriteBatch();
        diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

        foreGroundActors = new Group();
        backgroundActors = new Group();

        cup = new Cup(cupTexture, believing, foreGroundActors, backgroundActors);

        // Yeah, everything is show bigger because of the divideScreenByThisValue to prevent buttons and labels from being too small. Because of this the picture itself is also too big, so we divide it by the same number again to end up with a satisfying result.
        cup.setWidth(getCupWidth() / divideScreenByThis);
        cup.setHeight(getCupHeight() / divideScreenByThis);
        middleHeightForCup = (getMiddleY() - (getCupHeight() / 2)) / divideScreenByThis;
        middleWidthForCup = (getMiddleX() - (getCupWidth() / 2)) / divideScreenByThis;
        cup.setPosition(middleWidthForCup, middleHeightForCup);
        cup.addListener(new CupListener(cup, divideScreenByThis));

        leftDice = new Image(dice6);
        leftDice.setWidth(getDiceWidth() / divideScreenByThis);
        leftDice.setHeight(getDiceHeight() / divideScreenByThis);
        // This is the left dice, so we place it slightly left of the middle at the same height as the cup (with a little dynamic padding based on the dice size).
        leftDice.setPosition(((getMiddleX() - (getDiceWidth() / 2)) / 2) - (getDiceWidth() / divideScreenByThis), middleHeightForCup + (getDiceHeight() / (3 + divideScreenByThis)));

        middleDice = new Image(dice4);
        middleDice.setWidth(getDiceWidth() / divideScreenByThis);
        middleDice.setHeight(getDiceHeight() / divideScreenByThis);
        // This is the middle dice, so we place it in the middle at the same height as the cup (with a little dynamic padding based on the dice size).
        middleDice.setPosition((getMiddleX() - (getDiceWidth() / 2)) / 2, middleHeightForCup + (getDiceHeight() / (3+divideScreenByThis)));

        rightDice = new Image(dice3);
        rightDice.setWidth(getDiceWidth() / divideScreenByThis);
        rightDice.setHeight(getDiceHeight() / divideScreenByThis);
        // This is the right dice, so we place it slightly right of the middle at the same height as the cup (with a little dynamic padding based on the dice size).
        rightDice.setPosition(((getMiddleX() - (getDiceWidth() / 2)) / 2) + (getDiceWidth() / divideScreenByThis), middleHeightForCup + (getDiceHeight() / (3+divideScreenByThis)));

        addActor(backgroundActors);
        addActor(leftDice);
        addActor(middleDice);
        addActor(rightDice);
        addActor(foreGroundActors);
    }

    public void startGame(List<String> players, Settings settings) {
        this.settings = settings;
    }

    private int getMiddleX() {
        return width / 2;
    }

    private int getMiddleY() {
        return height / 2;
    }

    private int getCupWidth() {
        return cupTexture.getWidth() / 2;
    }

    private int getCupHeight() {
        return cupTexture.getHeight() / 2;
    }

    private int getDiceWidth() {
        return dice1.getWidth() / 2;
    }

    private int getDiceHeight() {
        return dice1.getHeight() / 2;
    }

    public void playDiceRoll() {
        if (visibility) {
            if (!cup.isMoving()) {
                resetCup();
                diceRoll.play(1.0f);
            }
        }
    }

    public void resetCup() {
        cup.setVisible(true);
        cup.setPosition(middleWidthForCup, middleHeightForCup);
    }

    public void dispose() {
        batch.dispose();
        cupTexture.dispose();
        believing.dispose();
        dice1.dispose();
        dice2.dispose();
        dice3.dispose();
        dice4.dispose();
        dice5.dispose();
        dice6.dispose();
        diceRoll.dispose();
        super.dispose();
    }
}
