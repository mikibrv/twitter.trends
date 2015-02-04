//$(document).ready(function () {
//    $('#statsGraph').click(function(){ // on dom ready
console.log('stats.js loaded...');
$(document).ready(function(){ // on dom ready

    $('#statsGraph').click(function () {

      var element = document.getElementById('containerStatsTweetsProcessed');

      var style = element.style;
      style.height = "400px";
      style.width = "600px";

        var element = document.getElementById('containerStatsWordsProcessed');

        var style = element.style;
        style.height = "400px";
        style.width = "600px";

      callAjax();
      setInterval(function(){
          callAjax();
      },10*1000);
    })

function callAjax(){
    $.ajax({
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        url:"/twitter/home/stats/",
        success:function(result){

            var containerTweetsProcessed='containerStatsTweetsProcessed';
            var record=result.slaveTweetsProcessed;
            var centerMessage='Master Tweets sent:<br>'+result.masterTweetsProcessed;
            var seriesText='Tweets Processed';
            var title=' Tweets processed';
            getPieGraph(record,containerTweetsProcessed,centerMessage,seriesText,title);

            var containerTweetsProcessed='containerStatsWordsProcessed';
            var record=result.slaveWordsProcessed;
            var centerMessage='Total words:<br>'+result.totalWordsProcessed;
            var seriesText='Words Processed';
            var title=' Words processed';
            getPieGraph(record,containerTweetsProcessed,centerMessage,seriesText,title);
        },
        failure:function(){

        }
    });

}

function getPieGraph(record,container,centerMessage,seriesText,title) {

//    record= [
//        ['Firefox', 44.2],
//        ['IE7', 26.6],
//        ['IE6', 20],
//        ['Chrome', 3.1],
//        ['Other', 5.4]
//    ];
//    container='containerStats';


    var chart = new Highcharts.Chart({
        chart: {
            renderTo: container,
            type: 'pie'
        },
        title: {
            text: title
        },
        tooltip: {
            formatter: function () {
                var message= '<b>' +'IP'+' '+ this.point.name+ '<br>' +this.series.name+' '+this.y + '</b>';
                return message;
            }
        },
        plotOptions: {
            pie: {
                borderColor: '#122200',
                innerSize: '50%',
                size: '70%',
                shadow: false,
                center: ['50%', '50%']
            }
        },
        series: [{
            name: seriesText,
            data: record
        }]
        },
        // using

        function(chart) { // on complete

            var xpos = '50%';
            var ypos = '57%';
            var circleradius = 0;

            // Render the circle
            chart.renderer.circle(xpos, ypos, circleradius).attr({
                fill: '#ddd',
            }).add();

            // Render the text
//            chart.renderer.text('THIS TEXT <span style="color: red">should be in the center of the donut</span>', 100, 215).css({
            chart.renderer.text(centerMessage, 240, 220).css({
                width: circleradius*2,
                color: '#4572A7',
                fontSize: '12px',
                textAlign: 'center'
            }).attr({
                // why doesn't zIndex get the text in front of the chart?
                zIndex: 1
            }).add();
        });
}

});