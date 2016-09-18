angular.module('myApp')
    .filter('scoreVoFilter', function () {
        return function (list, title) {
            var out = '-';
            for (var i = 0; i < list.length; i++) {
                if (list[i].title === title) {
                    out = list[i].score;
                    break;
                }
            }
            return out;
        }
    })
    .filter('activityScoreFilter', function () {
        return function (list, title) {
            var out = '-';
            for (var i = 0; i < list.length; i++) {
                if (list[i].title === title) {
                    out = list[i].score;
                    break;
                }
            }
            return out;
        }
    });