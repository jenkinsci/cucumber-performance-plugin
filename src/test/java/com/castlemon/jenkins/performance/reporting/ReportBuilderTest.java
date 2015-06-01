package com.castlemon.jenkins.performance.reporting;

import hudson.model.BuildListener;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;

public class ReportBuilderTest {

	BuildListener listener = Mockito.mock(BuildListener.class);
	private ReportBuilder builder = new ReportBuilder();

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private TestUtils testUtils = new TestUtils();

	@Test
	public void testGenerateProjectReports() {
		List<ProjectRun> projectRuns = new ArrayList<ProjectRun>();
		projectRuns.add(testUtils.generateRun("passed"));
		projectRuns.add(testUtils.generateRun("failed"));
		builder.generateProjectReports(projectRuns, testFolder.getRoot(),
				"test build 1");
		Assert.assertEquals(1, testFolder.getRoot().listFiles().length);
	}

}
