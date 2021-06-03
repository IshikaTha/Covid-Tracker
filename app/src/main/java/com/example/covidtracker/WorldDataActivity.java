package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;


public class WorldDataActivity extends AppCompatActivity {
    private TextView confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new, sample, sample_new, countryData;

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;

    private String Confirmed, Confirmed_new, Active, Recovered, Recovered_new, Death, Death_new, Sample, Sample_new;
    private int Active_new;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("World Data");

        confirmed = findViewById(R.id.confirmedW);
        confirmed_new = findViewById(R.id.confirmed_newW);
        active = findViewById(R.id.activeW);
        active_new = findViewById(R.id.active_newW);
        recovered = findViewById(R.id.recoveredW);
        recovered_new = findViewById(R.id.recovered_newW);
        death = findViewById(R.id.deathW);
        death_new = findViewById(R.id.death_newW);
        sample = findViewById(R.id.samplesW);
        sample_new = findViewById(R.id.sample_newW);
        countryData = findViewById(R.id.countryData);

        swipeRefreshLayout = findViewById(R.id.worldSwipe);
        pieChart = findViewById(R.id.piechartW);

        countryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldDataActivity.this, CountryDataActivity.class));
            }
        });

        fetchWorldData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchWorldData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void fetchWorldData() {
        ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://corona.lmao.ninja/v2/all";
        pieChart.clearChart();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Confirmed = response.getString("cases");
                            Confirmed_new = response.getString("todayCases");
                            Active = response.getString("active");
                            Recovered = response.getString("recovered");
                            Recovered_new = response.getString("todayRecovered");
                            Death = response.getString("deaths");
                            Death_new = response.getString("todayDeaths");
                            Sample = response.getString("tests");

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                            confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(Confirmed)));
                                            confirmed_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(Confirmed_new)));

                                            active.setText(NumberFormat.getInstance().format(Integer.parseInt(Active)));

                                            Active_new = Integer.parseInt(Confirmed_new)
                                                    - (Integer.parseInt(Recovered_new) + Integer.parseInt(Death_new));

                                            active_new.setText("+"+NumberFormat.getInstance().format(Active_new));

                                            recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered)));
                                            recovered_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(Recovered_new)));

                                            death.setText(NumberFormat.getInstance().format(Integer.parseInt(Death)));
                                            death_new.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(Death_new)));

                                            sample.setText(NumberFormat.getInstance().format(Long.parseLong(Sample)));

                                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(Active), Color.parseColor("#FFFBC233")));
                                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(Recovered), Color.parseColor("#FF08A045")));
                                            pieChart.addPieSlice(new PieModel("Deceased", Integer.parseInt(Death), Color.parseColor("#FFF6404F")));
                                            progressDialog.dismiss();
                                            pieChart.startAnimation();
                                        }
                                    }, 1000);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(request);
    }

    public void ShowDialog(Context context) {
        //setting up progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}


