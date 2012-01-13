function initiate_geolocation() 
{
  if(navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(handle_geolocation_query, handle_errors, {timeout: 10000});
  } else {
    yqlgeo.get('visitor', normalize_yql_response);
  }
}

function onGeoLocationEnabled()
{
	//alert("success");
	hasGeoLocation = true;
	
	$('#btnInit').attr('disabled', false);
	try
	{
		$('#btnInit').button('enable');
	}
	catch(e){}
	
	$('#search').attr('disabled', false);
	
	try
	{
		$('#search').button('enable');
	}
	catch(e){}
}

function handle_errors(error) 
{
	//alert("fail");
	/*
	switch(error.code) 
	{
        case error.PERMISSION_DENIED:
		  $.mobile.changePage("#onError", {
            transition: 'pop'
          });
        break;
        case error.POSITION_UNAVAILABLE:
          $.mobile.changePage("#onError", {
            transition: 'flip'
          });
        break;
        case error.TIMEOUT:
          $.mobile.changePage("#onError", {
            transition: 'flip'
          });
        break;
        default:
          $.mobile.changePage("#onError", {
            transition: 'flip'
          });
        break;
	}
	*/
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

function create(domType)
{
	return $(document.createElement(domType));
}

function createText(text)
{
	return $(document.createTextNode(text));
}