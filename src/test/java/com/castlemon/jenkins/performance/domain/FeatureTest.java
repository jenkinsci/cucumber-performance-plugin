package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class FeatureTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Feature.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Feature feature = new Feature();
		Assert.assertTrue(feature.getAdditionalProperties().isEmpty());
		feature.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, feature.getAdditionalProperties().size());
	}

}
