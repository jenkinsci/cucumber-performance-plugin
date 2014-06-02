package com.castlemon.jenkins.performance;

import hudson.model.AbstractProject;
import hudson.model.Run;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class CucumberProjectActionTest {

	@Mock
	private AbstractProject project = Mockito.mock(AbstractProject.class);

	@Mock
	private Summary projectSummary = Mockito.mock(Summary.class);

	@Mock
	Run run = Mockito.mock(Run.class);

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@InjectMocks
	private CucumberProjectAction cucumberProjectAction = new CucumberProjectAction(
			project, testFolder.getRoot());

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
