package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.leejjon.bluffpoker.interfaces.GameInputInterface;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.UserInterface;

public class ForfeitDialog extends Dialog {
    private final PauseStageInterface pauseStageInterface;

    public ForfeitDialog(Skin skin, PauseStageInterface pauseStageInterface, UserInterface userInterface) {
        super("Forfeit", skin);
        this.pauseStageInterface = pauseStageInterface;

        TextButton yesButton = new TextButton("Yes", skin);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseStageInterface.closeDialog();
                pauseStageInterface.continueClosingPauseMenu(true);
                userInterface.forfeit();
            }
        });

        TextButton noButton = new TextButton("No", skin);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseStageInterface.closeDialog();
                super.clicked(event, x, y);
            }
        });

        text(String.format("Are you sure you want to forfeit?"));
        button(yesButton);
        button(noButton);
    }

    @Override
    public Dialog show(Stage stage) {
        pauseStageInterface.openDialog();
        return super.show(stage);
    }
}
