package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

public class DistrictDetailsActivity extends AppCompatActivity {
    private TextView confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new;

    private PieChart pieChart;

    private String District, Confirmed, Confirmed_new, Active, Recovered, Recovered_new, Death, Death_new;


    MainActivity mainActivity = new MainActivity();
    public String conf_new = "confirm";
    public String rec_new = "recovered";
    public String dea_new = "deceased";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_details);

        confirmed = findViewById(R.id.confirmedD);
        confirmed_new = findViewById(R.id.confirmed_newD);
        active = findViewById(R.id.activeD);
        recovered_new = findViewById(R.id.recovered_newD);
        recovered = findViewById(R.id.recoveredD);

        death = findViewById(R.id.deathD);
        death_new = findViewById(R.id.death_newD);

        pieChart = findViewById(R.id.piechartD);

        Intent intent = getIntent();
        District = intent.getStringExtra("district");
        Confirmed = intent.getStringExtra("confirmed");
        Confirmed_new = intent.getStringExtra(conf_new);
        Active = intent.getStringExtra("active");
        Death = intent.getStringExtra("deaths");
        Death_new = intent.getStringExtra(dea_new);
        Recovered = intent.getStringExtra("recovered");
        Recovered_new = intent.getStringExtra(rec_new);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(District);
        fetchDistrictDetails();
    }
    private void fetchDistrictDetails() {
        mainActivity.ShowDialog(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(Confirmed)));
                confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Confirmed_new)));

                active.setText(NumberFormat.getInstance().format(Integer.parseInt(Active)));

                death.setText(NumberFormat.getInstance().format(Integer.parseInt(Death)));
                death_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Death_new)));

                recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered)));
                recovered_new.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered_new)));

                pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(Active), Color.parseColor("#FFFBC233")));
                pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(Recovered), Color.parseColor("#FF08A045")));
                pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(Death), Color.parseColor("#FFF6404F")));
                mainActivity.progressDialog.dismiss();
                pieChart.startAnimation();
            }
        }, 1000);
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}