package com.prerak.weather.android.view;

/*
    Forecast Item entity class
 */
public class ForecastItemView {

    private String day;
    private String desc;
    private long temp;
    private String imageId;

    public ForecastItemView(String day, String desc, long temp, String imageId) {
        this.day = day;
        this.desc = desc;
        this.temp = temp;
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTemp() {
        return temp;
    }

    public void setTemp(long temp) {
        this.temp = temp;
    }

}
