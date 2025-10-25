package net.leejjon.bluffpoker;

import java.util.ArrayList;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.dialogs.TutorialDialog;
import net.leejjon.bluffpoker.interfaces.PlatformSpecificInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.listener.ClosePauseMenuListener;
import net.leejjon.bluffpoker.listener.FancyOpenPauseMenuListener;
import net.leejjon.bluffpoker.listener.PhoneInputListener;
import net.leejjon.bluffpoker.stages.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static net.leejjon.bluffpoker.state.GameState.state;

public class BluffPokerApp extends ApplicationAdapter implements
        StageInterface, PhoneInputListener {
    public static final String TAG = "bluffpoker";
    private Skin uiSkin;

    private StartStage startMenuStage;
    private SelectPlayersStage selectPlayersStage;
    private SettingsStage settingsStage;
    private GameStage gameStage;
    private PauseStage pauseStage;

    private InputMultiplexer gameInput;
    private InputMultiplexer pauseMenuInput;

    private FancyOpenPauseMenuListener openPauseMenuListener;

    // Made it static because on iOS the zoomfactor cannot be calculated before the create method is initiated.
    private static PlatformSpecificInterface platformSpecificInterface;

    private ObjectMap<TextureKey, Texture> textureMap;

    public BluffPokerApp(PlatformSpecificInterface platformSpecificInterface) {
        BluffPokerApp.platformSpecificInterface = platformSpecificInterface;
    }

    @Override
    public void create() {
        // Use the default libgdx UI skin.
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));

        textureMap = TextureKey.getAllTextures();

        state().setUiSkin(uiSkin);

        TutorialDialog tutorialDialog = new TutorialDialog(uiSkin);

        // Create the stages.
        startMenuStage = new StartStage(uiSkin, this);
        settingsStage = new SettingsStage(uiSkin, this);
        selectPlayersStage = new SelectPlayersStage(uiSkin, tutorialDialog,this);
        gameStage = new GameStage(uiSkin, tutorialDialog, this);
        pauseStage = new PauseStage(this, gameStage, uiSkin);

        // Make sure touch input goes to the startStage.
        Gdx.input.setInputProcessor(startMenuStage);

        openPauseMenuListener = new FancyOpenPauseMenuListener(this, pauseStage);

        gameInput = new InputMultiplexer();
        gameInput.addProcessor(gameStage);
        gameInput.addProcessor(openPauseMenuListener);

        pauseMenuInput = new InputMultiplexer();
        pauseMenuInput.addProcessor(new ClosePauseMenuListener(pauseStage));
        pauseMenuInput.addProcessor(pauseStage);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.29f, 0.47f, 0.33f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // The stages will figure out by themselves if they need to be drawn or not.
        startMenuStage.draw();
        settingsStage.draw();
        selectPlayersStage.draw();
        gameStage.draw();
        pauseStage.draw();
    }

    @Override
    public void dispose() {
        startMenuStage.dispose();
        settingsStage.dispose();
        selectPlayersStage.dispose();
        gameStage.dispose();
        uiSkin.dispose();
        for (Texture t : textureMap.values()) {
            t.dispose();
        }
    }

    @Override
    public void startSelectingPlayersToPlayWith() {
        startMenuStage.setVisible(false);
        selectPlayersStage.startSelectingPlayers();
    }

    @Override
    public void continuePlaying() {
        startMenuStage.setVisible(false);
        gameStage.setVisible(true);
        gameStage.continueGame();
        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void openSettingsStage() {
        startMenuStage.setVisible(false);
        settingsStage.setVisible(true);
        Gdx.input.setInputProcessor(settingsStage);
    }

    @Override
    public void closeSettingsStage() {
        settingsStage.setVisible(false);
        startMenuStage.setVisible(true);
        Gdx.input.setInputProcessor(startMenuStage);
    }

    @Override
    public void backToStartStage() {
        gameStage.setVisible(false);
        startMenuStage.setVisible(true);
        Gdx.input.setInputProcessor(startMenuStage);
    }

    @Override
    public void backToSelectPlayersStage() {
        gameStage.setVisible(false);
        selectPlayersStage.startSelectingPlayers();
    }

    @Override
    public void startGame(ArrayList<String> players) {
        selectPlayersStage.setVisible(false);
        gameStage.setVisible(true);
        gameStage.startGame(players);
        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public Texture getTexture(TextureKey textureKey) {
        return textureMap.get(textureKey);
    }

    @Override
    public void startOpeningPauseScreen(int x) {
        pauseStage.setVisible(true);
        Gdx.input.setInputProcessor(openPauseMenuListener);
        pauseStage.setRightSideOfMenuX(x);
    }

    @Override
    public void openPauseScreen() {
        Gdx.input.setInputProcessor(pauseMenuInput);
    }

    @Override
    public void closePauseScreen() {
        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void shakePhone() {
        try {
            if (gameStage.isVisible()) {
                gameStage.shake();
            }
        } catch (NullPointerException e) {
            Gdx.app.log("bluffpoker", "GameStage is null, why??", e);
        }
    }

    public static PlatformSpecificInterface getPlatformSpecificInterface() {
        return platformSpecificInterface;
    }
}
