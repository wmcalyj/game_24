package com.wmcalyj.point24.listeners;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wmcalyj.point24.R;

/**
 * Created by mengchaowang on 1/28/17.
 */
public class KeypadOnTouchListener implements View.OnClickListener {
    private EditText[] editTexts;
    private Context mContext;

    public KeypadOnTouchListener(Context context, EditText... editable) {
        editTexts = editable;

        mContext = context;
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < editTexts.length; i++) {
            EditText myEditText = editTexts[i];
            if (myEditText != null && myEditText.isFocused()) {
                Button b = (Button) v;
                CharSequence buttonString = b.getText();
                int currentPosStart = myEditText.getSelectionStart();
                int currentPosEnd = myEditText.getSelectionEnd();
                if (buttonString.equals(mContext.getString(R.string.pad_clear))) {
                    myEditText.setText("");
                } else if (buttonString.equals(mContext.getString(R.string.pad_backspace))) {
                    if (myEditText.length() > 0) {
                        if (currentPosStart == currentPosEnd) {
                            myEditText.getText().replace(currentPosStart - 1, currentPosEnd, "");
                        } else {
                            myEditText.getText().replace(currentPosStart, currentPosEnd, "");
                        }
                    } else {
                        if (i > 0) {
                            editTexts[i - 1].requestFocus();
                        }
                    }
                } else {
                    if (currentPosEnd == currentPosStart) {
                        myEditText.getText().insert(currentPosStart, buttonString);
                    } else {
                        myEditText.getText().replace(currentPosStart, currentPosEnd, buttonString);
                    }
                }
                break;
            }
        }
    }
}
