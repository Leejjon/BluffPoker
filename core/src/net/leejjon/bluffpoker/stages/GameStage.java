package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.bluffpoker.*;
import net.leejjon.bluffpoker.actors.Cup;
import net.leejjon.bluffpoker.actors.Dice;
import net.leejjon.bluffpoker.dialogs.CallNotThreeIdenticalNumbersDialog;
import net.leejjon.bluffpoker.dialogs.CallTooLowDialog;
import net.leejjon.bluffpoker.dialogs.WarningDialog;
import net.leejjon.bluffpoker.dialogs.WinnerDialog;
import net.leejjon.bluffpoker.listener.ChangeStageListener;
import net.leejjon.bluffpoker.listener.UserInterface;

import java.util.List;

public class GameStage extends AbstractStage implements UserInterface {
    private Game currentGame;

    private Texture closedCupTexture;
    private Texture openCupTexture;
    private Texture diceLockTexture;
    private Texture cupLockTexture;
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

    private final Label thirdLatestOutputLabel;
    private final Label secondLatestOutputLabel;
    private final Label latestOutputLabel;

    private CallTooLowDialog callTooLowDialog;
    private CallNotThreeIdenticalNumbersDialog callNotThreeIdenticalNumbersDialog;
    private WarningDialog throwAtLeastOneDice;
    private WarningDialog throwAllDices;
    private WinnerDialog winnerDialog;

    private TextField callInputField;

    private TextButton autoButton;
    private TextButton callButton;

    // Putting certain images at the foreground or background usually goes via z index. However the z index seems broken
    // unless I pull off crazy hacks. What Actor is painted first is simply decided by the order you add them to the stage.
    // So I decided to create two groups and switch the actors between the groups.
    private Group foreGroundActors;
    private Group dicesUnderCupActors;
    private Group backgroundActors;
    private Group dicesBeforeCupActors;

