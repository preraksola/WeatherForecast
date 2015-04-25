package com.prerak.weather.android.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.prerak.weather.android.R;
import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.entity.TodayEntity;

import org.json.JSONException;
import org.json.JSONObject;

/*
    This class contains the methods to extract data from the received JSON from API
 */

public class ExtractTodayData {
    JSONObject mData;
    Context mContext;
    SharedPreferences mPreferences;
    TodayEntity mTodayEntity = new TodayEntity();

    public ExtractTodayData(JSONObject mData, Context mContext) {

        this.mData = mData;
        this.mContext = mContext;
    }


    public TodayEntity fetchDataFromResponse() {
        try
        {
            mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, mContext.MODE_PRIVATE);
            String tempUnit = mPreferences.getString(MainActivity.TEMP_UNIT, null);
            if(tempUnit.equals("C"))
                mTodayEntity.setCurrentTemp(mData.getJSONObject("main").getDouble("temp"));

            else
            {
                Double tmp = mData.getJSONObject("main").getDouble("temp");
                mTodayEntity.setCurrentTemp(((tmp * 9 / 5.0) + 32));
            }

            mTodayEntity.setCountry(mData.getJSONObject("sys").getString("country"));
            mTodayEntity.setWeatherType(mData.getJSONArray("weather").getJSONObject(0).getString("main"));

            mTodayEntity.setHumidity(mData.getJSONObject("main").getString("humidity"));
            mTodayEntity.setLocation(mData.getString("name"));
            mTodayEntity.setImageCode(mData.getJSONArray("weather").getJSONObject(0).getString("icon"));
            mTodayEntity.setWindSpeed(mData.getJSONObject("wind").getString("speed"));
            String measureUnit = mPreferences.getString(MainActivity.UNIT, null);

            if(measureUnit.equals("metric"))
                mTodayEntity.setPressure(mData.getJSONObject("main").getDouble("pressure"));

            else
            {
                Double tmp;
                tmp = mData.getJSONObject("main").getDouble("pressure");
                mTodayEntity.setPressure(Math.round(tmp * (1.450377 / 10000)) / 100.0);
            }

            if(mData.has("rain")) {
                if(measureUnit.equals("metric"))
                {
                    mTodayEntity.setRain(mData.getJSONObject("rain").getString("3h"));
                    mTodayEntity.setPressure(mData.getJSONObject("main").getDouble("pressure"));
                }

                else
                {
                    Double tmp = Double.parseDouble(mData.getJSONObject("rain").getString("3h"));
                    tmp = Math.round(tmp * 0.03937)/100.0;
                    mTodayEntity.setRain(tmp.toString());
                    tmp = mData.getJSONObject("main").getDouble("pressure");
                    mTodayEntity.setPressure(Math.round(tmp * (1.450377 / 10000)) / 100.0);
                }
            }

            else
                mTodayEntity.setRain("0");

            int deg = mData.getJSONObject("wind").getInt("deg");
            int tmp = (int)((deg/22.5)+0.5);
            mTodayEntity.setDirection((mContext.getResources().getStringArray(R.array.directions_array)[tmp]));
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
        return mTodayEntity;
    }
}
