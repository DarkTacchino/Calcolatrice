package com.example.calcolatrice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import org.mariuszgromada.math.mxparser.*;
import android.view.View;
import java.text.DecimalFormat;
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
        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(addstring);
            display.setSelection(cursorPos + 1);
        } else {
            display.setText(String.format("%s%s%s", leftStr, addstring, rightStr));
            display.setSelection(cursorPos + 1);
        }
    }

    public void backspaceBTN(View view)
    {
        int cursorPos = display.getSelectionStart();
        int textLen = display.getText().length();

        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos - 1);
        }
    }

    public void clearBTN(View view)
    {
        display.setText("");
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
        updateText(".");
    }

    public void exponentBTN(View view) {
        updateText("^");
    }

    public void equalsBTN(View view) {
        String userExp = display.getText().toString();

        userExp = userExp.replaceAll("÷", "/");
        userExp = userExp.replaceAll("x", "*");

        Expression exp = new Expression(userExp);

        double result = exp.calculate();

        DecimalFormat df = new DecimalFormat("#.################"); // Fino a 16 decimali
        String resultString = df.format(result);

        display.setText(resultString);
        display.setSelection(resultString.length());
    }

    public void plusMinusBTN(View view) {
        String text = display.getText().toString();
        int cursorPos = display.getSelectionStart();

        // Trova l'inizio del numero corrente
        int start = cursorPos;
        while (start > 0) {
            char ch = text.charAt(start - 1);
            if (Character.isDigit(ch) || ch == '.') {
                start--;
            } else if (ch == '-' && (start == 1 || !Character.isDigit(text.charAt(start - 2)))) {
                // Se il carattere precedente è un meno e è all'inizio o è preceduto da un operatore, consideralo parte del numero.
                start--;
            } else {
                // Se trovi un operatore diverso da '-' o non è preceduto da un numero, interrompi
                break;
            }
        }

        // Gestione del caso in cui start è 0
        if (start > 0 && text.charAt(start - 1) == '-' && (start == 1 || !Character.isDigit(text.charAt(start - 2)))) {
            start--;
        }

        // Estrai il numero corrente
        String currentNumber = text.substring(start, cursorPos);

        // Se non c'è un numero valido, inserisci semplicemente il "-"
        if (currentNumber.isEmpty()) {
            updateText("-");
            return;
        }

        // Se il numero inizia con '-', rimuovilo; altrimenti aggiungilo
        if (currentNumber.startsWith("-")) {
            // Rimuove il segno meno
            String newNumber = currentNumber.substring(1);
            String newText = text.substring(0, start) + newNumber + text.substring(cursorPos);
            display.setText(newText);
            // Aggiorna la posizione del cursore spostandolo indietro di 1
            display.setSelection(cursorPos - 1);
        } else {
            // Aggiunge il segno meno
            String newNumber = "-" + currentNumber;
            String newText = text.substring(0, start) + newNumber + text.substring(cursorPos);
            display.setText(newText);
            // Aggiorna la posizione del cursore spostandolo in avanti di 1
            display.setSelection(cursorPos + 1);
        }
    }

    public void subtractBTN(View view) {
        updateText("-");
    }

    public void multiplyBTN(View view) {
        updateText("x");
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