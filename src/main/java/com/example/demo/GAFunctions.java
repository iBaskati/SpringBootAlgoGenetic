package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Random;

import com.example.demo.DS.TourDS;
import com.example.demo.DS.ProbIndDS;

public class GAFunctions {
	
	public static ArrayList<TourDS> nextPopulation;
	static TreeMap<Integer,Integer> map = new TreeMap<>();
	public  static final double mutationRate = 0.02;
    public static final int tournamentSize = 15;
	
	public static TourDS nearest_neighbor() {
		int start = 0;
		TourDS tour = new TourDS(0, new int[SpringBootDemoAppApplication.numOfCities-1]);
		HashSet<Integer> set = new HashSet<>();
		set.add(0);
		for(int i=0 ; i<SpringBootDemoAppApplication.numOfCities-1; i++) {
			int ind = -1;
			double min = Double.MAX_VALUE;
			for(int j=0 ; j<SpringBootDemoAppApplication.numOfCities ; j++) {
				if(i!=j && SpringBootDemoAppApplication.matrixDis[start][j] < min && !set.contains(j)) {
					min = SpringBootDemoAppApplication.matrixDis[start][j];
					ind = j;
				}
			}
			start = ind;
			set.add(ind);
			tour.citiesOrder[i] = ind;
		}
		tour.distance = calculDistance(tour);
		tour.fitness = 1/(1+tour.distance);
		return tour;
	}
	
	
	public static void  mutation(TourDS tour) {
		for(int i=0 ; i<SpringBootDemoAppApplication.numOfCities-1 ; i++) {
			if(Math.random()<mutationRate) {
				int IndB = (int) Math.floor(Math.random()*(SpringBootDemoAppApplication.numOfCities-1));
				// permutation 
				int cityAInd = tour.citiesOrder[i];
				tour.citiesOrder[i] = tour.citiesOrder[IndB];
				tour.citiesOrder[IndB] = cityAInd;
			}
		}
		tour.distance = calculDistance(tour);
		tour.fitness = 1/(1+tour.distance);
	}

	
	public  static double calculDistance(TourDS tour) {
		double distance = 0;
		int prv = 0;
		for(int j=0 ; j<SpringBootDemoAppApplication.numOfCities -1 ; j++) {
			distance += SpringBootDemoAppApplication.matrixDis[prv][tour.citiesOrder[j]];
			prv = tour.citiesOrder[j];
		}
		distance += SpringBootDemoAppApplication.matrixDis[prv][0];
		return distance;
	}
	

	public static TourDS cross_over(TourDS tour1, TourDS tour2, int NC) {
		TourDS tour = new TourDS(0,new int[NC]);
		HashSet<Integer> map = new HashSet<>();
		int indexA = (int) (Math.random() * (NC));
		int indexB = (int) (Math.random() * (NC));
		if(indexA>indexB) {
			int ind = indexA;
			indexA = indexB;
			indexB = ind;
		}
		for(int i=indexA ; i<=indexB ; i++) {
			tour.citiesOrder[i] = tour1.citiesOrder[i];
			map.add(tour1.citiesOrder[i]);
		}
		int I = 0;
		for(int i=0 ; i<NC && I<NC; i++) {
			if(!map.contains(tour2.citiesOrder[i])) {
				if(I==indexA) {
					I = indexB + 1;
				}
				tour.citiesOrder[I] = tour2.citiesOrder[i];
				I++;
			}
		}
		tour.distance = calculDistance(tour);
		tour.fitness = 1/(1+tour.distance);
		return tour;
	}

	
	public static TourDS cross_over_2(TourDS tour) {
		int windowSz = (SpringBootDemoAppApplication.numOfCities-1)/3;
		int cut = (int) (Math.random() * (windowSz));
		int indA = (int) (Math.random()  * ((SpringBootDemoAppApplication.numOfCities-1)/2));
		int[] newOrd = new int[SpringBootDemoAppApplication.numOfCities-1];
		for(int i=0 ; i<indA ; i++) {
			newOrd[i] = tour.citiesOrder[i];
		}
		for(int i=indA ; i<indA + (windowSz-cut) ; i++) {
			newOrd[i] = tour.citiesOrder[i+cut];
		}
		for(int i = indA + (windowSz-cut) ; i<indA + windowSz ; i++ ) {
			newOrd[i] = tour.citiesOrder[i-(windowSz-cut)];
		}
		for(int i = indA + windowSz ; i<SpringBootDemoAppApplication.numOfCities-1 ; i++) {
			newOrd[i] = tour.citiesOrder[i];
		}
		tour.citiesOrder = Arrays.copyOf(newOrd,SpringBootDemoAppApplication.numOfCities-1);
		tour.distance = calculDistance(tour);
		tour.fitness = 1/(1+tour.distance);
		return tour;
	}
	

