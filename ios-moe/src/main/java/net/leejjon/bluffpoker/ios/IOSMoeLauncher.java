package net.leejjon.bluffpoker.ios;

import apple.c.Globals;
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
     * One in that selectContacts method, somehow the thread that executes these methods goes insane. When I try to
     * modify UI from these threads
     *
     * @param listener
     * @param alreadyExistingPlayers
     */
    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {
        listener.showPhonebookDialog();
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
                    // Do the UI changes
                    Globals.dispatch_async(Globals.dispatch_get_main_queue(), new Globals.Block_dispatch_async() {
                        @Override
                        public void call_dispatch_async() {
                            listener.loadFromPhonebook(name);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getZoomFactor() {
        return IPhones.getZoomFactorForResolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private enum IPhones {
        IPHONE_5S_SE(640, 1136, 2),
        IPHONE_6_6S_7_8(750, 1334, 2),
        IPHONE_6PLUS_7PLUS_8PLUS(1242, 2208, 3),
        IPHONE_X(1125, 2436, 3);

        private final int width;
        private final int height;
        private final int zoomfactor;

        IPhones(int width, int height, int zoomfactor) {
            this.width = width;
            this.height = height;
            this.zoomfactor = zoomfactor;
        }

        public static int getZoomFactorForResolution(int width, int height) {
            for (IPhones iPhones : IPhones.values()) {
                if (iPhones.width == width && iPhones.height == height) {
                    return iPhones.zoomfactor;
                }
            }
            return 2;
        }
    }
}
