package net.leejjon.bluffpoker.stages;

import org.junit.Test;
import java.util.List;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SelectPlayersStageTest {
    @Test
    public void testCutOffShortName() {
        List<String> emptyList = new ArrayList<>();
        assertEquals("Leon", SelectPlayersStage.cutOffPlayerName("Leon De", emptyList));

        List<String> listWithExistingUsers = new ArrayList<>();
        listWithExistingUsers.add("Leon");
        listWithExistingUsers.add("Leon Ded");
        assertEquals("Leon Ded D", SelectPlayersStage.cutOffPlayerName("Leon Ded D", listWithExistingUsers));
    }
}
