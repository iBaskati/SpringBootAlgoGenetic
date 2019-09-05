package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;

import com.example.demo.DS.savingsDS;

public class ClarkAndWright {
	
	
	static double[][] matrice;
    
    ArrayList<savingsDS> savings = new ArrayList<>();
    
    static ArrayList<Integer>  Tournee;
    
    
    
    
    public ClarkAndWright( double[][] matriceP ) {
        matrice = matriceP;
        calculSaving();
    }
    
    
    
    
    
    
    
    
    
    public void calculSaving(){
        int villes = matrice[0].length;
        for(int i=1 ; i<villes  ; i++){
            for (int j = i+1; j < villes; j++) {
                double saving = matrice[0][i] + matrice[0][j] - matrice[i][j];
                savingsDS S = new savingsDS(i,j,saving);
                savings.add(S);
            }
        }
        Collections.sort(savings);
        Collections.reverse(savings);
        
        //System.out.println(savings);
        
    }
    
    
    
    
    
    
    
    public int[] RunAlgoCLARKEandWRIGHT(){
        
        Tournee = new ArrayList<>();
        
        Tournee.add(0);
        Tournee.add(savings.get(0).i);
        Tournee.add(savings.get(0).j);
        Tournee.add(0);
        
        int sizeTournee = 4;
                
        for(int par=1 ; par<savings.size() ; par++){
            savingsDS S = savings.get(par);
            if(Tournee.contains(S.i) && Tournee.contains(S.j)){
                continue;
            }
            else if(!Tournee.contains(S.i) && !Tournee.contains(S.j)){
                continue;
            }
            else if(Tournee.contains(S.i)){
                if(Tournee.indexOf(S.i)==1){
                    for(int element = sizeTournee-1 ; element>=2 ; element-- ){
                        Tournee.set(element,Tournee.get(element-1));
                    }
                    Tournee.set(1,S.j);
                    Tournee.add(0);
                    sizeTournee++;
                }
                else if(Tournee.indexOf(S.i)==sizeTournee-2){
                    Tournee.set(sizeTournee-1,S.j);
                    Tournee.add(0);
                    sizeTournee++;
                }
            }
            else{
                if(Tournee.indexOf(S.j)==1){
                    for(int element = sizeTournee-1 ; element>=2 ; element-- ){
                        Tournee.set(element,Tournee.get(element-1));
                    }
                    Tournee.set(1,S.i);
                    Tournee.add(0);
                    sizeTournee++;
                }
                else if(Tournee.indexOf(S.j)==sizeTournee-2){
                    Tournee.set(sizeTournee-1,S.i);
                    Tournee.add(0);
                    sizeTournee++;
                }
            }
        }
        
        
        System.out.println(Tournee.size());
        
        
        int[] citiesOrder = new int[SpringBootDemoAppApplication.numOfCities-1];
        
        for(int i=1 ; i<Tournee.size()-1 ; i++) {
        	citiesOrder[i-1] = Tournee.get(i);
        }
        
        for(int x : citiesOrder) {
        	System.out.println(x);
			if(x==0) {
				for(int i=0 ; i<3 ; i++) {
					System.out.println("warning");
				}
				break;
			}
		}
        
        //System.out.println(citiesOrder.length);
        
        return citiesOrder;
    }
    
    
}
