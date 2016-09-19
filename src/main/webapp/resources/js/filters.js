angular.module('myApp')
    .filter('scoreVoFilter', function () {
        return function (list, title) {
            var out = '-';
            if (list) {
                for (var i = 0; i < list.length; i++) {
                    if (list[i].title === title) {
                        out = list[i].score;
                        break;
                    }
                }
            }

            return out;
        }
    })
    .filter('activityScoreFilter', ['$sce', function ($sce) {
        return function (scores, schoolNum) {
            var out = '';
            if (scores) {
                for (var i = 0; i < scores.length; i++) {
                    if (scores[i].student.schoolNum === schoolNum) {
                        out += scores[i].title + ' ' + scores[i].score + '<br>';
                    }
                }
            }
            if (out.length > 1) {
                out = out.substr(0, out.length - 4);
            }

            return $sce.trustAsHtml(out);
        }
    }]);