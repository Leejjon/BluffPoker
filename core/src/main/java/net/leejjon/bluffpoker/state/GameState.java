package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;

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
import net.leejjon.bluffpoker.ui.ScoreTableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton to contain all the state in the game, designed with testability in mind.
 */
public class GameState {
    static final String KEY = "gameState";
    // Font used in console is Microsoft JingHei
    private static final String console = "console";

    private boolean newGameState = true;

    public boolean isNewGameState() {
        return newGameState;
    }

    private void checkUiInitializedPreconditions() {
        Preconditions.checkNotNull(uiSkin);
        Preconditions.checkNotNull(callInputField);
        Preconditions.checkNotNull(thirdLatestOutputLabel);
        Preconditions.checkNotNull(secondLatestOutputLabel);
        Preconditions.checkNotNull(latestOutputLabel);
        Preconditions.checkNotNull(currentPlayerLabel);
        Preconditions.checkNotNull(autoButton);
        Preconditions.checkNotNull(callButton);
        Preconditions.checkNotNull(cup.getCupActor());
        Preconditions.checkNotNull(leftDice.getDiceActor());
        Preconditions.checkNotNull(middleDice.getDiceActor());
        Preconditions.checkNotNull(rightDice.getDiceActor());
        Preconditions.checkNotNull(scoreTable);
    }

    private ArrayList<Player> players;

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private int playerIterator = 0;

    public int getPlayerIterator() {
        return playerIterator;
    }

    private Cup cup = new Cup();

    public Cup getCup() {
        return cup;
    }

    // This is actually 643, but we show
    private Dice leftDice;

    public Dice getLeftDice() {
        return leftDice;
    }

    private Dice middleDice;

    public Dice getMiddleDice() {
        return middleDice;
    }

    private Dice rightDice;

    public Dice getRightDice() {
        return rightDice;
    }

    private Call latestCall = null;

    public Call getLatestCall() {
        return latestCall;
    }

    private String callInput = NumberCombination.MIN.toString();

    public String getCallInput() {
        return callInput;
    }

    String thirdLatestOutput = "";
    String secondLatestOutput = "";
    String latestOutput = "";

    // TODO: Create a statusses object.
    private boolean bokAvailable = true;

    public boolean isBokAvailable() {
        return bokAvailable;
    }

    private boolean firstThrowSinceDeath = true;

    public boolean isFirstThrowSinceDeath() {
        return firstThrowSinceDeath;
    }

    private boolean hasToThrow = true;
    private boolean hasThrown = false;

    public boolean isHasThrown() {
        return hasThrown;
    }

    private boolean allowedToBelieveOrNotBelieve = false;

    public boolean isAllowedToBelieveOrNotBelieve() {
        return allowedToBelieveOrNotBelieve;
    }

    private boolean allowedToViewOwnThrow = false;

    public boolean isAllowedToViewOwnThrow() {
        return allowedToViewOwnThrow;
    }

    private boolean allowedToCall = false;

    public boolean isAllowedToCall() {
        return allowedToCall;
    }

    private boolean believed666 = false;

    public boolean isBelieved666() {
        return believed666;
    }

    private boolean blindPass = false;

    public boolean isBlindPass() {
        return blindPass;
    }

    public void removeCurrentPlayer(String forfeitMessage) {
        Player playerToBeRemoved;
        if (isAllowedToBelieveOrNotBelieve() && !getCup().isBelieving()) {
            playerToBeRemoved = getNextPlayer();
        } else {
            playerToBeRemoved = getCurrentPlayer();
        }

        // Remove the player everywhere in the ui.
        for (ScoreTableRow str : scores) {
            if (str.getPlayerName().equals(playerToBeRemoved.getName())) {
                str.deleteRow(scoreTable);
                scores.remove(str);
                break;
            }
        }

        SelectPlayersStageState.getInstance().removePlayer(playerToBeRemoved.getName());

        // Don't update the current player label, that will happen automatically in the nextPlayer() method of the BluffPokerGame class.
        players.remove(playerToBeRemoved);
        logGameConsoleMessage(String.format(forfeitMessage, playerToBeRemoved.getName())); // This saves.
    }

    // Custom state getter methods
    public Player getCurrentPlayer() {
        return players.get(playerIterator);
    }

    public Player getNextPlayer() {
        Player nextPlayer;

        int localPlayerIterator = getPlayerIterator() + 1;

        // At this point (during placing a call) not all players should be dead.
        do {
            // If the localPlayerIterator runs out of the arrays bounds, we moveUp it to 0.
            if (localPlayerIterator == getPlayers().size()) {
                localPlayerIterator = 0;
            }

            nextPlayer = getPlayers().get(localPlayerIterator);

            localPlayerIterator++;
        } while (nextPlayer.isDead());
        return nextPlayer;
    }

    public boolean isBelievingBlind() { return !blindPass && !hasToThrow && allowedToBelieveOrNotBelieve; }

    public boolean isInitiatingBlindPass() { return blindPass && hasThrown && firstThrowSinceDeath && !allowedToBelieveOrNotBelieve; }

