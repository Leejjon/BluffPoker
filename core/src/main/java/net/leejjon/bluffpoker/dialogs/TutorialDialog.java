package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.logic.NumberCombination;
import net.leejjon.bluffpoker.state.Settings;

public class TutorialDialog extends Dialog {
    private Label label;
    private CheckBox disableTutorialCheckbox;
    private TextButton okButton;
    private Settings settings;
    private Stage lastStage = null;

    private final float padding = 5f;
    public TutorialDialog(Skin uiSkin, Settings settings) {
        super("Tutorial", uiSkin);
        this.settings = settings;

        label = new Label("", uiSkin, "consoleLabel");
        label.setAlignment(Align.center);
        label.setWrap(true);

        disableTutorialCheckbox = new CheckBox(" Disable tutorial", uiSkin, "big");
        okButton = new TextButton("Ok", uiSkin);

        // Just add the listener. The buttons will be added in the showDisableTutorialCheckBox() or hideDisableTutorialCheckBox() methods.
        getButtonTable().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (settings.isTutorialMode()) {
                    boolean comesFromOkButton = actor == okButton;
                    Gdx.app.log(BluffPokerGame.TAG, "Actor is okButton" + comesFromOkButton);
                    if (disableTutorialCheckbox.isChecked() && comesFromOkButton && lastStage != null) {
                        showTutorialMessage(lastStage, TutorialMessage.DISABLED_TUTORIAL);
                    }
                }
            }
        });
    }

    public void showTutorialMessage(Stage stage, TutorialMessage message) {
        this.lastStage = stage;

        if (settings.isTutorialMode()) {
            if (message == TutorialMessage.DISABLED_TUTORIAL) {
                settings.setTutorialMode(false);
                hideDisableTutorialCheckBox();
            } else {
                showDisableTutorialCheckBox();
            }

            getContentTable().clearChildren();
            getContentTable().padTop(padding);

            label.setText(message.getMessage());

            getContentTable().add(label).width(getFiftyPercentOfScreen());
            show(stage);
        }
    }

    private float getFiftyPercentOfScreen() {
        return ((Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis()) * 100) / 120;
    }

    private void showDisableTutorialCheckBox() {
        getButtonTable().clearChildren();
        disableTutorialCheckbox.setChecked(false);
        getButtonTable().add(disableTutorialCheckbox.padLeft(padding).padRight(padding));
        getButtonTable().row();
        button(okButton);
    }

    private void hideDisableTutorialCheckBox() {
        getButtonTable().clearChildren();
        button(okButton);
    }
}
