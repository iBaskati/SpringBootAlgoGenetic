package com.example.demo.DS;

public class ProbIndDS implements Comparable<ProbIndDS> {
	public double prob;
	public int cityInd;
	
	
	public ProbIndDS(double prob , int cityInd){
		this.prob = prob;
		this.cityInd = cityInd;
	}


	@Override
	public int compareTo(ProbIndDS arg0) {
		if(this.prob > arg0.prob) {
			return 1;
		}
		else if(this.prob < arg0.prob) {
			return -1;
		}
		return 0;
	}
	
	
	
}
