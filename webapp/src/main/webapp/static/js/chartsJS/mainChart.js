angular.module('app', ['ngAnimate']).controller('EventCtrl', function ($scope) {

    $scope.sortOrder = '-time';
    $scope.tweets = [
        {data: "", time: 1},
        {data: "", time: 6}
    ]

    $scope.addTweet = function (data) {

        $scope.tweets.push({data: data.tweet});
    }

});

$(document).ready(function () {


    var room = {
        join: function (name) {
            this._username = name;
            var location = "ws://10.10.0.181:8030/camel-tweet";
            this._ws = new WebSocket(location);
            this._ws.onmessage = this._onmessage;
            this._ws.onclose = this._onclose;
        },
        _onmessage: function (m) {
            if (m.data) {
                var tweet = JSON.parse(m.data);
                var scope = angular.element($('#EventCtrl')).scope();
                scope.$apply(function () {
                    scope.addTweet(tweet);
                })
            }
        },
        _onclose: function (m) {
            this._ws = null;
        }
    };

    room.join("Testing");


    var createChart = function (data) {
        $('#highCharts').highcharts({
            chart: {
                type: 'line'
            },
            title: {
                text: 'Live Statistics'
            },
            subtitle: {
                text: "Twitter's Trends"
            },
            xAxis: {
                categories: convertToDate(data.categories)

            },
            yAxis: {
                title: {
                    text: 'Trending'
                },
                min: 0
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: false
                    },
                    enableMouseTracking: true
                }
            },
            series: data.series
        });
    }

    var convertToDate = function (rawDate) {

        var pristineDate = [];
        for (var i = 0; i < rawDate.length; i++) {

            // console.log(rawDate[i]);
            pristineDate[i] = new Date(rawDate[i]).getHours();
        }

        return pristineDate;

    }

    var lastIndex = 0;
    var globalData = {};

    var splitTheData = function () {
        createChart(globalData);
        if (lastIndex + 5 < globalData.series.length) {
            var data = {};
            data.categories = globalData.categories;
            data.series = globalData.series.splice(lastIndex, 5);
            lastIndex += 5;
            createChart(data);
        } else {
            lastIndex = 0;
        }

    }

    var refreshChart = function () {
        //  http://10.10.0.181:8081/charts
        if (lastIndex == 0) {
            jQuery.getJSON('http://10.10.0.181:8081/charts', function (data) {
                globalData = data;
                lastIndex = 0;
                splitTheData();
            });
        } else {
            splitTheData();
        }


    };
    refreshChart();
    setInterval(function () {
        refreshChart();
    }, 10000);


    jQuery("#IBRinput").change(function () {
        var toFilter = $(this).val();
        jQuery.getJSON('http://10.10.0.181:8081/getTweets/' + toFilter, function (data) {
            console.log("ok");
            $(this).val("");
        });
    });


});