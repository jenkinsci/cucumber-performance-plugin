package com.castlemon.javatest;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(format = { "pretty", "json:target/cucumber.json" }, glue = "com.castlemon.javatest")
public class CukesTest {
}
