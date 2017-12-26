package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.actors.BlackBoard;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.dialogs.*;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.logic.*;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import java.util.ArrayList;

public class GameStage extends AbstractStage implements UserInterface {
    private Game currentGame;
    private Sound diceRoll;

    private CallTooLowDialog callTooLowDialog;
    private CallNotThreeIdenticalNumbersDialog callNotThreeIdenticalNumbersDialog;
    private WarningDialog throwAtLeastOneDice;
    private WarningDialog throwAllDices;
    private WinnerDialog winnerDialog;
    private TutorialDialog tutorialDialog;

    private static final String THROW_AT_LEAST_ONE_DICE = "Throw at least one dice!";
    private static final String THROW_WITH_ALL_DICES = "Throw with all dices!";
    public static final String AUTO_BUTTON_LABEL = "Auto";
    public static final String CALL_BUTTON_LABEL = "Call";
    private static final String COULD_NOT_THROW_BECAUSE_CUP_IS_MOVING = "Could not throw. CupActor moving: %b CupActor is open : %b";

    private boolean autoButtonPressed = false;

    public GameStage(Skin uiSkin, TutorialDialog tutorialDialog, final StageInterface stageInterface) {
        super(false);
        this.tutorialDialog = tutorialDialog;

        Texture callBoardTexture = stageInterface.getTexture(TextureKey.CALL_BOARD);
        Texture closedCupTexture = stageInterface.getTexture(TextureKey.CLOSED_CUP);
        Texture openCupTexture = stageInterface.getTexture(TextureKey.OPEN_CUP);
        Texture diceLockTexture = stageInterface.getTexture(TextureKey.DICE_LOCK);
        Texture cupLockTexture = stageInterface.getTexture(TextureKey.CUP_LOCK);

        diceRoll = Gdx.audio.newSound(Gdx.files.internal("sound/diceroll.mp3"));

        callTooLowDialog = new CallTooLowDialog(uiSkin);
        callNotThreeIdenticalNumbersDialog = new CallNotThreeIdenticalNumbersDialog(uiSkin);
        throwAtLeastOneDice = new WarningDialog(THROW_AT_LEAST_ONE_DICE, uiSkin);
        throwAllDices = new WarningDialog(THROW_WITH_ALL_DICES, uiSkin);
        winnerDialog = new WinnerDialog(stageInterface, uiSkin);

        Table topTable = new Table();
        topTable.top();

        final CallInputDialog callInputDialog = new CallInputDialog(this);

        float padding = 5f;

        topTable.add(state().createCallInputFieldLabel(uiSkin)).pad(padding).padTop(padding * 2).colspan(2).center();
        topTable.row();

        ClickableLabel autoButton = state().createAutoButton(uiSkin);
        autoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setAutoValue();
            }
        });
        autoButton.setDisabled(true);

        ClickableLabel callButton = state().createCallButton(uiSkin);
        callButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (state().isAllowedToCall()) {
                    String prefilledText = "";
                    if (autoButtonPressed) {
                        prefilledText = state().getCallInput();
                    }
                    Gdx.input.getTextInput(callInputDialog, CallInputDialog.ENTER_YOUR_CALL, prefilledText, CallInputDialog.ENTER_THREE_DIGITS);
                }
            }
        });
        callButton.setDisabled(true);

        float extraClickableSpace = 10f;
        topTable.add(autoButton).width(autoButton.getWidth() + (extraClickableSpace*2)).height(autoButton.getHeight() + extraClickableSpace).colspan(1).left();
        topTable.add(callButton).width(callButton.getWidth() + (extraClickableSpace*2)).height(callButton.getHeight() + extraClickableSpace).colspan(1).right();

        float middleXForTopTable = (GameStage.getMiddleX() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - ((topTable.getWidth() / 2) / 2);
        float topY = (GameStage.getTopY() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - (topTable.getHeight() / 2);

        topTable.setPosition(middleXForTopTable, topY);

        table.setFillParent(false);
        table.left();
        table.bottom();

        float bottomPadding = 3f;
        float maxLabelWidth = Gdx.graphics.getWidth() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor();

        table.add(state().createThirdLatestOutputLabel(uiSkin)).left().width(maxLabelWidth).padLeft(padding).padBottom(bottomPadding);
        table.row();
        table.add(state().createSecondLatestOutputLabel(uiSkin)).left().width(maxLabelWidth).padLeft(padding).padBottom(bottomPadding);
        table.row();
        table.add(state().createLatestOutputLabel(uiSkin)).left().width(maxLabelWidth).padLeft(padding).padBottom(bottomPadding);

        float bottomY = (GameStage.getBottomY() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) + (table.getHeight() / 2);

        table.setPosition(0, bottomY);

        // Putting certain images at the foreground or background usually goes via z index. However the z index seems broken
        // unless I pull off crazy hacks. What Actor is painted first is simply decided by the order you add them to the stage.
        // So I decided to create two groups and switch the actors between the groups.
        Group foreGroundActors = new Group();
        Group backgroundActors = new Group();
        Group dicesUnderCupActors = new Group();
        Group dicesBeforeCupActors = new Group();

        foreGroundActors.addActor(table);

        BlackBoard callBoard = new BlackBoard(callBoardTexture);

        state().createCupActor(closedCupTexture, openCupTexture, cupLockTexture, foreGroundActors, backgroundActors);

        // Load the textures of the dices.
        Texture dice1 = stageInterface.getTexture(TextureKey.DICE1);
        Texture dice2 = stageInterface.getTexture(TextureKey.DICE2);
        Texture dice3 = stageInterface.getTexture(TextureKey.DICE3);
        Texture dice4 = stageInterface.getTexture(TextureKey.DICE4);
        Texture dice5 = stageInterface.getTexture(TextureKey.DICE5);
        Texture dice6 = stageInterface.getTexture(TextureKey.DICE6);

        Texture[] diceTextures = new Texture[] {dice1, dice2, dice3, dice4, dice5, dice6};

        state().createDiceActors(diceTextures, diceLockTexture, dicesBeforeCupActors, dicesUnderCupActors);

        addActor(backgroundActors);
        addActor(dicesBeforeCupActors);
        addActor(foreGroundActors);
        addActor(dicesUnderCupActors);
        addActor(callBoard);
        addActor(topTable);
    }

    public void startGame(ArrayList<String> players) {
        if (currentGame == null) {
            currentGame = new Game(diceRoll, this);
        }

        state().updatePlayerIterator(0);
        currentGame.startGame(players);
    }

    @Override
    public void call(String call) {
        if (state().isAllowedToCall()) {
            boolean validCall = false;
            try {
                currentGame.validateCall(getNewCall(call));
                validCall = true;
            } catch (InputValidationException e) {
                if (state().isBelieved666()) {
                    callNotThreeIdenticalNumbersDialog.setInvalidCallMessage(state().getLatestCall().getNumberCombination());
                    callNotThreeIdenticalNumbersDialog.show(this);
                } else {
                    if (state().getLatestCall() != null) {
                        callTooLowDialog.callTooLow(state().getLatestCall().getNumberCombination());
                        callTooLowDialog.show(this);
                    } else {
                        Gdx.app.log(BluffPokerGame.TAG, String.format("Invalid call received from on screen keyboard: %s", call));
                    }
                }
            }

            if (validCall) {
                lockMessageAlreadyShowed = false;
                autoButtonPressed = false;
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

    public static int getTopY() {
        return Gdx.graphics.getHeight() - BluffPokerGame.getPlatformSpecificInterface().getTopPadding();
    }

    private static int getBottomY() {
        return BluffPokerGame.getPlatformSpecificInterface().getBottomPadding();
    }

    public void shake() {
        if (state().isAllowedToThrow()) {
            if (state().isBelieved666()) {
                // You must throw all dices at once.
                if (state().getNumberOfDicesToBeThrown() < 3) {
                    throwAllDices.show(this);
                } else {
                    currentGame.throwDices();
                }
            } else {
                // You must throw at least one dice.
                if (state().getNumberOfDicesToBeThrown() > 0 /* or is blind call and you dont need this warning. */) {
                    currentGame.throwDices();
                } else {
                    throwAtLeastOneDice.show(this);
                }
            }
        } else {
            Gdx.app.log(BluffPokerGame.TAG,
                    String.format(COULD_NOT_THROW_BECAUSE_CUP_IS_MOVING,
                            state().getCup().getCupActor().isMoving(),
                            (state().getCup().isWatchingOwnThrow() | state().getCup().isBelieving())));
        }
    }

    @Override
    public void showTutorialMessage(TutorialMessage message, String... arguments) {
        tutorialDialog.addToTutorialMessageQueue(this, message, arguments);
    }

    private boolean lockMessageAlreadyShowed = false;

    @Override
    public void showLockMessage() {
        if (!lockMessageAlreadyShowed) {
            lockMessageAlreadyShowed = true;
            showTutorialMessage(TutorialMessage.EXPLAIN_LOCK);
        }
    }

    private void setAutoValue() {
        if (state().isAllowedToCall()) {
            NumberCombination currentTextBoxValue;
            NumberCombination generatedCall;
            try {
                currentTextBoxValue = NumberCombination.validNumberCombinationFrom(state().getCallInput());
                if (state().isBelieved666()) {
                    generatedCall = currentTextBoxValue.incrementAll();
                } else {
                    generatedCall = currentTextBoxValue.increment();
                }
            } catch (InputValidationException e) {
                // Just put something in there.
                generatedCall = NumberCombination.JUNK;
            }
            state().setCallInput(generatedCall.toString());
            autoButtonPressed = true;
        }
    }

    public void dispose() {
        diceRoll.dispose();
        super.dispose();
    }

    @Override
    public void finishGame(String winner) {
        winnerDialog.setWinner(winner);
        winnerDialog.show(this);
    }

    private NumberCombination getNewCall(String call) throws InputValidationException {
        return NumberCombination.validNumberCombinationFrom(call);
    }

    public GameState state() {
        return GameState.get();
    }
}
