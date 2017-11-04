package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.actors.Logo;
import net.leejjon.bluffpoker.dialogs.CreditsDialog;
import net.leejjon.bluffpoker.enums.TextureKey;
import net.leejjon.bluffpoker.interfaces.StageInterface;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.xml.soap.Text;

public class StartStage extends AbstractStage {
	public StartStage(Skin uiSkin, final StageInterface stageInterface, boolean tablet) {
		super(true);

        Texture logoTexture = stageInterface.getTexture(TextureKey.LOGO);
        Logo logo = new Logo(logoTexture);
        // Calculate the positions for the logo and the table.
        int middleX = (GameStage.getMiddleX() / BluffPokerGame.getDivideScreenByThis() - ((logoTexture.getWidth() / 4) / 2));
        int firstQuarter = (GameStage.getMiddleY() / BluffPokerGame.getDivideScreenByThis()) - ((logoTexture.getHeight()) / 4) / 4;
        float thirdQuarter = (GameStage.getMiddleY() / BluffPokerGame.getDivideScreenByThis()) - (((logoTexture.getHeight()) / 4) * 1.4f);
        logo.setPosition(middleX, firstQuarter);
		
		Label titleLabel = new Label("Bluff Poker" + BluffPokerGame.getDivideScreenByThis() + " " + Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight(), uiSkin);
		titleLabel.setColor(Color.WHITE);

		TextButton continueButton = new TextButton("Continue", uiSkin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stageInterface.continuePlaying();
            }
        });

		TextButton playButton = new TextButton("Start", uiSkin);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stageInterface.startSelectingPlayersToPlayWith();
			}
		});
		
		TextButton settingsButton = new TextButton("Settings", uiSkin);
		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stageInterface.openSettingsStage();
			}
		});

        CreditsDialog creditsDialog = new CreditsDialog(uiSkin);
        TextButton creditsButton = new TextButton("Credits", uiSkin);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditsDialog.show(StartStage.this);
            }
        });

		// Create a table and add the ui components to it.
		table.center();

		Cell<Label> labelCell = table.add(titleLabel).padBottom(10f);
		if (tablet) {
			labelCell.padTop(15f * BluffPokerGame.getDivideScreenByThis());
		}

		table.row();
		table.add(playButton).padBottom(10f);
		table.row();

		if (!stageInterface.getState().isNewGameState()) {
            table.add(continueButton).padBottom(10f);
            table.row();
        }
		table.add(settingsButton).padBottom(10f);
		table.row();
		table.add(creditsButton);
        table.setY(thirdQuarter);

		addActor(logo);
		addActor(table);
	}
}
