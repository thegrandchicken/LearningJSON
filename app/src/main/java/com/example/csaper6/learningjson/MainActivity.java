package com.example.csaper6.learningjson;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private TextView searchText;
    private EditText userSearch;
    private Button searchButton;

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = (TextView) findViewById(R.id.result_text);
        searchText =(TextView) findViewById(R.id.text_search);
        userSearch = (EditText) findViewById(R.id.editText_search);
        searchButton = (Button) findViewById(R.id.button_search);

        //region Parsing Local JSON
        /*
        1. Create an InputStream from the file
        2. Create a BufferedReader using that InputStream
        3. Read the data line by line into String from the BufferedReader
        4, Make a JSON object from the String
         */

       /* String jsonString = ""; //initializes our string to store the data

        //1
        try {
            InputStream fileInput = getAssets().open("data.json");
            //2
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));
            String line;

            //3 as long as there's more data, keep adding to our string
            while((line = reader.readLine()) != null) {
                jsonString += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
            //TAG is supposed to tell you what class is causing the trouble
            Log.e(TAG, "onCreate: it crashed because file not found");
        }

        //Log.d(TAG, "onCreate: " + jsonString);

        //Create a JSONObject from the jsonString
        JSONObject jsonData = null;

        try {
            jsonData = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log it using the toString method
        if(jsonData != null) {
            //Log.d(TAG, "onCreate: " + jsonData.toString());
            //Log.d(TAG, "onCreate: " + jsonData.optString("name", "fail"));
            //Log.d(TAG, "onCreate: " + jsonData.optString("staplers_owned", "fail"));
            //Log.d(TAG, "onCreate: " + jsonData.optString("wpm", "fail"));
            //Log.d(TAG, "onCreate: " + jsonData.optJSONObject("the_object").toString());
            //Log.d(TAG, "onCreate: " + jsonData.optJSONObject("the_object").optInt("walruses"));
            Log.d(TAG, "onCreate: " + jsonData.optJSONArray("an_array")
                                            .optJSONObject(0)
                                            .optString("thing1"));
        }*/
        //endregion

        //Create our URL

        //Fetch the data -- AsyncTask to do it off the UI thread
        //Make a private inner class to do this

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Parse it
                String baseUrl = "https://swapi.co/api/people/?search=";
                String search = "" + userSearch.getText();
                String fullUrl = baseUrl + Uri.encode(search);
                //Display it
                new SwPersonSearch().execute(fullUrl);
            }
        });


    }

    //Inner class to get the data
    //First string in <>: parameter type
    //Void in <>: we don't have any progress to worry about
    //Last String in <>: return type


    private class SwPersonSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            //Make a new URL object
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                //Build a string just like you did with code in last project
                String jsonString = "";
                //Log the built string and try it out
//1
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    //3 as long as there's more data, keep adding to our string
                    while((line = reader.readLine()) != null) {
                        jsonString += line;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    //TAG is supposed to tell you what class is causing the trouble
                    Log.e(TAG, "onCreate: it crashed because file not found");
                }
                Log.d(TAG, "onCreate: " + jsonString);
                //Create a JSON object from the string
                JSONObject jsonData = null;

                try {
                    jsonData = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Dive into JSON and access just the name

                if(jsonData != null) {

                    Log.d(TAG, "onCreate: " + jsonData.optJSONArray("results")
                            .optJSONObject(0)
                            .optString("name")); }

                return jsonData.optJSONArray("results").optJSONObject(0).optString("name", "fail");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //return from doInBackground
            super.onPostExecute(s);
            if (s != null) {
                resultText.setText(s);
            }

        }
    }
}