    public GameStage(Skin uiSkin, final ChangeStageListener stageListener) {
        super(false);

        closedCupTexture = new Texture("data/closedCup.png");
        openCupTexture = new Texture("data/openCup.png");
        diceLockTexture = new Texture("data/dicelock.png");
        cupLockTexture = new Texture("data/cuplock.png");

        batch = new SpriteBatch();
        diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

        callTooLowDialog = new CallTooLowDialog(uiSkin);
        callNotThreeIdenticalNumbersDialog = new CallNotThreeIdenticalNumbersDialog(uiSkin);
        throwAtLeastOneDice = new WarningDialog("Throw at least one dice!", uiSkin);
        throwAllDices = new WarningDialog("Throw with all dices!", uiSkin);
        winnerDialog = new WinnerDialog(stageListener, this, uiSkin);

        Integer[] oneTillSix = new Integer[] {0, 1, 2, 3, 4, 5, 6};

        Table topTable = new Table();
        topTable.setFillParent(true);

        callInputField = new TextField(NumberCombination.JUNK.toString(), uiSkin);

        float padding = 5f;

        topTable.top();
        topTable.padTop(padding);
        topTable.add(callInputField).pad(padding).colspan(2);
        topTable.row();

        autoButton = new TextButton("Auto", uiSkin);
        autoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setAutoValue();
            }
        });
        autoButton.setDisabled(true);
        callButton = new TextButton("Call", uiSkin);
        callButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                call();
            }
        });
        callButton.setDisabled(true);

        topTable.add(autoButton).pad(padding).colspan(1).left();
        topTable.add(callButton).pad(padding).colspan(1).right();

        thirdLatestOutputLabel = new Label("", uiSkin);
        thirdLatestOutputLabel.setColor(Color.BLACK);

        secondLatestOutputLabel = new Label("", uiSkin);
        secondLatestOutputLabel.setColor(Color.BLACK);

        latestOutputLabel = new Label("", uiSkin);
        latestOutputLabel.setColor(Color.BLACK);

        table.left();
        table.bottom();
        table.add(thirdLatestOutputLabel).left();
        table.row();
        table.add(secondLatestOutputLabel).left();
        table.row();
        table.add(latestOutputLabel).left();

        foreGroundActors = new Group();
        foreGroundActors.addActor(table);
        backgroundActors = new Group();

        cup = new Cup(closedCupTexture, openCupTexture, cupLockTexture, foreGroundActors, backgroundActors);

        dicesUnderCupActors = new Group();
        dicesBeforeCupActors = new Group();

        // Load the textures of the dices.
        dice1 = new Texture("data/dice1.png");
        dice2 = new Texture("data/dice2.png");
        dice3 = new Texture("data/dice3.png");
        dice4 = new Texture("data/dice4.png");
        dice5 = new Texture("data/dice5.png");
        dice6 = new Texture("data/dice6.png");

        Texture[] diceTextures = new Texture[] {dice1, dice2, dice3, dice4, dice5, dice6};

        leftDice = new Dice(cup, diceTextures, diceLockTexture, 6, DiceLocation.LEFT, dicesUnderCupActors, dicesBeforeCupActors);
        leftDice.calculateAndSetPosition();

        middleDice = new Dice(cup, diceTextures, diceLockTexture, 4, DiceLocation.MIDDLE, dicesUnderCupActors, dicesBeforeCupActors);
        middleDice.calculateAndSetPosition();

        rightDice = new Dice(cup, diceTextures, diceLockTexture, 3, DiceLocation.RIGHT, dicesUnderCupActors, dicesBeforeCupActors);
        rightDice.calculateAndSetPosition();


        addActor(table);
        addActor(backgroundActors);
        addActor(dicesBeforeCupActors);
        addActor(foreGroundActors);
        addActor(dicesUnderCupActors);
        addActor(topTable);
    }

    public void startGame(List<String> players, Settings settings) {
        resetCall();
        resetLog();

        if (currentGame == null) {
            currentGame = new Game(cup, leftDice, middleDice, rightDice, diceRoll, this);
        }

        currentGame.resetPlayerIterator();
        currentGame.startGame(players, settings);
    }

    private void call() {
        if (currentGame.isAllowedToCall()) {
            boolean validCall = false;
            try {
                currentGame.validateCall(getNewCall());
                validCall = true;
            } catch (InputValidationException e) {
                if (currentGame.hasBelieved666()) {
                    callNotThreeIdenticalNumbersDialog.setInvalidCallMessage(currentGame.getLatestCall());
                    callNotThreeIdenticalNumbersDialog.show(this);
                } else {
                    callTooLowDialog.callTooLow(currentGame.getLatestCall());
                    callTooLowDialog.show(this);
                }
            }

            if (validCall) {
                disableCallUserInterface();
            }
        } else {
            throwAtLeastOneDice.show(this);
        }
    }

    public static int getMiddleX() {
        return Gdx.graphics.getWidth() / 2;
    }

    public static int getMiddleY() {
        return Gdx.graphics.getHeight() / 2;
    }

    public void shake() {
        if (currentGame.isAllowedToThrow()) {
            int numberOfDicesToBeThrown = 0;

            if ((leftDice.isUnderCup() && !cup.isLocked()) || !leftDice.isLocked()) {
                numberOfDicesToBeThrown++;
            }

            if ((middleDice.isUnderCup() && !cup.isLocked()) || !middleDice.isLocked()) {
                numberOfDicesToBeThrown++;
            }

            if ((rightDice.isUnderCup() && !cup.isLocked()) || !rightDice.isLocked()) {
                numberOfDicesToBeThrown++;
            }

            if (currentGame.hasBelieved666()) {
                // You must throw all dices at once.
                if (numberOfDicesToBeThrown < 3) {
                    throwAllDices.show(this);
                } else {
                    currentGame.throwDices();
                    enableCallUserInterface();
                }
            } else {
                // You must throw at least one dice.
                if (numberOfDicesToBeThrown > 0) {
                    currentGame.throwDices();
                    enableCallUserInterface();
                } else {
                    throwAtLeastOneDice.show(this);
                }
            }
        }
    }

    @Override
    public void disableCallUserInterface() {
        callInputField.setDisabled(true);
        callButton.setDisabled(true);
        autoButton.setDisabled(true);
    }

    @Override
    public void enableCallUserInterface() {
        callInputField.setDisabled(false);
        callButton.setDisabled(false);
        autoButton.setDisabled(false);
    }

    private void setAutoValue() {
        if (currentGame.isAllowedToCall()) {
            NumberCombination currentTextBoxValue;
            NumberCombination generatedCall;
            try {
                currentTextBoxValue = NumberCombination.validNumberCombinationFrom(callInputField.getText());
                if (currentGame.hasBelieved666()) {
                    generatedCall = currentTextBoxValue.incrementAll();
                } else {
                    generatedCall = currentTextBoxValue.increment();
                }
            } catch (InputValidationException e) {
                // Just put something in there.
                generatedCall = NumberCombination.JUNK;
            }
            callInputField.setText(generatedCall.toString());
        }
    }

    public void dispose() {
        batch.dispose();
        closedCupTexture.dispose();
        openCupTexture.dispose();
        diceLockTexture.dispose();
        cupLockTexture.dispose();
        dice1.dispose();
        dice2.dispose();
        dice3.dispose();
        dice4.dispose();
        dice5.dispose();
        dice6.dispose();
        diceRoll.dispose();
        super.dispose();
    }

    @Override
    public void log(String message) {
        thirdLatestOutputLabel.setText(secondLatestOutputLabel.getText());
        secondLatestOutputLabel.setText(latestOutputLabel.getText());
        latestOutputLabel.setText(message);
    }

    @Override
    public void finishGame(String winner ) {
        winnerDialog.setWinner(winner);
        winnerDialog.show(this);
    }

    @Override
    public void restart() {
        resetCall();
        resetLog();
        currentGame.restart();
    }

    private void resetLog() {
        thirdLatestOutputLabel.setText("");
        secondLatestOutputLabel.setText("");
        latestOutputLabel.setText("");
    }

    public NumberCombination getNewCall() throws InputValidationException {
        return NumberCombination.validNumberCombinationFrom(callInputField.getText());
    }

    @Override
    public void resetCall() {
        autoButton.setDisabled(true);
        callInputField.setDisabled(true);
        callInputField.setText(NumberCombination.MIN.toString());
    }
}
