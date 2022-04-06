package ca.davidpellegrini.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, TextWatcher, SeekBar.OnSeekBarChangeListener,
        RadioGroup.OnCheckedChangeListener {

    private float tipPercent = 0.2f;
    Button minusButton, plusButton;
    TextView tipPercentTextView;
    final private NumberFormat percentFormat = NumberFormat.getPercentInstance();
    final private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    EditText billAmountEditText;
    RadioGroup numPeopleRadioGroup;
    SeekBar tipPercentSB;
    int numPeople;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

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
        numPeopleRadioGroup.setOnCheckedChangeListener(this);

        // set up defaults
        tipPercentTextView.setText(percentFormat.format(tipPercent));


        /*
        // turning dice objectst into a massive string
        // numSides,type,sideUp
        String obj = 6 + "," + "d6" + "," + 5 + ";";
        obj += 20 + "," + "d20" + "," + 8 + ";";

        //taking the dice objects from a massive string to arrays
        String[] objects = obj.split(";");

        String[][] dice = new String[objects.length][3];
        for(int i = 0; i < dice.length; ++i){
            dice[i] = objects[i].split(",");
            //Die newDie = new Die(dice[i][0], dice[i][1]);
            /newDie.setSideUp(dice[i][2]);
        }

        Log.v("DiceInfo:", dice[0][1]);
         */


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

    @Override
    protected void onStart() {
        super.onStart();
        String billAmountString = prefs.getString("billAmountString", "");
        billAmountEditText.setText(billAmountString);
        tipPercent = prefs.getFloat("tipPercent", .2f);
        numPeople = prefs.getInt("numPeople", 2);
        if(numPeople == 1){
            numPeopleRadioGroup.check(R.id.onePersonRadioButton);
        }
        else if(numPeople == 3){
            numPeopleRadioGroup.check(R.id.threePeopleRadioButton);
        }
        else if(numPeople == 4){
            numPeopleRadioGroup.check(R.id.fourPeopleRadioButton);
        }
        else{ //numPeople == 2
            numPeopleRadioGroup.check(R.id.twoPeopleRadioButton);
        }
        updateScreen();
    }

    @Override
    protected void onStop() {



        SharedPreferences.Editor editor = prefs.edit();
        Log.v("onStop", "onStop");
        boolean saveVals = prefs.getBoolean("save_values_pref", false);
        if(saveVals){
            editor.putString("billAmountString", billAmountEditText.getText().toString());
            editor.putFloat("tipPercent", tipPercent);
            editor.putInt("numPeople", numPeople);
        }
        else{
            editor.clear();
            editor.putBoolean("save_values_pref", false);
        }

        //editor.commit(); //commit applies changes immediately
        //apply is async
        editor.apply();

        super.onStop();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        long id = item.getItemId();
        if(id == R.id.settings_menu){
            Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        else if(id == R.id.about_menu){
            //startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    // this method is being used through the activity_main.xml file

    /**
     *
     * @param view
     * @return
     */
    public String easterEgg(View view) {
        Toast.makeText(
                this, "Surprise!", Toast.LENGTH_SHORT)
            .show();
        return "1";
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
        String billAmountString = billAmountEditText.getText().toString();
        float billAmount = 0f;
        try{
            billAmount = Float.parseFloat(billAmountString);
        }
        catch(Exception ex){
            billAmount = 0;
        }


        float tipAmount = billAmount * tipPercent;
        float totalAmount = billAmount + tipAmount;

        String roundingPref = prefs.getString("rounding_pref", "0");
        if(roundingPref.equals(getString(R.string.round_tip))){
            tipAmount = Math.round(tipAmount);
        }
        else if(roundingPref.equals(getString(R.string.round_total))){
            totalAmount = Math.round(totalAmount);
            tipAmount = totalAmount - billAmount;
            tipPercent = tipAmount / billAmount;
        }

        float individualAmount = totalAmount / numPeople;



        TextView tipAmountTextView = findViewById(R.id.tipAmountTextView);
        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);
        TextView individualAmountTextView= findViewById(R.id.individualAmountTextView);

        tipPercentSB.setProgress((int)(tipPercent*100));
        tipPercentTextView.setText(percentFormat.format(tipPercent));
        tipAmountTextView.setText(currencyFormat.format(tipAmount));
        totalAmountTextView.setText(currencyFormat.format(totalAmount));
        individualAmountTextView.setText(currencyFormat.format(individualAmount));
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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        TableRow individualRow = findViewById(R.id.individualTableRow);

        if(individualRow.getVisibility() != View.VISIBLE)
            individualRow.setVisibility(View.VISIBLE);

        if(id == R.id.onePersonRadioButton){
            numPeople = 1;
            individualRow.setVisibility(View.INVISIBLE);
        }
        else if(id == R.id.threePeopleRadioButton){
            numPeople = 3;
        }
        else if(id == R.id.fourPeopleRadioButton){
            numPeople = 4;
        }
        else{
            numPeople = 2;
        }
        updateScreen();
    }
}