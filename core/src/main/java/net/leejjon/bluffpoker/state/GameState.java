package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.Getter;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.actors.DiceActor;
import net.leejjon.bluffpoker.interfaces.DiceValueGenerator;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.listener.DiceListener;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;
import net.leejjon.bluffpoker.logic.Call;
import net.leejjon.bluffpoker.logic.DiceLocation;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;
import net.leejjon.bluffpoker.stages.GameStage;
import net.leejjon.bluffpoker.ui.ClickableLabel;

/**
 * A singleton to contain all the state in the game, designed with testability in mind.
 */
public class GameState {
    static final String KEY = "gameState";
    // Font used in console is Microsoft JingHei
    private static final String console = "console";

    @Getter
    private boolean newGameState = true;

    private void checkUiInitializedPreconditions() {
        Preconditions.checkNotNull(callInputField);
        Preconditions.checkNotNull(thirdLatestOutputLabel);
        Preconditions.checkNotNull(secondLatestOutputLabel);
        Preconditions.checkNotNull(latestOutputLabel);
        Preconditions.checkNotNull(autoButton);
        Preconditions.checkNotNull(callButton);
        Preconditions.checkNotNull(cup.getCupActor());
        Preconditions.checkNotNull(leftDice.getDiceActor());
        Preconditions.checkNotNull(middleDice.getDiceActor());
        Preconditions.checkNotNull(rightDice.getDiceActor());
    }

    // Actual state
    @Getter
    private Player[] players;

    @Getter
    private int playerIterator = 0;

    @Getter
    private Cup cup = new Cup();

    // This is actually 643, but we show
    @Getter private Dice leftDice;
    @Getter private Dice middleDice;
    @Getter private Dice rightDice;

    @Getter
    private Call latestCall = null;

    @Getter private String callInput = NumberCombination.MIN.toString();

    String thirdLatestOutput = "";
    String secondLatestOutput = "";
    String latestOutput = "";

    // TODO: Create a statusses object.
    @Getter private boolean bokAvailable = true;
    @Getter private boolean firstThrowSinceDeath = true;

    private boolean hasToThrow = true;
    @Getter private boolean hasThrown = false;
    @Getter private boolean allowedToBelieveOrNotBelieve = false;
    @Getter private boolean canViewOwnThrow = false;
    @Getter private boolean allowedToCall = false;
    @Getter private boolean believed666 = false;
    @Getter private boolean blindPass = false;

    // Custom state getter methods
    public Player getCurrentPlayer() {
        return players[playerIterator];
    }

    /**
     * TODO: This method fails when believing
     * @return
     */
    public boolean isBelievingBlind() { return !blindPass && !hasToThrow && allowedToBelieveOrNotBelieve; }

    public boolean isInitiatingBlindPass() { return blindPass && hasThrown && firstThrowSinceDeath && !allowedToBelieveOrNotBelieve; }

    /**
     * TODO: This method is broken when believing 666.
     * @return
     */
    public boolean bailedOutOfBlindBelieving() { return blindPass && !hasToThrow && !firstThrowSinceDeath; }

    public boolean userTriesToLockOrUnlock() { return !blindPass && hasToThrow && !firstThrowSinceDeath; }

