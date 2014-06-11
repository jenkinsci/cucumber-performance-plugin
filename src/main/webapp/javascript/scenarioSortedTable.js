jQuery(document).ready(function() {
	/* Scenario-level table */
	jQuery('#sortedScenarioTable').dataTable({
		"aoColumns" : [ {
			"sType" : "string"
		}, null, null, {
			"iDataSort" : 6
		}, {
			"iDataSort" : 7
		}, {
			"iDataSort" : 8
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		}, {
			"bVisible" : false
		} ],
		aaSorting : [ [ 8, 'desc' ] ],
		"bPaginate" : false,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : false
	});
});