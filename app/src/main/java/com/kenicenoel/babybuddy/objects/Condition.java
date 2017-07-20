package com.kenicenoel.babybuddy.objects;

/**
 * Created as part of the BabyBuddy project.
 * Author: Software Developer. Date created: Jul, 20, 2017
 */

public class Condition
{
    private int _id;
    private int patientId;
    private String status;
    private String severity;
    private String code;
    private int encounter_id;

    public Condition()
    {

    }

    public Condition(int patientId, String status, String severity, String code, int encounter_id) {
        this._id = _id;
        this.patientId = patientId;
        this.status = status;
        this.severity = severity;
        this.code = code;
        this.encounter_id = encounter_id;
    }

    public Condition(String status, String severity, String code, int encounter_id)
    {
        this._id = _id;
        this.status = status;
        this.severity = severity;
        this.code = code;
        this.encounter_id = encounter_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getEncounter_id() {
        return encounter_id;
    }

    public void setEncounter_id(int encounter_id) {
        this.encounter_id = encounter_id;
    }
}
