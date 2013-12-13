package com.castlemon.jenkins.performance.domain.enums;

public enum SummaryType {
	FEATURE, SCENARIO, STEP;

	public boolean hasSeniorSummaries() {
		switch (this) {
		case FEATURE:
			return false;
		case SCENARIO:
			return true;
		case STEP:
			return true;
		}
		return false;
	}

	public boolean hasSubSummaries() {
		switch (this) {
		case FEATURE:
			return true;
		case SCENARIO:
			return true;
		case STEP:
			return false;
		}
		return false;
	}
}
