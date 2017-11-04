package net.leejjon.bluffpoker.interfaces;

import net.leejjon.bluffpoker.listener.ModifyPlayerListener;

import java.util.Collection;
import java.util.Set;

public interface PlatformSpecificInterface {
    // TODO: Use Optional when everyone uses Android 7 and move default name to core.
    String getDeviceOwnerName();

    void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers);

    int getZoomFactor();

    boolean isTablet();
}
