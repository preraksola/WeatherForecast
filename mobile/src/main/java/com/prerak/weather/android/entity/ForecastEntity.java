package com.prerak.weather.android.entity;

/**
 * Created by Prerak on 24/04/2015.
 */
public class ForecastEntity {

    String[] imageIds;
    String[] weekDays;
    long[] avgTemp;
    String[] desc;

    public String[] getImageIds() {
        return imageIds;
    }

    public void setImageIds(String[] imageIds) {
        this.imageIds = imageIds;
    }

    public String[] getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String[] weekDays) {
        this.weekDays = weekDays;
    }

    public long[] getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(long[] avgTemp) {
        this.avgTemp = avgTemp;
    }

    public String[] getDesc() {
        return desc;
    }

    public void setDesc(String[] desc) {
        this.desc = desc;
    }
}
