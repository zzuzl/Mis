angular.module('myApp')
    .config(function ($stateProvider) {
        $stateProvider.state('myScore', {
            parent: 'main',
            url: '/myScore',
            controller: myScoreController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/myScore.html'
        }).state('qualityManage', {
            parent: 'main',
            url: '/qualityManage',
            controller: QualityManageController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/qualityManage.html'
        }).state('qualityEdit', {
            parent: 'main',
            url: '/qualityEdit',
            controller: QualityEditController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/qualityEdit.html'
        }).state('listActivity', {
            parent: 'main',
            url: '/listActivity',
            controller: ActivityController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/listActivity.html'
        }).state('myQuality', {
            parent: 'main',
            url: '/myQuality',
            controller: QualityController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/myQuality.html'
        });
    });