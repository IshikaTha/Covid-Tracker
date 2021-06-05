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

import com.example.covidtracker.DistrictDetailsActivity;
import com.example.covidtracker.Model.DistrictData;
import com.example.covidtracker.R;

import java.util.ArrayList;

public class DistrictWiseAdapter extends RecyclerView.Adapter<DistrictWiseAdapter.viewHolder> {
    private Context context;
    private ArrayList<DistrictData> list;

    public String conf_new = "confirm";
    public String rec_new = "recovered";
    public String dea_new = "deceased";

    public DistrictWiseAdapter(Context context, ArrayList<DistrictData> list) {
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
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DistrictData districtData = list.get(position);
        String districtN = districtData.getDistrict();
        String districtC = districtData.getConfirmed();
        holder.districtName.setText(districtN);
        holder.districtCases.setText(districtC);

        holder.ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DistrictDetailsActivity.class);
                intent.putExtra("district", districtData.getDistrict());
                intent.putExtra("confirmed", districtData.getConfirmed());
                intent.putExtra(conf_new, districtData.getCofirmed_new());
                intent.putExtra("active", districtData.getActive());
                intent.putExtra("deaths", districtData.getDeath());
                intent.putExtra(dea_new, districtData.getDeath_new());
                intent.putExtra("recovered", districtData.getRecovered());
                intent.putExtra(rec_new, districtData.getRecovered_new());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 :list.size();
    }

    public void filterList(ArrayList<DistrictData> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView districtName, districtCases;
        LinearLayout ll2;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.stateName);
            districtCases = itemView.findViewById(R.id.stateCases);
            ll2 = itemView.findViewById(R.id.stateWiseL);
        }
    }
}
