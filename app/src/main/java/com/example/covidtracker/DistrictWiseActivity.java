package com.example.covidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.Adapter.DistrictWiseAdapter;
import com.example.covidtracker.Model.DistrictData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DistrictWiseActivity extends AppCompatActivity {
    private RecyclerView districtWiseRecyclerview;
    private DistrictWiseAdapter districtWiseAdapter;
    private ArrayList<DistrictData> list;
    private EditText searchState;
    private SwipeRefreshLayout swipeDistrict;
    private MainActivity mainActivity = new MainActivity();

    private String state, district, confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new, lastupdatedate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_wise);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("District Data");

        districtWiseRecyclerview = findViewById(R.id.districtWiseRecyclerview);
        searchState = findViewById(R.id.searchDistrict);
        swipeDistrict = findViewById(R.id.swipeDistrict);

        districtWiseRecyclerview.setHasFixedSize(true);
        districtWiseRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        districtWiseAdapter = new DistrictWiseAdapter(this, list);
        districtWiseRecyclerview.setAdapter(districtWiseAdapter);

        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        fetchDistrictData();
        swipeDistrict.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDistrictData();
                swipeDistrict.setRefreshing(false);
            }
        });

        searchState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter(s.toString());
            }
        });
    }

    private void fetchDistrictData() {
            mainActivity.ShowDialog(this);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "https://api.covid19india.org/v2/state_district_wise.json";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int flag=0;
                            list.clear();
                            for (int i=1;i<response.length();i++){
                                JSONObject jsonObjectState = response.getJSONObject(i);

                                if (state.toLowerCase().equals(jsonObjectState.getString("state").toLowerCase())){
                                    JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray("districtData");

                                    for (int j=0; j<jsonArrayDistrict.length(); j++){
                                        JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j);
                                        district = jsonObjectDistrict.getString("district");
                                        confirmed = jsonObjectDistrict.getString("confirmed");
                                        active = jsonObjectDistrict.getString("active");
                                        death = jsonObjectDistrict.getString("deceased");
                                        recovered = jsonObjectDistrict.getString("recovered");

                                        JSONObject jsonObjectDistNew = jsonObjectDistrict.getJSONObject("delta");
                                        confirmed_new = jsonObjectDistNew.getString("confirmed");
                                        recovered_new = jsonObjectDistNew.getString("recovered");
                                        death_new = jsonObjectDistNew.getString("deceased");

                                        DistrictData districtData = new DistrictData(district, confirmed, confirmed_new,
                                                active, death, death_new, recovered, recovered_new);
                                        list.add(districtData);
                                    }
                                    flag=1;
                                }
                                if (flag==1)
                                    break;
                            }
                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    districtWiseAdapter.notifyDataSetChanged();
                                    mainActivity.progressDialog.dismiss();
                                }
                            }, 1000);
                        }
                        catch (JSONException e) {
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
        requestQueue.add(jsonArrayRequest);
    }

    private void Filter(String text) {
        ArrayList<DistrictData> filteredList = new ArrayList<>();
        for (DistrictData item : list) {
            if (item.getDistrict().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        districtWiseAdapter.filterList(filteredList);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}