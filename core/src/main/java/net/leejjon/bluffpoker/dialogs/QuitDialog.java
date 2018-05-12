package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;

public class QuitDialog extends Dialog {
    private final PauseStageInterface pauseStageInterface;
    public QuitDialog(Skin skin, PauseStageInterface pauseStageInterface, StageInterface stageInterface) {
        super("Quit game", skin);
        this.pauseStageInterface = pauseStageInterface;

        TextButton yesButton = new TextButton("Yes", skin);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseStageInterface.closeDialog();
                pauseStageInterface.continueClosingPauseMenu(false);
                stageInterface.backToStartStage();
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

        text("Are you sure you want to quit the game?");
        button(yesButton);
        button(noButton);
    }

    @Override
    public Dialog show(Stage stage) {
        pauseStageInterface.openDialog();
        return super.show(stage);
    }
}
