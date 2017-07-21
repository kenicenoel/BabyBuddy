package com.kenicenoel.babybuddy;

/**
 * Created by Software Developer on 1/18/2016.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kenicenoel.babybuddy.objects.Condition;
import com.kenicenoel.babybuddy.objects.Encounter;
import com.kenicenoel.babybuddy.objects.Patient;
import com.kenicenoel.quicktools.SettingsBuddy;


public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText u;
    private EditText p;
    private TextView k;
    private TextView e;


    public final String DEFAULT = "N/A"; // A default constant for use with sharedPreferences
    public boolean isLoggedIn = false;
    private Button loginButton;
    private SettingsBuddy settingsBuddy;
    private String username;
    private String password;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setVisibility(View.INVISIBLE);
        settingsBuddy = SettingsBuddy.getInstance(getApplicationContext());

        // get handler for the database
        databaseHandler = new DatabaseHandler(this, null, null, 1);
        continueLoginProcedure();

    }




    private void continueLoginProcedure()
    {
        //  Check if the user has logged in before and if so, redirect them to the main menu
        isLoggedIn = isUserCredentialsStored();
        if(isLoggedIn)
        {
            Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        else
        {
            // Find the username, password and key fields
            u = (EditText) findViewById(R.id.usernameField);
            p = (EditText) findViewById(R.id.passwordField);

            // click the login button after password
            p.setOnEditorActionListener(new EditText.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE)
                    {
                        loginUser();
                        return true;
                    }
                    return false;
                }
            });

            e = (TextView) findViewById(R.id.loginMessage);

            loginButton.setVisibility(View.VISIBLE);


            if (loginButton != null)
            {
                loginButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                       loginUser();
                    }
                });
            }

        }

    }

    private void loginUser()
    {
        // Get the text from the username, password and key fields
        username = u.getText().toString();
        password = p.getText().toString();


        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please wait while we log you in.", Snackbar.LENGTH_LONG);
        snackbar.show();


        // Store the username, password, full name and key to enable automatic login each time

        settingsBuddy.saveData("username", username);
        settingsBuddy.saveData("password", password);


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        // create dummy data before starting the app
        createDummyPatientData();
        createDummyConditionsData();
        createDummyEncounters();

        startActivity(intent);
        finish();

    }




    // Checks if the user credentials are stored
    public boolean isUserCredentialsStored()
    {

        String usr = settingsBuddy.getData("username");
        String pass = settingsBuddy.getData("password");

        if( !usr.equals(DEFAULT) &&  !pass.equals(DEFAULT))
        {
            return true;
        }

        return false;

    }

    private void createDummyConditionsData()
    {
        databaseHandler.deleteAllConditions();
        Condition condition = new Condition(2, "Acute", "Moderate", "ICD-10", 1);
        Condition conditionB = new Condition(2, "Acute", "Moderate", "ICD-10", 2);
        Condition conditionC = new Condition(2, "Chronic", "Mild", "ICD-10", 3);
        Condition conditionD = new Condition(1, "Acute", "Severe", "ICD-10", 1);
        Condition conditionE = new Condition(1, "Chronic", "Moderate", "ICD-10", 2);
        Condition conditionF = new Condition(3, "Acute", "Moderate", "ICD-10", 1);
        Condition conditionG = new Condition(3, "Acute", "Mild", "ICD-10", 2);
        Condition conditionH = new Condition(4, "Acute", "Severe", "ICD-10", 1);

        databaseHandler.addCondition(condition);
        databaseHandler.addCondition(conditionB);
        databaseHandler.addCondition(conditionC);
        databaseHandler.addCondition(conditionD);
        databaseHandler.addCondition(conditionE);
        databaseHandler.addCondition(conditionF);
        databaseHandler.addCondition(conditionG);
        databaseHandler.addCondition(conditionH);
    }

    private void createDummyPatientData()
    {
        databaseHandler.deleteAllPatients();
        Patient a = new Patient("Donna", "Walker", "2016-07-13", "Female", "Old Creek", "Telescope", "St Andrew", "Grenada");
        Patient b = new Patient("Jurell", "Benjamin-Browne", "2015-03-18", "Male", "User Road", "Ocean Spray", "Laments", "Grenada");
        Patient c = new Patient("Ted", "Roosevelt", "2016-02-10", "Male", "Riverlet Drive", "L'esterre", "Hillsborough", "Carriacou");
        Patient d = new Patient("Sarah", "Pascal", "2017-02-15", "Female", "17 L'ordinateur Rd", "Grenville", "St Andrew", "Grenada");
        Patient e = new Patient("Seesharp", "Lang", "2014-10-10", "Female", "Locus Street", "IDE Blvd", "St Georges", "Grenada");
        Patient f = new Patient("Donald", "Covfefe", "2016-11-11", "Male", "Tower B", "Etang Drive", "St Mark", "Petite Martinique");


        // save patient to database
        databaseHandler.addPatient(a);
        databaseHandler.addPatient(b);
        databaseHandler.addPatient(c);
        databaseHandler.addPatient(d);
        databaseHandler.addPatient(e);
        databaseHandler.addPatient(f);


    }

    private void createDummyEncounters()
    {
        Encounter encounter = new Encounter(2, "12.00 pm", "12.35 pm", "General checkup", "Nothing out of the ordinary", 1);
        Encounter encounterB = new Encounter(2, "2.30 pm", "03.10 pm", "Follow up", "Nothing out of the ordinary", 1);
        Encounter encounterC = new Encounter(2, "8.00 am", "08.15 am", "Quick checkup", "Routine vaccine shot.", 1);
        Encounter encounterD = new Encounter(1, "10.30 am", "11.15 am", "General checkup", "Nothing out of the ordinary", 1);
        Encounter encounterE = new Encounter(3, "10.30 am", "11.15 am", "Follow up", "Blood pressure elevated", 1);
        Encounter encounterF = new Encounter(4, "10.30 am", "11.15 am", "General checkup", "Nothing out of the ordinary", 1);

        databaseHandler.addEncounter(encounter);
        databaseHandler.addEncounter(encounterB);
        databaseHandler.addEncounter(encounterC);
        databaseHandler.addEncounter(encounterD);
        databaseHandler.addEncounter(encounterE);
        databaseHandler.addEncounter(encounterF);

    }













}
