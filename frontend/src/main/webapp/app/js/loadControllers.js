/**
 * Created by peter on 27/02/14.
 */
// Little experiment to load controllers
console.log('=== Begin Experiment ===');

var head = document.getElementsByTagName('head')[0];
var jsControllers = ['addCar', 'addRoute', 'changePassword', 'changeRemoveCar', 'myProfile', 'password', 'login', 'register'];
// Add name of controller file here when adding new controller (without Ctrl)
// So if you add "addCarCtrl.js" you add "addCar" to the array

for (var i = 0; i < jsControllers.length; i++) {
    var script = document.createElement('script');
    script.src = 'js/controllers/' + jsControllers[i] + 'Ctrl.js';
    head.insertBefore(script, head.firstChild);
}

console.log('=== End Experiment ===');
// End little experiment */