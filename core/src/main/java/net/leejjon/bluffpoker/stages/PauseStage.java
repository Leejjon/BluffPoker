package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import net.leejjon.bluffpoker.BluffPokerApp;
import net.leejjon.bluffpoker.actions.ClosePauseMenuAction;
import net.leejjon.bluffpoker.actions.OpenPauseMenuAction;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.state.GameState;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;

import static net.leejjon.bluffpoker.state.GameState.state;

public class PauseStage extends AbstractStage implements PauseStageInterface {
    private StageInterface stageInterface;

    @Getter
    private float rightSideOfMenuX;

    private float backgroundAlpha = 0f;
    private final float maxBackgroundAlpha = 0.5f;
    private ShapeRenderer screenDimmerRenderer;

    private AtomicBoolean pauseMenuGestureActivated = new AtomicBoolean(false);
    private AtomicBoolean openPauseMenuActionRunning = new AtomicBoolean(false);
    private AtomicBoolean closePauseMenuActionRunning = new AtomicBoolean(false);
    private AtomicBoolean pauseMenuOpen = new AtomicBoolean(false);

    public PauseStage(StageInterface stageInterface, Skin skin) {
        super(false);
        this.stageInterface = stageInterface;

        TextureRegionDrawable topMenuBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.MENU_TOP_COLOR)));
        TextureRegionDrawable menuBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.MENU_COLOR)));

        int height = Gdx.graphics.getHeight() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();

        final float defaultPadding = 3f;
        final float borderPadding = 7f;
        final float topBottomPadding = 12f;
        final String console = "console";

        Table menuTop = new Table();
        menuTop.top();
        menuTop.left();
        menuTop.setBackground(topMenuBackgroundDrawable);

        Label turnLabel = new Label("Current turn:", skin, console, Color.BLACK);
        menuTop.add(turnLabel).align(Align.left).pad(defaultPadding).pad(defaultPadding).padTop(topBottomPadding).padLeft(borderPadding);
        menuTop.add(state().createCurrentPlayerLabel(skin)).pad(defaultPadding).padLeft(borderPadding).padTop(topBottomPadding).align(Align.right);
        menuTop.row();

        Color greyFontColor = new Color(0.75f, 0.75f, 0.75f, 1);

        TextButton forfeitButton = new TextButton("Forfeit", skin, "menu");
        forfeitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(BluffPokerApp.TAG, "click");
                super.clicked(event, x, y);
            }
        });
        TextButton endGameButton = new TextButton("Quit", skin, "menu");
        endGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log(BluffPokerApp.TAG, "click");
                super.clicked(event, x, y);
            }
        });

        menuTop.add(forfeitButton).pad(defaultPadding).padBottom(topBottomPadding).align(Align.right);
        menuTop.add(endGameButton).pad(defaultPadding).padBottom(topBottomPadding).align(Align.right);

        Label playerLabel = new Label("Players", skin, "arial32", greyFontColor);
        Label livesLabel = new Label(" Lives", skin, "arial32", greyFontColor);

        Table scores = state().createScores(skin, playerLabel, livesLabel, defaultPadding, topBottomPadding, greyFontColor);
        scores.center();
        scores.top();
        scores.setBackground(menuBackgroundDrawable);

        table.left();
        table.top();
        table.add(menuTop).width(getMenuWidth());
        table.row();
        table.add(scores).width(getMenuWidth()).height(height);

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
            screenDimmerRenderer.rect(rightSideOfMenuX, 0, Gdx.graphics.getWidth() - rightSideOfMenuX, Gdx.graphics.getHeight());
            screenDimmerRenderer.end();
        }
    }

    @Override
    public void dispose() {
        screenDimmerRenderer.dispose();
        super.dispose();
    }

    @Override
    public void setRightSideOfMenuX(float x) {
        if (x <= getRawMenuWidth()) {
            this.rightSideOfMenuX = x;
            this.backgroundAlpha = calculateAlpha(x);
            table.setX((rightSideOfMenuX / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()) - getMenuWidth());
        }
    }

    private float calculateAlpha(float x) {
        float alpha = 0;

        if (x > 0) {
            final float ratio = maxBackgroundAlpha * x;
            alpha = ratio / getRawMenuWidth();
        }

        return alpha;
    }

    @Override
    public void continueOpeningPauseMenu() {
        // TODO: Cancel out any close menu actions.
        if (openPauseMenuActionRunning.compareAndSet(false, true)) {
            Gdx.app.log(BluffPokerApp.TAG, "Adding open menu action.");
            addAction(new OpenPauseMenuAction(this));
        } else {
            Gdx.app.log(BluffPokerApp.TAG, "Attempted to open pause menu while an openPauseMenuAction was already running.");
        }
    }

    @Override
    public void continueClosingPauseMenu() {
        // TODO: Cancel out any close menu actions.
        if (closePauseMenuActionRunning.compareAndSet(false, true)) {
            addAction(new ClosePauseMenuAction(this));
        } else {
            Gdx.app.log(BluffPokerApp.TAG, "Attempted to close pause menu while a closePauseMenuAction was already running.");
        }
    }

    @Override
    public boolean activatePauseMenuGesture() {
        return pauseMenuGestureActivated.weakCompareAndSet(false, true);
    }

    @Override
    public boolean isPauseMenuGestureActivated() {
        return pauseMenuGestureActivated.get();
    }

    @Override
    public boolean isMenuOpen() {
        return pauseMenuOpen.get();
    }

    @Override
    public boolean hasOpenPauseMenuActionRunning() {
        return openPauseMenuActionRunning.get();
    }

    @Override
    public boolean hasClosePauseMenuActionRunning() {
        return closePauseMenuActionRunning.get();
    }

    @Override
    public void doneOpeningPauseMenu() {
        setRightSideOfMenuX(PauseStage.getRawMenuWidth());
        openPauseMenuActionRunning.set(false);
        pauseMenuGestureActivated.set(false);
        pauseMenuOpen.set(true);
        stageInterface.openPauseScreen();
        Gdx.app.log(BluffPokerApp.TAG, "Done opening method called.");
    }

    @Override
    public void doneClosingPauseMenu() {
        closePauseMenuActionRunning.set(false);
        pauseMenuGestureActivated.set(false);
        setRightSideOfMenuX(0);
        pauseMenuOpen.set(false);
        stageInterface.closePauseScreen();
        Gdx.app.log(BluffPokerApp.TAG, "Done closing method called.");
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
