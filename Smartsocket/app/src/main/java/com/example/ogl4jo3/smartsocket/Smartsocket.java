package com.example.ogl4jo3.smartsocket;

public class Smartsocket {

	private String name, device_id;
	private int hour, minute;

	public Smartsocket(String name, String device_id, int hour, int minute) {
		setName(name);
		setDevice_id(device_id);
		sethour(hour);
		setminute(minute);

	}

	public String getName() {
		return name;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_id() {
		return device_id;
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
