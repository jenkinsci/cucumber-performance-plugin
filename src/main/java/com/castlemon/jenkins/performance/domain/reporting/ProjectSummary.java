package com.castlemon.jenkins.performance.domain.reporting;

import com.castlemon.jenkins.performance.domain.reporting.comparator.SummaryAverageDurationDescComparator;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.*;

@XStreamAlias("projectsummary")
public class ProjectSummary {

    @XStreamAlias("overallsummary")
    private Summary overallSummary;

    @XStreamAlias("featuresummaries")
    private Map<String, Summary> featureSummaries;

    @XStreamAlias("scenariosummaries")
    private Map<String, Summary> scenarioSummaries;

    @XStreamAlias("stepsummaries")
    private Map<String, Summary> stepSummaries;

    @XStreamOmitField
    private int numberOfSummariesToDisplay;

    public Summary getOverallSummary() {
        return overallSummary;
    }

    public void setOverallSummary(Summary overallSummary) {
        this.overallSummary = overallSummary;
    }

    public Map<String, Summary> getFeatureSummaries() {
        return featureSummaries;
    }

    public void setFeatureSummaries(Map<String, Summary> featureSummaries) {
        this.featureSummaries = featureSummaries;
    }

    public Map<String, Summary> getScenarioSummaries() {
        return scenarioSummaries;
    }

    public void setScenarioSummaries(Map<String, Summary> scenarioSummaries) {
        this.scenarioSummaries = scenarioSummaries;
    }

    public Map<String, Summary> getStepSummaries() {
        return stepSummaries;
    }

    public void setStepSummaries(Map<String, Summary> stepSummaries) {
        this.stepSummaries = stepSummaries;
    }

    public Collection<Summary> getFeatureSummaryList() {
        return this.featureSummaries.values();
    }

    public Collection<Summary> getScenarioSummaryList() {
        return this.scenarioSummaries.values();
    }

    public Collection<Summary> getStepSummaryList() {
        return this.stepSummaries.values();
    }

    public Map<String, Summary> assembleAllSummaries() {
        Map<String, Summary> allSummaries = new HashMap<String, Summary>();
        allSummaries.putAll(this.featureSummaries);
        allSummaries.putAll(this.scenarioSummaries);
        allSummaries.putAll(this.stepSummaries);
        return allSummaries;
    }

    public Collection<Summary> getSortedFeatureSummaryList() {
        return extractSlowestSummaries(numberOfSummariesToDisplay,
                this.featureSummaries.values());
    }

    public Collection<Summary> getSortedScenarioSummaryList() {
        return extractSlowestSummaries(numberOfSummariesToDisplay,
                this.scenarioSummaries.values());
    }

    public Collection<Summary> getSortedStepSummaryList() {
        return extractSlowestSummaries(numberOfSummariesToDisplay,
                this.stepSummaries.values());
    }

    private List<Summary> extractSlowestSummaries(int count,
                                                  Collection<Summary> interimSummaries) {
        List<Summary> interimList = new ArrayList<Summary>(interimSummaries);
        Collections.sort(interimList,
                new SummaryAverageDurationDescComparator());
        if (interimList.size() <= count) {
            return interimList;
        }
        return (new ArrayList<Summary>(interimList.subList(0, count)));
    }

    public int getNumberOfSummariesToDisplay() {
        return numberOfSummariesToDisplay;
    }

    public void setNumberOfSummariesToDisplay(int numberOfSummariesToDisplay) {
        this.numberOfSummariesToDisplay = numberOfSummariesToDisplay;
    }

}
