package com.example.demo.DS;

public class PointDS {
	public double x;
	public double y;
	public String name;
	
	public PointDS(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	
	public double distanceFrom(PointDS p) {
		return Math.sqrt(Math.pow((this.x-p.x),2) + Math.pow((this.y - p.y), 2));
	}
}
