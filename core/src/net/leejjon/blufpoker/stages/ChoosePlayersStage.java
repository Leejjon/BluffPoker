package net.leejjon.blufpoker.stages;

import java.util.ArrayList;
import java.util.Collections;

import net.leejjon.blufpoker.dialogs.AddNewPlayerDialog;
import net.leejjon.blufpoker.dialogs.WarningDialog;
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
	
	private WarningDialog playerAlreadyExistsWarning;
	private WarningDialog playerNameInvalid;
	private WarningDialog minimalTwoPlayersRequired;
	
	public ChoosePlayersStage(int divideScreenByThis, Skin uiSkin, final ChangeStageListener changeScreen) {
		super(divideScreenByThis, false);
		
		playerAlreadyExistsWarning = new WarningDialog("Player already exists.", uiSkin);
		playerNameInvalid = new WarningDialog("Player name invalid.", uiSkin);
		minimalTwoPlayersRequired = new WarningDialog("Select at least two players!", uiSkin);
		
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
		upButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				swapPlayerUp();
			}
		});
		TextButton downButton = new TextButton("\\/", uiSkin);
		downButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				swapPlayerDown();
			}
		});
		
		TextButton removeButton = new TextButton("Remove", uiSkin);
		removeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeSelectedPlayer();
			}
		});
		
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
				Gdx.input.getTextInput(addNewPlayerDialog, "Insert new player name", "", "What");
			}
		});
		
		TextButton startGameButton = new TextButton("Start Game", uiSkin);
		startGameButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				startGame(changeScreen);
			}
		});
		
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
	
	protected void startGame(ChangeStageListener changeScreen) {
		if (currentPlayers.size() < 2) {
			minimalTwoPlayersRequired.show(this);
		} else {
			changeScreen.startGame(currentPlayers);
		}
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
		final int maxNameLength = 16;
		
		if (playerName.length() > 0 && playerName.length() <= maxNameLength) {
			if (!existingPlayers.contains(playerName)) {	
				existingPlayers.add(playerName);
				existingPlayerList.setItems(existingPlayers.toArray(new String[existingPlayers.size()]));
			} else {
				playerAlreadyExistsWarning.show(this);
			}
		} else {
			playerNameInvalid.show(this);
		}
	}

	private void addSelectedExistingPlayer() {
		String selectedPlayer = existingPlayerList.getSelected();
		if (selectedPlayer != null && !currentPlayers.contains(selectedPlayer)) {
			currentPlayers.add(selectedPlayer);
			currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
		} else {
			playerAlreadyExistsWarning.show(this);
		}
	}
	
	private void removeSelectedPlayer() {
		String selectedPlayer = currentPlayerList.getSelected();
		if (selectedPlayer != null) {
			currentPlayers.remove(selectedPlayer);
			currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
		}
	}
	
	private void swapPlayerUp() {
		int selectedIndex = currentPlayerList.getSelectedIndex(); 
		if (selectedIndex > 0) {
			Collections.swap(currentPlayers, selectedIndex, selectedIndex-1);
			currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
		}
	}
	
	private void swapPlayerDown() {
		int selectedIndex = currentPlayerList.getSelectedIndex();
		if (selectedIndex > -1 && selectedIndex < currentPlayers.size()-1 && currentPlayers.size() > 1) {
			Collections.swap(currentPlayers, selectedIndex, selectedIndex+1);
			currentPlayerList.setItems(currentPlayers.toArray(new String[currentPlayers.size()]));
		}
	}
}