    /**
     * TODO: This method is broken when believing 666.
     * @return
     */
    public boolean bailedOutOfBlindBelieving() { return blindPass && !hasToThrow && !firstThrowSinceDeath && !believed666; }

    public boolean userTriesToLockOrUnlock() { return !blindPass && hasToThrow && !firstThrowSinceDeath; }

    public boolean isAllowedToThrow() {
        if (hasToThrow && !cup.getCupActor().isMoving() && !cup.isBelieving() && !cup.isWatching()) {
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
    private transient Skin uiSkin;
    private transient Label callInputField;
    private transient Label thirdLatestOutputLabel;
    private transient Label secondLatestOutputLabel;
    private transient Label latestOutputLabel;
    private transient Label currentPlayerLabel;

    public Label getCurrentPlayerLabel() {
        return currentPlayerLabel;
    }

    private transient ClickableLabel autoButton;
    private transient ClickableLabel callButton;
    private transient Table scoreTable;

    private transient List<ScoreTableRow> scores = new ArrayList<>();

    public List<ScoreTableRow> getScores() {
        return scores;
    }

    public void setUiSkin(Skin uiSkin) {
        this.uiSkin = uiSkin;
    }

    // UI element initialization methods
    public Label createCallInputFieldLabel(Skin uiSkin) {
        callInputField = new Label(callInput, uiSkin, "arial64", Color.WHITE);
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

    public Label createCurrentPlayerLabel(Skin uiSkin) {
        currentPlayerLabel = new Label("DefaultPlayer", uiSkin, console, Color.BLACK);
        if (players != null) {
            updateCurrentPlayerLabel();
        }
        return currentPlayerLabel;
    }

    public void createCupActor(Texture closedCupTexture, Texture openCupTexture, Texture cupLockTexture, Group foreGroundActors, Group backgroundActors) {
        cup.setCupActor(new CupActor(closedCupTexture, openCupTexture, cupLockTexture, foreGroundActors, backgroundActors));
        cup.update();
    }

    public void createDiceActors(Texture[] diceTextures, Texture diceLockTexture, Group dicesBeforeCupActors, Group dicesUnderCupActors, DiceValueGenerator diceValueGenerator) {
        int middleYForCup = cup.getCupActor().getMiddleYForCup();

        if (leftDice == null) {
            leftDice = new Dice(diceValueGenerator, 6);
        } else {
            leftDice.setDiceValueGenerator(diceValueGenerator);
        }

        if (middleDice == null) {
            middleDice = new Dice(diceValueGenerator, 4);
        } else {
            middleDice.setDiceValueGenerator(diceValueGenerator);
        }

        if (rightDice == null) {
            rightDice = new Dice(diceValueGenerator, 3);
        } else {
            rightDice.setDiceValueGenerator(diceValueGenerator);
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
        autoButton.setDisabled(!allowedToCall);
        return autoButton;
    }

    public ClickableLabel createCallButton(Skin uiSkin) {
        callButton = new ClickableLabel(GameStage.CALL_BUTTON_LABEL, uiSkin);
        callButton.setDisabled(!allowedToCall);
        return callButton;
    }

    // UI element update methods.
    public void constructPlayers(java.util.List<String> originalPlayers, int numberOfLives) {
        players = new ArrayList<>(originalPlayers.size());
        for (int i = 0; i < originalPlayers.size(); i++) {
            players.add(new Player(originalPlayers.get(i), numberOfLives));
        }
        updateCurrentPlayerLabel();
        addPlayerScores();
        saveGame();
    }


    public void updatePlayerIterator() {
        if (state().getPlayerIterator() + 1 < state().getPlayers().size()) {
            state().updatePlayerIterator(state().getPlayerIterator() + 1);
        } else {
            state().updatePlayerIterator(0);
        }
    }

    public void updatePlayerIterator(int newPlayerIteratorValue) {
        playerIterator = newPlayerIteratorValue;

        saveGame();
    }

    public void updateCurrentPlayerLabel() {
        if (isAllowedToBelieveOrNotBelieve() && !getCup().isBelieving()) {
            currentPlayerLabel.setText(getNextPlayer().getName());
        } else {
            currentPlayerLabel.setText(getCurrentPlayer().getName());
        }
    }

    public Table createScores(Label playerNameTitle, Label playerLivesTitle, float defaultPadding, float topBottomPadding) {
        scoreTable = new Table();
        scoreTable.add(playerNameTitle).pad(defaultPadding).padTop(topBottomPadding).padBottom(topBottomPadding);
        scoreTable.add(playerLivesTitle).pad(defaultPadding).padTop(topBottomPadding).padBottom(topBottomPadding);
        addPlayerScores();
        return scoreTable;
    }

    private void addPlayerScores() {
        if (players != null) {
            for (Player player : players) {
                scoreTable.row();
                Cell<Label> nameCell = scoreTable.add(new Label(player.getName(), uiSkin, console, Color.WHITE)).align(Align.left).padLeft(4f);
                Cell<Label> livesCell = scoreTable.add(new Label(Integer.toString(player.getLives()), uiSkin, console, Color.WHITE)).align(Align.left).padLeft(13f);
                scores.add(new ScoreTableRow(nameCell, livesCell));
            }
        } else {
            Gdx.app.log(BluffPokerApp.TAG, "Attempting to load scores while players list is null.");
        }
    }

    public void currentPlayerLosesLife(boolean canUseBok) {
        getCurrentPlayer().loseLife(canUseBok);
        for (ScoreTableRow s : scores) {
            if (s.getPlayerName().equals(getCurrentPlayer().getName())) {
                s.loseLife(canUseBok);
            }
        }
        saveGame();
    }

    public void setBlindPass(boolean value) {
        blindPass = value;
        saveGame();
    }

    public void setAllowedToViewOwnThrow(boolean value) {
        allowedToViewOwnThrow = value;
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

    public void setBokAvailable(boolean value) {
        bokAvailable = value;
        saveGame();
    }

    private void updateLatestCall(Call newCall) {
        firstThrowSinceDeath = false;
        allowedToCall = false;
        allowedToViewOwnThrow = false;
        allowedToBelieveOrNotBelieve = true;
        latestCall = newCall;
    }

    public void resetLatestCall() {
        firstThrowSinceDeath = true;
        latestCall = null;
        allowedToBelieveOrNotBelieve = false;
        allowedToViewOwnThrow = false;
        believed666 = false;
        hasToThrow = true;
        hasThrown = false;
        updateCurrentPlayerLabel();
        saveGame();
    }

    @Deprecated
    public void allowPlayerToCallWithSave(boolean allow) {
        allowPlayerToCall(allow);
        saveGame();
    }

    public void allowPlayerToCall(boolean allow) {
        allowedToCall = allow;
        callButton.setDisabled(!allow);
        autoButton.setDisabled(!allow);
    }

    private void setCallInput(String callInput) {
        this.callInput = callInput;
        callInputField.setText(callInput);
    }

    public void setCallInputWithSave(String callInput) {
        setCallInput(callInput);
        saveGame();
    }

    public void resetCallInput() {
        setCallInput(NumberCombination.MIN.toString());
        saveGame();
    }

    public void submitCall(String callInput, Call call) {
        getCup().unlock();
        allowPlayerToCall(false);
        setCallInput(callInput);
        updateLatestCall(call);
        updateCurrentPlayerLabel();
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
        cup.unlockWithSave();
        if (leftDice.isUnderCup()) {
            leftDice.unlockWithSave();
        }
        if (middleDice.isUnderCup()) {
            middleDice.unlockWithSave();
        }
        if (rightDice.isUnderCup()) {
            rightDice.unlockWithSave();
        }
    }

    private GameState() {

    }

    private GameState(Skin uiSkin, Label callInputField, Label thirdLatestOutputLabel, Label secondLatestOutputLabel, Label latestOutputLabel, Label currentPlayerLabel, ClickableLabel autoButton,
                              ClickableLabel callButton, CupActor cupActor, DiceActor leftDiceActor, DiceActor middleDiceActor, DiceActor rightDiceActor, Table scoreTable, List<ScoreTableRow> scores) {
        this.uiSkin = uiSkin;
        this.callInputField = callInputField;
        this.callInputField.setText(callInput);
        this.thirdLatestOutputLabel = thirdLatestOutputLabel;
        this.secondLatestOutputLabel = secondLatestOutputLabel;
        this.latestOutputLabel = latestOutputLabel;
        this.currentPlayerLabel = currentPlayerLabel;
        this.autoButton = autoButton;
        this.autoButton.setDisabled(!allowedToCall);
        this.callButton = callButton;
        this.callButton.setDisabled(!allowedToCall);
        this.scoreTable = scoreTable;

        for (ScoreTableRow s : scores) {
            s.deleteRow(scoreTable);
        }
        this.scores = new ArrayList<>();

        updateOutputLabels();

        cup.setCupActor(cupActor);
        cup.update();

        int middleYForCup = cup.getCupActor().getMiddleYForCup();

        leftDice = new Dice(new DiceValueGenerator() {}, 6);
        leftDice.setDiceActor(leftDiceActor, middleYForCup);
        middleDice = new Dice(new DiceValueGenerator() {}, 4);
        middleDice.setDiceActor(middleDiceActor, middleYForCup);
        rightDice = new Dice(new DiceValueGenerator() {}, 3);
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
    public static synchronized GameState state() {
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
        instance = new GameState(instance.uiSkin, instance.callInputField, instance.thirdLatestOutputLabel, instance.secondLatestOutputLabel, instance.latestOutputLabel, instance.currentPlayerLabel, instance.autoButton,
                instance.callButton, instance.cup.getCupActor(), instance.leftDice.getDiceActor(), instance.middleDice.getDiceActor(), instance.rightDice.getDiceActor(), instance.scoreTable, instance.scores);
    }

    /**
     * Only to be used in tests.
     */
    static void resetToNull() {
        instance = null;
    }

    /**
     * Call this method once a player has won the game.
     */
    public void markAsNewGame() {
        newGameState = true;
        save();
    }
}
