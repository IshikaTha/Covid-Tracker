package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

public class StateDetailsActivity extends AppCompatActivity {
    private TextView confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new, dateS;

    private PieChart pieChart;

    private String StateN, Confirmed, Confirmed_new, Active, Recovered, Recovered_new, Death, Death_new, updateTime;

    private int Active_new;

    MaterialCardView district;

    private MainActivity mainActivity = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(StateN);

        confirmed = findViewById(R.id.confirmedS);
        confirmed_new = findViewById(R.id.confirmed_newS);
        active = findViewById(R.id.activeS);
        active_new = findViewById(R.id.active_newS);
        recovered_new = findViewById(R.id.recovered_newS);
        recovered = findViewById(R.id.recoveredS);

        death = findViewById(R.id.deathS);
        death_new = findViewById(R.id.death_newS);
        dateS = findViewById(R.id.dateS);

        district = findViewById(R.id.district);

        pieChart = findViewById(R.id.piechartS);

        Intent intent = getIntent();
        StateN = intent.getStringExtra("state");
        Confirmed = intent.getStringExtra("confirmed");
        Confirmed_new = intent.getStringExtra("deltaconfirmed");
        Active = intent.getStringExtra("active");
        Death = intent.getStringExtra("deaths");
        Death_new = intent.getStringExtra("deltadeaths");
        Recovered = intent.getStringExtra("recovered");
        Recovered_new = intent.getStringExtra("deltarecovered");
        updateTime = intent.getStringExtra("lastupdatedtime");

        fetchStateDetails();
    }

    private void fetchStateDetails() {
        mainActivity.ShowDialog(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(Confirmed)));
                confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Confirmed_new)));

                active.setText(NumberFormat.getInstance().format(Integer.parseInt(Active)));
                Active_new = Integer.parseInt(Confirmed_new) - Integer.parseInt(Recovered_new) - Integer.parseInt(Death_new);
                active_new.setText("+" + NumberFormat.getInstance().format(Active_new < 0 ? 0:Active_new));

                death.setText(NumberFormat.getInstance().format(Integer.parseInt(Death)));
                death_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Death_new)));

                recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered)));
                recovered_new.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered_new)));

                String formatDate = mainActivity.FormatDate(updateTime, 0);
                dateS.setText(formatDate);

                pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(Active), Color.parseColor("#FFFBC233")));
                pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(Recovered), Color.parseColor("#FF08A045")));
                pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(Death), Color.parseColor("#FFF6404F")));
                mainActivity.progressDialog.dismiss();
                pieChart.startAnimation();
            }
        }, 1000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}