package net.leejjon.bluffpoker.ios;

import apple.contacts.CNContact;
import apple.contacts.CNContactFetchRequest;
import apple.contacts.CNContactStore;
import apple.contacts.c.Contacts;
import apple.foundation.NSArray;
import apple.foundation.NSError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
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
        config.useAccelerometer = true; // We put it on false so libgdx thinks it's not used. We actually do use it in the BluffPokerIOSApplication.java.

        bluffPokerGame = new BluffPokerGame(this,2);
//        MyGdxGame bluffPokerGame = new MyGdxGame();

		  bluffPokerIOSApplication = new BluffPokerIOSApplication(bluffPokerGame, config);
		  return bluffPokerIOSApplication;
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    public String getDeviceOwnerName() {
        return "Player 1";
    }

	 /**
	  * Code ripped from: https://gist.github.com/willthink/024f1394474e70904728
	  * @param listener
	  * @param alreadyExistingPlayers
	  */
    @Override
    public void initiateSelectContacts(ModifyPlayerListener listener, Set<String> alreadyExistingPlayers) {
		  CNContactStore contactStore = bluffPokerIOSApplication.getContactStore();

		  Gdx.app.log("bluffpoker", Contacts.CNContactGivenNameKey());

		  NSArray<?> keysToFetch = NSArray.arrayWithObject(Contacts.CNContactGivenNameKey());
		  CNContactFetchRequest request = CNContactFetchRequest.alloc().initWithKeysToFetch(keysToFetch);
		  Ptr<NSError> error = PtrFactory.newObjectReference(NSError.class);
		  contactStore.enumerateContactsWithFetchRequestErrorUsingBlock(request, error, new CNContactStore.Block_enumerateContactsWithFetchRequestErrorUsingBlock() {
				@Override
				public void call_enumerateContactsWithFetchRequestErrorUsingBlock (CNContact arg0, BoolPtr arg1) {

					Gdx.app.log("bluffpoker", "we got a contact");
				}
		  });

		  Gdx.app.log("bluffpoker", error.get().localizedDescription());
    }
}
