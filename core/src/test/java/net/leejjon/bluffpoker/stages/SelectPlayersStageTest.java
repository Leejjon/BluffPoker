package net.leejjon.bluffpoker.stages;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

import net.leejjon.bluffpoker.state.SelectPlayersStageState;
import net.leejjon.bluffpoker.state.GdxTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SelectPlayersStageTest extends GdxTest {
    private static final String DEFAULT_PLAYERS = "{\"players\":[\"Leon\",\"Pya\"]}";

    @Test
    public void testRetrievingPlayers_validJson_parseSuccessful() {
        when(preferences.getString(SelectPlayersStageState.KEY)).thenReturn(DEFAULT_PLAYERS);

        SelectPlayersStageState selectPlayersStageState = SelectPlayersStageState.getInstance();
        assertEquals(2, selectPlayersStageState.getPlayers().size());
    }

    @Test
    public void testCreatingPlayerListInUi_constructAndUpdate_success() {
        when(preferences.getString(SelectPlayersStageState.KEY)).thenReturn(DEFAULT_PLAYERS);

        SelectPlayersStageState selectPlayersStageState = SelectPlayersStageState.getInstance();
        List<String> playerList = selectPlayersStageState.createPlayerList(SelectPlayersStage.getCustomListStyle(uiSkin));
        Array<String> items = playerList.getItems();

        assertEquals(2, items.size);

        ArrayList<String> players = selectPlayersStageState.getPlayers();
        players.add("Dirk");

        selectPlayersStageState.setPlayers(players);

        assertEquals(3, playerList.getItems().size);
    }

    @Test
    public void testCutOffShortName() {
        ArrayList<String> emptyList = new ArrayList<>();
        assertEquals("Leon", SelectPlayersStage.cutOffPlayerName("Leon De", emptyList));

        ArrayList<String> listWithExistingUsers = new ArrayList<>();
        listWithExistingUsers.add("Leon");
        listWithExistingUsers.add("Leon Ded");
        assertEquals("Leon Ded D", SelectPlayersStage.cutOffPlayerName("Leon Ded D", listWithExistingUsers));
    }

    @Test
    public void testMoveWinnerToTop() {
        when(preferences.getString(SelectPlayersStageState.KEY)).thenReturn(DEFAULT_PLAYERS);
        SelectPlayersStageState.getInstance().createPlayerList(SelectPlayersStage.getCustomListStyle(uiSkin));

        final String pya = "Pya";
        final String leon = "Leon";
        ArrayList<String> originalList = SelectPlayersStageState.getInstance().getPlayers();
        assertEquals(leon, originalList.get(0));
        ArrayList<String> updatedList = SelectPlayersStageState.updatePlayerListMoveWinnerOnTop("Pya");
        assertEquals(pya, updatedList.get(0));
    }
}
