package com.wmcalyj.point24;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wmcalyj.point24.services.AssetFileService;
import com.wmcalyj.point24.services.CalculationService;
import com.wmcalyj.point24.services.ComputeResultRunnable;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static final Double DIFF = 0.0000000001;
    private static final String TAG = "MainActivity";
    ImageView[] cardViews = new ImageView[4];
    Set<String> result;
    String resultString;
    TextView resultView, userResultView, includeFaceCardsInstruction;
    EditText editText;
    Button nextQuestion, seeAnswer, checkButton;
    Context mContext;
    Game g;
    int maxNum = 10;
    //    boolean isFreeStyle = false;
    boolean includeFaceCards = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        loadSettings();
        AssetFileService.loadPreCalculatedAnswers(mContext);

        if (savedInstanceState != null) {
            int[] nums = savedInstanceState.getIntArray(getString(R.string
                    .ORIENTATION_CHANGE_NUMBERS_SAVE));
            List<String> results = savedInstanceState.getStringArrayList(getString(R.string
                    .ORIENTATION_CHANGE_NUMBERS_RESULT));
            if (nums != null && nums.length == 4 && results != null && results.size() > 0) {
                g = new Game(nums);
                result = new HashSet<>(results);
            } else {
                g = CalculationService.getInstance().generateGame(maxNum);
                result = CalculationService.getInstance().getAllAnswers();
            }
        } else {
            g = CalculationService.getInstance().generateGame(maxNum);
            result = CalculationService.getInstance().getAllAnswers();
        }
        setNumberImagesAndResult(g, cardViews, result);
        nextQuestion = (Button) findViewById(R.id.nextQuestion);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g = CalculationService.getInstance().generateGame(maxNum);
                result = CalculationService.getInstance().getAllAnswers();
                setNumberImagesAndResult(g, cardViews, result);
                clearAllViews();
            }
        });
        seeAnswer = (Button) findViewById(R.id.seeAnswerButton);
        seeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultView == null) {
                    resultView = (TextView) findViewById(R.id.resultView);
                    resultView.setMovementMethod(new ScrollingMovementMethod());
                }
                resultView.setText(resultString);
                if (userResultView == null) {
                    userResultView = (TextView) findViewById(R.id.userResultView);
                }
                userResultView.setText("");


            }
        });
        editText = (EditText) findViewById(R.id.answerEditText);
        editText.setFilters(getEquationFilter());

        userResultView = (TextView) findViewById(R.id.userResultView);

        checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = evaluateUserAnswer(g.nums);
                if (resultView == null) {
                    resultView = (TextView) findViewById(R.id.resultView);
                    resultView.setMovementMethod(new ScrollingMovementMethod());
                }
                resultView.setText("");
                if (valid) {
                    if (userResultView == null) {
                        userResultView = (TextView) findViewById(R.id.userResultView);
                    }
                    userResultView.setText(R.string.BINGO);
                } else {
                    if (userResultView == null) {
                        userResultView = (TextView) findViewById(R.id.userResultView);
                    }
                    userResultView.setText(R.string.TRY_AGAIN);
                }
            }
        });
    }


    private void loadSettings() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//        isFreeStyle = sharedPref.getBoolean(getString(R.string.settings_free_style), false);
        includeFaceCards = sharedPref.getBoolean(getString(R.string.settings_include_face_cards),
                false);
        maxNum = sharedPref.getInt(getString(R.string.settings_max_num), 10);
    }

    private void clearAllViews() {
//        if (userResultView == null) {
        userResultView = (TextView) findViewById(R.id.userResultView);
//        }
        userResultView.setText("");
//        if (editText == null) {
        editText = (EditText) findViewById(R.id.answerEditText);
//        }
        editText.setText("");
//        if (resultView == null) {
        resultView = (TextView) findViewById(R.id.resultView);
//        }
        resultView.setText("");
    }

    private boolean evaluateUserAnswer(final int[] nums) {
        if (editText == null) {
            editText = (EditText) findViewById(R.id.answerEditText);
            editText.setFilters(getEquationFilter());
        }
        String userInput = editText.getText().toString();
        if (userInput != null && !userInput.isEmpty()) {
            try {
                Expression e = new ExpressionBuilder(userInput).build();
                double result = e.evaluate();
                if (Math.abs(result - 24) > DIFF) {
                    return false;
                }
                int invalidNumber = 0;
                if ((invalidNumber = checkNumber(userInput, nums)) != 0) {
                    Toast.makeText(this, getString(R.string.invalid_number_entered, invalidNumber),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    private int checkNumber(final String userInput, final int[] nums) {
        String[] numbersStr = userInput.split("[^\\d]");
        int count = 0;
        int[] userNums = new int[4];
        for (int i = 0; i < numbersStr.length; i++) {
            if (numbersStr[i] != null && numbersStr[i].length() > 0) {
                userNums[count++] = Integer.valueOf(numbersStr[i]);
            }
            if (count == 4) {
                break;
            }
        }
        Arrays.sort(userNums);
        for (int i = 0; i < 4; i++) {
            if (nums[i] != userNums[i]) {
                return userNums[i];
            }
        }
        return 0;
    }

    private InputFilter[] getEquationFilter() {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (!Character.isDigit(c) && (c != '(' && c != ')' && c != '+' && c != '-'
                            && c != '*' && c != '/')) {
                        Toast.makeText(mContext, R.string.invalid_char_detected, Toast.LENGTH_LONG)
                                .show();
                        return "";
                    } else if (Character.isWhitespace(c)) {
                        Toast.makeText(mContext, R.string.white_space, Toast.LENGTH_LONG)
                                .show();
                        return "";
                    }
                }
                return null;
            }
        };
        return filters;
    }


    private void setNumberImagesAndResult(Game g, ImageView[] cardViews, Set<String> results) {
        setNumberImages(g.nums, cardViews);
        set24Answer(results);
    }

    private void set24Answer(Set<String> results) {
        results = CalculationService.getInstance().getAllAnswers();
        if (getResources().getBoolean(R.bool.single_answer)) {
            if (results == null || results.isEmpty()) {
                resultString = getString(R.string.no_answer_found);
            } else {
                for (String result : results) {
                    resultString = result;
                    break;
                }
            }
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 1;
            for (String result : results) {
                sb.append(count++).append(". ").append(result).append("\n");
            }
            if (sb.length() > 0) {
                resultString = sb.toString();
            } else {
                resultString = getString(R.string.no_answer_found);
            }
        }
    }

    private void setNumberImages(int[] nums, ImageView[] cardViews) {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    cardViews[i] = (ImageView) findViewById(R.id.card1);
                    break;
                case 1:
                    cardViews[i] = (ImageView) findViewById(R.id.card2);
                    break;
                case 2:
                    cardViews[i] = (ImageView) findViewById(R.id.card3);
                    break;
                case 3:
                    cardViews[i] = (ImageView) findViewById(R.id.card4);
                    break;
            }

            int cardType = r.nextInt(4) + 1;
            String card = null;
            switch (cardType) {
                case 1:
                    card = "c" + nums[i];
                    break;
                case 2:
                    card = "d" + nums[i];
                    break;
                case 3:
                    card = "h" + nums[i];
                    break;
                case 4:
                    card = "s" + nums[i];
                    break;
            }
            if (card != null) {
                Integer resourceId = ImgResourceFinder.getInstance().findResource(card);
                if (resourceId != null) {
                    cardViews[i].setImageResource(resourceId);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.switch_mode) {
            Intent intent = new Intent(this, UserInputActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.settings) {
            displaySettingsDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displaySettingsDialog() {
        // custom dialog
        final Dialog settings = new Dialog(mContext);
        settings.setContentView(R.layout.settings);
        settings.setTitle(getString(R.string.SETTINGS_HEADER));
        setSettings(settings);
        settings.show();

    }

    private void setSettings(final Dialog settings) {
        CheckBox includeFaceCardsCheckBox = (CheckBox) settings.findViewById(R.id
                .settings_include_face_cards);
        includeFaceCardsCheckBox.setChecked(includeFaceCards);
        includeFaceCardsCheckBox.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                includeFaceCards = isChecked;
                maxNum = isChecked ? 13 : 10;
            }
        });
//        CheckBox freeStyle = (CheckBox) settings.findViewById(R.id.settings_free_style);
//        freeStyle.setChecked(isFreeStyle);
//        freeStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                isFreeStyle = isChecked;
//            }
//        });

        Button confirm = (Button) settings.findViewById(R.id.settings_confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInstructionsForFaceCards(includeFaceCards);
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.settings_max_num), maxNum);
//                editor.putBoolean(getString(R.string.settings_free_style), isFreeStyle);
                editor.putBoolean(getString(R.string.settings_include_face_cards),
                        includeFaceCards);
                editor.commit();
                resetGame(includeFaceCards);
                settings.dismiss();
            }
        });

    }

    private void resetGame(boolean include) {
        if (!include && g.nums[3] > 10) {
            g = CalculationService.getInstance().generateGame(maxNum);
            result = CalculationService.getInstance().getAllAnswers();
            setNumberImagesAndResult(g, cardViews, result);
        }
    }

    private void setInstructionsForFaceCards(boolean include) {
        if (includeFaceCardsInstruction == null) {
            includeFaceCardsInstruction = (TextView) findViewById(R.id.includeFaceCardsInstruction);
        }
        includeFaceCardsInstruction.setVisibility(include ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (g != null && g.nums != null && g.nums.length == 4 && result != null && result.size()
                > 0) {
            outState.putIntArray(getString(R.string.ORIENTATION_CHANGE_NUMBERS_SAVE), g.nums);
            outState.putStringArrayList(getString(R.string.ORIENTATION_CHANGE_NUMBERS_RESULT), new
                    ArrayList<String>(result));
        }
        super.onSaveInstanceState(outState);
    }
}
