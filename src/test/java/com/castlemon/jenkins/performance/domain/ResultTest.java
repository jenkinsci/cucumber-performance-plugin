package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class ResultTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Result.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Result result = new Result();
		Assert.assertTrue(result.getAdditionalProperties().isEmpty());
		result.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, result.getAdditionalProperties().size());
	}

}
