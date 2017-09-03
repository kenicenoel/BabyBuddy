package com.kenicenoel.babybuddy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kenicenoel.babybuddy.adapters.PatientListAdaptor;
import com.kenicenoel.babybuddy.objects.Condition;
import com.kenicenoel.babybuddy.objects.Encounter;
import com.kenicenoel.babybuddy.objects.Patient;
import com.kenicenoel.quicktools.SettingsBuddy;

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

public class MainActivity extends AppCompatActivity implements PatientListAdaptor.ClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private SettingsBuddy buddy;
    private TextView doctorName;
    private RecyclerView patients;
    private ArrayList<Patient> patientsList;
    private DatabaseHandler databaseHandler;
    private PatientListAdaptor adaptor;
    private EditText searchPatient;
    private Toolbar toolbar;
    private ImageView logout;
    private ImageView exportData;
    private ImageView addPatient;

    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get handler for the database
        databaseHandler = new DatabaseHandler(this, null, null, 1);

        doctorName = (TextView) findViewById(R.id.doctorName);
        buddy = SettingsBuddy.getInstance(this);
        patientsList = new ArrayList<>();
        searchPatient = (EditText) findViewById(R.id.searchPatients);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logout = (ImageView) toolbar.findViewById(R.id.logoutUser);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure you want to logout from the app? If so, choose 'yes' to continue.");
                builder.setPositiveButton("Yes, logout",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                buddy.removeData("username");
                                buddy.removeData("password");
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

        exportData = (ImageView) findViewById(R.id.exportData);
        exportData.setOnClickListener(new View.OnClickListener() {
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

        addPatient = (ImageView) findViewById(R.id.addPatient);
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), NewPatientActivity.class);
                startActivity(new Intent(intent));
            }
        });

        searchPatient.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                filter(editable.toString());
                System.out.println(editable.toString());
            }
        });


        // setup recyclerview and adpator
        adaptor = new PatientListAdaptor(patientsList);
        patients = (RecyclerView) findViewById(R.id.patientsRecyclerView);
        patients.setLayoutManager(new LinearLayoutManager(this));
        patients.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        patients.setAdapter(adaptor);
        adaptor.setClickListener(this);

        // Get the current logged in doctor
        doctorName.append(buddy.getData("username" ));



        if (buddy.getData("role").equals("Doctor"))
        {
            loadPatientDataFromDatabase();
        }

        else
        {
            loadChildrenFromDatabase();
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

    private void exportDataToJSON()
    {
        String jsonData= "";
        JSONObject mainObj = new JSONObject();

        JSONArray patients = new JSONArray();
        JSONArray conditions = new JSONArray();
        JSONArray encounters = new JSONArray();


        Cursor cursor = databaseHandler.getPatients();
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
                    "jsonUpload_"+ timeStamp + ".json");

            FileWriter writer = new FileWriter(file, false);
            Log.d(TAG, jsonData);
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





    private void loadPatientDataFromDatabase()
    {
        Cursor cursor = databaseHandler.getPatients();
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

            Patient patient = new Patient(_id, fname, lname,dob, sex, addr1, addr2, city, stateParish, country);
            patientsList.add(patient);
            adaptor.notifyDataSetChanged();


        }

    }

    private void loadChildrenFromDatabase()
    {
        Cursor cursor = databaseHandler.getOnePatient();
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

            Patient patient = new Patient(_id, fname, lname,dob, sex, addr1, addr2, city, stateParish, country);
            patientsList.add(patient);
            adaptor.notifyDataSetChanged();


        }

    }






    private void filter(String query)
    {
        query = query.toLowerCase();
        final List<Patient> filteredModelList = new ArrayList<>();
        for (Patient patient : patientsList)
        {
            final String text = patient.getGivenName().toLowerCase();
            if (text.contains(query))
            {
                filteredModelList.add(patient);
            }
        }
        adaptor.setFilter(filteredModelList);
    }

    @Override
    public void onItemClick(int position, View v)
    {
        TextView id = (TextView) v.findViewById(R.id.patientId);
        int _id = Integer.parseInt(id.getText().toString());

        Intent patientDetails = new Intent(getApplicationContext(), PatientDetailsActivity.class);
        patientDetails.putExtra("PatientId", _id);
        startActivity(patientDetails);

    }
}
