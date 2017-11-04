package net.leejjon.bluffpoker.ios;

import apple.contacts.CNContact;
import apple.contacts.CNContactFetchRequest;
import apple.contacts.CNContactStore;
import apple.contacts.c.Contacts;
import apple.foundation.NSArray;
import apple.foundation.NSError;
import apple.uikit.UIDevice;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.interfaces.PlatformSpecificInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;
import org.moe.natj.general.ptr.BoolPtr;
import org.moe.natj.general.ptr.Ptr;
import org.moe.natj.general.ptr.impl.PtrFactory;

import java.util.Set;

public class IOSMoeLauncher extends BluffPokerIOSApplication.Delegate implements PlatformSpecificInterface {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    private BluffPokerGame bluffPokerGame;

    private BluffPokerIOSApplication bluffPokerIOSApplication;

    @Override
    protected BluffPokerIOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = true;

        bluffPokerGame = new BluffPokerGame(this);
//        MyGdxGame bluffPokerGame = new MyGdxGame();

        bluffPokerIOSApplication = new BluffPokerIOSApplication(bluffPokerGame, config);
        return bluffPokerIOSApplication;
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public String getDeviceOwnerName() {
        return DeviceOwnerNameResolver.resolveDeviceOwnerName(UIDevice.currentDevice().name());
    }

    /**
     * Code ripped from: https://gist.github.com/willthink/024f1394474e70904728
     * <p>
     * Contact permissions needed to be added to the Info.plist for this code to work.
     * https://github.com/yonahforst/react-native-permissions/issues/38
     * <p>
     *
     * @param listener
     * @param alreadyExistingPlayers
     */
    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                listener.showPhonebookDialog();
            }
        });
        CNContactStore contactStore = CNContactStore.alloc().init();
        contactStore.requestAccessForEntityTypeCompletionHandler(apple.contacts.enums.CNEntityType.CNEntityTypeContacts, new CNContactStore.Block_requestAccessForEntityTypeCompletionHandler() {
            @Override
            public void call_requestAccessForEntityTypeCompletionHandler(boolean success, NSError error) {
                if (success) {
                    Gdx.app.log("bluffpoker", "The user gave us access to the contacts in the phonebook.");
                    selectContacts(listener, alreadyExistingPlayers, contactStore);
                } else {
                    Gdx.app.log("bluffpoker", "Did not have access to contacts. Error code: " + error.code());
                }
            }
        });
    }

    private void selectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers, CNContactStore contactStore) {
        // TODO: Handle the error. Currently everything I tried to do with the error object didn't work though...
        Ptr<NSError> error = PtrFactory.newObjectReference(NSError.class);
        // TODO: Also retrieve the last name. But for some reason that results in an ObjCException...
        NSArray<?> keysToFetch = NSArray.arrayWithObject(Contacts.CNContactGivenNameKey());//, Contacts.CNContactFamilyNameKey());
        CNContactFetchRequest request = CNContactFetchRequest.alloc().initWithKeysToFetch(keysToFetch);

        contactStore.enumerateContactsWithFetchRequestErrorUsingBlock(request, error, new CNContactStore.Block_enumerateContactsWithFetchRequestErrorUsingBlock() {
            @Override
            public void call_enumerateContactsWithFetchRequestErrorUsingBlock(CNContact contact, BoolPtr stop) {
                String name = contact.givenName(); // + contact.familyName();
                if (!(name.length() == 0) && !alreadyExistingPlayers.contains(name)) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            listener.loadFromPhonebook(name);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getZoomFactor() {
        int zoomfactor = IOSDevices.getZoomFactorForResolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return zoomfactor;
    }

    @Override
    public boolean isTablet() {
        return IOSDevices.isTablet(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private enum IOSDevices {
        IPHONE_5S_SE(640, 1136, 2),
        IPHONE_6_6S_7_8(750, 1334, 2),
        IPHONE_6PLUS_7PLUS(960, 1704, 3),
        IPHONE_8PLUS(1242, 2208, 4),
        IPHONE_X(1125, 2001, 3),
        IPAD_AIR(1536, 2048, 3, true);

        private final int width;
        private final int height;
        private final int zoomfactor;
        private final boolean tablet;

        IOSDevices(int width, int height, int zoomfactor) {
            this(width, height, zoomfactor, false);
        }

        IOSDevices(int width, int height, int zoomfactor, boolean tablet) {
            this.width = width;
            this.height = height;
            this.zoomfactor = zoomfactor;
            this.tablet = tablet;
        }

        public static int getZoomFactorForResolution(int width, int height) {
            for (IOSDevices IOSDevices : IOSDevices.values()) {
                if (IOSDevices.width == width && IOSDevices.height == height) {
                    return IOSDevices.zoomfactor;
                }
            }
            return 3;
        }

        public static boolean isTablet(int width, int height) {
            for (IOSDevices IOSDevices : IOSDevices.values()) {
                if (IOSDevices.width == width && IOSDevices.height == height) {
                    return IOSDevices.tablet;
                }
            }
            return false;
        }
    }
}
