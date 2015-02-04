<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Titlu</title>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script type="text/javascript" src="static/qtip/jquery.qtip.js"></script>

      <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="static/qtip/jquery.qtip.css" />
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="static/components/popup.css"></link>


  </head>

  <body>
    <header class="navbar-inverse">
      <div class="container">
        <nav role="navigation">
          <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
              <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
              </button>
              <a class="navbar-brand">Twitter Trends</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul class="nav navbar-nav">
                <li>
                  <a href="#" id='topWordsGraph'>Top words</a>
                </li>
                <li>
                  <a href="#" id='statsGraph'>Stats</a>
                </li>
                <li>
                  <a href="#" id='pop'>Live Tweets</a>
                </li>
              </ul>
            </div><!-- /.navbar-collapse -->
          </div><!-- /.container-fluid -->
        </nav>
      </div>
    </header>

    <div class="container">
      <div>
        <#attempt><#include "components/graphs/topWordsGraph.ftl"><#recover>Could not include topWordsGraph.ftl</#recover>
      </div>
      <div>
        <#attempt><#include "components/graphs/stats.ftl"><#recover>Could not include stats.ftl</#recover>
      </div>
    </div>
    <div class="container" id="containerTopWordsGraph"></div>
    <div class="container">
      <div class="col-md-6">
        <div class="center-block" id="containerStatsTweetsProcessed"></div>
      </div>
      <div class="col-md-6">
        <div class="center-block" id="containerStatsWordsProcessed"></div>
      </div>
    </div>

    <script src="static/components/popup.js"></script>

    <div id="popup" style="display: none;">
        <span id="closePopUp" class="button b-close">X<span></span></span>
        <p id="popupText"></p>
        <span class="logo"></span>
    </div>

    <#--<button id="pop">PopUp</button>-->



  <!-- Latest compiled and minified JavaScript -->

    <script src="http://code.highcharts.com/highcharts.js"></script>
    <script src="http://code.highcharts.com/modules/exporting.js"></script>
    <script src="http://raw.githubusercontent.com/dinbror/bpopup/master/jquery.bpopup.min.js"></script>
  </body>
</html>