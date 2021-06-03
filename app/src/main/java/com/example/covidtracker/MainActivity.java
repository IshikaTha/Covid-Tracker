package com.example.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new, sample, sample_new, time, date;
    private MaterialCardView worldData;

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;

    private String Confirmed, Confirmed_new, Active, Recovered, Recovered_new, Death, Death_new, Sample, Sample_new, TimeDate;
    private int Active_new;

    ProgressDialog progressDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private Toast backPressToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Covid-19 Tracker");

        confirmed = findViewById(R.id.confirmed);
        confirmed_new = findViewById(R.id.confirmed_new);
        active = findViewById(R.id.active);
        active_new = findViewById(R.id.active_new);
        recovered = findViewById(R.id.recovered);
        recovered_new = findViewById(R.id.recovered_new);
        death = findViewById(R.id.death);
        death_new = findViewById(R.id.death_new);
        sample = findViewById(R.id.samples);
        sample_new = findViewById(R.id.sample_new);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        MaterialCardView stateWise = findViewById(R.id.stateWise);
        worldData = findViewById(R.id.worldData);

        swipeRefreshLayout = findViewById(R.id.swipe);
        pieChart = findViewById(R.id.piechart);
        
        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        worldData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WorldDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchData() {
        ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";
        pieChart.clearChart();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray all_state;
                        JSONArray test_data;

                        try {
                            all_state = response.getJSONArray("statewise");
                            test_data = response.getJSONArray("tested");

                            JSONObject india_data = all_state.getJSONObject(0);
                            JSONObject india_test = test_data.getJSONObject(test_data.length()-1);

                            Confirmed = india_data.getString("confirmed");
                            Confirmed_new = india_data.getString("deltaconfirmed");

                            Active = india_data.getString("active");

                            Recovered = india_data.getString("recovered");
                            Recovered_new = india_data.getString("deltarecovered");

                            TimeDate = india_data.getString("lastupdatedtime");

                            Death = india_data.getString("deaths");
                            Death_new = india_data.getString("deltadeaths");

                            Sample= india_test.getString("totalsamplestested");
                            Sample_new = india_test.getString("samplereportedtoday");

                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(Confirmed)));
                                    confirmed_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Confirmed_new)));


                                    recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(Recovered)));
                                    recovered_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Recovered_new)));

                                    death.setText(NumberFormat.getInstance().format(Integer.parseInt(Death)));
                                    death_new.setText(NumberFormat.getInstance().format(Integer.parseInt(Death_new)));

                                    sample.setText(NumberFormat.getInstance().format(Integer.parseInt(Sample)));
                                    sample_new.setText("+" + NumberFormat.getInstance().format(Integer.parseInt(Sample_new)));

                                    Active_new = Integer.parseInt(Confirmed_new) - (Integer.parseInt(Recovered_new) + Integer.parseInt(Death_new));
                                    active.setText(NumberFormat.getInstance().format(Integer.parseInt(Active)));
                                    active_new.setText("+" + NumberFormat.getInstance().format(Active_new));

                                    date.setText(FormatDate(TimeDate, 1));
                                    time.setText(FormatDate(TimeDate, 2));

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

    public String FormatDate(String date, int testCase) {
        Date mDate = null;
        String dateFormat;
        try {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);
            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy").format(mDate);
                return dateFormat;
            } else if (testCase == 2) {
                dateFormat = new SimpleDateFormat("hh:mm a").format(mDate);
                return dateFormat;
            }
            else {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.about){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            backPressToast.cancel();
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        backPressToast = Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT);
        backPressToast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}