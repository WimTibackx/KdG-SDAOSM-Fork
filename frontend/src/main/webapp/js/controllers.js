var carpoolingControllers = angular.module('carpoolingControllers', []);

carpoolingControllers.controller('myProfileCtrl', ['$scope', function ($scope) {
    $scope.avatarsrc = '../frontend/img/avatar.JPG';
    $scope.gendersrc = '../frontend/img/female.png';
}]);
