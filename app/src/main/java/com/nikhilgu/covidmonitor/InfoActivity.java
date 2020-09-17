package com.nikhilgu.covidmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {

    private static final String STATS_URL = "https://api.covid19api.com/summary";
    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.getSupportActionBar().hide();

        testTV = findViewById(R.id.test);
        // Get JSON response from API website
        StringRequest sr = new StringRequest(Request.Method.GET, STATS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                testTV.setText("Error retrieving data.");
            }
        });

        RequestQueue rq = Volley.newRequestQueue(InfoActivity.this);
        rq.add(sr);
    }
    // Add COVID API data to the View
    private void handleResponse(String response){
        try{
            String data = "";
            JSONObject jsonObject = new JSONObject(response);

            // Global Stuff
            JSONObject global = jsonObject.getJSONObject("Global");

            String totalConfirmed = global.getString("TotalConfirmed");
            String totalDeaths = global.getString("TotalDeaths");
            data += "GLOBAL: " + "\n" +
                    "CONFIRMED: " + totalConfirmed + "\n" +
                    "DEATHS: " + totalDeaths + "\n" +
                    "------------------------" + "\n";

            // Country Stuff
            JSONArray countries = jsonObject.getJSONArray("Countries");

            for(int x = 0; x < countries.length(); x++){
                JSONObject country = countries.getJSONObject(x);
                data += "Country: " + country.getString("Country") + "\n" +
                        "Confirmed: " + country.getString("TotalConfirmed") + "\n" +
                        "Deaths: " + country.getString("TotalDeaths") + "\n" +
                        "------------------------" + "\n";
            }

            testTV.setText(data);
        }catch(Exception e){
            testTV.setText("Error occurred");
        }
    }
}