package com.castlemon.jenkins.performance.domain.enums;

import junit.framework.Assert;

import org.junit.Test;

public class SummaryTypeTest {

	@Test
	public void testHasSeniorSummaries() {
		Assert.assertFalse(SummaryType.PROJECT.hasSeniorSummaries());
		Assert.assertFalse(SummaryType.FEATURE.hasSeniorSummaries());
		Assert.assertTrue(SummaryType.SCENARIO.hasSeniorSummaries());
		Assert.assertTrue(SummaryType.STEP.hasSeniorSummaries());
	}

	@Test
	public void testHasSubSummaries() {
		Assert.assertTrue(SummaryType.PROJECT.hasSubSummaries());
		Assert.assertTrue(SummaryType.FEATURE.hasSubSummaries());
		Assert.assertTrue(SummaryType.SCENARIO.hasSubSummaries());
		Assert.assertFalse(SummaryType.STEP.hasSubSummaries());
	}

	@Test
	public void testGetSubType() {
		Assert.assertEquals("Feature", SummaryType.PROJECT.getSubType());
		Assert.assertEquals("Scenario", SummaryType.FEATURE.getSubType());
		Assert.assertEquals("Step", SummaryType.SCENARIO.getSubType());
		Assert.assertEquals(null, SummaryType.STEP.getSubType());
	}

	@Test
	public void testGetSubSubType() {
		Assert.assertEquals("Scenario", SummaryType.PROJECT.getSubSubType());
		Assert.assertEquals("Step", SummaryType.FEATURE.getSubSubType());
		Assert.assertEquals(null, SummaryType.SCENARIO.getSubSubType());
		Assert.assertEquals(null, SummaryType.STEP.getSubSubType());
	}

	@Test
	public void testGetSubLink() {
		Assert.assertEquals("feature", SummaryType.PROJECT.getSubLink());
		Assert.assertEquals("scenario", SummaryType.FEATURE.getSubLink());
		Assert.assertEquals("step", SummaryType.SCENARIO.getSubLink());
		Assert.assertEquals(null, SummaryType.STEP.getSubLink());
	}

	@Test
	public void testToString() {
		Assert.assertEquals("Project", SummaryType.PROJECT.toString());
		Assert.assertEquals("Feature", SummaryType.FEATURE.toString());
		Assert.assertEquals("Scenario", SummaryType.SCENARIO.toString());
		Assert.assertEquals("Step", SummaryType.STEP.toString());
	}

}
