package com.castlemon.jenkins.performance.domain;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class MatchTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(Match.class);
	}

	@Test
	public void testSetAdditionalProperties() {
		Match match = new Match();
		Assert.assertTrue(match.getAdditionalProperties().isEmpty());
		match.setAdditionalProperties("fred", "dredd");
		Assert.assertEquals(1, match.getAdditionalProperties().size());
	}

}
