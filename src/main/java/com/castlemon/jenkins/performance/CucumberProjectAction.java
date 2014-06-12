package com.castlemon.jenkins.performance;

import hudson.model.ProminentProjectAction;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.Run;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

@SuppressWarnings("rawtypes")
public class CucumberProjectAction implements ProminentProjectAction {

	private final AbstractProject<?, ?> project;
	private final ProjectSummary projectSummary;

	public CucumberProjectAction(AbstractProject<?, ?> project,
			int countOfSortedSummaries) {
		super();
		this.project = project;
		this.projectSummary = CucumberPerfUtils.readSummaryFromDisk(this.dir());
		this.projectSummary
				.setNumberOfSummariesToDisplay(countOfSortedSummaries);
	}

	public ProjectSummary getProjectSummary() {
		return projectSummary;
	}

	public String getDisplayName() {
		return "Cucumber Project Performance Report";
	}

	public String getIconFileName() {
		return "/plugin/cucumber-perf/performance.png";
	}

	public String getUrlName() {
		return "cucumber-perf-reports";
	}

	public Map<String, Summary> getFeature() {
		// ProjectSummary projectSummary = CucumberPerfUtils
		// .readSummaryFromDisk(this.dir());
		return getSummariesByUniqueId(projectSummary.getFeatureSummaries(),
				projectSummary.getScenarioSummaries());
	}

	public Map<String, Summary> getScenario() {
		// ProjectSummary projectSummary = CucumberPerfUtils
		// .readSummaryFromDisk(this.dir());
		return getSummariesByUniqueId(projectSummary.getScenarioSummaries(),
				projectSummary.getStepSummaries());
	}

	public Map<String, Summary> getStep() {
		// ProjectSummary projectSummary = CucumberPerfUtils
		// .readSummaryFromDisk(this.dir());
		Map<String, Summary> empty = new HashMap<String, Summary>();
		return getSummariesByUniqueId(projectSummary.getScenarioSummaries(),
				empty);
	}

	private Map<String, Summary> getSummariesByUniqueId(
			Map<String, Summary> inputSummaries,
			Map<String, Summary> subSummaries) {
		Map<String, Summary> outputSummaries = new HashMap<String, Summary>();
		for (Map.Entry<String, Summary> entry : inputSummaries.entrySet()) {
			Summary summary = entry.getValue();
			summary.setProject(this.project);
			summary.setUrlName(getUrlName());
			summary.setSubSummaries((CucumberPerfUtils.getRelevantSummaries(
					subSummaries, summary.getId())));
			outputSummaries.put(entry.getValue().getPageLink(), summary);

		}
		return outputSummaries;
	}

	public AbstractProject getProject() {
		return (AbstractProject) this.project;
	}

	/*
	 * public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws
	 * IOException, ServletException {
	 * 
	 * }
	 */

	protected File dir() {
		if (this.project instanceof AbstractProject) {
			AbstractProject abstractProject = (AbstractProject) this.project;
			Run run = abstractProject.getLastCompletedBuild();
			if (run != null) {
				File javadocDir = getBuildArchiveDir(run);
				if (javadocDir.exists()) {
					return javadocDir;
				}
			}
		}
		return getProjectArchiveDir(this.project);
	}

	private File getProjectArchiveDir(AbstractItem project) {
		return new File(project.getRootDir(), "cucumber-perf-reports");
	}

	private File getBuildArchiveDir(Run run) {
		return new File(run.getRootDir(), "cucumber-perf-reports");
	}
}