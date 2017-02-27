package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.actors.BlackBoard;
import net.leejjon.bluffpoker.assets.Textures;
import net.leejjon.bluffpoker.dialogs.AddNewPlayerDialog;
import net.leejjon.bluffpoker.dialogs.WarningDialog;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.StageInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.ArrayList;
import java.util.Collections;

public class SelectPlayersStage extends AbstractStage implements ModifyPlayerListener {
    private java.util.List<String> players;
    private List<String> playerList;

    private WarningDialog playerAlreadyExistsWarning;
    private WarningDialog playerNameInvalid;
    private WarningDialog minimalTwoPlayersRequired;

    public SelectPlayersStage(Skin uiSkin, final StageInterface stageInterface, ContactsRequesterInterface contactsRequester) {
        super(false);

        playerAlreadyExistsWarning = new WarningDialog(uiSkin);
        playerNameInvalid = new WarningDialog("Player name too long!", uiSkin);
        minimalTwoPlayersRequired = new WarningDialog("Select at least two players!", uiSkin);
        final AddNewPlayerDialog addNewPlayerDialog = new AddNewPlayerDialog(this);

        players = new ArrayList<>();

        players.add(contactsRequester.getDeviceOwnerName());

//        playerList = new List<>(uiSkin);
        List.ListStyle ls = uiSkin.get(List.ListStyle.class);
        ls.background = new Image(new Texture(getBackground())).getDrawable();
        playerList = new List<>(ls);
        playerList.setItems(players.toArray(new String[players.size()]));

        Texture callBoardTexture = stageInterface.getAsset(Textures.CALL_BOARD);
        BlackBoard choosePlayersBackground = new BlackBoard(callBoardTexture);

        Label chooseLabel = new Label("Choose", uiSkin, "arial32", Color.WHITE);
        Label playersLabel = new Label("Players", uiSkin, "arial32", Color.WHITE);

        float padding = 10f;

        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.center();
        topTable.top();
        topTable.add(chooseLabel).colspan(2).padTop(chooseLabel.getHeight() - padding).padBottom(padding);
        topTable.row();
        topTable.add(playersLabel).colspan(2);
        topTable.row();

        ScrollPane playersScrollPane = new ScrollPane(playerList, uiSkin);
        playersScrollPane.setScrollingDisabled(true, false);

        int width = Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis();
        int height = Gdx.graphics.getHeight() / BluffPokerGame.getDivideScreenByThis();

        table.center();
        table.bottom();
        // Take 50% of the screen.
        table.add(playersScrollPane).colspan(2).width((width * 100) / 170)
                .height((height * 100) / 200)
                .padBottom(padding);
        table.row();

        TextButton up = new TextButton("Move up", uiSkin);
        up.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerUp();
            }
        });
        TextButton down = new TextButton("Move down", uiSkin);
        down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                swapPlayerDown();
            }
        });
        TextButton delete = new TextButton("Remove", uiSkin);
        delete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeSelectedPlayer();
            }
        });
        TextButton enterNew = new TextButton("Enter new", uiSkin);
        enterNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(addNewPlayerDialog, "Insert new player name", "", "Enter name here.");
            }
        });
        TextButton phonebook = new TextButton("Phonebook", uiSkin);
        phonebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                contactsRequester.initiateSelectContacts(SelectPlayersStage.this);
            }
        });
        TextButton startGame = new TextButton("Start game", uiSkin);
        startGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame(stageInterface);
            }
        });

        table.add(up).left().width(down.getWidth()).padBottom(padding / 2);
        table.add(enterNew).right().width(phonebook.getWidth()).padBottom(padding / 2);
        table.row();
        table.add(down).left().padBottom(padding / 2);
        table.add(phonebook).right().padBottom(padding / 2);
        table.row();
        table.add(delete).left().width(down.getWidth());
        table.add(startGame).right().width(phonebook.getWidth());

        addActor(choosePlayersBackground);
        addActor(topTable);
        addActor(table);
    }

    private Pixmap getBackground() {
        Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        backgroundPixmap.setColor(0.25f,0.25f,0.25f, 1f);
        backgroundPixmap.fill();
        return backgroundPixmap;
    }

    protected void startGame(StageInterface changeScreen) {
        if (players.size() < 2) {
            minimalTwoPlayersRequired.show(this);
        } else {
            changeScreen.startGame(players);
        }
    }

    @Override
    public void addNewPlayer(String ... playerNames) {
        final int maxNameLength = 16;

        for (String playerName : playerNames) {
            if (playerName.length() > maxNameLength && playerName.contains(" ")) {
                final String[] split = playerName.split("\\s+");
                playerName = split[0];

                // TODO: Add first letter of last name maybe? will suck if people have a lot of initials...
            }

            if (playerName.length() > 0 && playerName.length() <= maxNameLength) {
                if (!players.contains(playerName)) {
                    players.add(playerName);
                } else {
                    playerAlreadyExistsWarning.setRuntimeSpecificWarning("Player " + playerName + " already exists.");
                    playerAlreadyExistsWarning.show(this);
                }
            } else {
                playerNameInvalid.show(this);
            }
        }

        // Update the actual UI list with the new players.
        playerList.setItems(players.toArray(new String[players.size()]));
    }

    private void swapPlayerUp() {
        int selectedIndex = playerList.getSelectedIndex();
        if (selectedIndex > 0) {
            Collections.swap(players, selectedIndex, selectedIndex - 1);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }

    private void swapPlayerDown() {
        int selectedIndex = playerList.getSelectedIndex();
        if (selectedIndex > -1 && selectedIndex < players.size() - 1 && players.size() > 1) {
            Collections.swap(players, selectedIndex, selectedIndex + 1);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }

    private void removeSelectedPlayer() {
        String selectedPlayer = playerList.getSelected();
        if (selectedPlayer != null) {
            players.remove(selectedPlayer);
            playerList.setItems(players.toArray(new String[players.size()]));
        }
    }
}
