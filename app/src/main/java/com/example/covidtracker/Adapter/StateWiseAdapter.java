package com.example.covidtracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidtracker.Model.StateData;
import com.example.covidtracker.R;

import java.util.ArrayList;

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.viewHolder> {

    Context context;
    private ArrayList<StateData> list;
    private String searchText = "";

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

    }

    public void filterList(ArrayList<StateData> filteredList, String text) {
        list = filteredList;
        this.searchText = text;
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
