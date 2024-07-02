package entities;

import java.io.Serializable;

public class DisplayTime implements Serializable {
    private String day;
    private String month;
    private String year;
    private String hour;
    private String minute;

    public DisplayTime(String full_time){
        // assume the format is HH:MM, DD/MM/YYYY

        this.hour=full_time.substring(0,2);
        this.minute=full_time.substring(3,5);
        this.day=full_time.substring(7,9);
        this.month=full_time.substring(10,12);
        this.year=full_time.substring(13);
    }

    public DisplayTime(String hour,String minute, String day, String month, String year){
        this.hour=hour;
        this.minute=minute;
        this.day=day;
        this.month=month;
        this.year=year;
    }

    public void setDay(String day){
        this.day=day;
    }
    public String getDay(){
        return this.day;
    }
    public void setMonth(String month){
        this.month=month;
    }
    public String getMonth(){
        return this.month;
    }
    public void setYear(String year){
        this.year=year;
    }
    public String getYear(){
        return this.year;
    }
    public void setHour(String hour){
        this.hour=hour;
    }
    public String getHour(){
        return this.hour;
    }
    public void setMinute(String minute){
        this.minute=minute;
    }
    public String getMinute(){
        return this.minute;
    }
    public String toString(){
        String s=this.hour+":"+this.minute+", "+this.day+"/"+this.month+"/"+this.year+"\n";
        return s;
    }
}
