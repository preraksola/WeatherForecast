package com.prerak.weather.android.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.entity.ForecastEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
    This class contains methods to extract various details for the forecast view from the received JSON from API
 */
public class ExtractForecastData {

    ForecastEntity mForecastEntity = new ForecastEntity();
    Context mContext;
    SharedPreferences mPreferences;
    JSONObject mData;

    public ExtractForecastData(JSONObject mData, Context mContext) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public ForecastEntity extractDataFromResponse() throws JSONException {
        JSONArray list = mData.getJSONArray("list");
        mForecastEntity.setAvgTemp(avgTemp(list));
        mForecastEntity.setDesc(weatherDesc(list));
        mForecastEntity.setImageIds(imageIds(list));
        mForecastEntity.setWeekDays(weekDays());

        return mForecastEntity;
    }

    public String[] imageIds(JSONArray list) throws JSONException {
        String[] tmp = new String[5];

        for(int i = 0; i < 5; i++) {

            JSONObject currentObj = list.getJSONObject(i);
            tmp[i]= currentObj.getJSONArray("weather").getJSONObject(0).getString("icon");
        }

        return tmp;
    }

    public String[] weekDays()
    {
        String[] tmp = new String[5];

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < 5; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_WEEK, i + 1);

            tmp[i] = dayFormat.format(calendar.getTime());
        }

        return tmp;
    }

    public long[] avgTemp(JSONArray list) throws JSONException {
        long[] tmp = new long[5];
        String[] time = {"day", "min", "max", "night", "eve", "morn"};
        Double[] temperatures = new Double[6];

        for(int i = 0; i < 5; i++) {
            JSONObject currentObj = list.getJSONObject(i);

            for (int j = 0; j < 6; j++) {
                temperatures[j] = currentObj.getJSONObject("temp").getDouble(time[j]);
            }

            Double avgTemp = 0.0;

            for (Double t : temperatures)
                avgTemp += t;

            avgTemp = avgTemp / 6;

            mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, mContext.MODE_PRIVATE);
            String unit = mPreferences.getString(MainActivity.TEMP_UNIT, null);

            if(unit.equals("C"))
                tmp[i] = Math.round(avgTemp);

            else
                tmp[i] = Math.round((avgTemp * 9 / 5.0) + 32);
        }
        return tmp;
    }

    public String[] weatherDesc(JSONArray list) throws JSONException {
        String[] tmp = new String[5];

        for(int i = 0; i < 5; i++) {

            JSONObject currentObj = list.getJSONObject(i);
            tmp[i]= currentObj.getJSONArray("weather").getJSONObject(0).getString("description");
        }
        return tmp;
    }
}