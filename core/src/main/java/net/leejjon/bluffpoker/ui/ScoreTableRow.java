package net.leejjon.bluffpoker.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ScoreTableRow {
    private Cell<Label> playerName;
    private Cell<Label> playerLives;

    public void deleteRow(Table table) {
        table.removeActor(playerName.getActor());
        table.removeActor(playerLives.getActor());
    }
}
