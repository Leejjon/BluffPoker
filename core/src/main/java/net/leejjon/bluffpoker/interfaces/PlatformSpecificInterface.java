package net.leejjon.bluffpoker.interfaces;

import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.Collection;
import java.util.Set;

public interface PlatformSpecificInterface {
    // TODO: Use Optional when everyone uses Android 7 and move default name to core.
    String getDeviceOwnerName();

    void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers);

    int getZoomFactor();

    /**
     * This method will probably only ever return something else than the expectedTop for the iPhone X.
     */
    default int getTop(int expectedTop) {
        return expectedTop;
    }

    /**
     * This method will probably only ever return something else than the expectedBottom for the iPhone X.
     */
    default int getBottom(int expectedBottom) {
        return expectedBottom;
    }
}
