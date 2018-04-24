package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import net.leejjon.bluffpoker.dialogs.QuitDialog;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.PauseStageInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;

import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Getter;

import static net.leejjon.bluffpoker.state.GameState.state;

public class PauseStage extends AbstractStage implements PauseStageInterface {
    private StageInterface stageInterface;
    private QuitDialog quitDialog;

    @Getter
    private float rightSideOfMenuX;

    private float backgroundAlpha = 0f;
    private final float maxBackgroundAlpha = 0.5f;
    private ShapeRenderer screenDimmerRenderer;
    private Table bottomTable;

    private AtomicBoolean pauseMenuGestureActivated = new AtomicBoolean(false);
    private AtomicBoolean openPauseMenuActionRunning = new AtomicBoolean(false);
    private AtomicBoolean closePauseMenuActionRunning = new AtomicBoolean(false);
    private AtomicBoolean pauseMenuOpen = new AtomicBoolean(false);
    private AtomicBoolean dialogOpen = new AtomicBoolean(false);

    public PauseStage(StageInterface stageInterface, Skin skin) {
        super(false);
        this.stageInterface = stageInterface;
        quitDialog = new QuitDialog(skin, this, stageInterface);

        TextureRegionDrawable bottomMenuDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.CURRENT_TURN_COLOR)));
        TextureRegionDrawable scoreBoardMenuDrawable = new TextureRegionDrawable(new TextureRegion(stageInterface.getTexture(TextureKey.SCOREBOARD_COLOR)));

        int height = Gdx.graphics.getHeight() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();

        final float defaultPadding = 3f;
        final float borderPadding = 7f;
        final float topBottomPadding = 12f;
        final String console = "console";

        Table menuTop = new Table();
        menuTop.top();
        menuTop.left();
        menuTop.setBackground(bottomMenuDrawable);

        Label turnLabel = new Label("Current turn:", skin, console, Color.BLACK);
        menuTop.add(turnLabel).align(Align.left).pad(defaultPadding).pad(defaultPadding).padTop(topBottomPadding).padLeft(borderPadding);
        menuTop.add(state().createCurrentPlayerLabel(skin)).pad(defaultPadding).padLeft(borderPadding).padTop(topBottomPadding).align(Align.right);
        menuTop.row();

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
                quitDialog.show(PauseStage.this);
            }
        });

        menuTop.add(forfeitButton).pad(defaultPadding).padBottom(topBottomPadding).align(Align.right);
        menuTop.add(endGameButton).pad(defaultPadding).padBottom(topBottomPadding).align(Align.right);

        Label playerLabel = new Label("Players", skin, "arial32", Color.WHITE);
        Label livesLabel = new Label(" Lives", skin, "arial32", Color.WHITE);

        Table scores = state().createScores(playerLabel, livesLabel, defaultPadding, topBottomPadding);
        scores.center();
        scores.top();
        scores.setBackground(scoreBoardMenuDrawable);
        scores.padBottom(topBottomPadding);

        Table middleTable = new Table();
        middleTable.setBackground(scoreBoardMenuDrawable);

        table.left();
        table.top();
        table.add(scores).width(getMenuWidth());
        table.row();
        table.add(middleTable).width(getMenuWidth()).height(height);

        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.left();
        bottomTable.bottom();
        bottomTable.add(menuTop).width(getMenuWidth());

        table.setX(0f);
        bottomTable.setX(0f);

        addActor(table);
        addActor(bottomTable);

        screenDimmerRenderer = new ShapeRenderer();
    }

    @Override
    public void draw() {
        // TODO: Find a better solution than the dialogOpen boolean.
        // When a dialog is open we don't want the transparant background to draw over it.
        if (dialogOpen.get()) {
            drawTransparantBackground();
        }
        super.draw();
        // When animations occur and we draw transparancy before the stage, you get a glitch, so we draw after.
        if (!dialogOpen.get()) {
            drawTransparantBackground();
        }
    }

    private void drawTransparantBackground() {
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
            float menuX = (rightSideOfMenuX / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor()) - getMenuWidth();
            table.setX(menuX);
            bottomTable.setX(menuX);
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
    public void continueClosingPauseMenu(boolean changeInputListener) {
        // TODO: Cancel out any close menu actions.
        if (closePauseMenuActionRunning.compareAndSet(false, true)) {
            addAction(new ClosePauseMenuAction(this, changeInputListener));
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
    public void doneClosingPauseMenu(boolean changeInputListener) {
        closePauseMenuActionRunning.set(false);
        pauseMenuGestureActivated.set(false);
        setRightSideOfMenuX(0);
        pauseMenuOpen.set(false);
        setVisible(false);

        if (changeInputListener) {
            stageInterface.closePauseScreen();
        }
        Gdx.app.log(BluffPokerApp.TAG, "Done closing method called.");
    }

    @Override
    public void openDialog() {
        dialogOpen.set(true);
    }

    @Override
    public void closeDialog() {
        dialogOpen.set(false);
    }

    public static int getMenuWidth() {
        int width = Gdx.graphics.getWidth() / BluffPokerApp.getPlatformSpecificInterface().getZoomFactor();
        int oneSixthOfWidth = width / 6;
        return (width / 2) + oneSixthOfWidth;
    }

//    public static int getTopMenuHeight() {
//        int height = Gdx.graphics.getHeight();
//    }

    public static int getRawMenuWidth() {
        int width = Gdx.graphics.getWidth();
        int oneSixthOfWidth = width / 6;
        return (width / 2) + oneSixthOfWidth;
    }
}
