var map;
var input;
var autocomplete;
var currentStep = 1;

var setStart = true;
var setEnd = false;
var setReady = false;
var visibleEnd = false;
var visibleReady = false;
var markers = [];

var buttonEnd;
var buttonReady;


function initialize() {
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


    //auto complete
    //get the html input element for the autocomplete search box
    input = document.getElementById("pac-input");
    //get input field for point on map to add a placeholder (text) and focus on the box
    input.placeholder = 'Geef beginpunt in';
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    //create autocomplete object
    autocomplete = new google.maps.places.Autocomplete(input);

    //add listener to box
    google.maps.event.addListener(autocomplete, 'place_changed', onPlaceChanged);
    $('#btnStep1').click(changeStep);
    $('#txtStep1').click(changeStep);

    input.focus();

}

function onPlaceChanged() {
    var place = autocomplete.getPlace();
    if (place.geometry) {
        map.panTo(place.geometry.location);
        map.setZoom(15);
        var marker = new google.maps.Marker({
            map: map,
            title: place.name,
            position: place.geometry.location,
            draggable: true
        });

        if (markers[currentStep] != null) {
            markers[currentStep].setMap(null);
        }
        markers[currentStep] = marker;

        $('#btnStep' + (currentStep + 1))
            .attr('class', 'enabledButton')
            .click(changeStep);
    }
}

function changeStep() {
    $('#btnStep' + currentStep).attr('class', 'enabledButton');
    currentStep = $(this).data('step');
    $('#btnStep' + currentStep).attr('class', 'activeButton');

    $(input).val('').focus();

    switch (currentStep) {
        case 1:
            input.placeholder = 'Geef beginpunt in';
            break;
        case 2:
            input.placeholder = 'Geef eindpunt in';
            break;
    }
}

//click on start button to give in a beginning point
function mapGoStart() {
    setStart = true;
    setEnd = false;
    //focus on search box
    $('#pac-input').val('').focus();
    //change placeholder
    document.getElementById('pac-input').placeholder = 'Geef beginpunt in';
}

function mapGoEnd() {
    setStart = false;
    setEnd = true;
    //focus on search box
    $('#pac-input').val('').focus();
    //change placeholder
    document.getElementById('pac-input').placeholder = 'Geef eindpunt in';
}

// Listener
google.maps.event.addDomListener(window, 'load', initialize);
