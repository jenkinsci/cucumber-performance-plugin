package com.castlemon.jenkins.performance.domain.reporting.comparator;

import java.util.Comparator;

import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class SummaryOrderComparator implements Comparator<Summary> {

	public int compare(Summary summary1, Summary summary2) {

		int order1 = summary1.getOrder();
		int order2 = summary2.getOrder();

		// ascending order
		return order1 - order2;

	}

};
