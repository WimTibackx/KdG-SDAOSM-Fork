/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Add route
carpoolingControllers.controller('addRouteCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("hey addRoute controller test");

    $scope.passages = {};

    $scope.addTime = function () {
        console.log("Angular: " + $('#days').find(':input:checked').length);

        $('#days').find(':input:checked').each(function () {
            var time = [];
            time.push($('#repDepTime').val(), $('#repArrTime').val());

            passages[$(this).data('day')] = time;

            $(this).prop('checked', false);

            $scope.passages = passages;
        });
    }

    initialize();
}]);