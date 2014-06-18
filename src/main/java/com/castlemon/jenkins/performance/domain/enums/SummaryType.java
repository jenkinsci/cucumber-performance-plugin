package com.castlemon.jenkins.performance.domain.enums;

public enum SummaryType {
    PROJECT, FEATURE, SCENARIO, STEP;

	/*
     * the default case for this enum will always be STEP
	 */

    public boolean hasSeniorSummaries() {
        switch (this) {
            case PROJECT:
                return false;
            case FEATURE:
                return false;
            case SCENARIO:
                return true;
            default:
                return true;
        }
    }

    public boolean hasSubSummaries() {
        switch (this) {
            case PROJECT:
                return true;
            case FEATURE:
                return true;
            case SCENARIO:
                return true;
            default:
                return false;
        }
    }

    public String getSuperType() {
        switch (this) {
            case PROJECT:
                return null;
            case FEATURE:
                return "Project";
            case SCENARIO:
                return "Feature";
            default:
                return "Scenario";
        }
    }

    public String getSubType() {
        switch (this) {
            case PROJECT:
                return "Feature";
            case FEATURE:
                return "Scenario";
            case SCENARIO:
                return "Step";
            default:
                return null;
        }
    }

    public String getSubSubType() {
        switch (this) {
            case PROJECT:
                return "Scenario";
            case FEATURE:
                return "Step";
            case SCENARIO:
                return null;
            default:
                return null;
        }
    }

    public String getSuperLink() {
        switch (this) {
            case PROJECT:
                return null;
            case FEATURE:
                return "project";
            case SCENARIO:
                return "feature";
            default:
                return "scenario";
        }
    }

    public String getSubLink() {
        switch (this) {
            case PROJECT:
                return "feature";
            case FEATURE:
                return "scenario";
            case SCENARIO:
                return "step";
            default:
                return null;
        }
    }

    public String toString() {
        switch (this) {
            case PROJECT:
                return "Project";
            case FEATURE:
                return "Feature";
            case SCENARIO:
                return "Scenario";
            default:
                return "Step";
        }
    }
}
