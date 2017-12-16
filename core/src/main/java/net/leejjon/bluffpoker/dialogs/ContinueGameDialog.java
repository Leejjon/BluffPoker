package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.state.GameState;

public class ContinueGameDialog extends Dialog {
    private final float padding = 5f;

    public ContinueGameDialog(Skin skin, StageInterface stageInterface) {
        super("Continue", skin);

        Label gameInProgressLabel = new Label("We found a game in progress. Do you want to continue playing it?", skin, "consoleLabel");
        gameInProgressLabel.setAlignment(Align.center);
        gameInProgressLabel.setWrap(true);

        getContentTable().padTop(padding);
        getContentTable().add(gameInProgressLabel).width(get80PercentOfScreen());

        TextButton yesButton = new TextButton("Yes", skin);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageInterface.continuePlaying();
            }
        });

        TextButton noButton = new TextButton("No", skin);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameState.resetInstance();
            }
        });

        button(yesButton);
        button(noButton);
    }

    private float get80PercentOfScreen() {
        return ((Gdx.graphics.getWidth() / BluffPokerGame.getPlatformSpecificInterface().getZoomFactor()) * 100) / 120;
    }
}
