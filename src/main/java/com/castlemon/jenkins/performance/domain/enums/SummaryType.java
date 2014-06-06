package com.castlemon.jenkins.performance.domain.enums;

public enum SummaryType {
	PROJECT, FEATURE, SCENARIO, STEP;

	public boolean hasSeniorSummaries() {
		switch (this) {
		case PROJECT:
			return false;
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
		case PROJECT:
			return true;
		case FEATURE:
			return true;
		case SCENARIO:
			return true;
		case STEP:
			return false;
		}
		return false;
	}

	public String getSubType() {
		switch (this) {
		case PROJECT:
			return "Feature";
		case FEATURE:
			return "Scenario";
		case SCENARIO:
			return "Step";
		case STEP:
			return null;
		}
		return null;
	}

	public String getSubLink() {
		switch (this) {
		case PROJECT:
			return "feature";
		case FEATURE:
			return "scenario";
		case SCENARIO:
			return "step";
		case STEP:
			return null;
		}
		return null;
	}

	public String toString() {
		switch (this) {
		case PROJECT:
			return "Project";
		case FEATURE:
			return "Feature";
		case SCENARIO:
			return "Scenario";
		case STEP:
			return "Step";
		}
		return null;
	}
}
