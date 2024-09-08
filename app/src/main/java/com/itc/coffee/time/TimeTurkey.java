package com.itc.coffee.time;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeTurkey {
    @SerializedName("datetime")
    @Expose
    private String dataTime;


    @SerializedName("day_of_week")
    @Expose
    private String dayOfWeek;

    @SerializedName("week_number")
    @Expose
    private String weekNumber;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(String weekNumber) {
        this.weekNumber = weekNumber;
    }
}
