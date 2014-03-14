/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Add route

var selectedCar;


carpoolingApp.controllerProvider.register('addRouteCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {

    deleteActiveClass();
    $('#MyProfileTab').addClass('active');

    $scope.passages = {};

    $http({
        method: 'GET',
        url: rootUrl + "/authorized/user/car/get"
    }).success(function (response) {
            if (response.hasOwnProperty("error")) {
                if (response["error"] == "AuthorizationNeeded") {
                    $location.path("/login");
                    SharedProperties.setProperty("Authorization is required")
                }
            }else{
                var cars = response;
                var parentElement = document.getElementById("carList");
                for(i = 0; i < cars.length; i++){
                    var imageElement = document.createElement("img");
                    imageElement.setAttribute("title", cars[i].brand + " " + cars[i].type);
                    imageElement.setAttribute("src", getCarImageUrl(cars[i].url));
                    imageElement.setAttribute("onclick" , "selectCar(this)");
                    imageElement.setAttribute("data-index", cars[i].id);
                    parentElement.appendChild(imageElement)
                }
            }



        });



    $scope.addTime = function () {

        $('#days').find(':input:checked').each(function () {
            var time = [];
            time.push($('#repDepTime').val(), $('#repArrTime').val());

            passages[$(this).data('day')] = time;

            $(this).prop('checked', false);

            $scope.passages = passages;
        });
    }

    function getCarImageUrl(part) {

        if (part != null){
            return "http://localhost:8080/BackEnd/carImages/"+part;
        }else{
            return "../app/img/carAvatar.jpg"
        }

    };


    initialize();
}]);

function selectCar(element){

    var parent = element.parentNode
    for(var i = 1; i < parent.childNodes.length; i++){
        if (element == parent.childNodes[i]) {
            parent.childNodes[i].setAttribute("id", "selectedCar" )
        }else{
            parent.childNodes[i].setAttribute("id", "" )
        }
    }


}
