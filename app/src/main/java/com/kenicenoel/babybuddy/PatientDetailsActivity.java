package com.kenicenoel.babybuddy;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.kenicenoel.babybuddy.objects.Patient;

import java.util.ArrayList;

public class PatientDetailsActivity extends AppCompatActivity
{


    private DatabaseHandler databaseHandler;
    private ArrayList<Patient> patientsList = new ArrayList<>();
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        name = (TextView) findViewById(R.id.patientName);

        // get handler for the database
        databaseHandler = new DatabaseHandler(this, null, null, 1);

        int patientId = getIntent().getIntExtra("PatientId", -1);
        if (patientId != -1)
        {
            loadPatientDataFromDatabase(patientId);
        }
        else
        {
            Toast.makeText(this, "Incorrect Id supplied", Toast.LENGTH_SHORT).show();
        }

    }


    private void loadPatientDataFromDatabase(int id)
    {
        Cursor cursor = databaseHandler.getPatientById(id);
        while (cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_ID);
            int firstNameIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_GIVEN_NAME);
            int lastNameIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_FAMILY_NAME);
            int dateOfBirthIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_BIRTHDATE);
            int sexIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_SEX);
            int addr1Index = cursor.getColumnIndex(DatabaseHandler.COLUMN_ADDR1);
            int addr2Index = cursor.getColumnIndex(DatabaseHandler.COLUMN_ADDR2);
            int cityIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_CITY);
            int stateParishIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_STATE_PARISH);
            int countryIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_COUNTRY);

            int _id = cursor.getInt(idIndex);
            String fname = cursor.getString(firstNameIndex);
            String lname = cursor.getString(lastNameIndex);
            String dob = cursor.getString(dateOfBirthIndex);
            String sex = cursor.getString(sexIndex);
            String addr1 = cursor.getString(addr1Index);
            String addr2 = cursor.getString(addr2Index);
            String city = cursor.getString(cityIndex);
            String stateParish = cursor.getString(stateParishIndex);
            String country = cursor.getString(countryIndex);

            name.setText(fname+ " "+lname);



        }

    }
}
