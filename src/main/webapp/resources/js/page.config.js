angular.module('myApp')
    .config(function ($stateProvider) {
        $stateProvider.state('myScore', {
            parent: 'main',
            url: '/myScore',
            controller: myScoreController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/myScore.html'
        }).state('qualityEdit', {
            parent: 'main',
            url: '/qualityEdit',
            controller: QualityController,
            controllerAs: 'vm',
            templateUrl: '/resources/pages/qualityEdit.html'
        });
    });