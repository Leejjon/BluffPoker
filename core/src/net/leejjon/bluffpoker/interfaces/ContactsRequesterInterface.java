package net.leejjon.bluffpoker.interfaces;

public interface ContactsRequesterInterface {
    // TODO: Use Optional when everyone uses Android 7 and move default name to core.
    String getDeviceOwnerName();

    boolean hasContactPermissions();

    void requestContactPermission();
}
