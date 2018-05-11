package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Preconditions;

import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.stages.SelectPlayersStage;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.state.SelectPlayersStageState;

import java.util.ArrayList;

public class WinnerDialog extends Dialog {
    private String winnerName = null;

    public WinnerDialog(final StageInterface stageListener, Skin skin) {
        super("We have a winner!", skin);
        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preconditions.checkNotNull(winnerName, "This window should not have opened without a winner being set.");
                GameState.reset();

                // Some logic to make the winner of the previous game start the next one.
                if (SelectPlayersStageState.getInstance().getPlayers().size() > 1) {
                    stageListener.startGame(SelectPlayersStageState.updatePlayerListMoveWinnerOnTop(winnerName));
                } else {
                    stageListener.backToSelectPlayersStage();
                }
            }
        });
        button(restartButton);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Some logic to make the winner of the previous game start the next one.
                SelectPlayersStageState.updatePlayerListMoveWinnerOnTop(winnerName);
                GameState.reset();
                stageListener.backToStartStage();
            }
        });
        button(backButton);
    }

    public void setWinner(String winner) {
        winnerName = winner;
        getContentTable().clearChildren();
        text(winner + ", has won the game!");
    }
 }
