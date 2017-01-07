package net.leejjon.bluffpoker.android.keyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidInputThreePlus;
import net.leejjon.bluffpoker.dialogs.CallInputDialog;

public class NumberCombinationInput extends AndroidInputThreePlus {
    // Cant use the handle of the superclass because they made it private :(
    private Handler handle;
    private final Context context;

    public NumberCombinationInput(Application activity, Context context, Object view, AndroidApplicationConfiguration config) {
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
            if (title.equals(CallInputDialog.ENTER_THREE_DIGITS)) {
                input.setFilters(new InputFilter[]{new NumberCombinationInputFilter()});
                input.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            input.setHint(hint);
            input.setText(text);
            input.setSingleLine();
            alert.setView(input);
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
            alert.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            listener.canceled();
                        }
                    });
                }
            });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            listener.canceled();
                        }
                    });
                }
            });
            alert.show();
        });
    }
}
