package net.leejjon.bluffpoker.state;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.enums.TextureTestLoader;
import net.leejjon.bluffpoker.interfaces.PlatformSpecificInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import net.leejjon.bluffpoker.ui.ClickableLabel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.util.Set;

import static org.mockito.Mockito.mock;

/**
 * Credits to this method for setting up tests:
 * http://manabreak.eu/java/2016/10/21/unittesting-libgdx.html
 */
public class GdxTest {
    private static Application application;
    protected static Preferences preferences;
    protected static Array<String> logMessages = new Array<>();
    protected ObjectMap<TextureKey, Texture> textureMap;
    protected Skin uiSkin;
    protected Sound diceRoll;

    protected Label thirdLatestOutputLabel;
    protected Label secondLatestOutputLabel;
    protected Label latestOutputLabel;
    protected Label callInputLabel;
    protected ClickableLabel autoButton;
    protected ClickableLabel callButton;
    protected Group dicesBeforeCupActors;
    protected Group dicesUnderCupActors;

    @BeforeClass
    public static void init() {
        // Note that we don't need to implement any of the listener's methods
        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {
            }
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        }) {
            @Override
            public Preferences getPreferences(String name) {
                return preferences;
            }

            @Override
            public void log(String tag, String message) {
                logMessages.add(message);
            }

            @Override
            public void log(String tag, String message, Throwable exception) {
                logMessages.add(message);
            }
        };

//        /**
//         * Give the mocked graphics some width and height so we don't get divide by zero errors.
//         */
//        MockGraphics myMockedGraphics = new MockGraphics() {
//            @Override
//            public int getWidth() {
//                return 480;
//            }
//
//            @Override
//            public int getHeight() {
//                return 320;
//            }
//        };
//
//        Gdx.graphics = myMockedGraphics;

        // Set platformSpecificInterface to avoid null pointers.
        new BluffPokerGame(new PlatformSpecificInterface() {
            @Override
            public String getDeviceOwnerName() {
                return "Leejjon";
            }

            @Override
            public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {

            }

            @Override
            public int getZoomFactor() {
                return 1;
            }
        });

        // Use Mockito to mock the OpenGL methods since we are running headlessly
        Gdx.gl20 = mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    @Before
    public void setup() {
        preferences = mock(Preferences.class);

        // Use the os specific file separator.
        String relativePath = String.format("..%1$sandroid%1$ssrc%1$smain%1$sassets%1$s", File.separator);
        // Use the default libgdx UI skin.
        uiSkin = new Skin(Gdx.files.internal(relativePath + "uiskin.json"));
        uiSkin.addRegions(new TextureAtlas(relativePath + "uiskin.atlas"));
        diceRoll = Gdx.audio.newSound(Gdx.files.internal(relativePath + "sound/diceroll.mp3"));

        textureMap = TextureTestLoader.getAllTexturesForInUnitTest(relativePath);

        SelectPlayersStageState.resetSingletonInstance();
    }


    protected void initializeUI(GameState gameState) {
        Texture closedCupTexture = textureMap.get(TextureKey.CLOSED_CUP);
        Texture openCupTexture = textureMap.get(TextureKey.OPEN_CUP);
        Texture diceLockTexture = textureMap.get(TextureKey.DICE_LOCK);
        Texture cupLockTexture = textureMap.get(TextureKey.CUP_LOCK);

        // Load the textures of the dices.
        Texture dice1 = textureMap.get(TextureKey.DICE1);
        Texture dice2 = textureMap.get(TextureKey.DICE2);
        Texture dice3 = textureMap.get(TextureKey.DICE3);
        Texture dice4 = textureMap.get(TextureKey.DICE4);
        Texture dice5 = textureMap.get(TextureKey.DICE5);
        Texture dice6 = textureMap.get(TextureKey.DICE6);

        Texture[] diceTextures = new Texture[] {dice1, dice2, dice3, dice4, dice5, dice6};

        Group foreGroundActors = new Group();
        Group backgroundActors = new Group();
        dicesBeforeCupActors = new Group();
        dicesUnderCupActors = new Group();

        gameState.createCallInputFieldLabel(uiSkin);
        thirdLatestOutputLabel = gameState.createThirdLatestOutputLabel(uiSkin);
        secondLatestOutputLabel = gameState.createSecondLatestOutputLabel(uiSkin);
        latestOutputLabel = gameState.createLatestOutputLabel(uiSkin);
        callInputLabel = gameState.createCallInputFieldLabel(uiSkin);
        gameState.createCupActor(closedCupTexture, openCupTexture, cupLockTexture, foreGroundActors, backgroundActors);
        autoButton = gameState.createAutoButton(uiSkin);
        callButton = gameState.createCallButton(uiSkin);
        gameState.createDiceActors(diceTextures, diceLockTexture, dicesBeforeCupActors, dicesUnderCupActors);
    }


    @After
    public void reset() {
        logMessages.clear();
        uiSkin.dispose();
        diceRoll.dispose();
        for (Texture t : textureMap.values()) {
            t.dispose();
        }
        GameState.resetToNull();
    }

    @AfterClass
    public static void cleanUp() {
        // Exit the application first
        application.exit();
        application = null;
    }
}
