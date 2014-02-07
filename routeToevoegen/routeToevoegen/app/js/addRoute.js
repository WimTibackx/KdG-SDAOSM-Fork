var map;
var inputStart, inputEnd;
var autocomplete = [];
var autoStart, autoEnd;
var markers = [];
var geoCoder;


function initialize() {
    // Creating new Geocoder
    geoCoder = new google.maps.Geocoder();

    //map options (latlng = value to show map)
    var mapOptions = {
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };
    // mapOption: zoomlevel, middelpunt bepalen, ...
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

    //create map with defined options
    map = new google.maps.Map(document.getElementById("map-canvas"),
        mapOptions);


    //auto complete
    //get the html input element for the autocomplete search box
    inputStart = document.getElementById("txtStart");
    inputEnd = document.getElementById("txtEnd");
    inputEnd.style.display = 'none';

    map.controls[google.maps.ControlPosition.TOP_LEFT].push(document.getElementById('controls'));
    //create autocomplete object

    autoStart = new google.maps.places.Autocomplete(inputStart);
    autoEnd = new google.maps.places.Autocomplete(inputEnd);

    autocomplete[0] = autoStart;
    autocomplete[1] = autoEnd;

    //add listener to box
    google.maps.event.addListener(autoStart, 'place_changed', onPlaceChanged);
    google.maps.event.addListener(autoEnd, 'place_changed', onPlaceChanged);

    inputStart.focus();

}

function onPlaceChanged() {
    var step = autocomplete.indexOf(this);
    var place = this.getPlace();
    if (place.geometry) {
        map.panTo(place.geometry.location);
        map.setZoom(15);
        var icon = 'img/map/marker' + (step + 1) + '.png';
        var marker = new google.maps.Marker({
            map: map,
            title: place.name,
            position: place.geometry.location,
            draggable: true,
            icon: icon
        });
        google.maps.event.addListener(marker, 'dragend', function () {
            updateAddress(step, this.getPosition());
        });

        if (markers[step] != null) {
            markers[step].setMap(null);
        }
        markers[step] = marker;

        if (markers[0]) {
            inputEnd.style.display = 'block';
        }
        if (markers[1]) {
            // TODO: display button
        }
    }
}

function updateAddress(step, latlng) {
    geoCoder.geocode({ location: latlng }, function (result, status) {
        if (status == "OK") {
            console.log('------------');
            for (var i = 0; i < result.length; i++) {
                console.log(result[i].formatted_address);
            }
            if (step == 0) {
                inputStart.value = result[0].formatted_address;
            } else {
                inputEnd.value = result[0].formatted_address;
            }
        } else {
            console.log("Error decoding address: " + status);
            setTimeout(function () {
                updateAddress(step, latlng);
            }, 1000);
        }
    })
}

// Listener
google.maps.event.addDomListener(window, 'load', initialize);
