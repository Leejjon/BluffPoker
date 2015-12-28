package net.leejjon.blufpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.blufpoker.listener.ChangeStageListener;
import net.leejjon.blufpoker.listener.UserInterface;
import net.leejjon.blufpoker.stages.GameInputInterface;

/**
 * Created by Leejjon on 4-12-2015.
 */
public class WinnerDialog extends Dialog {
    public WinnerDialog(final ChangeStageListener stageListener, final UserInterface userInterface, Skin skin) {
        super("We have a winner!", skin);
        button("Restart").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                userInterface.restart();
            }
        });
        button("Back").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageListener.backToStartStage();
            }
        });
    }

    public void setWinner(String winner) {
        text("Congratulations, " + winner + ", has won the game!");
    }
 }
