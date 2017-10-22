package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.enums.TutorialMessage;
import net.leejjon.bluffpoker.state.Settings;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TutorialDialog extends Dialog {
    private Label label;
    private CheckBox disableTutorialCheckbox;
    private TextButton okButton;
    private Settings settings;
    private Stage lastStage = null;

    private Queue<TutorialMessageWithArguments> tutorialMessageQueue = new ConcurrentLinkedQueue<>();
    private AtomicBoolean displayingTutorialMessage = new AtomicBoolean(false);

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
                boolean comesFromOkButton = actor == okButton;

                if (comesFromOkButton) {
                    if (disableTutorialCheckbox.isChecked() && settings.isTutorialMode()) {
                        tutorialMessageQueue.clear();
                        displayingTutorialMessage.set(false);

                        settings.setTutorialMode(false);
                        hideDisableTutorialCheckBox();
                        showTutorialMessage(new TutorialMessageWithArguments(lastStage, TutorialMessage.DISABLED_TUTORIAL, new String[]{}));
                    } else {
                        // Because the user just pressed the Ok button which will close this dialog,
                        // We can safely start showing the next one.
                        showNextTutorialMessageFromQueue(true);
                    }
                }
            }
        });
    }

    public void addToTutorialMessageQueue(Stage stage, TutorialMessage message, String ... parameters) {
        if (settings.isTutorialMode()) {
            tutorialMessageQueue.add(new TutorialMessageWithArguments(stage, message, parameters));

            if (displayingTutorialMessage.compareAndSet(false, true)) {
                showNextTutorialMessageFromQueue(false);
            }
        }
    }

    private void showNextTutorialMessageFromQueue(boolean replacePrevious) {
        if (displayingTutorialMessage.get() && replacePrevious) {
            tutorialMessageQueue.remove();
        }

        TutorialMessageWithArguments nextTutorialMessage = tutorialMessageQueue.peek();

        if (nextTutorialMessage != null) {
            showDisableTutorialCheckBox();
            showTutorialMessage(nextTutorialMessage);
        } else {
            displayingTutorialMessage.set(false);
        }
    }

    private void showTutorialMessage(TutorialMessageWithArguments nextTutorialMessage) {
        this.lastStage = nextTutorialMessage.getStage();

        getContentTable().clearChildren();
        getContentTable().padTop(padding);

        label.setText(String.format(nextTutorialMessage.getTutorialMessage().getMessage(), (Object[]) nextTutorialMessage.getParameters()));

        getContentTable().add(label).width(getFiftyPercentOfScreen());
        show(nextTutorialMessage.getStage());
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

    @AllArgsConstructor
    private static class TutorialMessageWithArguments {
        @Getter private Stage stage;
        @Getter private TutorialMessage tutorialMessage;
        @Getter private String[] parameters;
    }
}
