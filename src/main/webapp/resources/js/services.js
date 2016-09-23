angular.module('myApp')
    .factory('progression', ['$window', function ($window) {
        return {
            start: function () {
                $window.NProgress.start();
            },
            done: function () {
                $window.NProgress.done();
            }
        }
    }]);
