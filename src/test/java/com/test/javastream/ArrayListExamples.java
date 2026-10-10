package com.test.javastream;
import java.util.ArrayList;

import org.testng.Reporter;
import org.testng.annotations.Test;
public class ArrayListExamples {
	
	
	
	@Test

	public void test001() {
	
		//Qustion :- Find The Count Of Names Which Are Starts With Character R
		
		//Using Normal For Loop Method
		
		ArrayList<String> arrayListVar=new ArrayList<String>();
		
		arrayListVar.add("Rohit");
		
		arrayListVar.add("Rohan");
		
		arrayListVar.add("Hashim");
		
		arrayListVar.add("Amla");
		
		arrayListVar.add("Kiron Pollard");
		
		arrayListVar.add("Robin");
		
		arrayListVar.add("Ram");
		//arrayListVar.remove("Ram");
		
		
		int countNumber=0;
				
				
				
		
		for(int i=0;i<arrayListVar.size();i++) {
			
			
			String currentName = arrayListVar.get(i);
			
			
			if(currentName.startsWith("R")) {
				
				
				countNumber++;
				
			}
			
		}
		
		
		
		
		Reporter.log("The Number Of Count Which Are Starts With 'R' :: "+countNumber,true);
		
		
		
		
		
	}

	
	
	@Test

	public void test002() {
	
		//Qustion :- Find The Count Of Names Which Are Starts With Character R
//using java stream
	
		
	ArrayList<String> arrayListVar=new ArrayList<String>();
		
		arrayListVar.add("Rohit");
		
		arrayListVar.add("Rohan");
		
		arrayListVar.add("Hashim");
		
		arrayListVar.add("Amla");
		
		arrayListVar.add("Kiron Pollard");
		
		arrayListVar.add("Robin");
		
		arrayListVar.add("Ram");
	
		
		//long number=arrayListVar.stream().filter(s -> s.startsWith("R") ||s.startsWith("K")).count();
		long number=arrayListVar.stream().filter(s -> s.startsWith("R")).count();

		Reporter.log("The Number Of Count Which Are Starts With 'R' :: "+number,true);
		
		
	     String array[]= {"Ravan","Jivan","Narayan","Sai","Sudarshan","Sam"};
		
	     ArrayList<String> newString=new  ArrayList<String>();
	    
	     
	     for(int i=0;i<array.length;i++) {
	    	 
	    	 newString.add(array[i]);
	     }
	     
	     
	     number=newString.stream().filter(s->s.startsWith("S")).count();
	     
			Reporter.log(" From String Array The Number Of Count Which Are Starts With 'S' :: "+number,true);

	     

		

	}
	
	

	@Test

	public void test003() {
	
		//Qustion :- Find The Count Of Names Which Are Starts With Character 'R' And Which is Having 
		//length is greater than 3
//using java stream
	
		
	ArrayList<String> arrayListVar=new ArrayList<String>();
		
		arrayListVar.add("Rohit");
		
		arrayListVar.add("Rohan");
		
		arrayListVar.add("Hashim");
		
		arrayListVar.add("Amla");
		
		arrayListVar.add("Kiron Pollard");
		
		arrayListVar.add("Robin");
		
		arrayListVar.add("Ram");
	
     
		
		//First Way
		//long number=arrayListVar.stream().filter(s -> s.startsWith("R") ||s.startsWith("K")).count();
		long number=arrayListVar.stream().filter(s -> s.startsWith("R")).filter(t->t.length()>3).count();

		Reporter.log(" First Way The Number Of Count Which Are Starts With 'R'  And Having length greater than 3:: "+number,true);

		//second Way
		
		long second=arrayListVar.stream().filter(s -> s.startsWith("R") && s.length()>3).count();

		Reporter.log(" Second Way The Number Of Count Which Are Starts With 'R' And Having length greater than 3 :: "+second,false);

		
	}
	
	
	
}
