package com.example.calcolatrice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        display.setShowSoftInputOnFocus(false);

        display.setOnLongClickListener(v -> {
            if (getString(R.string.display).equals(display.getText().toString())) {
                display.setText("");
            }
            return true;
        });
    }
    private void updateText(String addstring) {
        String oldstr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String leftStr = oldstr.substring(0, cursorPos);
        String rightStr = oldstr.substring(cursorPos);
        if(getString(R.string.display).equals(display.getText().toString()))
        {
            display.setText(addstring);
            display.setSelection(cursorPos + 1);
        }
        else
        {
            display.setText(String.format("%s%s%s", leftStr, addstring, rightStr));
            display.setSelection(cursorPos + 1);
        }
    }

    public void parenthesesBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int openPar = 0;
        int closedPar = 0;
        int textLen = display.getText().length();

        // Evitare errori di accesso a stringhe vuote
        if (textLen == 0) {
            updateText("(");
            display.setSelection(cursorPos + 1);
            return;
        }

        // Contiamo le parentesi aperte e chiuse
        for (int i = 0; i < cursorPos; i++) {
            String currentChar = display.getText().toString().substring(i, i + 1);
            if (currentChar.equals("(")) {
                openPar++;
            } else if (currentChar.equals(")")) {
                closedPar++;
            }
        }

        String lastChar = display.getText().toString().substring(textLen - 1, textLen);

        // Logica per inserire parentesi
        if (openPar == closedPar || lastChar.equals("(")) {
            updateText("(");
            display.setSelection(cursorPos + 1);
        } else if (closedPar < openPar && !lastChar.equals(")")) {
            updateText(")");
            display.setSelection(cursorPos + 1);
        }
    }

    public void pointBTN(View view) {
        updateText(",");
    }

    public void exponentBTN(View view) {
        updateText("^");
    }

    public void equalsBTN(View view) {
        updateText("=");
    }

    public void subtractBTN(View view) {
        updateText("-");
    }

    public void multiplyBTN(View view) {
        updateText("X");
    }
    public void addBTN(View view) {
        updateText("+");
    }
    public void divideBTN(View view) {
        updateText("÷");
    }





    public void nineBTN(View view) {
        updateText("9");
    }
    public void eightBTN(View view) {
        updateText("8");
    }
    public void sevenBTN(View view) {
        updateText("7");
    }
    public void sixBTN(View view) {
        updateText("6");
    }
    public void fiveBTN(View view) {
        updateText("5");
    }
    public void fourBTN(View view) {
        updateText("4");
    }
    public void threeBTN(View view) {
        updateText("3");
    }
    public void twoBTN(View view) {
        updateText("2");
    }
    public void oneBTN(View view) {
        updateText("1");
    }
    public void zeroBTN(View view) {
        updateText("0");
    }
}