package com.castlemon.jenkins.performance.domain.reporting;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("performanceentry")
public class PerformanceEntry {

	@XStreamAlias("buildnumber")
	private int buildNumber;

	@XStreamAlias("elapsedtime")
	private long elapsedTime;

	@XStreamAlias("rundate")
	private Date runDate;

	@XStreamAlias("passedsteps")
	private int passedSteps;

	@XStreamAlias("failedsteps")
	private int failedSteps;

	@XStreamAlias("skippedsteps")
	private int skippedSteps;

	private boolean passed;

	public void addToPassedSteps(int passedSteps) {
		this.passedSteps += passedSteps;
	}

	public void addToSkippedSteps(int skippedSteps) {
		this.skippedSteps += skippedSteps;
	}

	public void addToFailedSteps(int failedSteps) {
		this.failedSteps += failedSteps;
	}

	public void addToElapsedTime(long elapsedTime) {
		this.elapsedTime += elapsedTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public int getPassedSteps() {
		return passedSteps;
	}

	public void setPassedSteps(int passedSteps) {
		this.passedSteps = passedSteps;
	}

	public int getFailedSteps() {
		return failedSteps;
	}

	public void setFailedSteps(int failedSteps) {
		this.failedSteps = failedSteps;
	}

	public int getSkippedSteps() {
		return skippedSteps;
	}

	public void setSkippedSteps(int skippedSteps) {
		this.skippedSteps = skippedSteps;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

}
