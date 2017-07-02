package net.leejjon.bluffpoker;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ObjectMap;
import net.leejjon.bluffpoker.assets.TextureKey;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.interfaces.StageInterface;
import net.leejjon.bluffpoker.listener.PhoneInputListener;
import net.leejjon.bluffpoker.state.GameState;
import net.leejjon.bluffpoker.stages.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import net.leejjon.bluffpoker.state.Settings;

public class BluffPokerGame extends ApplicationAdapter implements
        StageInterface, PhoneInputListener {
    private GameState state;
    private Settings settings;
    private Skin uiSkin;

    private StartStage startMenuStage;
    private SelectPlayersStage selectPlayersStage;
    private SettingsStage settingsStage;
    private GameStage gameStage;

    private ContactsRequesterInterface contactsRequester;

    private ObjectMap<TextureKey, Texture> textureMap;

    private static int divideScreenByThis;

    public BluffPokerGame(ContactsRequesterInterface contactsRequester, int divideScreenByThis) {
        this.contactsRequester = contactsRequester;
        this.divideScreenByThis = divideScreenByThis;
    }

    @Override
    public void create() {
        state = GameState.getGameState();
        settings = Settings.getSettings();

        // Use the default libgdx UI skin.
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        uiSkin.addRegions(new TextureAtlas("uiskin.atlas"));

        textureMap = TextureKey.getAllTextures();

        // Create the stages.
        startMenuStage = new StartStage(uiSkin, this);
        settingsStage = new SettingsStage(uiSkin, this, settings);
        selectPlayersStage = new SelectPlayersStage(uiSkin, this, contactsRequester);
        gameStage = new GameStage(uiSkin, this);

        // Make sure touch input goes to the startStage.
        Gdx.input.setInputProcessor(startMenuStage);
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
        selectPlayersStage.setVisible(true);
        Gdx.input.setInputProcessor(selectPlayersStage);
    }

    @Override
    public void continuePlaying() {
        startMenuStage.setVisible(false);

        gameStage.setVisible(true);
    }

    @Override
    public void openSettingsStage() {
        startMenuStage.setVisible(false);
        settingsStage.loadLatestSettings();
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
    public void startGame(List<String> players) {
        selectPlayersStage.setVisible(false);
        gameStage.startGame(players, settings);
        gameStage.setVisible(true);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public Texture getTexture(TextureKey textureKey) {
        return textureMap.get(textureKey);
    }

    @Override
    public void shakePhone() {
        try {
            if (gameStage.isVisible()) {
                gameStage.shake();
            }
        } catch (NullPointerException e) {
            Gdx.app.log("BLUFFPOKER", "GameStage is null, why??", e);
        }
    }

    public static int getDivideScreenByThis() {
        return divideScreenByThis;
    }
}
