package com.castlemon.javatest;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class Stepdefs {

    private int result;

    @Given("^I calculate the difference between (\\d+) and (\\d+)$")
    public void differenceCalc(int input1, int input2) throws Throwable {
        SimpleCalculator simpleCalculator = new SimpleCalculator();
        result = simpleCalculator.calculateDifference(input2, input1);
    }


    @Then("^the answer should be (\\d+)$")
    public void testResult(int input1) throws Throwable {
        Assert.assertEquals(input1, result);
    }

}
