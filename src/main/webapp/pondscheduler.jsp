<!doctype html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<title>Pond Schedule</title>
</head>
	<script src="./codebase/dhtmlxscheduler.js" type="text/javascript" charset="utf-8"></script>
	<link rel="stylesheet" href="./codebase/dhtmlxscheduler.css" type="text/css" title="no title" charset="utf-8">
    <script src='./codebase/ext/dhtmlxscheduler_serialize.js'></script>
	
<style type="text/css" media="screen">
	html, body{
		margin:0px;
		padding:0px;
		height:100%;
		overflow:hidden;
	}	

</style>

<script type="text/javascript" charset="utf-8">

	function init() {
		
		scheduler.config.start_on_monday=true;
		scheduler.config.xml_date="%Y-%m-%d %H:%i";

        scheduler.templates.week_scale_date   = function(date){
            var formatFunc = scheduler.date.date_to_str("%D");
            return formatFunc(date);
        };

        scheduler.templates.event_text=function(start, end, event){
            return "Pond Active: "+event.text;
        }

        scheduler.templates.day_scale_date   = function(date){
            var formatFunc = scheduler.date.date_to_str("%D");
            return formatFunc(date);
        };

        <%
            java.util.Calendar c = java.util.Calendar.getInstance();
            int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK);
        %>

        var formatFuncDay = scheduler.date.date_to_str("%D");
        var day = <%= dayOfWeek %>;
        var day;

        if(day==2){
            dayToDisplay = new Date(2014,4,19);
        } else if(day==3){
             dayToDisplay = new Date(2014,4,20);
        } else if(day==4){
             dayToDisplay = new Date(2014,4,21);
        } else if(day==5){
             dayToDisplay = new Date(2014,4,22);
        } else if(day==6){
             dayToDisplay = new Date(2014,4,23);
        } else if(day==7){
             dayToDisplay = new Date(2014,4,24);
        } else if(day==1){
             dayToDisplay = new Date(2014,4,25);
        }

		<%
		 if(request.getParameter("embedded")!=null){
		%>

        scheduler.config.readonly = true;
        scheduler.init('scheduler_here',dayToDisplay,"day");

        <%} else { %>

        scheduler.init('scheduler_here',dayToDisplay,"week");

        <% } %>

        scheduler.load("schedule?device=pond");

	}

	function save(){
        var form = document.getElementById("xml_form");
        form.elements.data.value = scheduler.toXML();
        form.submit();
    }

    function clearAll(){
        if(confirm("Are you sure you want to remove all activations?")){
            scheduler.clearAll();
        }
    }
</script>

<body onload="init();">

    <%
     if(request.getParameter("embedded")==null){
    %>
        <input type="button" name="save" value="Save" onclick="save()" >
        <input type="button" name="clear" value="Clear All" onclick="clearAll()" >
        <input type="button" name="back" value="Back" onclick="window.location='pond.jsp'" >
     <% } %>

	<div id="scheduler_here" class="dhx_cal_container" style='width:100%; height:100%;'>
		<div class="dhx_cal_navline" style="display:none">
			<div class="dhx_cal_prev_button" style="display:none">&nbsp;</div>
			<div class="dhx_cal_next_button" style="display:none">&nbsp;</div>
			<div class="dhx_cal_today_button" style="display:none"></div>
			<div class="dhx_cal_date" style="display:none" style="display:none"></div>
			<div class="dhx_cal_tab" name="day_tab" style="right:204px;display:none"></div>
			<div class="dhx_cal_tab" name="week_tab" style="right:140px;display:none"></div>
			<div class="dhx_cal_tab" name="month_tab" style="right:76px;display:none"></div>
		</div>
		<div class="dhx_cal_header">
		</div>
		<div class="dhx_cal_data">
		</div>		
	</div>


	<form id="xml_form" action="schedule" method="post" target="hidden-iframe">
         <input type="hidden" name="data" value="" id="data">
         <input type="hidden" name="device" value="pond" id="device">
    </form>

    <iframe width="1" height="1" name="hidden-iframe">

</body>