jQuery(document).ready(function() {
	/* Scenario-level table */
	jQuery('#sortedScenarioTable').dataTable({
		"aaData" : sortedScenarioData,
		"aoColumns" : [ {
			"sType" : "string"
		}, null, {
			"iDataSort" : 5
		}, {
			"iDataSort" : 6
		}, {
			"iDataSort" : 7
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		} ],
		aaSorting : [ [ 7, 'desc' ] ],
		"bPaginate" : false,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : false
	});
});