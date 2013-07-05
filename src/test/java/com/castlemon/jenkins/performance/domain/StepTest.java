package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class StepTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Step.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Step step = new Step();
		Assert.assertTrue(step.getAdditionalProperties().isEmpty());
		step.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, step.getAdditionalProperties().size());
	}

}
