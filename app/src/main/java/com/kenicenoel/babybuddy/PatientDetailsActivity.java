package com.kenicenoel.babybuddy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kenicenoel.babybuddy.adapters.ConditionsListAdaptor;
import com.kenicenoel.babybuddy.adapters.EncountersListAdaptor;
import com.kenicenoel.babybuddy.objects.Condition;
import com.kenicenoel.babybuddy.objects.Encounter;
import com.kenicenoel.babybuddy.objects.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int patientId;
    private Button exportDataButton;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;

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
        exportDataButton = (Button) findViewById(R.id.exportDataButton);
        exportDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Check if the device is currently running an android version less than 6.0
                if (Build.VERSION.SDK_INT < 23)
                {
                    // If android version is less than 6, it is not necessary to check for permissions
                    exportDataToJSON();
                }

                // since android version is at least 6 or higher, check if permissions are already granted
                else
                {
                    // if permissions are already granted continue setup logic and if not request permissions
                    if (checkAndRequestPermissions()) {
                        //If you have already permitted the permission
                        exportDataToJSON();

                    }

                    exportDataToJSON();
                }
            }
        });

        // get handler for the database
        databaseHandler = new DatabaseHandler(this, null, null, 1);




        patientId = getIntent().getIntExtra("PatientId", -1);
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

    private boolean checkAndRequestPermissions()
    {

        int storagePermissionRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int storagePermissionWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermissionRead != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (storagePermissionWrite != PackageManager.PERMISSION_GRANTED)
        {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,

                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:

                Map<String, Integer> perms = new HashMap<String, Integer>();

                // Initialize
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);


                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                {
                    perms.put(permissions[i], grantResults[i]);
                }

                // Check if all permissions are granted
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
                {
                    // All Permissions Granted
                    exportDataToJSON();
                }

                else
                {
                    // You did not accept the request can not use the functionality
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Your attention is required");
                    builder.setMessage("In order to export data, we need permission to read and write to your device.");
                    builder.setPositiveButton("Give permission",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    checkAndRequestPermissions();
                                    dialog.dismiss();
                                }
                            });

                    builder.setNegativeButton("Decline",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.show();
                }
                break;
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

    private void exportDataToJSON()
    {
        String jsonData= "";
        JSONObject mainObj = new JSONObject();

        JSONArray patients = new JSONArray();
        JSONArray conditions = new JSONArray();
        JSONArray encounters = new JSONArray();


        Cursor cursor = databaseHandler.getPatientById(patientId);
        while (cursor.moveToNext())
        {
            JSONObject patient = new JSONObject();
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

            int patientId = cursor.getInt(idIndex);
            String fname = cursor.getString(firstNameIndex);
            String lname = cursor.getString(lastNameIndex);
            String dob = cursor.getString(dateOfBirthIndex);
            String sex = cursor.getString(sexIndex);
            String addr1 = cursor.getString(addr1Index);
            String addr2 = cursor.getString(addr2Index);
            String city = cursor.getString(cityIndex);
            String stateParish = cursor.getString(stateParishIndex);
            String country = cursor.getString(countryIndex);

            try
            {
                patient.put("Id", patientId);
                patient.put("First Name", fname);
                patient.put("Last Name", lname);
                patient.put("Date of Birth", dob);
                patient.put("Sex", sex);
                patient.put("Address1", addr1);
                patient.put("Address2", addr2);
                patient.put("City", city);
                patient.put("State/Parish", stateParish);
                patient.put("Country", country);

                // Load patient conditions
                Cursor conditionCursor = databaseHandler.getConditionsForPatient(patientId);
                while (conditionCursor.moveToNext())
                {
                    JSONObject condition = new JSONObject();
                    int conditionIdIndex = conditionCursor.getColumnIndex(DatabaseHandler.COLUMN_ID);
                    int statusIndex = conditionCursor.getColumnIndex(DatabaseHandler.COLUMN_STATUS);
                    int severityIndex = conditionCursor.getColumnIndex(DatabaseHandler.COLUMN_SEVERITY);
                    int codeIndex = conditionCursor.getColumnIndex(DatabaseHandler.COLUMN_CODE);
                    int encounterIdIndex = conditionCursor.getColumnIndex(DatabaseHandler.COLUMN_ENCOUNTER_ID);


                    int conditionId = conditionCursor.getInt(conditionIdIndex);
                    String status = conditionCursor.getString(statusIndex);
                    String severity = conditionCursor.getString(severityIndex);
                    String code = conditionCursor.getString(codeIndex);
                    int encounterId = conditionCursor.getInt(encounterIdIndex);


                    try
                    {
                        condition.put("ConditionId", conditionId);
                        condition.put("Status", status);
                        condition.put("Severity", severity);
                        condition.put("Code", code);
                        condition.put("EncounterId", encounterId);

                        conditions.put(condition);


                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                // Load patient encounters
                Cursor encounterCursor = databaseHandler.getEncountersForPatient(patientId);
                while (encounterCursor.moveToNext())
                {
                    JSONObject encounter = new JSONObject();
                    int encounterIdIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_ID);
                    int patientIdIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_PATIENT_ID);
                    int startTimeIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_START_TIME);
                    int endTimeIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_END_TIME);
                    int typeIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_TYPE);
                    int notesIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_NOTES);
                    int practitionerIdIndex = encounterCursor.getColumnIndex(DatabaseHandler.COLUMN_PRACTIONER_ID);


                    int encounterId = encounterCursor.getInt(encounterIdIndex);
                    String start = encounterCursor.getString(startTimeIndex);
                    String end = encounterCursor.getString(endTimeIndex);
                    String type = encounterCursor.getString(typeIndex);
                    String notes = encounterCursor.getString(notesIndex);
                    int pracId = encounterCursor.getInt(practitionerIdIndex);

                    encounter.put("Encounter Id", encounterId);
                    encounter.put("Patient Id", patientId);
                    encounter.put("Start", start);
                    encounter.put("End", end);
                    encounter.put("Type", type);
                    encounter.put("Notes", notes);
                    encounter.put("Practitioner Id", pracId);

                    encounters.put(encounter);


                }



                patient.put("Encounters", encounters);
                patient.put("Conditions", conditions);
                patients.put(patient);

                // add patients to mainObj
                mainObj.put("Patient", patients);

            }


            catch (JSONException e) {
                e.printStackTrace();
            }


        }

        jsonData = mainObj.toString();
        try {
            createFileWithDataToUpload(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    // Create a file with the JSON data
    private void createFileWithDataToUpload(String jsonData) throws IOException
    {
        try
        {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "DonnaCare/Export");

            if (!mediaStorageDir.exists())
            {
                mediaStorageDir.mkdirs();

            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File file = new File(mediaStorageDir.getPath() + File.separator +
                    "patient_"+patientId+"_"+ timeStamp + ".json");

            FileWriter writer = new FileWriter(file, false);
            writer.write(jsonData);
            writer.flush();
            writer.close();

            Toast.makeText(this, "File created in your Documents > DonnaCare > Export folder", Toast.LENGTH_LONG).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
