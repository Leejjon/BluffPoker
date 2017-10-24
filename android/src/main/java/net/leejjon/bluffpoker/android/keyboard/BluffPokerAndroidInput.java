package net.leejjon.bluffpoker.android.keyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidInputThreePlus;
import net.leejjon.bluffpoker.dialogs.CallInputDialog;

public class BluffPokerAndroidInput extends AndroidInputThreePlus {
    // Cant use the handle of the superclass because they made it private :(
    private Handler handle;
    private final Context context;

    public BluffPokerAndroidInput(Application activity, Context context, Object view, AndroidApplicationConfiguration config) {
        super(activity, context, view, config);
        this.context = context;
        handle = new Handler();
    }

    @Override
    public void getTextInput(TextInputListener listener, String title, String text, String hint) {
        handle.post(() -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(title);
            final EditText input = new EditText(context);

            // I abuse the title text to detect if number input is needed.
            if (title.equals(CallInputDialog.ENTER_YOUR_CALL)) {
                input.setFilters(new InputFilter[]{new NumberCombinationInputFilter()});
                input.setInputType(InputType.TYPE_CLASS_PHONE);
            } else {
                input.setFilters(new InputFilter[]{new PlayerNameInputFilter()});
            }
            input.setHint(hint);

            // Instead of just setting the text, we first add an empty String.
            input.getText().clear();

            // And then we append our value so the cursor will be at the end.
            input.append(text);

            input.setSingleLine();

            // Open the keyboard when the EditText is finished loading.
            input.post(() -> {
                InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(input, 0);
            });

            alert.setView(input);
            alert.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Gdx.app.postRunnable(() -> listener.canceled());
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    Gdx.app.postRunnable(() -> listener.canceled());
                }
            });

            // Pressing the "ok" button requires some explanation.
            // Normally you'd create a positive button with a onClick listener like this:
            // We use this code for entering the player names.
            alert.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            listener.input(input.getText().toString());
                        }
                    });
                }
            });

            // However, this way the "ok" button always closes the window.
            // So if you added an EditText input like I did, it will always pass the input.
            // Character filtering for the inputs should be done in an InputFilter, and I do that in the NumberCombinationInputFilter.java and PlayerNameInputfilter.java filters.
            // That filtering happens when typing. Since you're typing your input, I cannot start filtering on length yet. That can only happen after "ok" has been pressed.

            // So after creating this dialog....
            AlertDialog alertDialog = alert.show();

            if (title.equals(CallInputDialog.ENTER_YOUR_CALL)) {
                // We retrieve the okButton.
                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                // And override it's onClickListener and add some validation. In this listener we are able to dismiss the dialog or not.
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (input.getText().length() == 3) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    listener.input(input.getText().toString());
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