    public boolean isAllowedToThrow() {
        if ((hasToThrow || (blindPass && !hasThrown)) && !cup.getCupActor().isMoving() && !cup.isBelieving() && !cup.isWatchingOwnThrow()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAllowedToLock() {
        if ((hasToThrow || (blindPass && !hasThrown)) && !cup.getCupActor().isMoving()) {
            return true;
        } else {
            return false;
        }
    }

    public int getNumberOfDicesToBeThrown() {
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
        return numberOfDicesToBeThrown;
    }

    // Stateful UI elements
    private transient Label callInputField;
    private transient Label thirdLatestOutputLabel;
    private transient Label secondLatestOutputLabel;
    private transient Label latestOutputLabel;
    private transient ClickableLabel autoButton;
    private transient ClickableLabel callButton;

    // UI element initialization methods
    public Label createCallInputFieldLabel(Skin uiSkin) {
        callInputField = new Label(NumberCombination.MIN.toString(), uiSkin, "arial64", Color.WHITE);
        return callInputField;
    }

    public Label createThirdLatestOutputLabel(Skin uiSkin) {
        thirdLatestOutputLabel = new Label(thirdLatestOutput, uiSkin, console, Color.BLACK);
        thirdLatestOutputLabel.setWrap(true);
        return thirdLatestOutputLabel;
    }

    public Label createSecondLatestOutputLabel(Skin uiSkin) {
        secondLatestOutputLabel = new Label(secondLatestOutput, uiSkin, console, Color.BLACK);
        secondLatestOutputLabel.setWrap(true);
        return this.secondLatestOutputLabel;
    }

    public Label createLatestOutputLabel(Skin uiSkin) {
        latestOutputLabel = new Label(latestOutput, uiSkin, console, Color.BLACK);
        latestOutputLabel.setWrap(true);
        return latestOutputLabel;
    }

    public void createCupActor(Texture closedCupTexture, Texture openCupTexture, Texture cupLockTexture, Group foreGroundActors, Group backgroundActors) {
        cup.setCupActor(new CupActor(closedCupTexture, openCupTexture, cupLockTexture, foreGroundActors, backgroundActors));
        cup.update();
    }

    // TODO: Can dices only be created after the cup?
    public void createDiceActors(Texture[] diceTextures, Texture diceLockTexture, Group dicesBeforeCupActors, Group dicesUnderCupActors, DiceValueGenerator diceValueGenerator) {
        int middleYForCup = cup.getCupActor().getMiddleYForCup();

        if (leftDice == null) {
            leftDice = new Dice(diceValueGenerator, 6);
        }

        if (middleDice == null) {
            middleDice = new Dice(diceValueGenerator, 4);
        }

        if (rightDice == null) {
            rightDice = new Dice(diceValueGenerator, 3);
        }

        leftDice.createDiceActor(diceTextures, diceLockTexture, DiceLocation.LEFT, dicesBeforeCupActors, dicesUnderCupActors, middleYForCup);
        middleDice.createDiceActor(diceTextures, diceLockTexture, DiceLocation.MIDDLE, dicesBeforeCupActors, dicesUnderCupActors, middleYForCup);
        rightDice.createDiceActor(diceTextures, diceLockTexture, DiceLocation.RIGHT, dicesBeforeCupActors, dicesUnderCupActors, middleYForCup);
    }

    public void addDiceListeners(UserInterface userInterface) {
        leftDice.getDiceActor().addListener(new DiceListener(leftDice, userInterface));
        middleDice.getDiceActor().addListener(new DiceListener(middleDice, userInterface));
        rightDice.getDiceActor().addListener(new DiceListener(rightDice, userInterface));
    }

    public ClickableLabel createAutoButton(Skin uiSkin) {
        autoButton = new ClickableLabel(GameStage.AUTO_BUTTON_LABEL, uiSkin);
        autoButton.setDisabled(true);
        return autoButton;
    }

    public ClickableLabel createCallButton(Skin uiSkin) {
        callButton = new ClickableLabel(GameStage.CALL_BUTTON_LABEL, uiSkin);
        callButton.setDisabled(true);
        return callButton;
    }

    // UI element update methods.
    public void constructPlayers(java.util.List<String> originalPlayers, int numberOfLives) {
        players = new Player[originalPlayers.size()];
        for (int i = 0; i < originalPlayers.size(); i++) {
            players[i] = new Player(originalPlayers.get(i), numberOfLives);
        }
        saveGame();
    }

    public void updatePlayerIterator(int newPlayerIteratorValue) {
        playerIterator = newPlayerIteratorValue;
        saveGame();
    }

    public void currentPlayerLosesLife(boolean canUseBok) {
        getCurrentPlayer().loseLife(canUseBok);
        saveGame();
    }

    public void setBlindPass(boolean value) {
        blindPass = value;
        saveGame();
    }

    public void setCanViewOwnThrow(boolean value) {
        canViewOwnThrow = value;
        saveGame();
    }

    public void setBelieved666(boolean value) {
        believed666 = value;
        saveGame();
    }

    public void setHasToThrow(boolean value) {
        hasToThrow = value;
    }

    public void setHasThrown(boolean value) {
        hasThrown = value;
        saveGame();
    }

    public void setAllowedToBelieveOrNotBelieve(boolean value) {
        allowedToBelieveOrNotBelieve = value;
        saveGame();
    }

    public void setFirstThrowSinceDeath(boolean value) {
        firstThrowSinceDeath = value;
        saveGame();
    }

    public void setBokAvailable(boolean value) {
        bokAvailable = value;
        saveGame();
    }

    public void updateLatestCall(Call newCall) {
        firstThrowSinceDeath = false;
        allowedToCall = false;
        canViewOwnThrow = false;
        allowedToBelieveOrNotBelieve = true;
        latestCall = newCall;
        saveGame();
    }

    public void resetLatestCall() {
        latestCall = null;
        allowedToBelieveOrNotBelieve = false;
        canViewOwnThrow = false;
        believed666 = false;
        hasToThrow = true;
        hasThrown = false;
        saveGame();
    }

    public void allowPlayerToCall(boolean allow) {
        allowedToCall = allow;
        callButton.setDisabled(!allow);
        autoButton.setDisabled(!allow);
        saveGame();
    }

    public void setCallInput(String callInput) {
        this.callInput = callInput;
        callInputField.setText(callInput);
        saveGame();
    }

    public void logGameConsoleMessage(String consoleMessage) {
        checkUiInitializedPreconditions();

        thirdLatestOutput = secondLatestOutput;
        secondLatestOutput = latestOutput;
        latestOutput = consoleMessage;
        updateOutputLabels();
        saveGame();
    }

    public void lockCupAndDicesUnderCup() {
        cup.lock();
        if (leftDice.isUnderCup()) {
            leftDice.lock();
        }
        if (middleDice.isUnderCup()) {
            middleDice.lock();
        }
        if (rightDice.isUnderCup()) {
            rightDice.lock();
        }
    }

    public void unlockCupAndDicesUnderCup() {
        cup.unlock();
        if (leftDice.isUnderCup()) {
            leftDice.unlock();
        }
        if (middleDice.isUnderCup()) {
            middleDice.unlock();
        }
        if (rightDice.isUnderCup()) {
            rightDice.unlock();
        }
    }

    private GameState() {}

    private GameState(Label callInputField, Label thirdLatestOutputLabel, Label secondLatestOutputLabel, Label latestOutputLabel, ClickableLabel autoButton,
                              ClickableLabel callButton, CupActor cupActor, DiceActor leftDiceActor, DiceActor middleDiceActor, DiceActor rightDiceActor) {
        this.callInputField = callInputField;
        this.callInputField.setText(callInput);
        this.thirdLatestOutputLabel = thirdLatestOutputLabel;
        this.secondLatestOutputLabel = secondLatestOutputLabel;
        this.latestOutputLabel = latestOutputLabel;
        this.autoButton = autoButton;
        this.autoButton.setDisabled(!allowedToCall);
        this.callButton = callButton;
        this.callButton.setDisabled(!allowedToCall);
        updateOutputLabels();

        cup.setCupActor(cupActor);
        cup.update();

        int middleYForCup = cup.getCupActor().getMiddleYForCup();
        leftDice.setDiceActor(leftDiceActor, middleYForCup);
        middleDice.setDiceActor(middleDiceActor, middleYForCup);
        rightDice.setDiceActor(rightDiceActor, middleYForCup);

        save();
    }

    private void updateOutputLabels() {
        thirdLatestOutputLabel.setText(thirdLatestOutput);
        secondLatestOutputLabel.setText(secondLatestOutput);
        latestOutputLabel.setText(latestOutput);
    }

    // TODO: Investigate whether this is neccesary.
    public void saveGame() {
        newGameState = false;
        save();
    }

    public static final String SAVED_GAMESTATE = "Saved GameState: ";

    private void save() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
        Gdx.app.log(BluffPokerApp.TAG,SAVED_GAMESTATE + json);
        preferences.putString(GameState.KEY, json);
        preferences.flush();
    }

    /**
     * Singleton with lazy initialization.
     */
    private static GameState instance;

    /**
     * @return The only instantiation of the GameState within the app.
     */
    public static synchronized GameState get() {
        if (instance != null) {
            return instance;
        } else {
            // Load game state if a previous state exists.
            Preferences bluffPokerState = Gdx.app.getPreferences(BluffPokerPreferences.KEY);

            Gson gson = new Gson();
            String stateString = bluffPokerState.getString(GameState.KEY);
            if (Strings.isNullOrEmpty(stateString)) {
                instance = new GameState();
            } else {
                Gdx.app.log(BluffPokerApp.TAG,"Loaded GameState from: " + stateString);
                instance = gson.fromJson(bluffPokerState.getString(GameState.KEY), GameState.class);
            }
            return instance;
        }
    }

    /**
     * Only to be used in tests.
     * @return Json value of the current GameState instance.
     */
    static String getStateString() {
        return (new Gson()).toJson(instance);
    }

    public static synchronized void reset() {
        instance = new GameState(instance.callInputField, instance.thirdLatestOutputLabel, instance.secondLatestOutputLabel, instance.latestOutputLabel, instance.autoButton,
                instance.callButton, instance.cup.getCupActor(), instance.leftDice.getDiceActor(), instance.middleDice.getDiceActor(), instance.rightDice.getDiceActor());
    }

    /**
     * Only to be used in tests.
     */
    static void resetToNull() {
        instance = null;
    }
}
