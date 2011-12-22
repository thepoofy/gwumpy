<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" 
    import="com.thepoofy.constants.VenueCategoryEnum"
    %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="assets/javascripts/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="assets/javascripts/yqlgeo.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=AIzaSyA8PcXaepll3qB9JajxXmUnT_G6UXWYgE4&sensor=true"></script>
  <style>
  	td{
  		border-bottom: 1px solid black;
  	}
  	#dataTable{
  		float:left;
  	}
  	#fsq_canvas{
  		float:left;
  		border: 1px solid black;
  		width: 400px;
  		height: 400px;
  	}
  	#yelp_canvas{
  		float:left;
  		border: 1px solid black;
  		width: 400px;
  		height: 400px;
  	}
  	
  </style>
  <script>
	var fsqMap;
	var yelpMap;
	var currentCenter;
	var circles = [];
  
  	$(document).ready(function()
  	{
  		initiate_geolocation();
  		
  		$("#searchButton").click(function(event){
			event.preventDefault();
			
			$("#results_list").empty();
			
			$.ajax({
	         url: $("#searchForm").attr("action"),
	         data: $('#searchForm').serialize(),
	         dataType: 'json',
	         success: function(data) 
	         {
	        	 clearCircles();
	        	 drawSearchCircle(fsqMap);
	        	 drawSearchCircle(yelpMap);
	        	 
	        	if(data !== null && data.length > 0)
	        	{
	        		$.each(data, function(i, resp) 
					{
	        			var row = $(document.createElement("tr"));
	        			
	        			var td = $(document.createElement("td"));
	        			if(data[i].fsq)
	        			{
	        				td.append(document.createTextNode(data[i].fsq.name));
	        			}
	        			if(data[i].fsq.location)
	        			{
	        				td.append(document.createElement("br"))
	        				td.append(document.createTextNode(data[i].fsq.location.address))
	        				td.append(document.createElement("br"))
	        				td.append(document.createTextNode(data[i].fsq.location.postalCode))
	        				
	        			}
	        			row.append(td);
	        			
	        			var td2 = $(document.createElement("td"));
	        			if(data[i].yelp !== null)
	        			{
	        				td2.append(document.createTextNode(data[i].yelp.name));
	        				td2.append(document.createElement("br"));
	        				td2.append(document.createTextNode(data[i].yelp.location.address[0]));
	        				td2.append(document.createElement("br"));
	        				td2.append(document.createTextNode(data[i].yelp.location.postal_code));
	        			}
	        			row.append(td2);
	        			
	        			$("#results_list").append(row);
					});
	        	}
	        	else
	         	{
	        		var list_data = '<ul data-json="" data-role="listview" data-inset="true" class="ui-listview ui-listview-inset ui-corner-all ui-shadow"><li data-theme="c" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-corner-top ui-btn-up-c"><h3 class="ui-li-heading">No data found.</h3></li></ul>';
	        		$(list_data).appendTo("#results_list");
	         	}
	         }
	        });
  		});
  		
  		var myOptions = {
			zoom: 8,
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
  		fsqMap = new google.maps.Map(document.getElementById("fsq_canvas"), myOptions);
  		yelpMap = new google.maps.Map(document.getElementById("yelp_canvas"), myOptions);
  		
  		

  	});
  
  	function initiate_geolocation() {
      if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(handle_geolocation_query, handle_errors, {timeout: 10000});
      } else {
        yqlgeo.get('visitor', normalize_yql_response);
      }
    }
    
	function handle_errors(error) 
    {
    	
    }

    function normalize_yql_response(response) {
      if (response.error) {
        var error = { code : 0 };
        handle_error(error);
        return;
      }

		var position = {
	        coords : {
	          latitude: response.place.centroid.latitude,
	          longitude: response.place.centroid.longitude,
	          accuracy: response.place.centroid.accuracy
	        },
	        address : {
	          city: response.place.locality2.content,
	          region: response.place.admin1.content,
	          country: response.place.country.content
	        }
		};

		handle_geolocation_query(position);
    }

    function handle_geolocation_query(position) 
    {
		$('#latitude').val(position.coords.latitude);                                                                                     
		$('#longitude').val(position.coords.longitude);
		$('#accuracy').val(position.coords.accuracy);
		
		currentCenter = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
        fsqMap.setCenter(currentCenter);
        yelpMap.setCenter(currentCenter);
    }

    function drawSearchCircle(map)
    {
		var populationOptions = {
			strokeColor: "#FF0000",
			strokeOpacity: 0.8,
			strokeWeight: 2,
			fillColor: "#FF0000",
			fillOpacity: 0.35,
			map: map,
			center: currentCenter,
			radius: 400
		};
		cityCircle = new google.maps.Circle(populationOptions);
		circles.push(cityCircle);
		
		map.fitBounds(new google.maps.LatLngBounds(
			new google.maps.LatLng(
				currentCenter.lat()-0.0046466,
				currentCenter.lng()-0.0046466
			),
			new google.maps.LatLng(
				currentCenter.lat()+0.0046466,
				currentCenter.lng()+0.0046466
			)
		));
    }
    
    function clearCircles()
    {
    	if(circles[0] != null)
    		circles[0].setMap(null);
    	if(circles[1] != null)
			circles[1].setMap(null);
    	
		circles[0] = null;
		circles[1] = null;
    }

  
  </script>
  
<title>Insert title here</title>
</head>
<body>

	<form id="searchForm" action="/search" >
		<label>Latitude: </label><input type="text" name="lat" id="latitude" /><br />
		<label>Longitude: </label><input type="text" name="lng" id="longitude" /><br />
		<label>Accuracy: </label><input type="text" name="llAcc" id="accuracy" /><br />
		<label>Category: </label><select name="category" value="FOOD">
		<% for(VenueCategoryEnum cat : VenueCategoryEnum.values()){ %>
			<option value="<%= cat.name() %>"><%= cat.name() %></option>
		<% } %>
		</select><br />
		
		<input type="button" name="submit" value="Search" id="searchButton" />
	</form>

	<table id="dataTable">
		<thead>
			<tr>
				<td>Foursquare</td>
				<td>Yelp</td>
			</tr>
		</thead>
		<tbody id="results_list">
			
		</tbody>
	</table>
	
	<div id="fsq_canvas"></div>
	
	<div id="yelp_canvas"></div>

</body>
</html>