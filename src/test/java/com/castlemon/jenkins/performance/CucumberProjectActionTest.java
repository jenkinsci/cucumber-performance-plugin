package com.castlemon.jenkins.performance;

import hudson.model.AbstractProject;
import hudson.model.Run;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CucumberProjectActionTest {

	@Mock
	private AbstractProject project = Mockito.mock(AbstractProject.class);

	@Mock
	Run run = Mockito.mock(Run.class);

	private CucumberProjectAction cucumberProjectAction = new CucumberProjectAction(
			project);

	@Test
	public void testGetDisplayName() {
		Assert.assertEquals("Cucumber Project Performance Report",
				cucumberProjectAction.getDisplayName());
	}

	@Test
	public void testGetIconFileName() {
		Assert.assertEquals("/plugin/cucumber-perf/performance.png",
				cucumberProjectAction.getIconFileName());
	}

	@Test
	public void testGetUrlName() {
		Assert.assertEquals("cucumber-perf-reports",
				cucumberProjectAction.getUrlName());
	}

}
