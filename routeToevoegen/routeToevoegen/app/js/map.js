var map;
var mapBounds;
var infoWindow, geoCoder;

// var callnr = 0;
var autocomplete;

var setStart = true;
var setEnd = false;
var setReady = false;
var visibleEnd = false;
var visibleReady = false;
var markers = [];

var buttonEnd;
var buttonReady;


function initialize() {
    //get buttons
    buttonEnd = document.getElementById('end');
    buttonReady = document.getElementById('klaar');

    //disable buttons end and ready (user needs to fill in a start point first before those can be clicked)
    buttonEnd.disabled = true;
    buttonReady.disabled = true;

    //get input field for point on map to add a placeholder (text) en focus on the box
    document.getElementById('pac-input').placeholder = 'Geef beginpunt in';

    /*
    geoCoder = new google.maps.Geocoder();
    // Geocoder: omzetten van adressen naar coordinaten
    */

    //map opions (latlng = value to show map)
    var mapOptions = {
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };
    // mapOption: zoomlevel, middelpunt bepalen, ...
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

    //create map with defined options
    map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);

    /*mapBounds = new google.maps.LatLngBounds(
        new google.maps.LatLng(-33.8902, 151.1759),
        new google.maps.LatLng(-33.8474, 151.2631)
    );*/

    //var options = {bounds: mapBounds};

    //auto complete
    //get the html input element for the autocomplete search box
    var input = document.getElementById("pac-input");
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    //create autocomplete object
    autocomplete = new google.maps.places.Autocomplete(input);

    //add listener to box
    google.maps.event.addListener(autocomplete, 'place_changed', onPlaceChanged);

    $('#pac-input').focus();
}

/*function getLocation(point, callback) {
    var markerLocation;
    if (point.coords.lat && point.coords.lng) {
        markerLocation = new google.maps.LatLng(point.coords.lat, point.coords.lng);
        callback(markerLocation);
    } else {
        // using address to coordinate conversion
        decodeAddress(point, callback);
    }
}

// Geocoder gebruiken om adres om te zetten naar LatLng
function decodeAddress(point, callback) {
    // console.log("calling " + ++callnr);
    geoCoder.geocode({ address: point.city }, function (result, status) {
        if (status == "OK") {
            point.coords.lat = result[0].geometry.location.lat(); // 13 calls with recording of result
            point.coords.lng = result[0].geometry.location.lng(); // 20 calls without (OVER_QUERY_LIMIT; limit: 1 req/user/sec)
            callback(result[0].geometry.location);
        } else {
            console.log("Error decoding address: " + status);
            setTimeout(function () {
                decodeAddress(point, callback)
            }, 1000);
        }
    })
}*/

function onPlaceChanged() {
    var place = autocomplete.getPlace();
    if (place.geometry) {
        map.panTo(place.geometry.location);
        map.setZoom(15);
        var marker = new google.maps.Marker({
                map: map,
                title: place.name,
                position: place.geometry.location
              });
        //if setStart is true, the marker will be added as the beginning point
        if(setStart==true){
            if(markers[0]!=null){ //when there is already a beginmarker, delete this marker and replace it with the new one
                 markers[0].setMap(null); //delete beginmarker
            }
            markers[0] = marker; //add beginmarker to array on the first place
            buttonEnd.disabled = false; //make button end clickable
        }else if(setEnd==true){ //when there is already an endmarker, delete this marker and replace it with the new one
            if(markers[1]!=null){
                markers[1].setMap(null); //delete endmarker
            }
            markers[1] = marker; //add endmarker to array on second place
            buttonReady.disabled = false;
        }
    } /*else {
        document.getElementById('pac-input').placeholder = 'Enter a city';
    }*/
}

//click on start button to give in a beginning point
function mapGoStart(){
    setStart = true;
    setEnd = false;
    //focus on search box
    $('#pac-input').val('').focus();
    //change placeholder
    document.getElementById('pac-input').placeholder = 'Geef beginpunt in';
}

function mapGoEnd(){
    setStart = false;
    setEnd = true;
    //focus on search box
    $('#pac-input').val('').focus();
    //change placeholder
    document.getElementById('pac-input').placeholder = 'Geef eindpunt in';
}

// Listener
google.maps.event.addDomListener(window, 'load', initialize);
