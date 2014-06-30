angular.module("cpa.svc").factory('cpa.svc.pageAccess.v1', function() {
	var current = undefined;
	var options = { GUEST: 'GUEST', MEMBER: 'MEMBER', ANY: 'ANY' };
	
	var external = {};
	external.set = function(option) {
		if (!options.hasOwnProperty(option)) {
			return undefined;
		}
		current = options[option];
	};
	external.setGuest = function() { external.set(options.GUEST); };
	external.setMember = function() { external.set(options.MEMBER); };
	external.setAny = function() { external.set(options.ANY); };
	
	external.get = function() { return current; };
	external.isGuest = function() { return external.get() == options.GUEST; };
	external.isMember = function() { return external.get() == options.MEMBER; };
	external.isAny = function() { return external.get() == options.ANY; };
	external.is = function(option) {
		if (!options.hasOwnProperty(option)) {
			return undefined;
		}
		return external.get() == options[option];
	}
	
	external.setAny();
	
	return external;
});