package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class ElementsTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Elements.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Elements elements = new Elements();
		Assert.assertTrue(elements.getAdditionalProperties().isEmpty());
		elements.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, elements.getAdditionalProperties().size());
	}

}
