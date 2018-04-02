package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.StageInterface;

public class PauseStage extends AbstractStage {
    public PauseStage(StageInterface stageInterface, Skin skin) {
        super(false);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.MENU_COLOR)));

        int width = Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int height = Gdx.graphics.getHeight() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();

        final float defaultPadding = 3f;
        final String console = "console";

        Table menuTable = new Table();
        menuTable.top();
        menuTable.left();
        menuTable.setBackground(backgroundDrawable);

        menuTable.row();
        menuTable.add(new Label("Players", skin, console, Color.BLACK)).pad(defaultPadding);
        menuTable.add(new Label("Lives", skin, console, Color.BLACK)).pad(defaultPadding);
        menuTable.row();

        menuTable.add(new Label("Leejjon", skin, console, Color.BLACK)).pad(defaultPadding);
        menuTable.add(new Label("1337", skin, console, Color.BLACK)).pad(defaultPadding);

        menuTable.row();
        TextButton forfeitButton = new TextButton("Forfeit", skin);
        forfeitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(BluffPokerApp.TAG, "click");
                super.clicked(event, x, y);
            }
        });

        menuTable.add(forfeitButton);

        table.left();
        table.add(menuTable).width(getMenuWidth()).height(height)
                .padTop(BluffPokerApp.getPlatformSpecificInterface().getTopPadding())
                .padBottom(BluffPokerApp.getPlatformSpecificInterface().getBottomPadding());

        Image screenDimmer = new Image(stageInterface.getTexture(TextureKey.SCREEN_DIMMER));
        screenDimmer.setPosition(getMenuWidth(), 0);
        screenDimmer.addListener(new ActorGestureListener() {
            /**
             * Override touch up instead of clicked because we want any gesture there to close the pause screen.
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                stageInterface.closePauseScreen();
            }
        });

        addActor(screenDimmer);
        addActor(table);

    }

    public static int getMenuWidth() {
        int width = Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int oneSixthOfWidth = width / 6;
        return (width / 2) + oneSixthOfWidth;
    }
}
