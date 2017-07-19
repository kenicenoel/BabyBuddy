package com.kenicenoel.babybuddy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenicenoel.babybuddy.R;
import com.kenicenoel.babybuddy.objects.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenice Noel on 6/7/2017.
 */

public class PatientListAdaptor extends RecyclerView.Adapter<PatientListAdaptor.PatientHolder>
{
    private ArrayList<Patient> patients;
    private static ClickListener clickListener;
    public PatientListAdaptor(ArrayList<Patient> patients)
    {
        this.patients = patients;
    }

    @Override
    public PatientHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_patient_list, viewGroup, false);
        return new PatientHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PatientHolder patientHolder, int i)
    {
        Patient patient = patients.get(i);
        patientHolder._id.setText(patient.get_id());
        patientHolder.name.setText(patient.getGivenName() + " "+patient.getFamilyName());
        patientHolder.birthDate.setText(patient.getDateOfBirth());
        patientHolder.sex.setText(patient.getSex());
        patientHolder.address.setText(patient.getAddress1() + " "+patient.getCity() + " "+patient.getStateParish()+" "+patient.getCountry());

    }

    public void setFilter(List<Patient> patient)
    {
        patients = new ArrayList<>();
        patients.addAll(patient);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        return patients.size();
    }

    public interface ClickListener
    {
        void onItemClick(int position, View v);
    }

    public static class PatientHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView _id;
        private TextView name;
        private TextView birthDate;
        private TextView sex;
        private TextView address;



        public PatientHolder(View v)
        {
            super(v);
            _id = (TextView) v.findViewById(R.id.patientId);
            name = (TextView) v.findViewById(R.id.patientName);
            birthDate = (TextView) v.findViewById(R.id.patientBirthDate);
            sex = (TextView) v.findViewById(R.id.patientSex);
            address = (TextView) v.findViewById(R.id.patientAddress);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view)
        {
            if (clickListener != null)
            {
                clickListener.onItemClick(getAdapterPosition(), view);
            }

        }
    }

    public void setClickListener(ClickListener clickListener)
    {
        PatientListAdaptor.clickListener = clickListener;
    }


}
