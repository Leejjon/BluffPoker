package net.leejjon.bluffpoker.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import net.leejjon.bluffpoker.BluffPokerGame;

public class CreditsDialog extends Dialog {
    public CreditsDialog(Skin skin) {
        super("Credits", skin);

        Label creditsLabel = new Label("\nCreated and developed by: \nLeejjon\n\n" +
                "Icon and logo designed by: \nAstrid Glas\n\n" +
                "Special thanks to Dirk aka Stofkat for borrowing me a phone and programming help.\n\n" +
                "Special thanks to the following people for playing this with me: Barry, Billy, Blao, King Charles, Rik and Shadowing\n", skin, "consoleLabel");
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setWrap(true);
        getContentTable().add(creditsLabel).width(get80PercentOfScreen());
        button("Ok");
    }

    private float get80PercentOfScreen() {
        return ((Gdx.graphics.getWidth() / BluffPokerGame.getDivideScreenByThis()) * 100) / 120;
    }
}
