package com.castlemon.jenkins.performance;

import hudson.model.AbstractItem;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CucumberProjectActionTest {

	@Mock
	AbstractItem project = Mockito.mock(AbstractItem.class);

	@Test
	public void testGetDisplayName() {
		CucumberProjectAction cucumberProjectAction = new CucumberProjectAction(
				project);
		Assert.assertEquals("Cucumber Project Performance Report",
				cucumberProjectAction.getDisplayName());
	}

	@Test
	public void testGetIconFileName() {
		CucumberProjectAction cucumberProjectAction = new CucumberProjectAction(
				project);
		Assert.assertEquals("/plugin/cucumber-perf/performance.png",
				cucumberProjectAction.getIconFileName());
	}

	@Test
	public void testGetUrlName() {
		CucumberProjectAction cucumberProjectAction = new CucumberProjectAction(
				project);
		Assert.assertEquals("cucumber-perf-reports",
				cucumberProjectAction.getUrlName());
	}

}
