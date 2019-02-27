package edu.studyup.entity;

import java.util.Arrays;

public class Location {

	public final double lon;
	public final double lat;
	public final double[] bounds;

	public Location(double lon, double lat) {
		this(lon, lat, null);
	}

	public Location(double lon, double lat, double[] bounds) {
		this.lat = lat;
		this.lon = lon;
		this.bounds = bounds;
	}

	public double[] getBoundingBox() {
		return this.bounds;
	}
	
	public String toString() {
		return "[" + this.lon + ", " + this.lat+ "]" + (this.bounds == null ? "" : "\t" + Arrays.toString(this.bounds));
	}
}
