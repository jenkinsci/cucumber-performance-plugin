package com.castlemon.jenkins.performance;

import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.io.File;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class CucumberProjectAction implements ProminentProjectAction {

    private final AbstractProject<?, ?> project;
    private final ProjectSummary projectSummary;

    public CucumberProjectAction(AbstractProject<?, ?> project,
                                 int countOfSortedSummaries) {
        super();
        this.project = project;
        this.projectSummary = CucumberPerfUtils.readSummaryFromDisk(this.dir());
        if (this.projectSummary != null) {
            this.projectSummary
                    .setNumberOfSummariesToDisplay(countOfSortedSummaries);
        }
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

    public Summary getFeature(String pageLink) {
        return getSpecificSummaryByPageLink(pageLink, projectSummary.getFeatureSummaries(),
                projectSummary.getScenarioSummaries());
    }

    public Summary getScenario(String pageLink) {
        return getSpecificSummaryByPageLink(pageLink, projectSummary.getScenarioSummaries(),
                projectSummary.getStepSummaries());
    }

    public Summary getStep(String pageLink) {
        return getSpecificSummaryByPageLink(pageLink, projectSummary.getStepSummaries(), null);
    }

    private Summary getSpecificSummaryByPageLink(String pageLink,
                                                 Map<String, Summary> inputSummaries,
                                                 Map<String, Summary> subSummaries) {
        Summary summary = inputSummaries.get(pageLink);
        summary.setProject(this.project);
        summary.setUrlName(getUrlName());
        if (subSummaries != null) {
            summary.setSubSummaries((CucumberPerfUtils.getRelevantSummaries(
                    subSummaries, summary.getId())));
        }
        return summary;
    }

    public AbstractProject getProject() {
        return (AbstractProject) this.project;
    }

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