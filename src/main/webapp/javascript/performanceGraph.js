/* performance graph */
jQuery(document).ready(function() {

	jQuery('#graphArea').highcharts({
		chart : {
			type : 'line'
		},
		tooltip : {
			enabled : false
		},
		title : {
			text : titleText
		},
		xAxis : {
			title : {
				text : 'Build Number'
			},
			tickInterval : 1
		},
		yAxis : {
			title : {
				text : 'Time taken (seconds)'
			}
		},
		series : [ {
			name : 'Time Taken',
			data : perfData
		}, {
			name : 'Average',
			data : averageData
		} ]
	});
});