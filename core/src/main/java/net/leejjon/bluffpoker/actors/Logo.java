package net.leejjon.bluffpoker.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.stages.GameStage;

public class Logo extends Table {
    private Image logo;

    public Logo(Texture logoTexture) {
        setFillParent(false);
        logo = new Image(logoTexture);

        int middleX = (GameStage.getMiddleX() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor());
        int firstQuarter = (GameStage.getMiddleY() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor());
        setPosition(middleX, firstQuarter);

        add(logo).width(logoTexture.getWidth() / 4).height(logoTexture.getHeight() / 4).padBottom(10f);
        row();
    }
}
