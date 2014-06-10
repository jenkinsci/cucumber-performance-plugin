package com.castlemon.jenkins.performance.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject) {
		PerformanceReporter reporter = new PerformanceReporter();
		reporter.initialiseEntryMaps();
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setOverallSummary(reporter
				.getPerformanceData(projectRuns));
		projectSummary.getOverallSummary().setName(buildProject);
		// feature reports
		projectSummary.setFeatureSummaries(reporter.getFeatureSummaries());
		// scenario reports
		projectSummary.setScenarioSummaries(reporter.getScenarioSummaries());
		// step reports
		projectSummary.setStepSummaries(reporter.getStepSummaries());
		// project reports
		List<Summary> summaryList = new ArrayList<Summary>(reporter
				.getFeatureSummaries().values());
		CucumberPerfUtils.sortSummaryList(summaryList);
		CucumberPerfUtils.writeSummaryToDisk(projectSummary, reportDirectory);
		return true;
	}
}
