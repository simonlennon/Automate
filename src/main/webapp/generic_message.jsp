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

    var statusMessageDefault = "Send Command";
    var connected = true;

    $(function() {
        $.ajaxSetup({
            error: handleError
        });
    });

    function handleError(jqXHR, exception){
        if (jqXHR.status === 0) {
            setDisconnected();
        } else if (jqXHR.status == 404) {
            alert('Requested page not found. [404]');
        } else if (jqXHR.status == 500) {
            alert('Internal Server Error [500].');
        } else if (exception === 'parsererror') {
            alert('Requested JSON parse failed.');
        } else if (exception === 'timeout') {
            alert('Time out error.');
        } else if (exception === 'abort') {
            alert('Ajax request aborted.');
        } else {
            alert('Uncaught Error.\n' + jqXHR.responseText);
        }
    }

    function setDisconnected(){
        if(connected){
            alert("Could not connect to server...");
            connected = false;
        }
        viewModel.statusMessage(statusMessageDefault + ": Error - Could not connect...");
    }

    function setConnected(){
        if(!connected){
            connected = true;
        }
        viewModel.statusMessage(statusMessageDefault);
    }

    var viewModel = {
        statusMessage: ko.observable(statusMessageDefault),
        command: ko.observable(),
        outboundHistory: ko.observable(),
        inboundHistory: ko.observable()
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);
        loadStatusInfo();
        setInterval(loadStatusInfo, 5000);
    });


    function loadStatusInfo() {
        var status = new Object();
        var dataUp = { opp: "loadGenericStatus" };

        $.ajax({
            url: "status",
            type: 'POST',
            data: JSON.stringify(dataUp),
            timeout: 15000,

            success: function (data) {

                updateStatusInfo(data);

                if(!connected)
                    setConnected();
            }

        });

    }

    function issueServerCommand(cmd) {

          var dataUp = { opp: cmd };
          $.ajax({
              url: "status",
              type: 'POST',
              data: JSON.stringify(dataUp),

              success: function (data) {
                    updateStatusInfo(data);
                        if(!connected)
                            setConnected();

                    updateStatusInfo(data);
              }
          });
    }

    function issueGenericCommand() {

          var dataUp = { opp: "issueGenericCommand", command: viewModel.command() };

          $.ajax({
              url: "status",
              type: 'POST',
              data: JSON.stringify(dataUp),

              success: function (data) {
                    updateStatusInfo(data);
                        if(!connected)
                            setConnected();

                    updateStatusInfo(data);
              }
          });

    }

    function updateStatusInfo(data){
        viewModel.outboundHistory(JSON.stringify(data.outboundHistory));
        viewModel.inboundHistory(JSON.stringify(data.inboundHistory));
    }


    </script>
  </head>

  <body>

    <ul class="nav nav-pills">
      <li><a href="index.jsp">Home</a></li>
      <li><a href="heating.jsp">Heating</a></li>
      <li><a href="pond.jsp">Pond</a></li>
      <li class="active"><a href="#">Generic</a></li>
    </ul>

    <div class="panel panel-default">
          <div class="panel-heading">
             <h3 class="panel-title" data-bind="text: statusMessage"></h3>
          </div>
          <div class="panel-body">
            <div class="col-lg-6">
                <div class="input-group">
                  <input type="text" class="form-control" data-bind="value: command">
                  <span class="input-group-btn">
                    <button class="btn btn-default" type="button" onClick="issueGenericCommand()">Send</button>
                  </span>
                </div>
            <div class="alert alert-info" role="alert" data-bind="text: outboundHistory">...</div>
            <button class="btn btn-default" type="button" onClick="issueServerCommand('genericClearOutboundHistory')">Clear History</button>
            </div>
          </div>
    </div>

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">Inbound Commands</h3>
      </div>
      <div class="panel-body">
       <div class="col-lg-6">
         <div class="alert alert-info" role="alert" data-bind="text: inboundHistory">...</div>
         <button class="btn btn-default" type="button" onClick="issueServerCommand('genericClearInboundHistory')">Clear History</button>
      </div></div>
    </div>

    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

  </body>
</html>
