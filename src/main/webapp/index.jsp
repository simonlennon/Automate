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

    <script>

    function setup(){
        loadStatusInfo();
        setInterval(loadStatusInfo, 5000);

    }


    function loadStatusInfo() {

        // get inputs
        var status = new Object();

        $.ajax({
            url: "status",
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            mimeType: 'application/json',

            success: function (data) {

             $('#boiler-status').text(data.boilerStatus);
             $('#rads-status').text(data.radsStatus);

            },
            error:function(data,status,er) {
                alert("error: "+data+" status: "+status+" er:"+er);
            }
        });
    }
    </script>

  </head>
  <body onLoad="setup()">

    <ul class="nav nav-pills">
      <li class="active"><a href="#">Home</a></li>
      <li><a href="heating.jsp">Heating</a></li>
      <li><a href="#">Pond</a></li>
       <li><a href="#">A/C</a></li>
    </ul>




    <div class="label label-success">Hello</div>
    <br/>
    Boiler <div class="label label-success" id="boiler-status">-</div>
    <br/>
    Rads <div class="label label-success" id="rads-status">-</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://code.jquery.com/jquery.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
