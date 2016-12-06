package com.castlemon.jenkins.performance.domain.reporting;

import java.util.Date;
import java.util.List;

import com.castlemon.jenkins.performance.domain.Feature;

public class ProjectRun {

	private int buildNumber;

	private Date runDate;

	private List<Feature> features;

	public Date getRunDate() {
		if (runDate == null) {
			return null;
		} else {
			return new Date(runDate.getTime());
		}
	}

	public void setRunDate(final Date runDate) {
		if (runDate == null) {
			this.runDate = null;
		} else {
			this.runDate = new Date(runDate.getTime());
		}
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

}
