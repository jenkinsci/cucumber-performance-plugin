package com.castlemon.jenkins.performance;

import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Run;

import java.io.File;
import java.util.HashMap;
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

    public Map<String, Summary> getFeature() {
        return getSummariesByUniqueId(projectSummary.getFeatureSummaries(),
                projectSummary.getScenarioSummaries());
    }

    public Map<String, Summary> getScenario() {
        return getSummariesByUniqueId(projectSummary.getScenarioSummaries(),
                projectSummary.getStepSummaries());
    }

    public Map<String, Summary> getStep() {
        return getSummariesByUniqueId(projectSummary.getStepSummaries(), null);
    }

    private Map<String, Summary> getSummariesByUniqueId(
            Map<String, Summary> inputSummaries,
            Map<String, Summary> subSummaries) {
        Map<String, Summary> outputSummaries = new HashMap<String, Summary>();
        for (Map.Entry<String, Summary> entry : inputSummaries.entrySet()) {
            Summary summary = entry.getValue();
            summary.setProject(this.project);
            summary.setUrlName(getUrlName());
            if (subSummaries != null) {
                summary.setSubSummaries((CucumberPerfUtils.getRelevantSummaries(
                        subSummaries, summary.getId())));
            }
            outputSummaries.put(entry.getValue().getPageLink(), summary);
        }
        return outputSummaries;
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