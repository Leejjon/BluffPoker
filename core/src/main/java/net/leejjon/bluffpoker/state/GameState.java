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

import net.leejjon.bluffpoker.actors.CupActor;
import net.leejjon.bluffpoker.logic.BluffPokerPreferences;
import net.leejjon.bluffpoker.logic.Call;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.logic.Player;
import net.leejjon.bluffpoker.stages.GameStage;
import net.leejjon.bluffpoker.ui.ClickableLabel;

/**
 * A singleton to contain all the state in the game, designed with testability in mind.
 */
public class GameState {
    public static final String KEY = "gameState";
    // Font used in console is Microsoft JingHei
    public static final String console = "console";

    @Getter
    private boolean newGameState = true;

    private void checkUiInitializedPreconditions() {
        Preconditions.checkNotNull(callInputField);
        Preconditions.checkNotNull(thirdLatestOutputLabel);
        Preconditions.checkNotNull(secondLatestOutputLabel);
        Preconditions.checkNotNull(latestOutputLabel);
    }

    // Actual state
    @Getter
    private Player[] players;

    @Getter
    private int playerIterator = 0;

    @Getter
    private Cup cup = new Cup();

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

    public ClickableLabel createAutoButton(Skin uiSkin) {
        autoButton = new ClickableLabel(GameStage.AUTO_BUTTON_LABEL, uiSkin);
        return autoButton;
    }

    public ClickableLabel createCallButton(Skin uiSkin) {
        callButton = new ClickableLabel(GameStage.CALL_BUTTON_LABEL, uiSkin);
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

    private GameState() {}

    private GameState(Label callInputField, Label thirdLatestOutputLabel, Label secondLatestOutputLabel, Label latestOutputLabel, CupActor cupActor) {
        this.callInputField = callInputField;
        this.callInputField.setText(callInput);
        this.thirdLatestOutputLabel = thirdLatestOutputLabel;
        this.secondLatestOutputLabel = secondLatestOutputLabel;
        this.latestOutputLabel = latestOutputLabel;
        updateOutputLabels();

        cup.setCupActor(cupActor);
        cup.update();

        save();
    }

    private void updateOutputLabels() {
        thirdLatestOutputLabel.setText(thirdLatestOutput);
        secondLatestOutputLabel.setText(secondLatestOutput);
        latestOutputLabel.setText(latestOutput);
    }

    public void saveGame() {
        newGameState = false;
        save();
    }

    private void save() {
        Gson gson = new Gson();
        Preferences preferences = Gdx.app.getPreferences(BluffPokerPreferences.KEY);
        String json = gson.toJson(this);
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
                instance = gson.fromJson(bluffPokerState.getString(GameState.KEY), GameState.class);
            }
            return instance;
        }
    }

    public static synchronized void reset() {
        instance = new GameState(instance.callInputField, instance.thirdLatestOutputLabel, instance.secondLatestOutputLabel, instance.latestOutputLabel, instance.cup.getCupActor());
    }
}
