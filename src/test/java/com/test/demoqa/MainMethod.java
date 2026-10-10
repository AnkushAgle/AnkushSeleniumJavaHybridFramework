package com.test.demoqa;

import com.aa.constant.Status;
import com.aa.constant.StatusCode;
import com.aa.constant.StatusDesc;
import com.aa.constant.EnumTest;

public class MainMethod {

	public static void main(String[] args) {
		
		
		
		
		for(int i=0;i<=1000000000;i++) {
		
			
			int num=i;
	
		int originalNumber=num;
	
		

		int additionOfPowerNumbers=0;
		
		String totalNumberOfIntrger=Integer.toString(num);
		
		
		
		while(num>0) {
			
			
			int singaleNumber=num%10;//
			
			//System.out.println("The single Number Is ::"+singaleNumber);
			
			num=num/10;
			
			
			double power = Math.pow(singaleNumber, totalNumberOfIntrger.length());
			
			
			//System.out.println("The Power Of Sinlae Number "+singaleNumber+"^"+totalNumberOfIntrger.length()+"Is :: "+power);
			
			additionOfPowerNumbers=(int) (additionOfPowerNumbers+power);
			
			
			
		}
		
		
		
		
		//System.out.println("The Sum Of Power Is :: "+additionOfPowerNumbers);
		
		
		if(additionOfPowerNumbers==originalNumber) {
			
			System.out.println(" Given Number OK  ArmStrong Number ::"+originalNumber);
		}
		else {
			
			//System.out.println(" Given Number Is NOT  ArmStrong Number ::"+originalNumber);

		}
		
		
		
		}
		
		if(true) {
			
			return;
		}
				
				
		
		
		
//		
//		
//		// TODO Auto-generated method stub
//   var a=10;
//   
//   var g="Rohit Sharma";
//   
//   
//   var b=20;
   
  //int c=a+b;
  
  
  //System.out.println(c);
  System.out.println(Status.PASS.toString());
  System.out.println(Status.CANCELLED);
  
  System.out.println(Status.CANCELLED);
  
  System.out.println(StatusCode.CLIENT_ERROR_BAD_REQUEST.getStatus());
  System.out.println(StatusDesc.SUCCESS_CREATED.getCode());
  System.out.println(StatusDesc.SUCCESS_CREATED.getReasonPhrase());
  
  MainMethod.showDescription(EnumTest.CLIENT_ERROR_BAD_REQUEST);

  MainMethod.showDescription(EnumTest.CLIENT_ERROR_NOT_FOUND);
  MainMethod.showDescription(EnumTest.SUCCESS_OK);
	}

	
	
	public  static void showDescription(EnumTest enumvalue) {
		
		System.out.println("===================================================================");
		System.out.println("The  Status Code :: "+enumvalue.returnStatusCode());

		System.out.println();
		System.out.println("The  Reason Phrase :: "+enumvalue.returnDescription());
		System.out.println();
		System.out.println("The  Status Is Success :: "+enumvalue.isSuccuss());

		
		System.out.println("===================================================================");

	}
}
