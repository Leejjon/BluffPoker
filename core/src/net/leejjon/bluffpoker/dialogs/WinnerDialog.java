package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.bluffpoker.listener.StageInterface;
import net.leejjon.bluffpoker.listener.UserInterface;

/**
 * Created by Leejjon on 4-12-2015.
 */
public class WinnerDialog extends Dialog {
    public WinnerDialog(final StageInterface stageListener, final UserInterface userInterface, Skin skin) {
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
        text(winner + ", has won the game!");
    }
 }
