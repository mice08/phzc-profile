package com.phzc.profile.web.utils;

import java.util.Random;

public class RandomUtil {
	
	public static String rands(int randomLenght) {
		Random rd = new Random();
		String n = "";
		int rdGet;

		do {
			if (rd.nextInt() % 2 == 1) {
				rdGet = Math.abs(rd.nextInt()) % 10 + 48;
			} else{
//				rdGet = Math.abs(rd.nextInt()) % 26 + 97;
				rdGet = Math.abs(rd.nextInt()) % 10 + 48;
			}
			char num1 = (char) rdGet;
			String dd = Character.toString(num1);
			n += dd;

		} while (n.length() < randomLenght);

		return n;

	}
	
}
