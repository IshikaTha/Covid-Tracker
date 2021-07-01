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

public class CountryDetailActivity extends AppCompatActivity {
    private TextView confirmed, confirmed_new, active, active_new, death, death_new, recovered, recovered_new, tests;

    private String countryName, Confirmed, Confirmed_new, Active, Death, Death_new, Recovered, Tests;
    private PieChart pieChart;
    private MainActivity activity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);


        Intent intent = getIntent();
        countryName = intent.getStringExtra("country");
        Confirmed = intent.getStringExtra("cases");
        Active = intent.getStringExtra("active");
        Death = intent.getStringExtra("deaths");
        Recovered = intent.getStringExtra("recovered");
        Confirmed_new = intent.getStringExtra("todayCases");
        Death_new = intent.getStringExtra("todayDeaths");
        Tests = intent.getStringExtra("tests");


        getSupportActionBar().setTitle(countryName);

        //back menu icon on toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        confirmed = findViewById(R.id.confirmedC);
        confirmed_new = findViewById(R.id.confirmedNewC);
        active = findViewById(R.id.activeNew);
        active_new = findViewById(R.id.activeNewC);
        recovered = findViewById(R.id.recoveredC);
        recovered_new = findViewById(R.id.recoveredNewC);
        death = findViewById(R.id.deathC);
        death_new = findViewById(R.id.deathNewC);
        tests = findViewById(R.id.casesC);
        pieChart = findViewById(R.id.country);

        LoadCountryData();
    }
    private void LoadCountryData() {
        activity.ShowDialog(this);

        Handler postDelayToshowProgress = new Handler();
        postDelayToshowProgress.postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(Confirmed)));
                confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(Confirmed_new)));

                active.setText(NumberFormat.getInstance().format(Integer.parseInt(Active)));
                active_new.setText("N/A");

                death.setText(NumberFormat.getInstance().format(Integer.parseInt(Death)));
                death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(Death_new)));

                recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered)));
                recovered_new.setText("N/A");

                tests.setText(NumberFormat.getInstance().format(Integer.parseInt(Tests)));

                pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(Active), Color.parseColor("#FFFBC233")));
                pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(Recovered), Color.parseColor("#FF08A045")));
                pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(Death), Color.parseColor("#FFF6404F")));

                pieChart.startAnimation();

                activity.progressDialog.dismiss();
            }
        },1000);
    }
    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();
    }
}