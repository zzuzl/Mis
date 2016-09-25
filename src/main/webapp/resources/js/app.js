/*var myApp = angular.module('myApp', ['ng-admin', 'ui.bootstrap']);
 project.creationView()
 .title('新建项目')
 .fields([
 nga.field('title').label('标题')
 .attributes({placeholder: '标题'})
 .validation({required: true, minlength: 3, maxlength: 20}),
 nga.field('minScore', 'float').label('最小分值')
 .format('0.00')
 .defaultValue(0)
 .attributes({placeholder: '最小分值'})
 .validation({required: true}),
 nga.field('maxScore', 'float').label('最大分值')
 .format('0.00')
 .defaultValue(0)
 .attributes({placeholder: '最大分值'})
 .validation({required: true}),
 nga.field('desc').label('描述')
 .attributes({placeholder: '描述'})
 .validation({maxlength: 100}),
 // 以下为item字段
 nga.field('itemList', 'embedded_list').label('子项目')
 .targetFields([
 nga.field('title').label('子项标题')
 .validation({required: true, minlength: 3, maxlength: 20}),
 nga.field('minScore', 'float')
 .format('0.00')
 .defaultValue(0)
 .label('最小分值')
 .validation({required: true}),
 nga.field('maxScore', 'float')
 .format('0.00')
 .defaultValue(0)
 .label('最大分值')
 .validation({required: true})
 ])
 ]);

 }]);*/
// todo list
// 1、添加分数时防止分数超出范围

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
    .config(function ($stateProvider, $urlRouterProvider) {
        // router config
        $stateProvider.state('dashboard', {
            url: '/dashboard',
            templateUrl: '/resources/pages/dashboard.html',
            controller: 'DashboardController as vm'
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
        }).state('qualityManage', {
            url: '/qualityManage',
            templateUrl: '/resources/pages/qualityManage.html',
            controller: 'QualityManageController as vm'
        });

        $urlRouterProvider.otherwise('/dashboard');
    });