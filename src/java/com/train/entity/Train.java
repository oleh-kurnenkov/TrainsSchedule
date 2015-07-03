package com.train.entity;

import java.sql.Time;

public class Train {
    private int id=0;
    private String destPoint;
    private Time time;
    private Day day;
    
    public Train(int i,String dest,Time t,Day d){
        id = i;
        day = d;
        if(dest!=null)
        destPoint =dest;
        else
        destPoint = "";

        if(t!=null)
        time = t;
        else
            time = new Time(0,0,0);
    }

    public Train(int i){
        this(i,"",new Time(0,0,0),Day.Workdays);
    }

    public Train(int i,String dest){
        this(i,dest,new Time(0,0,0),Day.Workdays);
    }

    public Train(){
        this(0,"",new Time(0,0,0),Day.Workdays);
    }

    public void setId(int i){
        id = i;
    }

    public int getId(){
        return id;
    }

    public void setDestPoint(String dest){
        if(dest!=null)
        destPoint = dest;
    }

    public String getDestPoint(){
        return destPoint;
    }

    public void settime(Time t){
        if(t!=null)
        time = t;
    }

    public Time getTime(){
        return time;
    }
    
    public void setDay(Day d){
    	day = d;
    }
    public Day getDay(){
    	return day;
    }
    
}
