jQuery(document).ready(function() {
	/* Step-level table */
	jQuery('#sortedStepTable').dataTable({
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
		"bPaginate" : true,
		"bFilter" : false,
		"bSort" : true,
		"bInfo" : true,
		"pagingType": "full_numbers"
	});
});