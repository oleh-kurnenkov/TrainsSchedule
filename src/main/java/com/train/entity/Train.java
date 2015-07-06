package com.train.entity;

import java.sql.Time;

public class Train {
	private int id = 0;
	private String destPoint;
	private Time time;
	private Day day;

	public Train(int i, String dest, Time t, Day d) {
		id = i;
		day = d;
		destPoint = dest;
		time = t;
	}

	public Train() {
		this(0, "", new Time(0, 0, 0), Day.Workdays);
	}

	public void setId(int i) {
		id = i;
	}

	public int getId() {
		return id;
	}

	public void setDestPoint(String dest) {
		destPoint = dest;
	}

	public String getDestPoint() {
		return destPoint;
	}

	public void settime(Time t) {
		time = t;
	}

	public Time getTime() {
		return time;
	}

	public void setDay(Day d) {
		day = d;
	}

	public Day getDay() {
		return day;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		 Train train = (Train)obj;
		 return this.getId()==train.getId() && this.getDestPoint().equals(train.getDestPoint()) &&
				 this.getTime().equals(train.getTime()) && this.getDay().equals(train.getDay());
	}

}
