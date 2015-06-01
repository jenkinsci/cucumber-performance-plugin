package com.castlemon.jenkins.performance.reporting;

import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportBuilder {

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProjectName) {
		PerformanceReporter reporter = new PerformanceReporter();
		reporter.initialiseEntryMaps();
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setOverallSummary(reporter
				.getPerformanceData(projectRuns));
		projectSummary.getOverallSummary().setName(buildProjectName);
		// feature reports - re-do the map to have the pageLink as the key
		projectSummary.setFeatureSummaries(getMapByPageLink(reporter.getFeatureSummaries()));
		// scenario reports - update senior links and re-do the map to have the pageLink as the key
        updateSeniorPageLinks(reporter.getScenarioSummaries(),reporter.getFeatureSummaries());
		projectSummary.setScenarioSummaries(getMapByPageLink(reporter.getScenarioSummaries()));
		// step reports - update senior links and re-do the map to have the pageLink as the key
        updateSeniorPageLinks(reporter.getStepSummaries(),reporter.getScenarioSummaries());
		projectSummary.setStepSummaries(getMapByPageLink(reporter.getStepSummaries()));
		// project reports
		List<Summary> summaryList = new ArrayList<Summary>(reporter
				.getFeatureSummaries().values());
		CucumberPerfUtils.sortSummaryList(summaryList);
		CucumberPerfUtils.writeSummaryToDisk(projectSummary, reportDirectory);
		return true;
	}

    private Map<String,Summary> getMapByPageLink(Map<String,Summary> inputSummaries) {
        Map<String,Summary> pageLinkSummaries = new HashMap<String,Summary>();
        for(Summary summary : inputSummaries.values()) {
            pageLinkSummaries.put(summary.getPageLink(),summary);
        }
        return pageLinkSummaries;
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
