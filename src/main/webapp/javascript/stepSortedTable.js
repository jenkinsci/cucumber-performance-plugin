jQuery(document).ready(function() {
	/* Step-level table */
	jQuery('#sortedStepTable').dataTable({
		"aaData" : sortedStepData,
		"aoColumns" : [ {
			"sType" : "string"
		}, {
			"sType" : "string"
		}, {
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