package com.example.ogl4jo3.smartsocket;

public class Smartsocket {

	private String name;
	private int hour, minute;

	public Smartsocket(String name, int hour, int minute) {
		setName(name);
		sethour(hour);
		setminute(minute);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int gethour() {
		return hour;
	}

	public void sethour(int hour) {
		this.hour = hour;
	}

	public int getminute() {
		return minute;
	}

	public void setminute(int minute) {
		this.minute = minute;
	}

}
