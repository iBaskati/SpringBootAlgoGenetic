package com.example.demo.DS;

import java.util.Arrays;

import com.example.demo.SpringBootDemoAppApplication;

public class TourDS implements  Comparable<TourDS>{
	public double  distance; 
	public int[] citiesOrder;
	public double probablity;
	public double fitness;
	
	public TourDS(double  distance, int[] citiesOrder) {
		this.distance = distance;
		this.citiesOrder = citiesOrder;
		this.fitness = 1/(1+distance);
	}
	
	
	public void copy(TourDS LFO) {
		this.citiesOrder = Arrays.copyOf(LFO.citiesOrder,SpringBootDemoAppApplication.numOfCities-1);
		this.distance = LFO.distance;
		this.fitness = LFO.fitness;
		this.probablity = LFO.probablity;
	}

	@Override
	public int compareTo(TourDS arg0) {
		if(this.distance > arg0.distance) {
			return 1;
		}
		else if(this.distance < arg0.distance) {
			return -1;
		}
		return 0;
	}


	@Override
	public String toString() {
		return "ListFitnessObject [distance=" + distance + ", citiesOrder=" + Arrays.toString(citiesOrder) + "]";
	}
	
	
	
	
	
	
}
