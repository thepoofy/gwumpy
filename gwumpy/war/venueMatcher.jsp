<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="assets/javascripts/jquery-1.6.4.min.js"></script>
<script type="text/javascript" src="assets/javascripts/yqlgeo.js"></script>
  
  <script>
  	$(document).ready(function(){
  		initiate_geolocation();
  		
  		$("#searchButton").click(function(event){
			event.preventDefault();
			
			$.ajax({
	         url: $("#searchForm").attr("action"),
	         data: $('#searchForm').serialize(),
	         dataType: 'json',
	         success: function(data) 
	         {
	        	if(data !== null && data.length > 0)
	        	{
	        		$.each(data, function(i, resp) 
					{
	        			var row = $(document.createElement("tr"));
	        			row.append($(document.createElement("td"))
	        					.append(document.createTextNode(data[i].fsq.name))
	        			);
	        			var td = $(document.createElement("td"));
	        			if(data[i].yelp !== null)
	        			{
	        				td.append(document.createTextNode(data[i].yelp.name));	
	        			}
	        			row.append(td);
	        			
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
    }
  
  </script>
  
<title>Insert title here</title>
</head>
<body>

	<form id="searchForm" action="/search" >
		<label>Latitude: </label><input type="text" name="lat" id="latitude" /><br />
		<label>Longitude: </label><input type="text" name="lng" id="longitude" /><br />
		<label>Accuracy: </label><input type="text" name="llAcc" id="accuracy" /><br />
		
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

</body>
</html>