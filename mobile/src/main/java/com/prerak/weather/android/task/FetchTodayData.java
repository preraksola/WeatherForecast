package com.prerak.weather.android.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.prerak.weather.android.fragment.TodayFragment;
import com.prerak.weather.android.activity.MainActivity;
import com.prerak.weather.android.utility.CheckAvailability;
import com.prerak.weather.android.utility.ExtractTodayData;

import org.json.JSONException;
import org.json.JSONObject;

/*
    This class fetches the current weather data from the API in an background thread
 */

public class FetchTodayData extends AsyncTask<String, String, String> {

    SharedPreferences mPreferences;
    TodayFragment mTodayFragment = null;
    Context mContext = null;

    public FetchTodayData(TodayFragment mTodayFragment, Context mContext)
    {
        this.mTodayFragment = mTodayFragment;
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, mContext.MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(String... params)
    {
        if(CheckAvailability.isNetworkAvailable(mContext)) {
            APIInteraction apiInteraction = new APIInteraction(mContext);
            return apiInteraction.APICallForToday(params[0]);
        }

        else if (mPreferences.contains(MainActivity.TODAY_DATA))
            return mPreferences.getString(MainActivity.TODAY_DATA, "");

        else
            return "Please connect to internet";
    }

    @Override
    protected void onPostExecute(String json) {

        if(json.contains("Please connect") )
            mTodayFragment.errorRaised(2);

        else {
            try
            {
                JSONObject data = new JSONObject(json);
                ExtractTodayData etd = new ExtractTodayData(data, mContext);
                mTodayFragment.fetchedData(etd.fetchDataFromResponse());
            }

            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}