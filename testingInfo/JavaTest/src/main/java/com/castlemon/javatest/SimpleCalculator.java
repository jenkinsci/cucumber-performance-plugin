package com.castlemon.javatest;


import java.util.Random;

public class SimpleCalculator {

    public int calculateDifference(int input1, int input2) {
        //delay between 1 and 5 seconds randomly
        try {
            Thread.sleep(getRandomDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input1 - input2;
    }

    private int getRandomDelay() {
        int Low = 10;
        int High = 100;
        Random random = new Random();
        int delay = random.nextInt(High - Low) + Low;
        return delay;
    }
}
