<!DOCTYPE html>
<html lang="en">
  <head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pond Control 0.1</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://code.jquery.com/jquery.js"></script>
    <script type='text/javascript' src='js/knockout-3.0.0.js'></script>

    <script>

    var viewModel = {
        pumpStatus: ko.observable(),
        onPermitted: ko.observable(false),
        offPermitted: ko.observable(false),
        mode: ko.observable("manual")
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);
        loadStatusInfo();
        setInterval(loadStatusInfo, 5000);
        initSchedule();
    });

    function loadStatusInfo() {
        issueServerCommand("loadPumpStatus");
    }

    function pumpOn(){
        issueServerCommand("pondPumpOn");
    }

    function pumpOff(){
        issueServerCommand("pondPumpOff");
    }

    function issueServerCommand(cmd) {
          var dataUp = { opp: cmd };
          $.ajax({
              url: "status",
              type: 'POST',
              data: JSON.stringify(dataUp),

              success: function (data) {
                    updateStatusInfo(data);
              },
              error:function(data,status,er) {
                  alert("error: "+data+" status: "+status+" er:"+er);
              }
          });
    }

    function updateStatusInfo(data){

        viewModel.pumpStatus(data.pumpStatus);
        viewModel.mode(data.mode);

        if(data.pumpStatus=="on"){
            viewModel.offPermitted(true);
            viewModel.onPermitted(false);
        } else {
            viewModel.offPermitted(false);
            viewModel.onPermitted(true);
        }

    }

    function switchToManualMode(){

        issueServerCommand("pondToManual");

    }

    function switchToAutoMode(){

        issueServerCommand("pondToAuto");

    }

    </script>
  </head>

  <body>

    <ul class="nav nav-pills">
      <li><a href="index.jsp">Home</a></li>
      <li><a href="heating.jsp">Heating</a></li>
      <li class="active"><a href="pond.jsp">Pond</a></li>
      <li><a href="#">A/C</a></li>
    </ul>

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">Pump Status</h3>
      </div>
      <div class="panel-body">
        Pond pump is <span data-bind="text: pumpStatus">-</span>
      </div>
    </div>

     <div class="panel panel-default">
         <div class="panel-heading">
            <h3 class="panel-title">Mode</h3>
         </div>
         <div class="panel-body">
            <input type="radio" name="mode" value="manual" data-bind="checked: mode" onChange="switchToManualMode()"> Manual
            <input type="radio" name="mode" value="auto" data-bind="checked: mode" onChange="switchToAutoMode()"> Auto
        </div>
    </div>

    <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Manual Control</h3>
          </div>
          <div class="panel-body">
            <button type="button" class="btn btn-default btn-lg" onClick="pumpOn()" data-toggle="button" data-bind="visible: onPermitted,disable:mode()=='auto'">
              <span class="glyphicon glyphicon-play"></span> Pump On
            </button>
            <button type="button" class="btn btn-default btn-lg" onClick="pumpOff()" data-toggle="button" data-bind="visible: offPermitted,disable:mode()=='auto'">
              <span class="glyphicon glyphicon-stop"></span> Pump Off
            </button>
          </div>
    </div>

    <div class="panel panel-default">
          <div class="panel-heading">
            <h3 class="panel-title">Schedule</h3>
          </div>

        <div class="panel-body">
            <button type="button" class="btn btn-default btn-lg" onClick="window.location='pondscheduler.jsp'" data-bind="disable:mode()=='auto'"><span class="glyphicon glyphicon-time"></span> Edit Schedule</button>
        </div>

        <iframe src="pondscheduler.jsp?embedded=true" width="100%" height="300"></iframe>
    </div>



    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

  </body>
</html>
