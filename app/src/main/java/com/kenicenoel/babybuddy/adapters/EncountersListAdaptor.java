package com.kenicenoel.babybuddy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenicenoel.babybuddy.R;
import com.kenicenoel.babybuddy.objects.Encounter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenice Noel on 6/7/2017.
 */

public class EncountersListAdaptor extends RecyclerView.Adapter<EncountersListAdaptor.EncountersHolder>
{
    private ArrayList<Encounter> encounters;
    private static ClickListener clickListener;
    public EncountersListAdaptor(ArrayList<Encounter> encounters)
    {
        this.encounters = encounters;
    }

    @Override
    public EncountersHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_encounter_list, viewGroup, false);
        return new EncountersHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(EncountersHolder encounterHolder, int i)
    {
        Encounter encounter = encounters.get(i);
        encounterHolder._id.setText(String.valueOf(encounter.get_id()));
//        encounterHolder.patientId.setText(encounter.getPatientId());
        encounterHolder.startTime.setText(encounter.getStartTime());
        encounterHolder.endTime.setText(encounter.getEndTime());
        encounterHolder.type.setText(encounter.getType());
        encounterHolder.notes.setText(encounter.getNotes());
        encounterHolder.practitionerId.setText(String.valueOf(encounter.getPractitionerId()));

    }

    public void setFilter(List<Encounter> encounter)
    {
        encounters = new ArrayList<>();
        encounters.addAll(encounter);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        return encounters.size();
    }

    public interface ClickListener
    {
        void onItemClick(int position, View v);
    }

    public static class EncountersHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView _id;
//        private TextView patientId;
        private TextView startTime;
        private TextView endTime;
        private TextView type;
        private TextView notes;
        private TextView practitionerId;



        public EncountersHolder(View v)
        {
            super(v);
            _id = (TextView) v.findViewById(R.id.encounterId);
//            patientId = (TextView) v.findViewById(R.id.patientId);
            startTime = (TextView) v.findViewById(R.id.startTime);
            endTime = (TextView) v.findViewById(R.id.endTime);
            type = (TextView) v.findViewById(R.id.type);
            notes = (TextView) v.findViewById(R.id.notes);
            practitionerId = (TextView) v.findViewById(R.id.practitionerId);
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
        EncountersListAdaptor.clickListener = clickListener;
    }


}
