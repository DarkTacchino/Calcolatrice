package com.example.calcolatrice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.math.BigDecimal;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    Button buttonPiu, buttonMeno, buttonMoltiplicazione, buttonDivisione, buttonCalcola, buttonVirgola;
    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button buttonCANC, buttonEliminaultimonum;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializzazione del TextView
        textView = findViewById(R.id.textView);

        // Assegnazione bottoni numerici
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        // Assegnazione bottoni operazioni
        buttonPiu = findViewById(R.id.buttonPiu);
        buttonMeno = findViewById(R.id.buttonMeno);
        buttonMoltiplicazione = findViewById(R.id.buttonMoltiplicazione);
        buttonDivisione = findViewById(R.id.buttonDivisione);
        buttonCalcola = findViewById(R.id.buttonCalcola);
        buttonVirgola = findViewById(R.id.buttonVirgola);
        buttonCANC = findViewById(R.id.buttonCANC);
        buttonEliminaultimonum = findViewById(R.id.buttonEliminaultimonum);

        // Listener per i numeri
        View.OnClickListener numberListener = v -> {
            if (v instanceof Button) {
                Button b = (Button) v;
                textView.append(b.getText().toString());
            }
        };

        // Listener per le operazioni
        View.OnClickListener operationListener = v -> {
            if (v instanceof Button) {
                Button b = (Button) v;
                textView.append(" " + b.getText().toString() + " ");
            }
        };

        // Assegno il listener ai bottoni numerici
        button0.setOnClickListener(numberListener);
        button1.setOnClickListener(numberListener);
        button2.setOnClickListener(numberListener);
        button3.setOnClickListener(numberListener);
        button4.setOnClickListener(numberListener);
        button5.setOnClickListener(numberListener);
        button6.setOnClickListener(numberListener);
        button7.setOnClickListener(numberListener);
        button8.setOnClickListener(numberListener);
        button9.setOnClickListener(numberListener);
        buttonVirgola.setOnClickListener(numberListener);

        // Assegno il listener ai bottoni operazioni
        buttonPiu.setOnClickListener(operationListener);
        buttonMeno.setOnClickListener(operationListener);
        buttonMoltiplicazione.setOnClickListener(operationListener);
        buttonDivisione.setOnClickListener(operationListener);

        // Bottone CANC per cancellare tutto
        buttonCANC.setOnClickListener(v -> textView.setText(""));

        // Bottone per eliminare l'ultimo carattere
        buttonEliminaultimonum.setOnClickListener(v -> {
            String currentText = textView.getText().toString();
            if (!currentText.isEmpty()) {
                textView.setText(currentText.substring(0, currentText.length() - 1));
            }
        });

        // Bottone "=" per calcolare il risultato
        buttonCalcola.setOnClickListener(v -> calcolaRisultato());
    }

    // Metodo per calcolare il risultato dell'espressione
    private void calcolaRisultato() {
        try {
            String espressione = textView.getText().toString();
            BigDecimal risultato = valutaEspressione(espressione);
            textView.setText(risultato.toPlainString());
        } catch (Exception e) {
            textView.setText("Errore");
        }
    }

    private BigDecimal valutaEspressione(String espressione) {
        Stack<BigDecimal> numeri = new Stack<>();
        Stack<Character> operazioni = new Stack<>();

        for (int i = 0; i < espressione.length(); i++) {
            char c = espressione.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder numero = new StringBuilder();
                while (i < espressione.length() && (Character.isDigit(espressione.charAt(i)) || espressione.charAt(i) == '.')) {
                    numero.append(espressione.charAt(i++));
                }
                i--;
                numeri.push(new BigDecimal(numero.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operazioni.isEmpty() && precedenza(operazioni.peek()) >= precedenza(c)) {
                    eseguiOperazione(numeri, operazioni.pop());
                }
                operazioni.push(c);
            }
        }

        while (!operazioni.isEmpty()) {
            eseguiOperazione(numeri, operazioni.pop());
        }

        return numeri.pop();
    }

    private int precedenza(char operatore) {
        return (operatore == '+' || operatore == '-'|| operatore == '*' || operatore == '/') ? 1 : 2 ;
    }

    private void eseguiOperazione(Stack<BigDecimal> numeri, char operatore) {
        BigDecimal b = numeri.pop();
        BigDecimal a = numeri.pop();

        switch (operatore) {
            case '+': numeri.push(a.add(b)); break;
            case '-': numeri.push(a.subtract(b)); break;
            case '*': numeri.push(a.multiply(b)); break;
            case '/': numeri.push(a.divide(b, 10, BigDecimal.ROUND_HALF_UP)); break;
        }
    }
}
