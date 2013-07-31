package com.castlemon.jenkins.performance;

import hudson.model.Action;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepMonitor;
import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class CucumberPerfRecorderTest {

	String jsonReportDirectory = "";
	String pluginUrlPath = "";

	CucumberPerfRecorder cucumberPerfRecorder = new CucumberPerfRecorder(
			jsonReportDirectory, pluginUrlPath);

	@Test
	public void testGetRequiredMonitorService() {
		Assert.assertEquals(BuildStepMonitor.NONE,
				cucumberPerfRecorder.getRequiredMonitorService());
	}

	@Test
	public void testGetProjectAction() {
		AbstractProject project = Mockito.mock(AbstractProject.class);
		Assert.assertTrue(cucumberPerfRecorder.getProjectAction(project) instanceof Action);
	}

}
