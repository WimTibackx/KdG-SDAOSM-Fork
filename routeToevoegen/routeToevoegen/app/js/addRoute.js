var map;
var txtStart, txtEnd, btnSend;
var autocomplete = [];
var autoStart, autoEnd;
var markers = [];
var geoCoder;

var directionsDisplay;
var directionsService;

var points;

var timesArray = [[0,0], [0,0], [0,0], [0,0], [0,0], [0,0], [0,0]];

function initialize() {
    addListeners();
    // Creating new Geocoder (for turning coordinates into human-readable addresses)
    geoCoder = new google.maps.Geocoder();

    // Creating directionsService
    directionsService = new google.maps.DirectionsService();
    directionsDisplay = new google.maps.DirectionsRenderer({
        suppressMarkers: true,
        draggable: true
    });

    //map options (latlng = value to show map)
    var mapOptions = {
        center: new google.maps.LatLng(51.219448, 4.402464),
        zoom: 8
    };
    // mapOption: zoomlevel, middelpunt bepalen, ...
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions

    //create map with defined options
    map = new google.maps.Map(document.getElementById('map-canvas'),
        mapOptions);
    directionsDisplay.setMap(map);

    //auto complete
    // Get the HTML control elements and store them in variables because we will need them later.
    txtStart = document.getElementById('txtStart');
    txtEnd = document.getElementById('txtEnd');
    btnSend = document.getElementById('btnSend');

    // Hide second box and button
    txtEnd.style.display = 'none';
    btnSend.style.display = 'none';

    map.controls[google.maps.ControlPosition.TOP_LEFT].push(document.getElementById('controls'));
    //create autocomplete object

    autoStart = new google.maps.places.Autocomplete(txtStart);
    autoEnd = new google.maps.places.Autocomplete(txtEnd);

    autocomplete[0] = autoStart;
    autocomplete[1] = autoEnd;

    //add listener to box
    google.maps.event.addListener(autoStart, 'place_changed', onPlaceChanged);
    google.maps.event.addListener(autoEnd, 'place_changed', onPlaceChanged);
    google.maps.event.addDomListener(btnSend, 'click', saveRoute);

    google.maps.event.addListenerOnce(map, 'idle', function () {
        // Do something only the first time the map is loaded
        txtStart.focus();
    });
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
            title: place.formatted_address,
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
            txtEnd.style.display = 'block';
            txtEnd.focus();
        }
        if (markers[1]) {
            btnSend.style.display = 'block';
        }
        calcRoute();
    }
}

function updateAddress(step, latlng) {
    geoCoder.geocode({ location: latlng }, function (result, status) {
        if (status == "OK") {
            /* console.log('------------');
             for (var i = 0; i < result.length; i++) {
             console.log(result[i].formatted_address);
             } */
            //change marker title
            markers[step].setTitle(result[0].formatted_address);
            if (step == 0) {
                txtStart.value = result[0].formatted_address;
            } else {
                txtEnd.value = result[0].formatted_address;
            }
            calcRoute();
        } else {
            console.log("Error decoding address: " + status);
            setTimeout(function () {
                updateAddress(step, latlng);
            }, 1000);
        }
    })
}

function calcRoute() {
    if (markers[1]) {
        var request = {
            origin: markers[0].getPosition(),
            destination: markers[1].getPosition(),
            travelMode: google.maps.TravelMode.DRIVING
        };
        directionsService.route(request, function (result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
            } else {
                console.log("Error calculating route: " + status);
                setTimeout(function () {
                    calcRoute();
                }, 1000);
            }
        });
    }
}

function saveRoute() {
    points = {};

    points.start = {};
    points.start.lat = markers[0].getPosition().lat();
    points.start.long = markers[0].getPosition().lng();
    points.start.address = markers[0].getTitle();

    points.end = {};
    points.end.lat = markers[1].getPosition().lat();
    points.end.long = markers[1].getPosition().lng();
    points.end.address = markers[1].getTitle();

    console.log(JSON.stringify(points));

    openWindow();
}

function openWindow() {
    var dateElement = $('#startDatePicker');
    if (dateElement.prop('type') != 'date') {
        alert(dateElement.prop('type'));
    } else {
        console.log("No problem");
    }

    $('#overlay').show();
}

function addTime() {
    console.log($('#days').find(':input:checked').length);

    $('#days').find(':input:checked').each(function() {
        var time = [];
        time.push($('#repDepTime').val(), $('#repArrTime').val());

        timesArray[$(this).data('index')] = time;
    });
}

function submitAllData() {
    var car = $('#selectedCar').data('index');
    var places = $('#placeNumber').text();
    var date = $('#startDatePicker').val();
    var repeating = $('#repeatBox').prop('checked');
    if (repeating) {
        var endDate = $('#endDatePicker').val();
    } else {
        var depTime = $('#depTime').val();
        var arrTime = $('#arrTime').val();
    }

    var jsonObject = {};
    jsonObject.car = car;
    jsonObject.freeSpots = places;
    jsonObject.repeating = repeating;
    jsonObject.startDate = date;
    if (repeating) {
        jsonObject.endDate = endDate;
        jsonObject.repeatingDays = timesArray;
    } else {
        jsonObject.departureTime = depTime;
        jsonObject.arrivalTime = arrTime;
    }
    jsonObject.route = points;

    console.log(JSON.stringify(jsonObject));
    //jQuery.post('http://localhost:8080/BackEnd/route/add', JSON.stringify(jsonObject)); // TODO: enable sending Json to backend
}

function addListeners() {
    $('#addHours').click(addTime);
    $('#finalAdd').click(submitAllData);
}

google.maps.event.addDomListener(window, 'load', initialize);