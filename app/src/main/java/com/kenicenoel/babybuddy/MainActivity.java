package com.kenicenoel.babybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenicenoel.babybuddy.adapters.PatientListAdaptor;
import com.kenicenoel.babybuddy.objects.Patient;
import com.kenicenoel.quicktools.SettingsBuddy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PatientListAdaptor.ClickListener
{
    private SettingsBuddy buddy;
    private TextView doctorName;
    private RecyclerView patients;
    private ArrayList<Patient> patientsList;
    private DatabaseHandler databaseHandler;
    private PatientListAdaptor adaptor;
    private EditText searchPatient;
    private Toolbar toolbar;
    private ImageView logout;



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




        loadPatientDataFromDatabase();

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
