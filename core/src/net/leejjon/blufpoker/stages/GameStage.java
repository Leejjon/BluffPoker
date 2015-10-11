package net.leejjon.blufpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.blufpoker.DiceLocation;
import net.leejjon.blufpoker.Game;
import net.leejjon.blufpoker.Settings;
import net.leejjon.blufpoker.actors.Cup;
import net.leejjon.blufpoker.actors.Dice;

import java.util.List;

public class GameStage extends AbstractStage {
    private Game currentGame;

    private Texture closedCupTexture;
    private Texture openCupTexture;
    private Texture dice1;
    private Texture dice2;
    private Texture dice3;
    private Texture dice4;
    private Texture dice5;
    private Texture dice6;

    private SpriteBatch batch;
    private Sound diceRoll;
    private final Cup cup;
    private Dice leftDice;
    private Dice middleDice;
    private Dice rightDice;
    private final Label secondLatestOutputLabel;
    private final Label latestOutputLabel;

    private SelectBox<Integer> firstNumberOfCall;
    private SelectBox<Integer> secondNumberOfCall;
    private SelectBox<Integer> thirdNumberOfCall;

    private TextButton autoButton;
    private TextButton submitButton;

    // Putting certain images at the foreground or background usually goes via z index. However the z index seems broken
    // unless I pull off crazy hacks. What Actor is painted first is simply decided by the order you add them to the stage.
    // So I decided to create two groups and switch the actors between the groups.
    private Group foreGroundActors;
    private Group backgroundActors;

    public GameStage(Skin uiSkin) {
        super(false);

        closedCupTexture = new Texture("data/closedCup.png");
        openCupTexture = new Texture("data/openCup.png");

        batch = new SpriteBatch();
        diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

        Integer[] oneTillSix = new Integer[] {0, 1, 2, 3, 4, 5, 6};

        Table topTable = new Table();
        topTable.setFillParent(true);

        firstNumberOfCall = new SelectBox<>(uiSkin);
        firstNumberOfCall.setItems(oneTillSix);
        firstNumberOfCall.setSelected(0);
        firstNumberOfCall.setDisabled(true);

        secondNumberOfCall = new SelectBox<>(uiSkin);
        secondNumberOfCall.setItems(oneTillSix);
        secondNumberOfCall.setSelected(0);
        secondNumberOfCall.setDisabled(true);

        thirdNumberOfCall = new SelectBox<>(uiSkin);
        thirdNumberOfCall.setItems(oneTillSix);
        thirdNumberOfCall.setSelected(0);
        thirdNumberOfCall.setDisabled(true);

        float padding = 5f;

        topTable.top();
        topTable.padTop(padding);
        topTable.add(firstNumberOfCall).pad(padding).colspan(2);
        topTable.add(secondNumberOfCall).pad(padding).colspan(2);
        topTable.add(thirdNumberOfCall).pad(padding).colspan(2);
        topTable.row();

        autoButton = new TextButton("Auto", uiSkin);
        autoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setAutoValue();
            }
        });
        submitButton = new TextButton("Submit", uiSkin);

        topTable.add(autoButton).colspan(3).left();
        topTable.add(submitButton).colspan(3).right();

        secondLatestOutputLabel = new Label("", uiSkin);
        secondLatestOutputLabel.setColor(Color.BLACK);

        latestOutputLabel = new Label("", uiSkin);
        latestOutputLabel.setColor(Color.BLACK);

        table.left();
        table.bottom();
        table.add(secondLatestOutputLabel).left();
        table.row();
        table.add(latestOutputLabel).left();

        foreGroundActors = new Group();
        foreGroundActors.addActor(table);
        backgroundActors = new Group();

        cup = new Cup(closedCupTexture, openCupTexture, foreGroundActors, backgroundActors);

        // Load the textures of the dices.
        dice1 = new Texture("data/dice1.png");
        dice2 = new Texture("data/dice2.png");
        dice3 = new Texture("data/dice3.png");
        dice4 = new Texture("data/dice4.png");
        dice5 = new Texture("data/dice5.png");
        dice6 = new Texture("data/dice6.png");

        Texture[] diceTextures = new Texture[] {dice1, dice2, dice3, dice4, dice5, dice6};

        leftDice = new Dice(diceTextures, 6);
        leftDice.calculateAndSetPosition(DiceLocation.LEFT, cup.getMiddleHeightForCup());

        middleDice = new Dice(diceTextures, 4);
        middleDice.calculateAndSetPosition(DiceLocation.MIDDLE, cup.getMiddleHeightForCup());

        rightDice = new Dice(diceTextures, 3);
        rightDice.calculateAndSetPosition(DiceLocation.RIGHT, cup.getMiddleHeightForCup());

        addActor(table);
        addActor(backgroundActors);
        addActor(leftDice);
        addActor(middleDice);
        addActor(rightDice);
        addActor(foreGroundActors);
        addActor(topTable);
    }

    public void startGame(List<String> players, Settings settings) {
        currentGame = new Game(players, settings, cup, leftDice, middleDice, rightDice, diceRoll);

        logMessage(currentGame.startGame());
    }

    private void logMessage(String message) {
        secondLatestOutputLabel.setText(latestOutputLabel.getText());
        latestOutputLabel.setText(message);
    }

    public static int getMiddleX() {
        return Gdx.graphics.getWidth() / 2;
    }

    public static int getMiddleY() {
        return Gdx.graphics.getHeight() / 2;
    }

    public void shake() {
        if (currentGame.isAllowedToThrow()) {
            logMessage(currentGame.throwDicesInCup());
            firstNumberOfCall.setDisabled(false);
            secondNumberOfCall.setDisabled(false);
            thirdNumberOfCall.setDisabled(false);
            autoButton.setDisabled(false);
        }
    }

    private void setAutoValue() {
        firstNumberOfCall.setSelected(6);
        secondNumberOfCall.setSelected(4);
        thirdNumberOfCall.setSelected(3);
    }

    public void dispose() {
        batch.dispose();
        closedCupTexture.dispose();
        openCupTexture.dispose();
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
