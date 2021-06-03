package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidtracker.Adapter.StateWiseAdapter;
import com.example.covidtracker.Model.StateData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateWiseDataActivity extends AppCompatActivity {

    private RecyclerView stateWiseRecyclerview;
    private StateWiseAdapter stateWiseAdapter;
    private ArrayList<StateData> list;
    private EditText searchState;
    private SwipeRefreshLayout swipeState;
    private MainActivity mainActivity = new MainActivity();

    private String state, confirmed, confirmed_new, active, active_new, recovered, recovered_new, death, death_new, lastupdatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_wise_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("State Data");

        stateWiseRecyclerview = findViewById(R.id.stateWiseRecyclerview);
        searchState = findViewById(R.id.searchState);
        swipeState = findViewById(R.id.swipeState);

        stateWiseRecyclerview.setHasFixedSize(true);
        stateWiseRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        stateWiseAdapter = new StateWiseAdapter(this, list);
        stateWiseRecyclerview.setAdapter(stateWiseAdapter);

        fetchStateData();
        swipeState.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStateData();
                swipeState.setRefreshing(false);
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

    private void Filter(String text) {
        ArrayList<StateData> filteredList = new ArrayList<>();
        for (StateData item : list) {
            if (item.getState().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        stateWiseAdapter.filterList(filteredList, text);
    }

    private void fetchStateData() {
        mainActivity.ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("statewise");
                            list.clear();

                            for(int i = 1; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                state = jsonObject.getString("state");

                                confirmed = jsonObject.getString("confirmed");
                                confirmed_new = jsonObject.getString("deltaconfirmed");

                                active = jsonObject.getString("active");

                                death = jsonObject.getString("deaths");
                                death_new = jsonObject.getString("deltadeaths");

                                recovered = jsonObject.getString("recovered");
                                recovered_new = jsonObject.getString("deltarecovered");
                                lastupdatedate = jsonObject.getString("lastupdatedtime");

                                //Creating an object of our statewise model class and passing the values in the constructor
                                StateData stateData= new StateData(state, confirmed, confirmed_new, active, death, death_new, recovered, recovered_new, lastupdatedate);
                                //adding data to our arraylist
                                list.add(stateData);
                            }

                            Handler makeDelay = new Handler();
                            makeDelay.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stateWiseAdapter.notifyDataSetChanged();
                                    mainActivity.progressDialog.dismiss();
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

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}