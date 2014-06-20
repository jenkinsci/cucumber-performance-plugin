package com.castlemon.javatest;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class Stepdefs {

	private int result;

	@Given("^I calculate the difference between (\\d+) and (\\d+)$")
	public void differenceCalc(int input1, int input2) throws Throwable {
		performDelay();
		SimpleCalculator simpleCalculator = new SimpleCalculator();
		result = simpleCalculator.calculateDifference(input1, input2);
	}

	@Then("^the answer should be (\\d+)$")
	public void testResult(int input1) throws Throwable {
		performDelay();
		Assert.assertEquals(input1, result);
	}

	private void performDelay() {
		// delay between 30 seconds and 1 minute randomly
		try {
			Thread.sleep(TestHelper.getRandomDelay(TestHelper.THIRTY_SECONDS,
					TestHelper.ONE_MINUTES));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
