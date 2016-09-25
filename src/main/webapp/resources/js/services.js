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
    }])
    .factory('zlDlg', ['$window', function ($window) {
        return {
            confirm: function (text, callback) {
                text = text ? text : '';
                $window.bootbox.confirm(text, callback);
            }
        }
    }])
    .service('studentService', ['$http', function ($http) {
        return {
            list: function (params, callback) {
                $http.get('/students', {
                    params: params
                }).then(function (response) {
                    if (response && response.data) {
                        callback(response.data);
                    }
                });
            },
            detail: function (schoolNum, callback) {
                if (schoolNum) {
                    $http.get('/students/' + schoolNum)
                        .then(function (response) {
                            if (response && response.data) {
                                callback(response.data);
                            }
                        });
                }
            }
        }
    }])
    .service('projectService', ['$http', function ($http) {
        return {
            list: function (params, callback) {
                $http.get('/projects', {
                    params: params
                }).then(function (response) {
                    if (response && response.data) {
                        callback(response.data);
                    }
                });
            },
            detail: function (id, callback) {
                if (id) {
                    $http.get('/projects/' + id)
                        .then(function (response) {
                            if (response && response.data) {
                                callback(response.data);
                            }
                        });
                }
            }
        }
    }]);
