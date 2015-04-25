package com.prerak.weather.android.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.prerak.weather.android.activity.MainActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/*
    This class contains the methods to make a request to the API and fetch the data
 */

public class APIInteraction {

    private static String API_URL_FORECAST = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt=5&mode=json&units=metric&";
    private static String API_URL_TODAY = "http://api.openweathermap.org/data/2.5/weather?units=metric&";
    SharedPreferences preferences;
    Context mContext;

    public APIInteraction(Context mContext)
    {
        this.mContext = mContext;
    }

    public  String APICallForToday(String data)
    {
        /*
            Function to make a request to API to fetch current weather details
         */

        String json = null;
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            preferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            URL requestURL = new URL(API_URL_TODAY + data);

            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            is = conn.getInputStream();

            if (conn.getResponseCode() != 200)
            {
                SharedPreferences mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES,mContext.MODE_PRIVATE);
                if(mPreferences.contains(MainActivity.TODAY_DATA))
                    return mPreferences.getString(MainActivity.TODAY_DATA, null);
                else
                    return "Please connect to internet";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String tmp;

            while ((tmp = reader.readLine()) != null)
                builder.append(tmp + "\n");

            is.close();
            json = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException fff) {
            fff.printStackTrace();
            return "Please connect to internet";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return "Please connect to internet";

        } finally {
            conn.disconnect();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MainActivity.TODAY_DATA, json);
        editor.apply();

        return json;
    }

    public String APICallForForecast(String data)
    {
        /*
            Function to make a request to API to fetch forecast weather details
         */

        String json = null;
        HttpURLConnection conn = null;
        InputStream is = null;

        try {
            preferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            URL requestURL = new URL(API_URL_FORECAST + data);
            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            is = conn.getInputStream();

            if (conn.getResponseCode() != 200)
            {
                SharedPreferences mPreferences = mContext.getSharedPreferences(MainActivity.APP_PREFERENCES,mContext.MODE_PRIVATE);
                if(mPreferences.contains(MainActivity.TODAY_DATA))
                    return mPreferences.getString(MainActivity.TODAY_DATA, null);
                else
                    return "Please connect to internet";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String tmp;

            while ((tmp = reader.readLine()) != null)
                builder.append(tmp + "\n");

            is.close();
            conn.disconnect();

            json = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return "Please connect to internet.";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e)   {
            e.printStackTrace();
            return "Please connect to internet.";
        }

        finally {
            conn.disconnect();
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MainActivity.FORECAST_DATA, json);
        editor.commit();
        return json;
    }
}
