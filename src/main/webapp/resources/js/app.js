angular.module('myApp', ['ui.router', 'ui-notification', 'ui.bootstrap', 'chart.js'])
// chart.js config
    .config(['ChartJsProvider', function (ChartJsProvider) {
        // Configure all charts
        ChartJsProvider.setOptions({
            colors: ['#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'],
            responsive: true
        });
    }])
    .config(function (NotificationProvider) {
        // notification config
        NotificationProvider.setOptions({
            delay: 2000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20,
            positionX: 'center',
            positionY: 'top'
        });
    })
    .config(['$httpProvider', function ($httpProvider) {
        // http interceptor config
        $httpProvider.interceptors.push(function ($q, $window) {
            return {
                'responseError': function (rejection) {
                    // 403 error
                    if (rejection.status == 403) {
                        $window.NProgress.done();
                        angular.element('#view').html(rejection.data);
                    }
                    return $q.reject(rejection);
                }
            };
        });
    }])
    .config(function ($stateProvider, $urlRouterProvider) {
        // router config
        $stateProvider.state('dashboard', {
            url: '/dashboard',
            templateUrl: '/resources/pages/dashboard.html',
            controller: 'DashboardController as vm'
        }).state('myInfo', {
            url: '/myInfo',
            templateUrl: '/resources/pages/myInfo.html',
            controller: 'MyInfoController as vm'
        }).state('myScore', {
            url: '/myScore',
            templateUrl: '/resources/pages/myScore.html',
            controller: 'MyScoreController as vm'
        }).state('myQuality', {
            url: '/myQuality',
            template: '<ui-view/>',
            abstract: true
        }).state('myQuality.list', {
            url: '/list',
            templateUrl: '/resources/pages/listActivity.html',
            controller: 'ActivityController as vm'
        }).state('myQuality.edit', {
            url: '/edit',
            templateUrl: '/resources/pages/qualityEdit.html',
            controller: 'QualityEditController as vm'
        }).state('myQuality.hz', {
            url: '/hz',
            templateUrl: '/resources/pages/myQuality.html',
            controller: 'QualityController as vm'
        }).state('students', {
            url: '/students',
            template: '<ui-view/>',
            abstract: true
        }).state('students.list', {
            url: '/list',
            templateUrl: '/resources/pages/students/list.html',
            controller: 'StudentListController as vm'
        }).state('students.show', {
            url: '/show/:schoolNum',
            templateUrl: '/resources/pages/students/show.html',
            controller: 'StudentDetailController as vm'
        }).state('students.edit', {
            url: '/edit/:schoolNum',
            templateUrl: '/resources/pages/students/edit.html',
            controller: 'StudentDetailController as vm'
        }).state('projects', {
            url: '/projects',
            template: '<ui-view/>',
            abstract: true
        }).state('projects.list', {
            url: '/list',
            templateUrl: '/resources/pages/projects/list.html',
            controller: 'ProjectListController as vm'
        }).state('projects.edit', {
            url: '/edit/:id',
            templateUrl: '/resources/pages/projects/edit.html',
            controller: 'ProjectDetailController as vm'
        }).state('projects.create', {
            url: '/create',
            templateUrl: '/resources/pages/projects/create.html',
            controller: 'ProjectDetailController as vm'
        }).state('qualityManage', {
            url: '/qualityManage',
            templateUrl: '/resources/pages/qualityManage.html',
            controller: 'QualityManageController as vm'
        });

        $urlRouterProvider.otherwise('/dashboard');
    });