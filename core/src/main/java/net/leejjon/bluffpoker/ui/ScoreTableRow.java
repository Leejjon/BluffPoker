package net.leejjon.bluffpoker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.leejjon.bluffpoker.BluffPokerApp;

public class ScoreTableRow {
    public ScoreTableRow(Cell<Label> playerLives, Cell<Label> playerName) {
        this.playerLives = playerLives;
        this.playerName = playerName;
    }

    public static final String ON_THE_BOK_ICON = "0*";

    private final Cell<Label> playerName;
    private final Cell<Label> playerLives;

    public void deleteRow(Table table) {
        table.removeActor(playerName.getActor());
        table.removeActor(playerLives.getActor());
    }

    public void loseLife(boolean canUseBok) {
        if (getPlayerLives().equals(ON_THE_BOK_ICON)) {
            playerLives.getActor().setText("0");
        } else if (Integer.parseInt(getPlayerLives()) > 1) {
            int lives = Integer.parseInt(getPlayerLives()) - 1;
            playerLives.getActor().setText(Integer.toString(lives));
        } else if (Integer.parseInt(getPlayerLives()) == 1) {
            if (canUseBok) {
                playerLives.getActor().setText(ON_THE_BOK_ICON);
            } else {
                playerLives.getActor().setText("0");
            }
        } else {
            Gdx.app.log(BluffPokerApp.TAG, "You cannot kill that which has no life: " + playerName);
        }
    }

    public String getPlayerName() {
        return playerName.getActor().getText().toString();
    }

    public String getPlayerLives() {
        return playerLives.getActor().getText().toString();
    }
}
