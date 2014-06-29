/*
The MIT License (MIT)

Copyright (c) 2013 Matt Goodall

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
/*
 * This service is based on work by Matt Goodall. 
 * The original can be found at https://github.com/emgee/angular-route-urls .
 * 
 * This fork adds the .is() function and was based off of the 0.3 release.
 * (it also integrates it into the carpoolServices module and adds the name to align with cpa).
 */
carpoolServices.provider("cpaSvcRouteurlsV1", ['$locationProvider',function($locationProvider) {
	console.log($locationProvider);
    return {
        $get: function($route) {
        	console.log($route);
            // Cache the routing paths for any named routes.
            var pathsByName = {};
            angular.forEach($route.routes, function (route, path) {
                if (route.name) {
                    pathsByName[route.name] = path;
                }
            });

            // Build a path for the named route from the route's URL and the given
            // params.
            var path = function (name, params) {

                // Accept an object or array of params.
                var isObject = angular.isObject(params);
                if (!isObject) {
                    params = Array.prototype.slice.call(arguments, 1);
                }

                // Iterate the path segments replacing named groups.
                var path = (pathsByName[name] || "/").split("/");
                for (var i=0, idx=0; i<path.length; i++) {
                    if (path[i] && path[i][0] === ":") {
                        value = isObject ? params[path[i].substring(1)] : params[idx++];
                        if (value) {
                            path[i] = value;
                        }
                    }
                }
                return path.join("/");
            };

            // Query $locationProvider for its configuration.
            var html5Mode = $locationProvider.html5Mode();
            var hashPrefix = $locationProvider.hashPrefix();

            return {
                path: path,
                href: function (name, params) {
                    var url = path.apply(this, arguments);
                    if (html5Mode) {
                        return url;
                    }
                    return "#" + hashPrefix + url;
                }
            };
        }
    };
}]);