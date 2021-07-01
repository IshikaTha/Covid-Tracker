package com.example.covidtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covidtracker.CountryDetailActivity;
import com.example.covidtracker.Model.CountryData;
import com.example.covidtracker.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class CountryWiseAdapter extends RecyclerView.Adapter<CountryWiseAdapter.viewHolder> {
    Context context;
    ArrayList<CountryData> list;

    public CountryWiseAdapter(Context context, ArrayList<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    public void filterList(ArrayList<CountryData> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryWiseAdapter.viewHolder holder, int position) {
        CountryData countryData = list.get(position);
        String countryName = countryData.getCountry();
        String countryTotal = countryData.getConfirmed();
        String countryFlag = countryData.getFlag();
        String countryRank = String.valueOf(position+1);
        int countryTotalInt = Integer.parseInt(countryTotal);
        holder.tv_countryName.setText((countryName));
        holder.tv_rankTextView.setText(countryRank+".");
        holder.tv_countryTotalCases.setText(NumberFormat.getInstance().format(countryTotalInt));

        Glide.with(context).load(countryFlag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.iv_flagImage);
        holder.lin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CountryDetailActivity.class);

                intent.putExtra("country", countryData.getCountry());
                intent.putExtra("cases", countryData.getConfirmed());
                intent.putExtra("active", countryData.getActive());
                intent.putExtra("recovered", countryData.getRecovered());
                intent.putExtra("deaths", countryData.getDeceased());
                intent.putExtra("todayCases", countryData.getNewConfirmed());
                intent.putExtra("todayDeaths", countryData.getNewDeceased());
                intent.putExtra("tests", countryData.getTests());
                intent.putExtra("flag", countryData.getFlag());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null || list.isEmpty() ? 0 : list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tv_countryName, tv_countryTotalCases, tv_rankTextView;
        ImageView iv_flagImage;
        LinearLayout lin_country;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_countryName = itemView.findViewById(R.id.countryName);
            tv_countryTotalCases = itemView.findViewById(R.id.confirmedCountry);
            iv_flagImage = itemView.findViewById(R.id.flag);
            tv_rankTextView = itemView.findViewById(R.id.rankC);
            lin_country = itemView.findViewById(R.id.llC);
        }
    }
}
