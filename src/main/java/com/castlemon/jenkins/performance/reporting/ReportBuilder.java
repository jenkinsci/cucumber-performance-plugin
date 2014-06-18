package com.castlemon.jenkins.performance.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProjectName) {
		PerformanceReporter reporter = new PerformanceReporter();
		reporter.initialiseEntryMaps();
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setOverallSummary(reporter
				.getPerformanceData(projectRuns));
		projectSummary.getOverallSummary().setName(buildProjectName);
		// feature reports
		projectSummary.setFeatureSummaries(reporter.getFeatureSummaries());
		// scenario reports
        updateSeniorPageLinks(reporter.getScenarioSummaries(),reporter.getFeatureSummaries());
		projectSummary.setScenarioSummaries(reporter.getScenarioSummaries());
		// step reports
        updateSeniorPageLinks(reporter.getStepSummaries(),reporter.getScenarioSummaries());
		projectSummary.setStepSummaries(reporter.getStepSummaries());
		// project reports
		List<Summary> summaryList = new ArrayList<Summary>(reporter
				.getFeatureSummaries().values());
		CucumberPerfUtils.sortSummaryList(summaryList);
		CucumberPerfUtils.writeSummaryToDisk(projectSummary, reportDirectory);
		return true;
	}

    private void updateSeniorPageLinks(Map<String, Summary> summaries, Map<String, Summary> seniorSummaries) {
        //create new map of senior summaries
        Map<String, Summary> seniors = new HashMap<String, Summary>();
        for (Summary seniorSummary : seniorSummaries.values()) {
         seniors.put(seniorSummary.getId(),seniorSummary);
        }
        //update the lower summaries with the pagelinks from the relevant senior one
        for (Summary summary : summaries.values()) {
            summary.setSeniorPageLink(seniors.get(summary.getSeniorId()).getPageLink());
        }
    }
}
