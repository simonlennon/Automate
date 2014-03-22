<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap 101 Template</title>

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
        boilerStatus: ko.observable(),
        radiatorStatus: ko.observable(),
        boostTime: ko.observable(60),
        fireRads: ko.observable(false),
        tankBoostStatus: ko.observable(),
        radBoostStatus: ko.observable(),
        boostPermitted: ko.observable(false),
        cancelPermitted: ko.observable(false)
    };

    $(document).ready(function () {
        ko.applyBindings(viewModel);
        loadStatusInfo();
        setInterval(loadStatusInfo, 5000);
    });



    function loadStatusInfo() {

        var status = new Object();
        var dataUp = { opp: "loadStatus" };

        $.ajax({
            url: "status",
            type: 'POST',
            data: JSON.stringify(dataUp),

            success: function (data) {
            viewModel.boilerStatus(data.boilerStatus);
            viewModel.radiatorStatus(data.radsStatus);
            viewModel.tankBoostStatus(data.tankBoostStatus);
            viewModel.radBoostStatus(data.radsBoostStatus);

                if(data.tankBoostStatus!="on" && data.radsBoostStatus!="on"){
                    viewModel.boostPermitted(true);
                    viewModel.cancelPermitted(false);
                } else {
                    viewModel.boostPermitted(false);
                    viewModel.cancelPermitted(true);
                }
            },
            error:function(data,status,er) {
                alert("error: "+data+" status: "+status+" er:"+er);
            }
        });
    }

    function boost(){

      var dataUp = { opp: "boost", duration: viewModel.boostTime(), fireTank:"true", fireRads: viewModel.fireRads()  };

      $.ajax({
          url: "status",
          type: 'POST',
          data: JSON.stringify(dataUp),

          success: function (data) {

          },
          error:function(data,status,er) {
              alert("error: "+data+" status: "+status+" er:"+er);
          }
      });

      loadStatusInfo();

    }

    function cancelBoost(){

      var dataUp = { opp: "cancelBoost" };

      $.ajax({
          url: "status",
          type: 'POST',
          data: JSON.stringify(dataUp),

          success: function (data) {
                window.status="Boost cancel request sent..."
          },
          error:function(data,status,er) {
              alert("error: "+data+" status: "+status+" er:"+er);
          }
      });

      loadStatusInfo();

    }

    </script>

  </head>
  <body>

    <ul class="nav nav-pills">
      <li><a href="index.jsp">Home</a></li>
      <li class="active"><a href="#">Heating</a></li>
      <li><a href="#">Pond</a></li>
       <li><a href="#">A/C</a></li>
    </ul>

    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">Heating Status</h3>
      </div>
      <div class="panel-body">
        Boiler is <span data-bind="text: boilerStatus">-</span>
      </div>
       <div class="panel-body">
        Radiators are <span data-bind="text: radiatorStatus">-</span>
       </div>

         <div class="panel-body">
           Tank Boost <span data-bind="text: tankBoostStatus">-</span>
         </div>

         <div class="panel-body">
           Radiator Boost <span data-bind="text: radBoostStatus">-</span>
         </div>

         <div class="panel-body">
           Permit Boost <span data-bind="text: boostPermitted">-</span>
         </div>
    </div>

     <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title">Control</h3>
      </div>
      <div class="panel-body">
          <div class="btn-group">
            <button type="button" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-time"></span> Auto Schedule</button>
            <button type="button" class="btn btn-default btn-lg"><span class="glyphicon glyphicon-stop"></span> Off</button>
          </div>
       </div>

       <div class="panel-body">
         <div class="col-lg-3">
             <div class="input-group input-group-lg">
                <select class="form-control" data-bind="value: boostTime">
                    <option value="1">1 Mins</option>
                    <option value="30">30 Mins</option>
                    <option value="60" selected>60 Mins</option>
                    <option value="90">90 Mins</option>
                    <option value="120">120 Mins</option>
                    <option value="180">180 Mins</option>
                    <option value="240">240 Mins</option>
                </select>
                <span class="input-group-addon">
                    <input type="checkbox" data-bind="checked: fireRads"> Fire Rads
                </span>
           </div>
            <button type="button" class="btn btn-default btn-lg" onClick="boost()" data-toggle="button" data-bind="visible: boostPermitted">
              <span class="glyphicon glyphicon-fire"></span> Start Boost
            </button>
            <button type="button" class="btn btn-default btn-lg" onClick="cancelBoost()" data-toggle="button" data-bind="visible: cancelPermitted">
              <span class="glyphicon glyphicon-remove"></span> Cancel Boost
            </button>

        </div>
    </div>

    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

  </body>
</html>
