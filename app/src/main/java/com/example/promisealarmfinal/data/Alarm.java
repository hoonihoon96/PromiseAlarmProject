package com.example.promisealarmfinal.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Alarm {
    private int id;
    private String title;
    private String description;
    private String creator;
    private Calendar alarmDateTime;
    private int endMinute;
    private Calendar createdDateTime;
    private int modifiedCount;

    public Alarm() {

    }

    public Alarm(int id, String title, String description, String creator,
                 Calendar alarmDateTime, int endMinute, Calendar createdDateTime, int modifiedCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.alarmDateTime = alarmDateTime;
        this.endMinute = endMinute;
        this.createdDateTime = createdDateTime;
        this.modifiedCount = modifiedCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Calendar getAlarmDateTime() {
        return alarmDateTime;
    }

    public String getAlarmDateTimeInString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return format.format(alarmDateTime.getTime());
    }

    public String getAlarmDateInString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(alarmDateTime.getTime());
    }

    public String getAlarmTimeInString() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        return format.format(alarmDateTime.getTime());
    }

    public void setAlarmDateTime(Calendar alarmDateTime) {
        this.alarmDateTime = alarmDateTime;
    }

    public void setAlarmDateTimeOfString(String alarmDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date tempDate = null;

        try {
            tempDate = format.parse(alarmDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);

        this.alarmDateTime = calendar;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public String getEndMinuteInString() {
        return endMinute + "";
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public Calendar getCreatedDateTime() {
        return createdDateTime;
    }

    public String getCreatedDateTimeInString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return format.format(createdDateTime.getTime());
    }

    public void setCreatedDateTime(Calendar createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setCreatedDateTimeOfString(String createdDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date tempDate = null;

        try {
            tempDate = format.parse(createdDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(tempDate);

        this.createdDateTime = tempCalendar;
    }

    public int getModifiedCount() {
        return modifiedCount;
    }

    public void setModifiedCount(int modifiedCount) {
        this.modifiedCount = modifiedCount;
    }
}
