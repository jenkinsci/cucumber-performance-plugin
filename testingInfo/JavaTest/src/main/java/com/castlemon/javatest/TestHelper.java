package com.castlemon.javatest;

import java.util.Random;

public class TestHelper {

	public static final int THIRTY_SECONDS = 30000;
	public static final int ONE_MINUTES = 60000;
	public static final int TWO_MINUTES = 120000;
	public static final int THREE_MINUTES = 180000;

	public static int getRandomDelay(int low, int high) {
		Random random = new Random();
		int delay = random.nextInt(high - low) + low;
		return delay;
	}

}
