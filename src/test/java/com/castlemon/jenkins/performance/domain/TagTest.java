package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class TagTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Tag.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Tag tag = new Tag();
		Assert.assertTrue(tag.getAdditionalProperties().isEmpty());
		tag.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, tag.getAdditionalProperties().size());
	}

}
