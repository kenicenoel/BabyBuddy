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



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setVisibility(View.INVISIBLE);
        settingsBuddy = SettingsBuddy.getInstance(getApplicationContext());

        continueLoginProcedure();

    }




    private void continueLoginProcedure()
    {
        //        Check if the user has logged in before and if so, redirect them to the main menu
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













}
