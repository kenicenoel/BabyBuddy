package com.kenicenoel.babybuddy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenicenoel.babybuddy.R;
import com.kenicenoel.babybuddy.objects.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenice Noel on 6/7/2017.
 */

public class ConditionsListAdaptor extends RecyclerView.Adapter<ConditionsListAdaptor.ConditionHolder>
{
    private ArrayList<Condition> conditions;
    private static ClickListener clickListener;
    public ConditionsListAdaptor(ArrayList<Condition> conditions)
    {
        this.conditions = conditions;
    }

    @Override
    public ConditionHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View inflatedView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_condition_list, viewGroup, false);
        return new ConditionHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ConditionHolder conditionHolder, int i)
    {
        Condition condition = conditions.get(i);
        conditionHolder._id.setText(String.valueOf(condition.get_id()));
//        conditionHolder.patientId.setText(condition.getPatientId());
        conditionHolder.status.setText(condition.getStatus());
        conditionHolder.severity.setText(condition.getSeverity());
        conditionHolder.code.setText(condition.getCode());
        conditionHolder.encounterId.setText(String.valueOf(condition.getEncounter_id()));

    }

    public void setFilter(List<Condition> condition)
    {
        conditions = new ArrayList<>();
        conditions.addAll(condition);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount()
    {
        return conditions.size();
    }

    public interface ClickListener
    {
        void onItemClick(int position, View v);
    }

    public static class ConditionHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView _id;
//        private TextView patientId;
        private TextView status;
        private TextView code;
        private TextView severity;
        private TextView encounterId;



        public ConditionHolder(View v)
        {
            super(v);
            _id = (TextView) v.findViewById(R.id.conditionId);
//            patientId = (TextView) v.findViewById(R.id.patientId);
            status = (TextView) v.findViewById(R.id.status);
            code = (TextView) v.findViewById(R.id.code);
            severity = (TextView) v.findViewById(R.id.severity);
            encounterId = (TextView) v.findViewById(R.id.encounterId);
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
        ConditionsListAdaptor.clickListener = clickListener;
    }


}