	public static void nextGeneration(ArrayList<TourDS> currentPopulation) {
		nextPopulation = new ArrayList<>();
		int N = currentPopulation.size();
		double totalF = 0;
		nextPopulation.add(Collections.min(currentPopulation)); // keep the first one 
		totalF += 1/(Collections.min(currentPopulation).distance);
		for (int i = 1; i < N ; i++) {
			TourDS tour = cross_over(tournament_selection(currentPopulation),tournament_selection(currentPopulation),
					SpringBootDemoAppApplication.numOfCities - 1);
			nextPopulation.add(tour);
		}
		for (int i = 1 ; i < N ; i++) {
			GAFunctions.mutation(nextPopulation.get(i));
			totalF += 1/(1+nextPopulation.get(i).distance);
		}
		for(TourDS t : nextPopulation) {
			t.probablity = t.fitness / totalF;
		}
	}
	

	public static TourDS tournament_selection(ArrayList<TourDS> currentPopulation) {
		int randomId = (int) (Math.random() * (currentPopulation.size()));
        TourDS fittest = new TourDS(0,new int[SpringBootDemoAppApplication.numOfCities-1]);
        fittest.copy(currentPopulation.get(randomId));
        for (int i = 0; i < tournamentSize; i++) {
            randomId = (int) (Math.random() * (currentPopulation.size()));
            if(currentPopulation.get(randomId).distance < fittest.distance ) {
            	fittest.copy(currentPopulation.get(randomId));
            }
        }
        return fittest;
    }
	
	
	public static TourDS proportionate_selection(ArrayList<TourDS> currentPopulation) {
		double totalF = 0;
		for(int i=0 ; i<currentPopulation.size(); i++) {
			totalF += currentPopulation.get(i).fitness;
		}
		double r = new Random().nextDouble() * totalF;
		for(int i=0 ; i<currentPopulation.size(); i++) {
			r = r - currentPopulation.get(i).fitness;
			if(r<=0) {
				return currentPopulation.get(i);
			}
		}
		return currentPopulation.get(currentPopulation.size()-1);
	}
	
	
	public static ArrayList<TourDS> listInitialPopulations(int k){
		ArrayList<TourDS> populations =  new ArrayList<>();
		ArrayList<ProbIndDS> list;
		double totalF = 0;
		//// random Population 
		for(int i=0 ; i<k/2 - 5  ; i++) {
			int[] citiesOrder = new int[SpringBootDemoAppApplication.numOfCities-1];
			list = new ArrayList<>();
			
			for(int j=0 ; j< SpringBootDemoAppApplication.numOfCities-1 ; j++) {
				list.add(new ProbIndDS(Math.random(),j+1));
			}
			Collections.sort(list);
			double distance = 0;
			int prv = 0;
			for(int j=0 ; j<SpringBootDemoAppApplication.numOfCities -1 ; j++) {
				citiesOrder[j] = list.get(j).cityInd;
				distance += SpringBootDemoAppApplication.matrixDis[prv][citiesOrder[j]];
				prv = citiesOrder[j];
			}
			distance += SpringBootDemoAppApplication.matrixDis[prv][0];
			populations.add(new TourDS(distance, Arrays.copyOf(citiesOrder,SpringBootDemoAppApplication.numOfCities-1)));
			totalF += 1/(1.0+distance);
		}
		/// 5 nearest_neighbor 
		for(int i=0 ; i<5; i++) {
			populations.add(nearest_neighbor());
			totalF += 1/(1.0+populations.get(k/2 -5 + i).distance);
		}
		/// cross_over
		for(int i=0 ; i<k-k/2; i++) {
			TourDS tour = cross_over(tournament_selection(populations),tournament_selection(populations),
					SpringBootDemoAppApplication.numOfCities - 1);
			totalF += 1/(1.0+tour.distance);
			populations.add(tour);
		}
		//Collections.sort(populations);
		for(TourDS t : populations) {
			t.probablity = t.fitness / totalF;
			System.out.println(t.fitness+" "+t.distance);
		}
		return populations;
	}


	public static int[]  callAlgo() {
		//// call algo 
		GeniticAlgo geniticAlgo = new GeniticAlgo();
		/// wrap up result 
		int[] solution = new int[SpringBootDemoAppApplication.numOfCities+1];
		solution[0] = 0;
		/// Ruuuuuunnnnn 
		int[] res = geniticAlgo.RunGeniticAlgo(100,Integer.MAX_VALUE);
		int i=1;
		for(int city : res) {
			solution[i] = city;
			i++;
		}
		solution[SpringBootDemoAppApplication.numOfCities] = 0;
		return solution;
	}
	
}
