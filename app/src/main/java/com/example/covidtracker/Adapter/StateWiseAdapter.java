package com.example.covidtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.Model.StateData;
import com.example.covidtracker.R;
import com.example.covidtracker.StateDetailsActivity;

import java.util.ArrayList;

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.viewHolder> {

    Context context;
    private ArrayList<StateData> list;
    //private String searchText = "";

    public StateWiseAdapter(Context context, ArrayList<StateData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.state_wise_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateWiseAdapter.viewHolder holder, int position) {
        StateData stateData = list.get(position);
        String StateName = stateData.getState();
        String StateTotal = stateData.getConfirmed();
        holder.stateName.setText(StateName);
        holder.stateCases.setText(StateTotal);
        holder.stateWiseL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StateDetailsActivity.class);
                intent.putExtra("state", stateData.getState());
                intent.putExtra("confirmed", stateData.getConfirmed());
                intent.putExtra("deltaconfirmed", stateData.getCofirmed_new());
                intent.putExtra("active", stateData.getActive());
                intent.putExtra("deaths", stateData.getDeath());
                intent.putExtra("deltadeaths", stateData.getDeath_new());
                intent.putExtra("recovered", stateData.getRecovered());
                intent.putExtra("deltarecovered", stateData.getRecovered_new());
                intent.putExtra("lastupdatedtime", stateData.getLastUpdate());
                context.startActivity(intent);
            }
        });

    }

    public void filterList(ArrayList<StateData> filteredList) {
        list = filteredList;
        //this.searchText = text;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView stateName, stateCases;
        LinearLayout stateWiseL;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            stateName = itemView.findViewById(R.id.stateName);
            stateCases = itemView.findViewById(R.id.stateCases);
            stateWiseL = itemView.findViewById(R.id.stateWiseL);
        }
    }
}
