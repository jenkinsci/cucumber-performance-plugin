package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;

public class ReportBuilderTest {

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
				"test build 1", "1", "pluginPath");
		Assert.assertEquals(3, testFolder.getRoot().listFiles().length);
	}

	@Test
	public void testGenerateProjectReportsNullPlugin() {
		List<ProjectRun> projectRuns = new ArrayList<ProjectRun>();
		projectRuns.add(testUtils.generateRun("passed"));
		projectRuns.add(testUtils.generateRun("failed"));
		builder.generateProjectReports(projectRuns, testFolder.getRoot(),
				"test build 1", "1", null);
		Assert.assertEquals(3, testFolder.getRoot().listFiles().length);
	}

	@Test
	public void testGenerateProjectReportsEmptyPlugin() {
		List<ProjectRun> projectRuns = new ArrayList<ProjectRun>();
		projectRuns.add(testUtils.generateRun("passed"));
		projectRuns.add(testUtils.generateRun("failed"));
		builder.generateProjectReports(projectRuns, testFolder.getRoot(),
				"test build 1", "1", "");
		Assert.assertEquals(3, testFolder.getRoot().listFiles().length);
	}
}
