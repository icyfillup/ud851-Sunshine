/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////
    // TODO (5) [DONE]Add a layout for an item in the list called forecast_list_item.xml
    // TODO (6) [DONE]Make the root of the layout a vertical LinearLayout
    // TODO (7) [DONE]Set the width of the LinearLayout to match_parent and the height to wrap_content

    // TODO (8) [DONE]Add a TextView with an id @+id/tv_weather_data
    // TODO (9) [DONE]Set the text size to 22sp
    // TODO (10) [DONE]Make the width and height wrap_content
    // TODO (11) [DONE]Give the TextView 16dp of padding

    // TODO (12) [DONE]Add a View to the layout with a width of match_parent and a height of 1dp
    // TODO (13) [DONE]Set the background color to #dadada
    // TODO (14) [DONE]Set the left and right margins to 8dp
    // Within forecast_list_item.xml //////////////////////////////////////////////////////////////


    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
    // TODO (15) [DONE]Add a class file called ForecastAdapter
    // TODO (22) [DONE]Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>

    // TODO (23) [DONE]Create a private string array called mWeatherData

    // TODO (47) Create the default constructor (we will pass in parameters in a later lesson)

    // TODO (16) [DONE]Create a class within ForecastAdapter called ForecastAdapterViewHolder
    // TODO (17) [DONE]Extend RecyclerView.ViewHolder

    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
    // TODO (18) [DONE]Create a public final TextView variable called mWeatherTextView

    // TODO (19) [DONE]Create a constructor for this class that accepts a View as a parameter
    // TODO (20) [DONE]Call super(view) within the constructor for ForecastAdapterViewHolder
    // TODO (21) [DONE]Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
    // Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////


    // TODO (24) [DONE]Override onCreateViewHolder
    // TODO (25) [DONE]Within onCreateViewHolder, inflate the list item xml into a view
    // TODO (26) [DONE]Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter

    // TODO (27) [DONE]Override onBindViewHolder
    // TODO (28) [DONE]Set the text of the TextView to the weather for this list item's position

    // TODO (29) [DONE]Override getItemCount
    // TODO (30) [DONE]Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null

    // TODO (31) [DONE]Create a setWeatherData method that saves the weatherData to mWeatherData
    // TODO (32) [DONE]After you save mWeatherData, call notifyDataSetChanged
    // Within ForecastAdapter.java /////////////////////////////////////////////////////////////////


    // TODO (33) [DONE]Delete mWeatherTextView

    // TODO (34) [DONE]Add a private RecyclerView variable called mRecyclerView
    private RecyclerView mRecyclerView;

    // TODO (35) [DONE]Add a private ForecastAdapter variable called mForecastAdapter
    private ForecastAdapter mForecastAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Log.d(TAG, "onCreate: hello");

        // TODO (36) [DONE]Delete the line where you get a reference to mWeatherTextView
        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */

        // TODO (37) [DONE]Use findViewById to get a reference to the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // TODO (38) [DONE]Create layoutManager, a LinearLayoutManager with VERTICAL orientation and shouldReverseLayout == false
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // TODO (39) [DONE]Set the layoutManager on mRecyclerView
        mRecyclerView.setLayoutManager(layoutManager);

        // TODO (40) [DONE]Use setHasFixedSize(true) on mRecyclerView to designate that all items in the list will have the same size
        mRecyclerView.setHasFixedSize(true);

        // TODO (41) [DONE]set mForecastAdapter equal to a new ForecastAdapter
        mForecastAdapter = new ForecastAdapter();

        // TODO (42) [DONE]Use mRecyclerView.setAdapter and pass in mForecastAdapter
        mRecyclerView.setAdapter(mForecastAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        /* Once all of our views are setup, we can load the weather data. */
        loadWeatherData();
    }

    /**
     * This method will get the user's preferred location for weather, and then tell some
     * background method to get the weather data in the background.
     */
    private void loadWeatherData() {
        showWeatherDataView();

        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        // TODO (43) [DONE]Show mRecyclerView, not mWeatherTextView
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        // TODO (44) [DONE]Hide mRecyclerView, not mWeatherTextView
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                // TODO (45) [DONE]Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: hello");
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            // TODO (46) Instead of setting the text to "", set the adapter to null before refreshing
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}