/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.DS;

/**
 *
 * @author 0110
 */
public class savingsDS implements Comparable<savingsDS> {
    public int i;
    public int j;
    public double saving; 
    
    
    
    public savingsDS(int i, int j, double saving){
        this.i = i;
        this.j = j;
        this.saving = saving;
    }

    @Override
    public int compareTo(savingsDS o) {
    	if(this.saving > o.saving) {
    		return 1;
    	}
        return -1;
    }
    
    @Override
    public String toString(){
        return "From "+this.i+" to "+this.j+" and saving = "+this.saving+"\n";
    }
}
