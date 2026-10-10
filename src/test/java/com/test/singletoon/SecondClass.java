package com.test.singletoon;

public class SecondClass {

	
	public static void main(String[] args) {
		
		
		FirstClass firstClass=FirstClass.getFirstClass();
		
		FirstClass firstClass2=FirstClass.getFirstClass();
		
		
		System.out.println(firstClass==firstClass2);
		
		
		firstClass.getAdditionOfTwoNumber(10, 12);
		
		firstClass2.getAdditionOfTwoNumber(10, 12);
		
		
	}
}
