package ca.davidpellegrini.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener {

    private float tipPercent = 0.2f;
    Button minusButton, plusButton;
    TextView tipPercentTextView;
    final private NumberFormat percentFormat = NumberFormat.getPercentInstance();
    final private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    EditText billAmountEditText;
    RadioGroup numPeopleRadioGroup;
    SeekBar tipPercentSB;
    int numPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //System.out.println("Hello World!");
        //Log.d("onCreate", "THIS CODE DOESN'T WORK");
        //Toast.makeText(this, "Hello World!", Toast.LENGTH_LONG).show();

        /*
            Get a button reference
            subscribe to the onClick
         */

        // get references to the Views
            //Buttons
        minusButton = findViewById(R.id.decreaseButton);
        plusButton = findViewById(R.id.increaseButton);
            //TextViews
        tipPercentTextView = findViewById(R.id.tipPercentTextView);
        billAmountEditText = findViewById(R.id.billAmountEditText);
        numPeopleRadioGroup = findViewById(R.id.numPeopleRadioGroup);
            //SeekBar
        tipPercentSB = findViewById(R.id.tipPercentSeekBar);

        // set click listeners
        minusButton.setOnClickListener(this);
        plusButton.setOnClickListener(this);
        billAmountEditText.addTextChangedListener(this);
        tipPercentSB.setOnSeekBarChangeListener(this);

        // set up defaults
        tipPercentTextView.setText(percentFormat.format(tipPercent));

        /*
        // Anonymous classes can allow for modularized click listeners

        View.OnClickListener listener = v -> {
            switch(v.getId()){
                case R.id.decreaseButton:
                    tipPercent -= 0.05f;
                    Toast.makeText(getApplicationContext(),
                            "Decreasing the tip! " + tipPercent,
                            Toast.LENGTH_LONG).show();
                    break;
                case R.id.increaseButton:
                    tipPercent += 0.05f;
                    break;
            }
        };

        minusButton = findViewById(R.id.decreaseButton);
        minusButton.setOnClickListener(listener);
        Button plusButton = findViewById(R.id.increaseButton);
        plusButton.setOnClickListener(listener);
         */


        /*
        // Anonymous inner classes *tend* to cause a lot of repeated code
        Button plusButton = findViewById(R.id.increaseButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override // The @ sign is the start of an annotation, or fancy comment
            public void onClick(View view) {
                tipPercent += 0.05f;
                Toast.makeText(getApplicationContext(),
                    "Increasing the tip! " + tipPercent,
                    Toast.LENGTH_LONG).show();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override // The @ sign is the start of an annotation, or fancy comment
            public void onClick(View view) {
                tipPercent -= 0.05f;
                Toast.makeText(getApplicationContext(),
                        "Decreasing the tip! " + tipPercent,
                        Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    // this method is being used through the activity_main.xml file
    public void easterEgg(View view) {
        Toast.makeText(this, "Surprise!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if(id == R.id.decreaseButton){
            tipPercent -= 0.05f;
            if(tipPercent < 0)
                tipPercent = 0;
        }
        else if(id == R.id.increaseButton){
            tipPercent += 0.05f;
            if(tipPercent > 1)
                tipPercent = 1;
        }
        updateScreen();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // no work to be done today
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        updateScreen();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // no work to be done today
    }

    private void updateScreen(){
        tipPercentSB.setProgress((int)(tipPercent*100));
        String billAmountString = billAmountEditText.getText().toString();
        float billAmount = 0f;
        try{
            billAmount = Float.parseFloat(billAmountString);
        }
        catch(Exception ex){
            // default to 0
        }

        tipPercentTextView.setText(percentFormat.format(tipPercent));

        float tipAmount = billAmount * tipPercent;
        TextView tipAmountTextView = findViewById(R.id.tipAmountTextView);
        tipAmountTextView.setText(currencyFormat.format(tipAmount));

        float totalAmount = 0;

        long checkedId = numPeopleRadioGroup.getCheckedRadioButtonId();
        int numPeople;

        if(checkedId == R.id.onePersonRadioButton){
            numPeople = 1;
        }
        else if(checkedId == R.id.threePeopleRadioButton){
            numPeople = 3;
        }
        else if(checkedId == R.id.fourPeopleRadioButton){
            numPeople = 4;
        }
        else{
            numPeople = 2;
        }

        float individualAmount = totalAmount / numPeople;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean userUpdate) {
        if(userUpdate){
            tipPercent = progress / 100f;
            updateScreen();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // no work to be done
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // no work to be done
    }
}