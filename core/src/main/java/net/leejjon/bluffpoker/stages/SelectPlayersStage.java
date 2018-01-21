package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.actors.BlackBoard;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.dialogs.AddNewPlayerDialog;
import net.leejjon.bluffpoker.dialogs.PlayersFromPhonebookDialog;
import net.leejjon.bluffpoker.dialogs.TutorialDialog;
import net.leejjon.bluffpoker.dialogs.WarningDialog;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import net.leejjon.bluffpoker.state.SelectPlayersStageState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class SelectPlayersStage extends AbstractStage implements ModifyPlayerListener {
    public static final int MAX_PLAYER_NAME_LENGTH = 10;

    private final TutorialDialog tutorialDialog;
    private final WarningDialog playerAlreadyExistsWarning;
    private final WarningDialog playerNameInvalid;
    private final WarningDialog minimalTwoPlayersRequired;
    private final PlayersFromPhonebookDialog playersFromPhonebookDialog;

    private AtomicBoolean orderingHintShown = new AtomicBoolean(false);
    private SelectPlayersStageState state;

    public SelectPlayersStage(Skin uiSkin, TutorialDialog tutorialDialog, final StageInterface stageInterface) {
        super(false);
        this.tutorialDialog = tutorialDialog;

        state = SelectPlayersStageState.getInstance();
        playerAlreadyExistsWarning = new WarningDialog(uiSkin);
        playerNameInvalid = new WarningDialog("Player name empty or too long!", uiSkin);
        minimalTwoPlayersRequired = new WarningDialog("Select at least two players!", uiSkin);
        final AddNewPlayerDialog addNewPlayerDialog = new AddNewPlayerDialog(this);
        playersFromPhonebookDialog = new PlayersFromPhonebookDialog(uiSkin, this);

        if (state.getPlayers().isEmpty()) {
            ArrayList<String> players = new ArrayList<>();
            players.add(BluffPokerGame.getPlatformSpecificInterface().getDeviceOwnerName());
            state.setPlayers(players);
        }


        Texture callBoardTexture = stageInterface.getTexture(TextureKey.CALL_BOARD);
        BlackBoard choosePlayersBackground = new BlackBoard(callBoardTexture);

        Label chooseLabel = new Label("Choose", uiSkin, "arial32", Color.WHITE);
        Label playersLabel = new Label("players", uiSkin, "arial32", Color.WHITE);

        Table topTable = new Table();
        topTable.center();
        topTable.top();

        float middleX = (GameStage.getMiddleX() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - ((topTable.getWidth() / 2) / 2);
        float topY = (GameStage.getTopY() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) - (topTable.getHeight() / 2);

        topTable.setPosition(middleX, topY);

        final float padding = 7f;

        topTable.add(chooseLabel).colspan(2).padTop(chooseLabel.getHeight() - padding).padBottom(padding);
        topTable.row();
        topTable.add(playersLabel).colspan(2);

        ScrollPane playersScrollPane = new ScrollPane(state.createPlayerList(getCustomListStyle(uiSkin)), uiSkin);
        playersScrollPane.setScrollingDisabled(true, false);

        int width = Gdx.graphics.getWidth() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor();
        int height = Gdx.graphics.getHeight() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor();

        table.center();
        table.bottom();

        // Take 50% of the screen.
        float fiftyPercentOfScreen = (width * 100) / 170;
        table.add(playersScrollPane).width(fiftyPercentOfScreen)
                .height((height * 100) / 200)
                .padBottom(padding);
        table.row();

        ImageButton up = new ImageButton(uiSkin, "up");
        up.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerUp();
            }
        });
        ImageButton down = new ImageButton(uiSkin, "down");
        down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerDown();
            }
        });
        ImageButton remove = new ImageButton(uiSkin, "minus");
        remove.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeSelectedPlayer();
            }
        });
        ImageButton addButton = new ImageButton(uiSkin, "plus");
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(addNewPlayerDialog, "Insert new player name", "", "Enter name here.");
            }
        });
        ImageButton phonebook = new ImageButton(uiSkin, "phonebook");
        phonebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BluffPokerGame.getPlatformSpecificInterface().initiateSelectContacts(SelectPlayersStage.this, new TreeSet<>(state.getPlayers()));
            }
        });
        TextButton startGame = new TextButton("Start game", uiSkin);
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(stageInterface);
            }
        });

        Table buttonTable = new Table();
        Cell<ImageButton> upCell = buttonTable.add(up).height(startGame.getHeight());
        Cell<ImageButton> downCell = buttonTable.add(down).height(startGame.getHeight());
        Cell<ImageButton> addCell = buttonTable.add(addButton).right().height(startGame.getHeight());
        Cell<ImageButton> removeCell = buttonTable.add(remove).right().height(startGame.getHeight());
        Cell<ImageButton> phoneBookCell = buttonTable.add(phonebook).right().height(startGame.getHeight());

        // Calculate the padding to make sure the buttons are nicely alligned with the list.
        float widthOfAllButtons = upCell.getMinWidth() + downCell.getMinWidth() + addCell.getMinWidth() + removeCell.getMinWidth() + phoneBookCell.getMinWidth();
        float buttonPadding = (fiftyPercentOfScreen - (widthOfAllButtons)) / 8;

        upCell.padRight(buttonPadding);
        downCell.padRight(buttonPadding * 5);
        removeCell.padLeft(buttonPadding);
        phoneBookCell.padLeft(buttonPadding);

        table.add(buttonTable).width(fiftyPercentOfScreen).center();
        table.row();
        table.add(startGame).center().pad(padding * 4);

        addActor(choosePlayersBackground);
        addActor(topTable);
        addActor(table);
    }

    static List.ListStyle getCustomListStyle(Skin uiSkin) {
        List.ListStyle ls = uiSkin.get(List.ListStyle.class);
        ls.selection = addBordersToTextArea(ls.selection);
        ls.fontColorSelected = new Color(1f, 1f, 1f, 1.0f);
        return ls;
    }

    private static Drawable addBordersToTextArea(Drawable drawable) {
        drawable.setLeftWidth(2f);
        drawable.setTopHeight(2f);
        drawable.setBottomHeight(2f);
        return drawable;
    }

    public void startSelectingPlayers() {
        setVisible(true);
        Gdx.input.setInputProcessor(this);
        tutorialDialog.addToTutorialMessageQueue(this, TutorialMessage.PLAYER_EXPLANATION);
    }

    private void startGame(StageInterface changeScreen) {
        if (state.getPlayers().size() < 2) {
            minimalTwoPlayersRequired.show(this);
        } else {
            orderingHintShown.set(false);
            changeScreen.startGame(state.getPlayers());
        }
    }

    private void swapPlayerUp() {
        int selectedIndex = state.getPlayerList().getSelectedIndex();
        if (selectedIndex > 0) {
            ArrayList<String> players = state.getPlayers();
            Collections.swap(players, selectedIndex, selectedIndex - 1);
            state.setPlayers(players);
        }
    }

    private void swapPlayerDown() {
        int selectedIndex = state.getPlayerList().getSelectedIndex();
        ArrayList<String> players = state.getPlayers();
        if (selectedIndex > -1 && selectedIndex < players.size() - 1 && players.size() > 1) {
            Collections.swap(players, selectedIndex, selectedIndex + 1);
            state.setPlayers(players);
        }
    }

    private void removeSelectedPlayer() {
        String selectedPlayer = state.getPlayerList().getSelected();
        if (selectedPlayer != null) {
            ArrayList<String> players = state.getPlayers();
            players.remove(selectedPlayer);
            state.setPlayers(players);
        }
    }

    private void addPlayersToGame(String ... playerNames) {
        ArrayList<String> players = state.getPlayers();
        for (String playerName : playerNames) {

            String trimmedPlayerName = playerName.trim();
            // If a playerName is to long and contains spaces split it up.
            if (trimmedPlayerName.length() > MAX_PLAYER_NAME_LENGTH && trimmedPlayerName.substring(0,10).contains(" ")) {
                trimmedPlayerName = cutOffPlayerName(trimmedPlayerName, players);
            } else if (trimmedPlayerName.length() > MAX_PLAYER_NAME_LENGTH && !trimmedPlayerName.substring(0,10).contains(" ")) {
                trimmedPlayerName = trimmedPlayerName.substring(0,10);
            }

            if (!trimmedPlayerName.isEmpty() && trimmedPlayerName.length() <= MAX_PLAYER_NAME_LENGTH) {
                if (!players.contains(trimmedPlayerName)) {
                    if (players.size() == 2 && orderingHintShown.compareAndSet(false, true)) {
                        tutorialDialog.addToTutorialMessageQueue(this, TutorialMessage.ORDERING_PLAYERS);
                    }
                    players.add(trimmedPlayerName);
                } else {
                    playerAlreadyExistsWarning.setRuntimeSpecificWarning("Player " + trimmedPlayerName + " already exists.");
                    playerAlreadyExistsWarning.show(this);
                }
            } else {
                playerNameInvalid.show(this);
            }
        }

        // Update the actual UI list with the new players.
        state.setPlayers(players);
    }

    static String cutOffPlayerName(final String playerName, java.util.List<String> players) {
        final String[] parts = playerName.split("\\s+");

        String shorterPlayerName = "";
        // Why < MAX_PLAYER_NAME_LENGTH - 1? Because if the name is "12345678 X Y", the name will become "12345678 X".
        // If I didn't put the -1, the last character will always be a space and is not worth adding.
        for (int i = 0; i < parts.length && shorterPlayerName.length() < MAX_PLAYER_NAME_LENGTH - 1 && (i == 0 || players.contains(shorterPlayerName)); i++) {
            // If it's not the first part, add a space.
            if (i > 0) {
                shorterPlayerName += " ";
            }

            String potentialPlayerName = shorterPlayerName + parts[i];
            String potentialPlayerNameCutOff = shorterPlayerName + parts[i].charAt(0);

            if (potentialPlayerName.length() <= MAX_PLAYER_NAME_LENGTH) { // If the potentialPlayerName fits within the limit, use that.
                shorterPlayerName = potentialPlayerName;
            } else { // If the potentialPlayerName doesn't fit, use the cut off version.
                shorterPlayerName = potentialPlayerNameCutOff;
            }
        }
        return shorterPlayerName;
    }

    @Override
    public void addContactsToGame(String... playerNames) {
        addPlayersToGame(playerNames);
    }

    @Override
    public void loadFromPhonebook(String ... phoneBookContactNames) {
        playersFromPhonebookDialog.addNewPlayer(phoneBookContactNames);
    }

    @Override
    public void showPhonebookDialog() {
        playersFromPhonebookDialog.show(this);
    }
}
