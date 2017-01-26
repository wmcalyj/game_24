package com.wmcalyj.point24;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wmcalyj.point24.services.CalculationService;

import org.w3c.dom.Text;

public class UserInputActivity extends AppCompatActivity {
    private static final String TAG = "UserInput";
    EditText num1, num2, num3, num4;
    Button checkUserInputAnswer, clearUserInput;
    TextView userInputResult;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        mContext = this;
        prepareAllFourNums();
        autoFocusOnNum1();
        setCheckAnswerButton();
        setClearUserInputButton();
    }

    private void setClearUserInputButton() {
        clearUserInput = (Button) findViewById(R.id.user_input_clear);
        clearUserInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num1 != null) {
                    num1.setText("");
                    num1.requestFocus();
                }
                if (num2 != null) {
                    num2.setText("");
                }
                if (num3 != null) {
                    num3.setText("");
                }
                if (num4 != null) {
                    num4.setText("");
                }

            }
        });
    }

    private void setCheckAnswerButton() {
        checkUserInputAnswer = (Button) findViewById(R.id.user_input_check_answer_button);
        checkUserInputAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInputResult == null) {
                    userInputResult = (TextView) findViewById(R.id.user_input_result);
                }
                Game g = formGame();
                if (g == null) {
                    Toast.makeText(mContext, getString(R.string.INVALID_USER_INPUT), Toast
                            .LENGTH_LONG).show();
                } else {
                    String result = CalculationService.getInstance().getSingleResult(g);
                    if (result == null || result.isEmpty()) {
                        userInputResult.setGravity(Gravity.LEFT);
                        userInputResult.setText(getString(R.string.user_input_no_answer, num1
                                .getText(), num2.getText(), num3.getText(), num4.getText()));
                    } else {
                        userInputResult.setGravity(Gravity.CENTER_HORIZONTAL);
                        userInputResult.setText(result);
                    }
                }
            }
        });
    }

    private Game formGame() {
        try {
            int n1 = Integer.valueOf(num1.getText().toString()),
                    n2 = Integer.valueOf(num2.getText().toString()),
                    n3 = Integer.valueOf(num3.getText().toString()),
                    n4 = Integer.valueOf(num4.getText().toString());
            return new Game(n1, n2, n3, n4);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void autoFocusOnNum1() {
        num1.requestFocus();
    }

    private void prepareAllFourNums() {
        num1 = (EditText) findViewById(R.id.user_input_num_1);
        num1.addTextChangedListener(new UserInputNumTextWatcher(-1, R.id.user_input_num_2));
        num1.setOnEditorActionListener(new UserInputNumOnEditorActionListener(-1, R.id
                .user_input_num_2));
        num1.setFilters(getNumberFilters());
        num2 = (EditText) findViewById(R.id.user_input_num_2);
        num2.addTextChangedListener(new UserInputNumTextWatcher(R.id.user_input_num_1, R.id
                .user_input_num_3));
        num2.setOnEditorActionListener(new UserInputNumOnEditorActionListener(R.id
                .user_input_num_1, R.id.user_input_num_3));
        num2.setFilters(getNumberFilters());
        num3 = (EditText) findViewById(R.id.user_input_num_3);
        num3.addTextChangedListener(new UserInputNumTextWatcher(R.id.user_input_num_2, R.id
                .user_input_num_4));
        num3.setOnEditorActionListener(new UserInputNumOnEditorActionListener(R.id
                .user_input_num_2, R.id.user_input_num_4));
        num3.setFilters(getNumberFilters());
        num4 = (EditText) findViewById(R.id.user_input_num_4);
        num4.addTextChangedListener(new UserInputNumTextWatcher(R.id.user_input_num_3, -1));
        num4.setOnEditorActionListener(new UserInputNumOnEditorActionListener(R.id
                .user_input_num_3, -1));
        num4.setFilters(getNumberFilters());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_mode) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.settings);
        item.setVisible(false);
        return true;
    }

    InputFilter[] getNumberFilters() {
        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                Log.d(TAG, "source: " + source + ", start: " + start + ", end: " + end + ", dest:" +
                        " " + dest + ", dstart: " + dstart + ", dend: " + dend);
                if (source == null || source.length() == 0) {
                    return null;
                }
                if (dstart == 0) {
                    if (source.equals("0")) {
                        Toast.makeText(mContext, getString(R.string.USER_INPUT_LEADING_ZERO_ERROR),
                                Toast.LENGTH_LONG).show();
                        return "";
                    }
                }
                if (dest.length() == dstart) {
                    try {
                        int newNum = Integer.valueOf(dest.toString() + source);
                        if (newNum > 13) {
                            Toast.makeText(mContext, getString(R.string
                                            .USER_INPUT_NUMBER_TOO_LARGE_ERROR),
                                    Toast.LENGTH_LONG).show();
                            return "";
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, getString(R.string
                                        .USER_INPUT_CHAR_ERROR),
                                Toast.LENGTH_LONG).show();
                        return "";
                    }
                } else if (dstart == 0) {
                    try {
                        int newNum = Integer.valueOf(source + dest.toString());
                        if (newNum > 13) {
                            Toast.makeText(mContext, getString(R.string
                                            .USER_INPUT_NUMBER_TOO_LARGE_ERROR),
                                    Toast.LENGTH_LONG).show();
                            return "";
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, getString(R.string
                                        .USER_INPUT_CHAR_ERROR),
                                Toast.LENGTH_LONG).show();
                        return "";
                    }
                } else {
                    try {
                        int newNum = Integer.valueOf(dest.subSequence(0, dstart).toString() +
                                source + dest.subSequence(dend, dest.length()));
                        if (newNum > 13) {
                            Toast.makeText(mContext, getString(R.string
                                            .USER_INPUT_NUMBER_TOO_LARGE_ERROR),
                                    Toast.LENGTH_LONG).show();
                            return "";
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(mContext, getString(R.string
                                        .USER_INPUT_CHAR_ERROR),
                                Toast.LENGTH_LONG).show();
                        return "";
                    }
                }
                return null;
            }
        };
        return filters;
    }

    private class UserInputNumTextWatcher implements TextWatcher {
        int prevFocusId, nextFocusId;

        public UserInputNumTextWatcher(int prevFocusId, int nextFocusId) {
            this.prevFocusId = prevFocusId;
            this.nextFocusId = nextFocusId;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() == 1) {
                if (s.charAt(0) != '1' && nextFocusId != -1) {
                    findViewById(nextFocusId).requestFocus();
                }
            } else if (s.length() == 2) {
                if (nextFocusId != -1) {
                    findViewById(nextFocusId).requestFocus();
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Do nothing
        }

    }

    private class UserInputNumOnEditorActionListener implements TextView.OnEditorActionListener {
        int prevFocusId, nextFocusId;

        public UserInputNumOnEditorActionListener(int prevFocusId, int nextFocusId) {
            this.prevFocusId = prevFocusId;
            this.nextFocusId = nextFocusId;
        }


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            Log.d(TAG, "Pressed: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo
                    .IME_ACTION_SEND || actionId == EditorInfo.IME_FLAG_NAVIGATE_NEXT) {
                if (nextFocusId != -1) {
                    findViewById(nextFocusId).requestFocus();
                }
            }
            if (actionId == EditorInfo.IME_ACTION_PREVIOUS || actionId == EditorInfo
                    .IME_FLAG_NAVIGATE_PREVIOUS || actionId == KeyEvent.KEYCODE_DEL) {
                if (prevFocusId != -1) {
                    findViewById(prevFocusId).requestFocus();
                }
            }
            return false;
        }
    }
}
