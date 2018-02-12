package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.interfaces.UserInterface;
import net.leejjon.bluffpoker.state.GameState;

public class WinnerDialog extends Dialog {
    public WinnerDialog(final StageInterface stageListener, Skin skin) {
        super("We have a winner!", skin);
        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameState.reset();
            }
        });
        button(restartButton);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageListener.backToStartStage();
            }
        });
        button(backButton);
    }

    public void setWinner(String winner) {
        getContentTable().clearChildren();
        text(winner + ", has won the game!");
    }
 }
