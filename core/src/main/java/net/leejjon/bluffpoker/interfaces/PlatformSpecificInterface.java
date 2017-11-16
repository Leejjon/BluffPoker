package net.leejjon.bluffpoker.interfaces;

import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.Collection;
import java.util.Set;

public interface PlatformSpecificInterface {
    // TODO: Use Optional when multi os engine uses Android 7 and move default name to core.
    String getDeviceOwnerName();

    void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers);

    int getZoomFactor();

    /**
     * This method will probably only ever return something else than the 0 for the iPhone X.
     */
    default int getTopPadding() {
        return 0;
    }

    /**
     * This method will probably only ever return something else than 0 for the iPhone X.
     */
    default int getBottomPadding() { return 0; }
}
