package com.kenicenoel.babybuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kenicenoel.babybuddy.objects.Condition;
import com.kenicenoel.babybuddy.objects.Encounter;
import com.kenicenoel.babybuddy.objects.Patient;
import com.kenicenoel.babybuddy.objects.User;



public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "babybuddy.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONDITIONS = "conditions";
    private static final String TABLE_ENCOUNTER = "encounter";
    public static final String TABLE_PATIENT = "patient";
    public static final String TABLE_PERSONAL_OBSERVATIONS = "personalobservations";
    public static final String TABLE_PRACTITIONER = "practitioner";
    public static final String TABLE_PRACTITIONER_OBSERVATIONS = "practitionerobservations";
    public static final String TABLE_SHARED_CARE_GIVERS = "sharedcaregivers";
    public static final String TABLE_USER = "user";

    private static final String TAG = DatabaseHandler.class.getSimpleName();
//    Column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENT_ID = "patientId";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_SEVERITY = "severity";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_ENCOUNTER_ID = "encounterId";


    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_END_TIME = "endTime";
    public static final String COLUMN_TYPE = "type";
    public static  final String COLUMN_NOTES = "notes";
    public static  final String COLUMN_PRACTIONER_ID = "practitionerId";

    public static final String COLUMN_BIRTHDATE = "birthdate";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_GIVEN_NAME = "givenName";
    public static final String COLUMN_FAMILY_NAME  = "familyName";
    public static final String COLUMN_ADDR1     = "addressLine1";
    public static final String COLUMN_ADDR2     = "addressLine2";
    public static final String COLUMN_CITY     = "city";
    public static final String COLUMN_STATE_PARISH     = "stateParishProvidence";
    public static final String COLUMN_COUNTRY     = "country";

    public static final String COLUMN_OBSERVATION_ID     = "observationId";
    public static final String COLUMN_DATE     = "date";
    public static final String COLUMN_WEIGHT     = "weight";
    public static final String COLUMN_HEIGHT     = "height";
    public static final String COLUMN_DISPOSITION    = "disposition";
    public static final String COLUMN_MEDS     = "meds";

    public static final String COLUMN_EMAIL     = "email";
    public static final String COLUMN_INSTITUTE     = "institute";

    public static final String COLUMN_PRAC_OBS_ID     = "pracObservationsId";

    public static final String COLUMN_USERNAME     = "username";
    public static final String COLUMN_PASSWORD     = "password";


    // Drop table statement tables
    private static final String DROP_TABLE_CONDITIONS = "DROP TABLE " + TABLE_CONDITIONS+ " IF EXISTS";
    private static final String DROP_TABLE_PATIENTS = "DROP TABLE " + TABLE_PATIENT+ " IF EXISTS";




    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // create the tables
        sqLiteDatabase.execSQL(createUserTable());
        sqLiteDatabase.execSQL(createPatientTable());
        sqLiteDatabase.execSQL(createEncountersTable());
        sqLiteDatabase.execSQL(createPersonalObservationsTable());
        sqLiteDatabase.execSQL(createPractionionerTable());
        sqLiteDatabase.execSQL(createConditionsTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int newVersion)
    {
//        sqLiteDatabase.execSQL();
        onCreate(sqLiteDatabase);
    }

    // Methods
    private String createPatientTable()
    {
        Log.d(TAG, "Creating the Patient table");
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PATIENT +
            "("
            +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUMN_GIVEN_NAME+" TEXT ,"
            +COLUMN_FAMILY_NAME+" TEXT ,"
            +COLUMN_ADDR1+" TEXT ,"
            +COLUMN_ADDR2+" TEXT ,"
            +COLUMN_CITY+" TEXT, "
            +COLUMN_STATE_PARISH+" TEXT, "
            +COLUMN_COUNTRY+" TEXT, "
            +COLUMN_BIRTHDATE+" TEXT, "
            +COLUMN_SEX+" TEXT"+

            ");";

    }

    private String createUserTable()
    {
        Log.d(TAG, "Creating the user table");
        return "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_GIVEN_NAME+" TEXT ,"
                +COLUMN_FAMILY_NAME+" TEXT ,"
                +COLUMN_USERNAME+" TEXT ,"
                +COLUMN_PASSWORD+" TEXT ,"
                +COLUMN_ADDR1+" TEXT ,"
                +COLUMN_ADDR2+" TEXT ,"
                +COLUMN_CITY+" TEXT ,"
                +COLUMN_STATE_PARISH+" TEXT ,"
                +COLUMN_COUNTRY+" TEXT"+
                ");";
    }

    private String createConditionsTable()
    {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_CONDITIONS +
                "("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_PATIENT_ID+" INTEGER ,"
                +COLUMN_STATUS+" TEXT ,"
                +COLUMN_SEVERITY+" TEXT ,"
                +COLUMN_CODE+" TEXT ,"
                +COLUMN_ENCOUNTER_ID+" TEXT"+
                ");";
    }

    private String createEncountersTable()
    {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_ENCOUNTER +
                "("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_PATIENT_ID+" INTEGER ,"
                +COLUMN_START_TIME+" TEXT ,"
                +COLUMN_END_TIME+" TEXT ,"
                +COLUMN_TYPE+" TEXT ,"
                +COLUMN_NOTES+" TEXT ,"
                +COLUMN_PRACTIONER_ID+" INTEGER"+
                ");";
    }

    private String createPersonalObservationsTable()
    {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_OBSERVATIONS +
                "("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_DATE+" TEXT ,"
                +COLUMN_PATIENT_ID+" INTEGER ,"
                +COLUMN_WEIGHT+" TEXT ,"
                +COLUMN_HEIGHT+" TEXT ,"
                +COLUMN_DISPOSITION+" TEXT ,"
                +COLUMN_MEDS+" TEXT"+
                ");";
    }

    private String createPractionionerTable()
    {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_PERSONAL_OBSERVATIONS +
                "("
                +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +COLUMN_EMAIL+" TEXT ,"
                +COLUMN_GIVEN_NAME+" TEXT ,"
                +COLUMN_FAMILY_NAME+" TEXT ,"
                +COLUMN_INSTITUTE+" TEXT"+
                ");";
    }



    // Pass in a new Patient Object and extract then save the values to the db
    public void addPatient(Patient patient)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GIVEN_NAME, patient.getGivenName());
        values.put(COLUMN_FAMILY_NAME, patient.getFamilyName());
        values.put(COLUMN_BIRTHDATE, patient.getDateOfBirth());
        values.put(COLUMN_SEX, patient.getSex());
        values.put(COLUMN_ADDR1, patient.getAddress1());
        values.put(COLUMN_ADDR2, patient.getAddress2());
        values.put(COLUMN_CITY, patient.getCity());
        values.put(COLUMN_STATE_PARISH, patient.getStateParish());
        values.put(COLUMN_COUNTRY, patient.getCountry());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_PATIENT, null, values);

    }

    public void addCondition(Condition condition)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, condition.getPatientId());
        values.put(COLUMN_STATUS, condition.getStatus());
        values.put(COLUMN_SEVERITY, condition.getSeverity());
        values.put(COLUMN_CODE, condition.getCode());
        values.put(COLUMN_ENCOUNTER_ID, condition.getEncounter_id());


        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_CONDITIONS, null, values);

    }


    public void addEncounter(Encounter encounter)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_ID, encounter.getPatientId());
        values.put(COLUMN_START_TIME, encounter.getStartTime());
        values.put(COLUMN_END_TIME, encounter.getEndTime());
        values.put(COLUMN_TYPE, encounter.getType());
        values.put(COLUMN_NOTES, encounter.getNotes());
        values.put(COLUMN_PRACTIONER_ID, encounter.getPractitionerId());


        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_ENCOUNTER, null, values);

    }







    // Pass in a new User object and extract then save the values to the db
    public void addUser(User user)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_GIVEN_NAME, user.getGivenName());
        values.put(COLUMN_FAMILY_NAME, user.getFamilyName());
        values.put(COLUMN_USERNAME, user.getUserName());
        values.put(COLUMN_PASSWORD, user.getPassword());

        SQLiteDatabase database = getWritableDatabase();
        database.insert(TABLE_USER, null, values);
        database.close();
    }


    // Get a list of all users
    public Cursor getUsers()
    {
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_USER;
        return database.rawQuery(query, null);
    }



    // Get a list of all patients
    public Cursor getPatients()
    {
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_PATIENT+" ORDER BY date("+COLUMN_BIRTHDATE+") DESC";
        return database.rawQuery(query, null);
    }

    public Cursor getPatientById(int _id)
    {
        SQLiteDatabase database = getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_PATIENT+" WHERE "+TABLE_PATIENT+"."+COLUMN_ID+"='"+_id+"';";
        return database.rawQuery(query, null);
    }

    public Cursor getConditionsForPatient(int _id)
    {
        SQLiteDatabase database = getWritableDatabase();
        String query =
                "SELECT * FROM " + TABLE_CONDITIONS
                        +" INNER JOIN " +TABLE_PATIENT
                        +" ON "+TABLE_CONDITIONS+"."+COLUMN_PATIENT_ID+"="+_id+" AND "+TABLE_PATIENT+"."+COLUMN_ID+"="+_id+";";
        return database.rawQuery(query, null);
    }

    public Cursor getEncountersForPatient(int _id)
    {
        SQLiteDatabase database = getWritableDatabase();
        String query =
                "SELECT * FROM " + TABLE_ENCOUNTER
                        +" INNER JOIN " +TABLE_PATIENT
                        +" ON "+TABLE_ENCOUNTER+"."+COLUMN_PATIENT_ID+"="+_id+" AND "+TABLE_PATIENT+"."+COLUMN_ID+"="+_id+";";
        return database.rawQuery(query, null);
    }





    public void deleteAllPatients()
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_PATIENT, null, null);
        database.close();
    }

    public void deleteAllConditions()
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_CONDITIONS, null, null);
        database.close();
    }




}
