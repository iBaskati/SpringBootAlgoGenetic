package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DS.PointDS;



@SpringBootApplication
@Controller
public class SpringBootDemoAppApplication {
	
	public static double[][] matrixDis;
	public static int numOfCities;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoAppApplication.class, args);
	}
	
	/// Controllers !! 
	
	@RequestMapping(value = "/tspLib", method = RequestMethod.GET)
	public String tspLib() {
	      return "tspLib";
	}
	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public String excel() {
	      return "excel";
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/tspLib", method = RequestMethod.POST)
    public String tspService(	Model model, @RequestParam("dataFile") MultipartFile dataFile, @RequestParam("resFile") MultipartFile resFile) throws IOException {
        if (!dataFile.isEmpty() && !resFile.isEmpty()) {
        	ArrayList<PointDS> listData = this.readInputDataFromTSPLIB(dataFile);        	
            numOfCities = listData.size();
            matrixDis  = new double[numOfCities][numOfCities];
            for(int i=0 ; i<numOfCities ; i++) {
    			for(int j=0 ; j<numOfCities ; j++ ) {
    				matrixDis[i][j] = listData.get(i).distanceFrom(listData.get(j));
    			}
    		}
            int[] listRes = this.readOutputFileTSPLIB(resFile);
            int[] GARes = GAFunctions.callAlgo();
            System.out.println("Final Solution ::: ");
            double mySol = calculScoreDistance(GARes);
            System.out.println(mySol);
            double optRes = calculScoreDistance(listRes);
            System.out.println("Data Score "+ optRes);
            model.addAttribute("mySol",mySol);
            model.addAttribute("optRes",optRes);
        }
        else {
            System.out.println("File Not Found !! ");
            model.addAttribute("Error","File Not Found");
        }
        return "tspLib";
    }
	
	
	
	
	
	
	
	@RequestMapping(value = "/showExcelInMap", method = RequestMethod.POST)
	public String showExcelInMap(Model model , @RequestParam("excelFile") MultipartFile excelFile) throws IOException {
	    XSSFWorkbook workbook = new XSSFWorkbook(excelFile.getInputStream());
	    XSSFSheet worksheet = workbook.getSheetAt(0);
	    ArrayList<PointDS> listData = new ArrayList<>();
	    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
	        XSSFRow row = worksheet.getRow(i);
	        double x = (double) (row.getCell(1).getNumericCellValue());
	        double y = (double) (row.getCell(2).getNumericCellValue());
	        PointDS P = new PointDS(x,y);
	        P.name = row.getCell(0).getStringCellValue();
	        listData.add(P);   
	    }
	    workbook.close();
	    model.addAttribute("listData",listData);
	    return "showExcelInMap";
	}
	
	
	
	
	
	/////////////////////////// Functions /////////////// 
	
	public  double calculScoreDistance(int[] list) {
		
		double distance = 0;
		
		for(int j=1 ; j<list.length ; j++) {
			distance += SpringBootDemoAppApplication.matrixDis[list[j-1]][list[j]];
		}
				
		return distance;
		
	}
	
	
	
	public ArrayList<PointDS> readInputDataFromTSPLIB(MultipartFile dataFile) throws IOException{
		/// Read Data !!! 
        InputStream isData = dataFile.getInputStream();
        Scanner sc = new Scanner(isData);
        while(!sc.nextLine().equals("NODE_COORD_SECTION")) {
        	//System.out.println("----");
        }
        ArrayList<PointDS> listData = new ArrayList<>();
        String l;
        while(!(l = sc.nextLine()).equals("EOF")) {
        	String line[] = l.split(" ");
        	listData.add(new PointDS(Double.parseDouble(line[line.length-2]),Double.parseDouble(line[line.length-1])));
        }
        sc.close();
        return listData;
	}
	
	
	public int[] readOutputFileTSPLIB(MultipartFile resFile) throws IOException {
		InputStream isRes = resFile.getInputStream();
        Scanner sc2 = new Scanner(isRes);
        while(!sc2.nextLine().equals("TOUR_SECTION")) {
        	//System.out.println("----");
        }
        int[] listRes = new int[numOfCities+1];
        int i=0;
        String l;
        while(!(l = sc2.nextLine()).equals("-1")) {
        	listRes[i] = Integer.parseInt(l)-1;
        	i++;
        }
        listRes[i] = 0;
        sc2.close();
        return listRes;
	}
	
	
}

