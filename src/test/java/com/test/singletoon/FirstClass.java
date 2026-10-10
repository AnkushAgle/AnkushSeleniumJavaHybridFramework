package com.test.singletoon;

public class FirstClass {
	
	private static FirstClass firstClass=new FirstClass();
	
	
	private  FirstClass() {
		
		
	}
	
	public static FirstClass getFirstClass() {
		
		
		if(firstClass==null) {
			
			
			return new FirstClass();
		}
		
		return firstClass;
	}
	public int getAdditionOfTwoNumber(int firstNum,int secondNum) {
		
		System.out.println(" The Addition Of Two Number ::"+firstNum+secondNum);
		return firstNum+secondNum;
		
	}
	
	
public int getMultiplicationOfTwoNumber(int firstNum,int secondNum) {
		
	System.out.println(" The Multiplication Of Two Number ::"+firstNum*secondNum);

		return firstNum*secondNum;
		
	}


public int getDivisonOfTwoNumber(int firstNum,int secondNum) {
	
	
	return firstNum/secondNum;
	
}



}
