package com.jaydi.ruby.models;

public class RubyZone {
	private long id;
	private double lat;
	private double lng;
	private int range;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

}
