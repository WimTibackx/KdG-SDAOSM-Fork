var map;
var counter = 0;
var txtStart, txtEnd, btnSend;
var autocomplete = [];
//0 start, 1 stop, other indexes are for
var autoPositions = [];
//var autoStart, autoEnd;
var markers = [];
var geoCoder;

var directionsDisplay;
var directionsService;

var points;
var controlScope;

var passages = {};

function initialize($scope) {

    controlScope = $scope;

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

    autocomplete[0] = new google.maps.places.Autocomplete(txtStart);
    autocomplete[1] = new google.maps.places.Autocomplete(txtEnd);

    //add listener to box
    google.maps.event.addListener( autocomplete[0], 'place_changed', onPlaceChanged);
    google.maps.event.addListener( autocomplete[1], 'place_changed', onPlaceChanged);

    google.maps.event.addDomListener(btnSend, 'click', saveRoute);

    google.maps.event.addListenerOnce(map, 'idle', function () {
        // Do something only the first time the map is loaded
        txtStart.focus();
    });

    // TODO: remove this
    // openWindow();
}

function onPlaceChanged() {

    var step = autocomplete.indexOf(this);
    var place = this.getPlace();
    if (place.geometry) {
        map.panTo(place.geometry.location);
        map.setZoom(15);
        var marker = new google.maps.Marker({
            map: map,
            title: place.formatted_address,
            position: place.geometry.location,
            draggable: true
        });

        if (step <2){
            var icon = 'img/map/marker' + (step + 1) + '.png';
            marker.setIcon(icon);

        }

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
    if (markers[0] != null) {
        var passages = [];
        for(i = 2; i < markers.length; i++){
            passages.push({
                location: markers[i].getPosition(),
                stopover:true
            });
        }
        var request = {
            origin: markers[0].getPosition(),
            destination: markers[1].getPosition(),
            waypoints: passages,
            optimizeWaypoints: true,
            travelMode: google.maps.TravelMode.DRIVING
        };
        var titles = [];
        for(i = 0; i < markers.length; i++){
            titles.push(markers[i].title)
        }
        controlScope.locations = JSON.stringify(titles);
        console.log(controlScope.locations)
        directionsService.route(request, function (result, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(result);
                var routepoints = result.routes[0].overview_path;
                console.log(routepoints);
                /*for (i = 0; i < routepoints.length; i++) {
                    console.log(new google.maps.Marker({
                        map: map,
                        position: routepoints[i]
                    }));

                } */
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
    points = [];

    for (i = 0; i < markers.length; i++) {
        points[i] = {};
        points[i].lat = markers[i].getPosition().lat();
        points[i].long = markers[i].getPosition().lng();
        points[i].address = markers[i].getTitle();

    }


    console.log(JSON.stringify(points));

    openWindow();
}

function openWindow() {
    var parentElement = document.getElementById('locations');

    var newTable = document.createElement('table');
    newTable.setAttribute('id', 'days');
    var newTbody = document.createElement('tbody');
    var tableRowHeader = document.createElement('tr');
    var tableHeader1 = document.createElement('th');
    tableHeader1.appendChild(document.createTextNode('Passage point'));
    var tableHeader2 = document.createElement('th');
    tableHeader2.appendChild(document.createTextNode('Passage time'));
    tableRowHeader.appendChild(tableHeader1);
    tableRowHeader.appendChild(tableHeader2);
    newTbody.appendChild(tableRowHeader);
    newTable.appendChild(newTbody);
    parentElement.appendChild(newTable);$
    for (i = 0; i < markers.length; i++) {
        if (i != 1){
            addTableRow(i, newTbody);
        }
    }
    addTableRow(1, newTbody);
    var dateElement = $('#startDatePicker');
    if (dateElement.prop('type') != 'date') {
        alert(dateElement.prop('type'));
    } else {
        console.log("No problem");
    }

    $('#overlay').show();
}

/*
function addTime() {
    if (typeof(passages)) {

    }
    console.log($('#days').find(':input:checked').length);

    $('#days').find(':input:checked').each(function () {
        var time = [];
        time.push($('#repDepTime').val(), $('#repArrTime').val());

        passages[$(this).data('day')] = time;

        $(this).prop('checked', false);
    });

    fillTable();
}

function fillTable() {
    var table = document.getElementById('days');
    for (i = 0; i < 2; i++) {
        for (j = 0; j < passages.length; j++) {
            if (passages[j][i] != 0) {
                table.rows[i + 1].cells[j + 1].innerText = passages[j][i];
            }
        }
    }
}
*/

function submitAllData() {
    var car = $('#selectedCar').data('index');
    var places = $('#placeNumber').val();
    var startDate = $('#startDatePicker').val();
    var repeating = $('#repeatBox').prop('checked');
    var endDate = $('#endDatePicker').val();
    if (!repeating) {
        endDate = startDate
        ;
        passages = [$('#depTime').val(), $('#arrTime').val()];
    }

    for(i = 0; i < points.length; i++){
        passages[i] = document.getElementById('passageTime'+ i).value;
    }

    var jsonObject = {
    car: car,
    freeSpots : places,
    repeating : repeating,
    startDate : startDate,
    endDate : endDate,
    passages : passages,
    route : points
    }

    console.log(JSON.stringify(jsonObject));
    var rootUrl = "http://localhost:8080/BackEnd/";
    $.ajax({
        url: rootUrl + "authorized/route/add",
        method: "POST",
        data: JSON.stringify(jsonObject),
        headers: {'Content-Type': "text/plain; charset=utf-8"},
        success: function (){
        }
    });
    //jQuery.post('http://localhost:8080/BackEnd//authorized/route/add', JSON.stringify(jsonObject)); // TODO: enable sending Json to backend
}

function addListeners() {
//    $('#addHours').click(addTime);
    $('#addPassagePoint').click(addPassagePoint)
}

function addPassagePoint(){
    counter++;
    var parentElement = document.getElementById('controls');
    //<input id="txtEnd" class="controls" type="text" placeholder="Geef eindpunt in.">

    var newInput = document.createElement('input');

    newInput.setAttribute('id', 'txtPassage'+counter);
    newInput.setAttribute('class', 'controls');
    newInput.setAttribute('type', 'text');
    newInput.setAttribute('placeholder', 'Geef passagepunt in');

    parentElement.appendChild(newInput);

    autocomplete[counter+1] = new google.maps.places.Autocomplete(document.getElementById('txtPassage'+counter));

    google.maps.event.addListener( autocomplete[counter+1], 'place_changed', onPlaceChanged);


}

function addTableRow(index, tbody){
    var newTableRow = document.createElement('tr');
    var newTableData1 = document.createElement('td');
    newTableData1.appendChild(document.createTextNode(markers[index].title));
    var newTableData2 = document.createElement('td');
    var newTimeInput = document.createElement('input');
    newTimeInput.setAttribute('type', 'text');
    newTimeInput.setAttribute('id', 'passageTime' + index);
    newTimeInput.setAttribute('placeholder', '9:00');
    newTableData2.appendChild(newTimeInput);
    newTableRow.appendChild(newTableData1);
    newTableRow.appendChild(newTableData2);
    tbody.appendChild(newTableRow);
}

window.onload = function () {
    google.maps.event.addDomListener(window, 'load', initialize);
}

function focus(){
    console.log("Gets focus");
}


function cancelRoute(){
    var myNode = document.getElementById('locations');
    myNode.innerHTML = '';
    $('#overlay').hide();
}