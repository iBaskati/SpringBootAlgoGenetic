package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import com.example.demo.DS.TourDS;

public class GeniticAlgo {
	
	public TourDS best;
	
	
	public GeniticAlgo() {
		this.best = new TourDS(Integer.MAX_VALUE, new int[SpringBootDemoAppApplication.numOfCities-1]);
	}
	
	
	public int[] RunGeniticAlgo(int initPopSz, int numOfIteration){
		// Step One ::: Generate First Population 
		ArrayList<TourDS> currentPopulation = GAFunctions.listInitialPopulations(initPopSz);
		this.best.copy(Collections.min(currentPopulation));
		System.out.println(this.best.toString());
		long startingTime = System.currentTimeMillis();
		//// create next generation for numOfIteration time 
		for(long i=0 ; i!=-1 ; i++) {
			// break in 1/2 minutes 			
			if(System.currentTimeMillis() - startingTime > 30*1000) {
				break;
			}		
			// next generation and mutation .. 
			GAFunctions.nextGeneration(currentPopulation);
			currentPopulation = new ArrayList<>();
			currentPopulation.addAll(GAFunctions.nextPopulation);
			//Collections.sort(currentPopulation);		
			if(this.best.distance > Collections.min(currentPopulation).distance) {
				this.best.copy(Collections.min(currentPopulation));
				System.out.println("new Sol "+this.best.toString());
			}
			if(i%50==0) {
				System.out.println(this.best.distance+" "+Collections.min(currentPopulation).distance+"   "+Collections.max(currentPopulation).distance);
			}
		}
		System.out.println("Distance of the best solution !!  "+ this.best.distance);
		return this.best.citiesOrder;
	}
	
}
