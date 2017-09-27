package net.leejjon.bluffpoker.ios;

import apple.contacts.CNContact;
import apple.contacts.CNContactFetchRequest;
import apple.contacts.CNContactStore;
import apple.contacts.c.Contacts;
import apple.foundation.NSArray;
import apple.foundation.NSError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.interfaces.ContactsRequesterInterface;
import net.leejjon.bluffpoker.listener.ModifyPlayerListener;
import org.moe.natj.general.Pointer;

import apple.uikit.c.UIKit;
import org.moe.natj.general.ptr.BoolPtr;
import org.moe.natj.general.ptr.Ptr;
import org.moe.natj.general.ptr.impl.PtrFactory;

import java.util.Set;

public class IOSMoeLauncher extends BluffPokerIOSApplication.Delegate implements ContactsRequesterInterface {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    private BluffPokerGame bluffPokerGame;

    private BluffPokerIOSApplication bluffPokerIOSApplication;

    @Override
    protected BluffPokerIOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.useAccelerometer = true;

        bluffPokerGame = new BluffPokerGame(this, 2);
//        MyGdxGame bluffPokerGame = new MyGdxGame();

        bluffPokerIOSApplication = new BluffPokerIOSApplication(bluffPokerGame, config);
        return bluffPokerIOSApplication;
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public String getDeviceOwnerName() {
//        func unifiedMeContactWithKeys(toFetch: [CNKeyDescriptor])
//
//        Fetches the unified contact that is the "me" card.

        return "Player 1";
    }

    /**
     * Code ripped from: https://gist.github.com/willthink/024f1394474e70904728
     * <p>
     * Contact permissions needed to be added to the Info.plist for this code to work.
     * https://github.com/yonahforst/react-native-permissions/issues/38
     *
     * One in that selectContacts method, somehow the thread that executes these methods goes insane.
     * When I try to modify UI from these threads
     *
     * @param listener
     * @param alreadyExistingPlayers
     */
    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {
        listener.showAndReset();
        if (names == null) {
            names = new Array<>();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    CNContactStore contactStore = CNContactStore.alloc().init();
                    Gdx.app.log(BluffPokerGame.TAG, "Succesful alloc");
                    contactStore.requestAccessForEntityTypeCompletionHandler(apple.contacts.enums.CNEntityType.CNEntityTypeContacts, new CNContactStore.Block_requestAccessForEntityTypeCompletionHandler() {
                        @Override
                        public void call_requestAccessForEntityTypeCompletionHandler(boolean success, NSError error) {
                            if (success) {
                                Gdx.app.log("bluffpoker", "The user gave us access to the contacts in the phonebook.");
                                selectContacts(alreadyExistingPlayers, contactStore);
                            } else {
                                Gdx.app.log("bluffpoker", "Did not have access to contacts. Error code: " + error.code());
                            }
                        }
                    });
                }
            });
            thread.start();
            try {
                // With 217 contacts, 100 ms wasn't fast enough to load them. 200 was. So that's why I picked 250, for people with lots of contacts.
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        listener.loadFromPhonebook(names);
    }

    private Array<String> names = null;

    private void selectContacts(Set<String> alreadyExistingPlayers, CNContactStore contactStore) {
        // TODO: Handle the error. Currently everything I tried to do with the error object didn't work though...
        Ptr<NSError> error = PtrFactory.newObjectReference(NSError.class);
        // TODO: Also retrieve the last name. But for some reason that results in an ObjCException...
        NSArray<?> keysToFetch = NSArray.arrayWithObject(Contacts.CNContactGivenNameKey());//, Contacts.CNContactFamilyNameKey());
        CNContactFetchRequest request = CNContactFetchRequest.alloc().initWithKeysToFetch(keysToFetch);

        // TODO: Figure out why we keep getting the "libc++abi.dylib: terminating with uncaught exception of type ObjCException"
        contactStore.enumerateContactsWithFetchRequestErrorUsingBlock(request, error, new CNContactStore.Block_enumerateContactsWithFetchRequestErrorUsingBlock() {
            @Override
            public void call_enumerateContactsWithFetchRequestErrorUsingBlock(CNContact contact, BoolPtr stop) {
                String name = contact.givenName(); // + contact.familyName();
                if (!(name.length() == 0) && !alreadyExistingPlayers.contains(name)) {
                    names.add(name);
                }
            }
        });
    }
}
