package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

/**
 * Created by icyfillup on 6/25/2017.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private String[] mWeatherData;

    public ForecastAdapter() {

    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: hello");
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder viewHolder, int position) {
        String weatherForThisDay = mWeatherData[position];
        viewHolder.mWeatherTextView.setText(weatherForThisDay);
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) {
            return 0;
        }
        return mWeatherData.length;
    }

    public void setWeatherData(String[] WeatherData) {
        mWeatherData = WeatherData;
        notifyDataSetChanged();
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        final public TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);

            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
        }
    }
}
