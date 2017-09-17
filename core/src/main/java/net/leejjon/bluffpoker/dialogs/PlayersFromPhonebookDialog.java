package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayersFromPhonebookDialog extends Dialog {
    private Map<String, CheckBox> players = new HashMap<>();
    private Table checkBoxPerPhonebookEntryTable;
    private Skin uiSkin;

    public PlayersFromPhonebookDialog(Skin uiSkin, final ModifyPlayerListener playerListener) {
        super("Select players from phonebook", uiSkin, "dialog");
        this.uiSkin = uiSkin;

        Table contentTable = getContentTable();
        contentTable.center();

        checkBoxPerPhonebookEntryTable = new Table();
        checkBoxPerPhonebookEntryTable.top();
        checkBoxPerPhonebookEntryTable.left();

        ScrollPane playersScrollPane = new ScrollPane(checkBoxPerPhonebookEntryTable, uiSkin);

        // Take 50% of the screen.
        int width = Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis();
        int height = Gdx.graphics.getHeight() / BluffPokerGame.getDivideScreenByThis();
        contentTable.add(playersScrollPane).width((width * 100) / 155).height((height * 100) / 200).padTop(3f).padRight(1f);

        final TextButton add = new TextButton("Add", uiSkin);
        add.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Set<String> playerNames = new HashSet<>();
                for (Map.Entry<String, CheckBox> player : players.entrySet()) {
                    if (player.getValue().isChecked() && !playerNames.contains(player.getKey())) {
                        playerNames.add(player.getKey());
                    }
                }
                playerListener.addContactsToGame(playerNames.toArray(new String[playerNames.size()]));
            }
        });
        button(add);
        button(new TextButton("Cancel", uiSkin));

        contentTable.row().padBottom(10f);
    }

    public void addNewPlayer(Array<String> playerNames) {
        // This code seemed to be the culprit for the "libc++abi.dylib: terminating with uncaught exception of type ObjCException"....
        // TODO: Isolate it in a separate project.
//        for (CheckBox box : players.values()) {
//            checkBoxPerPhonebookEntryTable.removeActor(box);
//        }
//        players.clear();

        for (String playerName : playerNames) {
            if (!players.containsKey(playerName)) {
                CheckBox checkBox = new CheckBox(playerName, uiSkin, "big");
                checkBox.setChecked(false);
                players.put(playerName, checkBox);
                checkBoxPerPhonebookEntryTable.add(checkBox).left();
                checkBoxPerPhonebookEntryTable.row();
            } else {
                CheckBox checkBox = players.get(playerName);
                checkBox.setChecked(false);
            }
        }
    }
}
