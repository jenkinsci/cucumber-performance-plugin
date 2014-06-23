/* performance pie chart */
jQuery(document).ready(function() {

	jQuery('#pieChartArea').highcharts({
		chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: 1,//null,
                    plotShadow: false
                },
        title: {
                    text: 'Performance Breakdown'
                },
        tooltip: {
            	    pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
                },
        plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false,
                            //format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            //style: {
                            //    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            //}
                        }
                    }
        },
        series: [{
                    type: 'pie',
                    name: 'Performance Breakdown',
                    data: pieChartData
                }]
            });
});