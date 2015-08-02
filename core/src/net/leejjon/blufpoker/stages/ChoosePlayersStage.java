package net.leejjon.blufpoker.stages;

import java.util.ArrayList;

import net.leejjon.blufpoker.dialogs.AddNewPlayerDialog;
import net.leejjon.blufpoker.listener.ChangeStageListener;
import net.leejjon.blufpoker.listener.ModifyPlayerListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ChoosePlayersStage extends AbstractStage implements ModifyPlayerListener {
	private java.util.List<String> currentPlayers;
	private java.util.List<String> existingPlayers;
	
	private List<String> currentPlayerList = null;
	private List<String> existingPlayerList = null;
	
	public ChoosePlayersStage(float w, float h, int divideScreenByThis, Skin uiSkin, final ChangeStageListener changeScreen) {
		super(w, h, divideScreenByThis, false);
		
		// Creating the ui components.
		Label playersInGameLabel = new Label("Players in game", uiSkin);
		Label existingPlayersLabel = new Label("Existing players", uiSkin);
		
		currentPlayerList = getCurrentPlayerList(uiSkin);

		ScrollPane playersInGameScrollPane = new ScrollPane(currentPlayerList, uiSkin);
		playersInGameScrollPane.setScrollingDisabled(true, false);
		
		existingPlayerList = getExistingPlayerList(uiSkin);

		ScrollPane existingPlayersScrollPane = new ScrollPane(existingPlayerList, uiSkin);
		existingPlayersScrollPane.setScrollingDisabled(true, false);
		
		TextButton upButton = new TextButton("/\\", uiSkin);
		TextButton downButton = new TextButton("\\/", uiSkin);
		TextButton removeButton = new TextButton("Remove", uiSkin);
		
		TextButton addButton = new TextButton("Add", uiSkin);
		addButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addSelectedExistingPlayer();
			}
		});
		
		TextButton newButton = new TextButton("New", uiSkin);
		
		final AddNewPlayerDialog addNewPlayerDialog = new AddNewPlayerDialog(this);
		newButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.getTextInput(addNewPlayerDialog, "Insert new player name", "Player 1", "What");
			}
		});
		
		TextButton startGameButton = new TextButton("Start Game", uiSkin);
		
		// Adding the components to the table.
		final float enoughDistanceToFitAnyName = 150f;
		
		table.debug();
		table.center();
		table.add(playersInGameLabel).padTop(5f).colspan(3);
		table.add(existingPlayersLabel).padTop(5f).colspan(3);
		table.row();
		table.add(playersInGameScrollPane).width(enoughDistanceToFitAnyName).fill().padRight(5f).colspan(3);
		table.add(existingPlayersScrollPane).width(enoughDistanceToFitAnyName).fill().padLeft(5f).colspan(3);
		table.row();
		table.add(upButton, downButton, removeButton);
		table.add(addButton).colspan(2);
		table.add(newButton);
		table.row();
		table.add(startGameButton).padTop(10f).padBottom(5f).colspan(6);

		addActor(table);
	}
	
	private List<String> getCurrentPlayerList(Skin uiSkin) {
		currentPlayers = new ArrayList<>();
		
		if (currentPlayerList == null) {
			currentPlayerList = new List<String>(uiSkin);
		}
		
		currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
		
		return currentPlayerList;
	}

	private List<String> getExistingPlayerList(Skin uiSkin) {
		existingPlayers = new ArrayList<>();

		final String[] commonPlayersArray = { "Barry", "Blao", "Billy", "Joop", "King Charles", "Leejjon", "Rik", "Shadowing", "Stofkat" };

		for (String commonPlayer : commonPlayersArray) {
			existingPlayers.add(commonPlayer);
		}
		
		if (existingPlayerList == null) {
			existingPlayerList = new List<String>(uiSkin);
		}
		
		existingPlayerList.setItems(existingPlayers.toArray(new String[existingPlayers.size()]));
		
		return existingPlayerList; 
	}

	@Override
	public void addNewPlayer(String playerName) {
		if (!existingPlayers.contains(playerName)) {	
			existingPlayers.add(playerName);
			existingPlayerList.setItems(existingPlayers.toArray(new String[existingPlayers.size()]));
		}
	}

	public void addSelectedExistingPlayer() {
		String selectedPlayer = existingPlayerList.getSelected();
		if (selectedPlayer != null && !currentPlayers.contains(selectedPlayer)) {
			currentPlayers.add(selectedPlayer);
			currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
			
			int indexOfSelectedPlayer = existingPlayerList.getSelectedIndex();
			if (indexOfSelectedPlayer > -1) {
				existingPlayerList.getItems().removeIndex(indexOfSelectedPlayer);
			}
		}
	}
}
