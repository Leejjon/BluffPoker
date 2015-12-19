package net.leejjon.blufpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.leejjon.blufpoker.listener.UserInterface;

/**
 * Created by Leejjon on 4-12-2015.
 */
public class WinnerDialog extends Dialog {
    public WinnerDialog(Skin skin) {
        super("We have a winner!", skin);
        button("Restart");
        button("Back");
    }

    public void setWinner(String winner) {
        text("Congratulations, " + winner + ", has won the game!");
    }
 }
