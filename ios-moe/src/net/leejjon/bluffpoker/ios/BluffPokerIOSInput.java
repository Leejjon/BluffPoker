package net.leejjon.bluffpoker.ios;

import apple.coregraphics.struct.CGPoint;
import apple.coregraphics.struct.CGRect;
import apple.coregraphics.struct.CGSize;
import apple.coremotion.CMAccelerometerData;
import apple.coremotion.CMMotionManager;
import apple.foundation.NSError;
import apple.foundation.NSOperationQueue;
import apple.foundation.struct.NSRange;
import apple.uikit.UIDevice;
import apple.uikit.UITextField;
import apple.uikit.enums.*;
import apple.uikit.protocol.UITextFieldDelegate;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSInput;
import net.leejjon.bluffpoker.BluffPokerGame;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * For detecting shakes (the only use of the accelero meter within the bluffpoker app) the IOSInput class in libgdx is not specific enough.
 *
 * So in this class we extend and overwrite methods that access it to modify it into what we need.
 */
public class BluffPokerIOSInput extends IOSInput {
    // Stuff required for onscreen keyboard.
    private IOSApplication app;
    boolean keyboardCloseOnReturn;

    private CMMotionManager motionManager;
    private AtomicLong lastUpdate;
    private AtomicInteger numberOfTimesShaked = new AtomicInteger(0);
    private volatile double last_x, last_y, last_z;
    private BluffPokerGame game;

    public BluffPokerIOSInput(IOSApplication app, IOSApplicationConfiguration config, BluffPokerGame game) {
        super(app);
        this.app = app;
        this.game = game;
        this.keyboardCloseOnReturn = config.keyboardCloseOnReturn;
    }

    private UITextField textfield = null;
    private final UITextFieldDelegate textDelegate = new UITextFieldDelegate() {
        @Override
        public boolean textFieldShouldChangeCharactersInRangeReplacementString (UITextField textField, NSRange range, String string) {
            for (int i = 0; i < range.length(); i++) {
                getInputProcessor().keyTyped((char)8);
            }

            if (string.isEmpty()) {
                if (range.length() > 0) Gdx.graphics.requestRendering();
                return false;
            }

            char[] chars = new char[string.length()];
            string.getChars(0, string.length(), chars, 0);

            for (int i = 0; i < chars.length; i++) {
                getInputProcessor().keyTyped(chars[i]);
            }
            Gdx.graphics.requestRendering();

            return true;
        }

        @Override
        public boolean textFieldShouldEndEditing (UITextField textField) {
            // Text field needs to have at least one symbol - so we can use backspace
            textField.setText("x");
            Gdx.graphics.requestRendering();

            return true;
        }

        @Override
        public boolean textFieldShouldReturn (UITextField textField) {
            if (keyboardCloseOnReturn) setOnscreenKeyboardVisible(false);
            getInputProcessor().keyDown(Keys.ENTER);
            getInputProcessor().keyTyped((char)13);
            Gdx.graphics.requestRendering();
            return false;
        }
    };

    @Override
    public void setOnscreenKeyboardVisible (boolean visible) {
        if (textfield == null) createDefaultTextField();
        if (visible) {
            textfield.becomeFirstResponder();
            textfield.setDelegate(textDelegate);
        } else {
            textfield.resignFirstResponder();
        }
    }

    private void createDefaultTextField () {
        textfield = UITextField.alloc();
        textfield.initWithFrame(new CGRect(new CGPoint(10, 10), new CGSize(100, 50)));
        //Parameters
        // Setting parameters
        textfield.setKeyboardType(UIKeyboardType.NumberPad);
        textfield.setReturnKeyType(UIReturnKeyType.Done);
        textfield.setAutocapitalizationType(UITextAutocapitalizationType.None);
        textfield.setAutocorrectionType(UITextAutocorrectionType.No);
        textfield.setSpellCheckingType(UITextSpellCheckingType.No);
        textfield.setHidden(true);
        // Text field needs to have at least one symbol - so we can use backspace
        textfield.setText("x");
        app.getUIViewController().view().addSubview(textfield);
    }

    public void setupAccelerometer () {
        motionManager = CMMotionManager.alloc().init();
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
                            game.shakePhone();
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
