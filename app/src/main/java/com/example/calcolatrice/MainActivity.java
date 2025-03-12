package com.example.calcolatrice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import org.mariuszgromada.math.mxparser.*; // Libreria per il parsing e la valutazione delle espressioni matematiche
import android.view.View;
import java.text.DecimalFormat; // Classe per formattare i numeri
import java.text.DecimalFormatSymbols; // Classe per personalizzare i simboli di formattazione
import java.util.Locale; // Classe per gestire le impostazioni locali (formato numeri, date, ecc.)
import android.widget.EditText; // Componente per l'input e la visualizzazione di testo

public class MainActivity extends AppCompatActivity {

    private EditText display; // Dichiarazione della variabile per il campo di testo (EditText)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Chiama il metodo onCreate della superclasse (AppCompatActivity)
        setContentView(R.layout.activity_main); // Imposta il layout dell'activity (activity_main.xml)
        display = findViewById(R.id.display); // Inizializza la variabile display trovando l'EditText tramite il suo ID
        display.setShowSoftInputOnFocus(false); // Disabilita la visualizzazione automatica della tastiera virtuale quando l'EditText riceve il focus

        // Imposta un listener per il long click (pressione prolungata) sull'EditText
        display.setOnLongClickListener(v -> {
            // Se il testo corrente dell'EditText è uguale alla stringa predefinita (R.string.display),
            // cancella il contenuto dell'EditText
            if (getString(R.string.display).equals(display.getText().toString())) {
                display.setText("");
            }
            return true; // Restituisce true per indicare che l'evento è stato gestito
        });
    }

    // Metodo per aggiornare il testo nell'EditText
    private void updateText(String addstring) {
        String oldstr = display.getText().toString(); // Ottiene il testo corrente dall'EditText
        int cursorPos = display.getSelectionStart(); // Ottiene la posizione corrente del cursore
        String leftStr = oldstr.substring(0, cursorPos); // Ottiene la parte sinistra del testo rispetto al cursore
        String rightStr = oldstr.substring(cursorPos); // Ottiene la parte destra del testo rispetto al cursore

        // Se il testo corrente dell'EditText è uguale alla stringa predefinita (R.string.display),
        // imposta il testo dell'EditText con la stringa da aggiungere
        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(addstring);
            display.setSelection(cursorPos + 1); // Sposta il cursore alla posizione successiva
        } else {
            // Altrimenti, inserisce la stringa da aggiungere nel testo esistente
            display.setText(String.format("%s%s%s", leftStr, addstring, rightStr));
            display.setSelection(cursorPos + 1); // Sposta il cursore alla posizione successiva
        }
    }

    // Metodo per gestire l'evento di pressione del pulsante "backspace" (cancella un carattere)
    public void backspaceBTN(View view) {
        int cursorPos = display.getSelectionStart(); // Ottiene la posizione corrente del cursore
        int textLen = display.getText().length(); // Ottiene la lunghezza del testo corrente

        // Se il cursore non è all'inizio e il testo non è vuoto,
        // cancella il carattere precedente alla posizione del cursore
        if (cursorPos != 0 && textLen != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText(); // Ottiene il testo come SpannableStringBuilder (per la modifica)
            selection.replace(cursorPos - 1, cursorPos, ""); // Sostituisce il carattere precedente con una stringa vuota (cancellazione)
            display.setText(selection); // Imposta il nuovo testo nell'EditText
            display.setSelection(cursorPos - 1); // Sposta il cursore alla posizione precedente
        }
    }

    // Metodo per gestire l'evento di pressione del pulsante "clear" (cancella tutto)
    public void clearBTN(View view) {
        display.setText(""); // Imposta il testo dell'EditText a una stringa vuota
    }

    // Metodo per gestire l'evento di pressione del pulsante delle parentesi
    public void parenthesesBTN(View view) {
        int cursorPos = display.getSelectionStart(); // Ottiene la posizione corrente del cursore
        int openPar = 0; // Inizializza il contatore per le parentesi aperte
        int closedPar = 0; // Inizializza il contatore per le parentesi chiuse
        int textLen = display.getText().length(); // Ottiene la lunghezza del testo corrente

        // Evitare errori di accesso a stringhe vuote
        if (textLen == 0) {
            updateText("("); // Aggiunge una parentesi aperta
            display.setSelection(cursorPos + 1); // Sposta il cursore
            return;
        }

        // Contiamo le parentesi aperte e chiuse fino alla posizione del cursore
        for (int i = 0; i < cursorPos; i++) {
            String currentChar = display.getText().toString().substring(i, i + 1); // Ottiene il carattere alla posizione i
            if (currentChar.equals("(")) {
                openPar++; // Incrementa il contatore delle parentesi aperte
            } else if (currentChar.equals(")")) {
                closedPar++; // Incrementa il contatore delle parentesi chiuse
            }
        }

        String lastChar = display.getText().toString().substring(textLen - 1, textLen); // Ottiene l'ultimo carattere del testo

        // Logica per inserire parentesi
        // Se il numero di parentesi aperte è uguale al numero di parentesi chiuse
        // oppure se l'ultimo carattere è una parentesi aperta, aggiunge una parentesi aperta
        if (openPar == closedPar || lastChar.equals("(")) {
            updateText("(");
            display.setSelection(cursorPos + 1);
        } else if (closedPar < openPar && !lastChar.equals(")")) {
            // Altrimenti, se ci sono più parentesi aperte che chiuse e l'ultimo carattere non è una parentesi chiusa,
            // aggiunge una parentesi chiusa
            updateText(")");
            display.setSelection(cursorPos + 1);
        }
    }

    // Metodo per gestire l'evento di pressione del pulsante del punto decimale
    public void pointBTN(View view) {
        updateText("."); // Aggiunge un punto decimale
    }

    // Metodo per gestire l'evento di pressione del pulsante dell'esponente
    public void exponentBTN(View view) {
        updateText("^"); // Aggiunge il simbolo di esponente
    }

    // Metodo per gestire l'evento di pressione del pulsante "equals" (calcola il risultato)
    public void equalsBTN(View view) {
        String userExp = display.getText().toString(); // Ottiene l'espressione matematica dall'EditText

        userExp = userExp.replaceAll("÷", "/"); // Sostituisce il simbolo di divisione "÷" con "/"
        userExp = userExp.replaceAll("x", "*"); // Sostituisce il simbolo di moltiplicazione "x" con "*"

        Expression exp = new Expression(userExp); // Crea un oggetto Expression per valutare l'espressione

        double result = exp.calculate(); // Calcola il risultato dell'espressione

        // Imposta il Locale per formattare i numeri secondo le convenzioni italiane
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALIAN);
        symbols.setDecimalSeparator(','); // Imposta la virgola come separatore decimale
        symbols.setGroupingSeparator('.'); // Imposta il punto come separatore di migliaia

        DecimalFormat df = new DecimalFormat("#,##0.##", symbols); // Pattern per la formattazione

        String resultString = df.format(result); // Formatta il risultato double in una String

        display.setText(resultString); // Imposta il testo dell'EditText con il risultato formattato
        display.setSelection(resultString.length()); // Sposta il cursore alla fine del risultato
    }

    // Metodo per gestire l'evento di pressione del pulsante "più/meno" (cambia il segno di un numero)
    public void plusMinusBTN(View view) {
        String text = display.getText().toString(); // Ottiene il testo corrente dall'EditText
        int cursorPos = display.getSelectionStart(); // Ottiene la posizione corrente del cursore

        // Trova l'inizio del numero corrente
        int start = cursorPos;
        while (start > 0) {
            char ch = text.charAt(start - 1); // Ottiene il carattere precedente
            // Se il carattere precedente è una cifra o un punto decimale, continua a cercare
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

    // Metodo per gestire l'evento di pressione del pulsante di sottrazione
    public void subtractBTN(View view) {
        updateText("-"); // Aggiunge il simbolo di sottrazione
    }

    // Metodo per gestire l'evento di pressione del pulsante di moltiplicazione
    public void multiplyBTN(View view) {
        updateText("x"); // Aggiunge il simbolo di moltiplicazione
    }

    // Metodo per gestire l'evento di pressione del pulsante di addizione
    public void addBTN(View view) {
        updateText("+"); // Aggiunge il simbolo di addizione
    }

    // Metodo per gestire l'evento di pressione del pulsante di divisione
    public void divideBTN(View view) {
        updateText("÷"); // Aggiunge il simbolo di divisione
    }

    // Metodi per gestire l'evento di pressione dei pulsanti numerici
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