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
        return function (scores, schoolNum, showDetail) {
            var out = showDetail ? '' : 0;
            if (scores) {
                for (var i = 0; i < scores.length; i++) {
                    if (scores[i].student.schoolNum === schoolNum) {
                        if (showDetail) {
                            out += scores[i].title + ' ' + scores[i].score + '<br>';
                        } else {
                            out += scores[i].score;
                        }
                    }
                }
            }
            if (out.length > 1 && showDetail) {
                out = out.substr(0, out.length - 4);
            }

            return showDetail ? $sce.trustAsHtml(out) : (out == 0 ? '' : $sce.trustAsHtml(out.toFixed(2)));
        }
    }]);