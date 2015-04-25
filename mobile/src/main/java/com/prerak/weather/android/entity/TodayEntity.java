package com.prerak.weather.android.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.prerak.weather.android.R;
import com.prerak.weather.android.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prerak on 21/04/2015.
 */
public class TodayEntity {

    private String country;
    private String location;
    private Double currentTemp;
    private String windSpeed;
    private String weatherType;
    private String humidity;
    private Double pressure;
    private String imageCode;
    private String rain;
    private String direction;


    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private static View view;

    SharedPreferences preferences;

    public TodayEntity()
    {
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(Double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }
}
