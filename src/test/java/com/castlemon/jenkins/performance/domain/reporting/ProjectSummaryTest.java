package com.castlemon.jenkins.performance.domain.reporting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class ProjectSummaryTest {

	@Test
	public void testProperties() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(ProjectSummary.class);
	}

	@Test
	public void testGetFeatureSummaryList() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> featureSummaries = new HashMap<String, Summary>();
		featureSummaries.put("summary1", summary1);
		featureSummaries.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setFeatureSummaries(featureSummaries);
		Collection<Summary> features = projectSummary.getFeatureSummaryList();
		Assert.assertEquals(2, features.size());
		Assert.assertTrue(features.contains(summary1));
		Assert.assertTrue(features.contains(summary2));
	}

	@Test
	public void testAssembleAllSummaries() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> featureSummaries1 = new HashMap<String, Summary>();
		featureSummaries1.put("summary1", summary1);
		featureSummaries1.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setFeatureSummaries(featureSummaries1);
		Map<String, Summary> scenarioSummaries1 = new HashMap<String, Summary>();
		scenarioSummaries1.put("summary3", summary1);
		scenarioSummaries1.put("summary4", summary2);
		projectSummary.setScenarioSummaries(scenarioSummaries1);
		Map<String, Summary> stepSummaries1 = new HashMap<String, Summary>();
		stepSummaries1.put("summary5", summary1);
		stepSummaries1.put("summary6", summary2);
		projectSummary.setStepSummaries(stepSummaries1);
		Map<String, Summary> allSummaries = projectSummary
				.assembleAllSummaries();
		Assert.assertEquals(6, allSummaries.size());
	}

}
