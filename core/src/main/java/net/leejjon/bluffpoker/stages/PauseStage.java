package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.actors.BlackBoard;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.StageInterface;

public class PauseStage extends AbstractStage {
    private StageInterface stageInterface;

    public PauseStage(StageInterface stageInterface, Skin skin) {
        super(false);
        this.stageInterface = stageInterface;

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.MENU_COLOR)));

        int width = Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int height = Gdx.graphics.getHeight() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int oneSixthOfWidth = width / 6;
        final int perfectMenuWidth = (width / 2) + oneSixthOfWidth;

        Table menuTable = new Table();
        menuTable.top();
        menuTable.setBackground(backgroundDrawable);
        menuTable.row();
        menuTable.add(new Label("Hoi", skin));

        table.left();
        table.add(menuTable).width(perfectMenuWidth).height(height);

        Texture screenDimmer = stageInterface.getTexture(TextureKey.SCREEN_DIMMER);

        addActor(new Image(screenDimmer));
        addActor(table);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // If the click is in the dimmed screen area, close.
                if (x > perfectMenuWidth) {
                    stageInterface.closePauseScreen();
                }
            }
        });
    }
}
