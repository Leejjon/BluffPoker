package net.leejjon.bluffpoker.ios;

import apple.coremotion.CMAccelerometerData;
import apple.coremotion.CMMotionManager;
import apple.foundation.NSError;
import apple.foundation.NSOperationQueue;
import apple.uikit.UIAlertView;
import apple.uikit.UITextField;
import apple.uikit.enums.UIAlertViewStyle;
import apple.uikit.enums.UIKeyboardType;
import apple.uikit.protocol.UIAlertViewDelegate;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSInput;
import net.leejjon.bluffpoker.BluffPokerGame;
import net.leejjon.bluffpoker.dialogs.CallInputDialog;
import net.leejjon.bluffpoker.ios.keyboard.NumberCombinationTextFieldValidator;
import net.leejjon.bluffpoker.ios.keyboard.PlayerNameTextFieldValidator;
import net.leejjon.bluffpoker.listener.PhoneInputListener;
import org.moe.natj.general.ann.NInt;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BluffPokerIOSInput extends IOSInput {
    private final IOSApplicationConfiguration config;
    private final PhoneInputListener phoneInput;

    private AtomicLong lastUpdate;
    private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
    private volatile double last_x, last_y, last_z;

    public BluffPokerIOSInput(IOSApplication app, PhoneInputListener phoneInput, IOSApplicationConfiguration config) {
        super(app);
        this.phoneInput = phoneInput;
        this.config = config;
    }

    /**
     * For detecting shakes (the only use of the accelero meter within the bluffpoker app) the IOSInput class in libgdx is not specific enough.
     */
    @Override
    protected void setupAccelerometer () {
        if (config.useAccelerometer) {
            motionManager.setAccelerometerUpdateInterval(0.01d);
            CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler handler = new CMMotionManager.Block_startAccelerometerUpdatesToQueueWithHandler() {
                @Override
                public void call_startAccelerometerUpdatesToQueueWithHandler (CMAccelerometerData data, NSError nsError) {
                    final int SHAKE_THRESHOLD = 500;

                    long currentTime = System.currentTimeMillis();

                    if (lastUpdate == null) {
                        lastUpdate = new AtomicLong(currentTime);
                    }

                    if ((currentTime - lastUpdate.get()) > 100) {
                        long diffTime = (currentTime - lastUpdate.get());
                        lastUpdate.set(currentTime);

                        double x, y, z;
                        x = data.acceleration().x();
                        y = data.acceleration().y();
                        z = data.acceleration().z();

                        double xyz = x + y + z;
                        double xyzMinusLastXYZ = xyz - last_x - last_y - last_z;
                        double speed = Math.abs(xyzMinusLastXYZ) / diffTime * 10000;

                        if (speed > SHAKE_THRESHOLD) {
                            if (numberOfTimesShaked.incrementAndGet() == 3) {
                                Gdx.app.log("bluffpoker", "shake detected: ");
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        phoneInput.shakePhone();
                                    }
                                });
                                numberOfTimesShaked.set(0);
                                return;
                            }
                        }
                        last_x = x;
                        last_y = y;
                        last_z = z;
                    }
                }
            };
            motionManager.startAccelerometerUpdatesToQueueWithHandler(NSOperationQueue.alloc().init(), handler);
        }
    }

    @Override
    public void getTextInput(TextInputListener listener, String title, String text, String hint) {
        buildUIAlertView(listener, title, text, hint).show();
    }

    /** Builds an {@link UIAlertView} with an added {@link UITextField} for inputting text.
     * @param listener Text input listener
     * @param title Dialog title
     * @param text Text for text field
     * @return UiAlertView */
    private UIAlertView buildUIAlertView (final TextInputListener listener, String title, String text, String placeholder) {
        boolean callInput = title.equals(CallInputDialog.ENTER_YOUR_CALL);
        UIAlertViewDelegate delegate = new UIAlertViewDelegate() {
            @Override
            public void alertViewClickedButtonAtIndex (UIAlertView alertView, @NInt long buttonIndex) {
                if (buttonIndex == 0) {
                    // user clicked "Cancel" button
                    listener.canceled();
                } else if (buttonIndex == 1) {
                    // user clicked "Ok" button
                    UITextField textField = alertView.textFieldAtIndex(0);

                    if (callInput) {
                        if (textField.text().length() != 3) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    buildUIAlertView(listener, title, textField.text(), placeholder).show();
                                }
                            });
                            return;
                        }
                    }

                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            listener.input(textField.text());
                        }
                    });
                }
            }

            @Override
            public void alertViewCancel (UIAlertView alertView) {
                listener.canceled();
            }
        };

        // build the view
        final UIAlertView uiAlertView = UIAlertView.alloc().init();
        uiAlertView.setTitle(title);
        uiAlertView.addButtonWithTitle("Cancel");
        uiAlertView.addButtonWithTitle("Ok");
        uiAlertView.setAlertViewStyle(UIAlertViewStyle.PlainTextInput);
        uiAlertView.setDelegate(delegate);

        UITextField textField = uiAlertView.textFieldAtIndex(0);
        textField.setPlaceholder(placeholder);
        textField.setText(text);

        if (callInput) {
            textField.setKeyboardType(UIKeyboardType.NumberPad);
            textField.setDelegate(new NumberCombinationTextFieldValidator());
        } else {
            textField.setDelegate(new PlayerNameTextFieldValidator());
        }

        return uiAlertView;
    }
}
