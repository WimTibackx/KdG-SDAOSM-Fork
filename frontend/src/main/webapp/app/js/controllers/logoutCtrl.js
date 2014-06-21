//document.addEventListener("DOMContentLoaded", function() {
carpoolingApp.controllerProvider.register('logoutCtrl', ['cpa.svc.api.v1','$location', function(cpa_api, $location) {
	var cbAll = function() {
		$location.path("/login");
		$("#error").text("");
        $("#error").show();
	}
	cpa_api.logout(cbAll);
}]);
//});