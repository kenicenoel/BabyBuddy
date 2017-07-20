package com.kenicenoel.babybuddy.objects;

/**
 * Created as part of the BabyBuddy project.
 * Author: Kenice Noel. Date created: Jul, 20, 2017
 */

public class Encounter
{
    private int _id;
    private int patientId;
    private String startTime;
    private String endTime;
    private String type;
    private String notes;
    private int practitionerId;


    public Encounter()
    {

    }

    public Encounter(int patientId, String startTime, String endTime, String type, String notes, int practitionerId) {
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.notes = notes;
        this.practitionerId = practitionerId;
    }

    public Encounter(int _id, int patientId, String startTime, String endTime, String type, String notes, int practitionerId)
    {

        this._id = _id;
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.notes = notes;
        this.practitionerId = practitionerId;
    }



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(int practitionerId) {
        this.practitionerId = practitionerId;
    }
}
