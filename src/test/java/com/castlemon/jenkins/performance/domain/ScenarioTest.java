package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class ScenarioTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Scenario.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Scenario scenario = new Scenario();
		Assert.assertTrue(scenario.getAdditionalProperties().isEmpty());
		scenario.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, scenario.getAdditionalProperties().size());
	}

}
