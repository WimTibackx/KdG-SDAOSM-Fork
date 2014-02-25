/**
 * Created by peter on 25/02/14.
 */
// CONTROLLER: Password
carpoolingControllers.controller('passwordCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    console.log("hey password controller test");

    var password = $('#passwordform');

    password.submit(function (e) {
        console.log("start mailing...");
        var link = "mailto:" + document.getElementById('emailadres') + "?cc=melissa.warrens@student.kdg.be" + "&subject=password forget" + "&body=your password is: test";
        console.log("end mailing...");
        window.location.href = link;
    });
}]);