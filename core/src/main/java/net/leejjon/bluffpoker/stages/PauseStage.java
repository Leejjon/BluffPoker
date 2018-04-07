package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.StageInterface;

public class PauseStage extends AbstractStage {
    private int rightSideOfMenuX;

    private float backgroundAlpha = 0.5f;
    private ShapeRenderer screenDimmerRenderer;

    public PauseStage(StageInterface stageInterface, Skin skin) {
        super(false);

        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.MENU_COLOR)));
//        TextureRegionDrawable screenDimmerDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.SCREEN_DIMMER)));

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
        table.setX(0f);

        screenDimmerRenderer = new ShapeRenderer();

        addActor(table);
    }

    @Override
    public void draw() {
        super.draw();
        if (isVisible()) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            screenDimmerRenderer.begin(ShapeRenderer.ShapeType.Filled);
            screenDimmerRenderer.setColor(0f, 0f, 0f, backgroundAlpha);
            // All Scene2D related
            screenDimmerRenderer.rect(rightSideOfMenuX, 0, Gdx.graphics.getWidth() - rightSideOfMenuX, Gdx.graphics.getHeight());
            screenDimmerRenderer.end();
        }
    }

    @Override
    public void dispose() {
        screenDimmerRenderer.dispose();
        super.dispose();
    }

    public void setRightSideOfMenuX(int x) {
        if (x <= getRawMenuWidth()) {
            this.rightSideOfMenuX = x;
            table.setX((rightSideOfMenuX / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()) - getMenuWidth());
        }
    }

    public static int getMenuWidth() {
        int width = Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int oneSixthOfWidth = width / 6;
        return (width / 2) + oneSixthOfWidth;
    }

    public static int getRawMenuWidth() {
        int width = Gdx.graphics.getWidth();
        int oneSixthOfWidth = width / 6;
        return (width / 2) + oneSixthOfWidth;
    }
}
