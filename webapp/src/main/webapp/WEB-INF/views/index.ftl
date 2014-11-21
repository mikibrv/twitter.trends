<html  >
<head>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">


    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">

    <link rel="stylesheet" href="static/css/custom.css">

</head>
<body ng-app="app" id="EventCtrl" ng-controller = 'EventCtrl'>




<div id = 'highCharts'>

</div>

<img width="400" height="200" src="http://www.site-seeker.com/wp-content/uploads/twitter-logo.png"/>
<h1>                trends 1.0</h1>

<div class = 'containerBottomRight'>
    <div class = 'innerBottomRight'>
        <table class="table table-striped">
            <tbody>

            <tr ng-repeat="tweet in tweets | orderBy: sortOrder | filter: search | limitTo: 40" class = 'animate'>
                <td class = 'text-left'>{{tweet.data}}</td>
            </tr>


            </tbody>

        </table>

    </div>
    <form>
        <div class = 'form-group'>
            <input type = 'text' placeholder = 'Filter by keyword...' class = 'form-control' id = "IBRinput"/>
        </div>
    </form>
</div>


<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.3/angular.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.3/angular-route.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/angular.js/1.3.3/angular-animate.min.js"></script>
<script src="static/js/app.js"></script>
<script src="static/js/controllers/EventCtrl.js"></script>
<script src="static/highcharts/highcharts.src.js"></script>
<script src="static/highcharts/exporting.js"></script>
<script src="static/js/chartsJS/mainChart.js"></script>


</body>
</html>