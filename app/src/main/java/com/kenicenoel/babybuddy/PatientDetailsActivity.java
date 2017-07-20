package com.kenicenoel.babybuddy;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kenicenoel.babybuddy.adapters.ConditionsListAdaptor;
import com.kenicenoel.babybuddy.adapters.EncountersListAdaptor;
import com.kenicenoel.babybuddy.objects.Condition;
import com.kenicenoel.babybuddy.objects.Encounter;
import com.kenicenoel.babybuddy.objects.Patient;

import java.util.ArrayList;

public class PatientDetailsActivity extends AppCompatActivity
{


    private DatabaseHandler databaseHandler;
    private ArrayList<Patient> patientsList = new ArrayList<>();
    private TextView name;
    private TextView dateOfBirth;
    private TextView sex;
    private TextView address;

    private ArrayList<Condition> conditionsList = new ArrayList<>();
    private ArrayList<Encounter> encountersList = new ArrayList<>();
    private ConditionsListAdaptor conditionsListAdaptor;
    private EncountersListAdaptor encountersListAdaptor;
    private RecyclerView conditionsRecyclerview;
    private RecyclerView encountersRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        name = (TextView) findViewById(R.id.patientName);
        dateOfBirth = (TextView) findViewById(R.id.dob);
        sex = (TextView) findViewById(R.id.sex);
        address = (TextView) findViewById(R.id.addr);
        conditionsListAdaptor = new ConditionsListAdaptor(conditionsList);
        encountersListAdaptor = new EncountersListAdaptor(encountersList);
        conditionsRecyclerview = (RecyclerView) findViewById(R.id.conditonsRecyclerView);
        encountersRecyclerview = (RecyclerView) findViewById(R.id.encountersRecyclerView);
        conditionsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        encountersRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        conditionsRecyclerview.setAdapter(conditionsListAdaptor);
        encountersRecyclerview.setAdapter(encountersListAdaptor);

        // get handler for the database
        databaseHandler = new DatabaseHandler(this, null, null, 1);

        int patientId = getIntent().getIntExtra("PatientId", -1);
        if (patientId != -1)
        {
            loadPatientDataFromDatabase(patientId);
            loadPatientConditions(patientId);
            loadPatientEncounters(patientId);

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
            String gender = cursor.getString(sexIndex);
            String addr1 = cursor.getString(addr1Index);
            String addr2 = cursor.getString(addr2Index);
            String city = cursor.getString(cityIndex);
            String stateParish = cursor.getString(stateParishIndex);
            String country = cursor.getString(countryIndex);

            name.setText(fname+ " "+lname);
            dateOfBirth.setText(dob);
            sex.setText(gender);
            address.setText(addr1+", "+addr2+", "+city+", "+stateParish+", "+country);


        }

    }

    private void loadPatientConditions(int id)
    {
        Cursor cursor = databaseHandler.getConditionsForPatient(id);
        System.out.println(DatabaseUtils.dumpCursorToString(cursor));
        while (cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_ID);
            int statusIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_STATUS);
            int severityIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_SEVERITY);
            int codeIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_CODE);
            int encounterIdIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_ENCOUNTER_ID);


            int _id = cursor.getInt(idIndex);
            String status = cursor.getString(statusIndex);
            String severity = cursor.getString(severityIndex);
            String code = cursor.getString(codeIndex);
            int encounter = cursor.getInt(encounterIdIndex);

            Condition condition = new Condition(_id, status, severity, code, encounter);
            conditionsList.add(condition);
            conditionsListAdaptor.notifyDataSetChanged();


        }

    }


    private void loadPatientEncounters(int id)
    {
        Cursor cursor = databaseHandler.getEncountersForPatient(id);
        System.out.println(DatabaseUtils.dumpCursorToString(cursor));
        while (cursor.moveToNext())
        {
            int idIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_ID);
            int patientIdIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_PATIENT_ID);
            int startTimeIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_START_TIME);
            int endTimeIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_END_TIME);
            int typeIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_TYPE);
            int notesIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_NOTES);
            int practitionerIdIndex = cursor.getColumnIndex(DatabaseHandler.COLUMN_PRACTIONER_ID);


            int _id = cursor.getInt(idIndex);
            int patientId = cursor.getInt(patientIdIndex);
            String start = cursor.getString(startTimeIndex);
            String end = cursor.getString(endTimeIndex);
            String type = cursor.getString(typeIndex);
            String notes = cursor.getString(notesIndex);
            int pracId = cursor.getInt(practitionerIdIndex);

            Encounter encounter = new Encounter(_id, patientId, start, end, type, notes, pracId);
            encountersList.add(encounter);
            encountersListAdaptor.notifyDataSetChanged();


        }

    }

    public void newEncounter(View view)
    {

    }

    public void newCondition(View view)
    {

    }
}
