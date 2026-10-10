package com.pom.demoqa;

import com.aa.utility.EnvironmentUtils;

public class JustTest {

	public static void main(String[] args) {
		EnvironmentUtils.getValue("TO_EMAIL");
		System.out.println("TO_EMAIL is configured.");
	}

}
