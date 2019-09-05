package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetResult {
	
	
	
	
	@RequestMapping(value="/getResult",  method = RequestMethod.POST)
	public int[] controllerMethod(@RequestParam(value="disMatrix[]") List<Integer> disMatrix, @RequestParam(value="numberOfMarkers") Integer numberOfMarkers){
       
		/// convert 1d matrix to 2d one 
		SpringBootDemoAppApplication.numOfCities = numberOfMarkers;
		SpringBootDemoAppApplication.matrixDis  = new double[SpringBootDemoAppApplication.numOfCities][SpringBootDemoAppApplication.numOfCities];
		for(int i=0 ; i<SpringBootDemoAppApplication.numOfCities ; i++) {
			for(int j=0 ; j<SpringBootDemoAppApplication.numOfCities ; j++ ) {
				SpringBootDemoAppApplication.matrixDis[i][j] = disMatrix.get(j+i*SpringBootDemoAppApplication.numOfCities);
			}
		}
		disMatrix = null;
		
		return GAFunctions.callAlgo();
		
    }
	
	
	
	
}
