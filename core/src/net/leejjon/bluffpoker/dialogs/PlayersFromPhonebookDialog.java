package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import net.leejjon.bluffpoker.BluffPokerGame;

public class PlayersFromPhonebookDialog extends Dialog {
    public PlayersFromPhonebookDialog(Skin uiSkin) {
        super("Select players from phonebook", uiSkin, "dialog");

        Table contentTable = getContentTable();
        contentTable.center();

//        List.ListStyle ls = uiSkin.get(List.ListStyle.class);
//        ls.selection = addBordersToTextArea(ls.selection);
//        ls.fontColorSelected = new Color(1f, 1f, 1f, 1.0f);
//        List<String> playerList = new List<>(ls);
//        playerList.setItems("Leon", "Richard");
//
        Table checkBoxPerPhonebookEntryTable = new Table();
        checkBoxPerPhonebookEntryTable.left();
        CheckBox checkBox = new CheckBox("Leon", uiSkin, "big");
        checkBoxPerPhonebookEntryTable.add(checkBox);
        ScrollPane playersScrollPane = new ScrollPane(checkBoxPerPhonebookEntryTable, uiSkin);
//
//        // Take 50% of the screen.
        int width = Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis();
        int height = Gdx.graphics.getHeight() / BluffPokerGame.getDivideScreenByThis();
        contentTable.add(playersScrollPane).width((width * 100) / 155).height((height * 100) / 200).padTop(3f).padRight(1f);

        getButtonTable().add(new TextButton("Add", uiSkin)).padBottom(5f);
        getButtonTable().add(new TextButton("Cancel", uiSkin)).padBottom(5f);
    }
}
