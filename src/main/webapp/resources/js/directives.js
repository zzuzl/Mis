angular.module('myApp')
    .directive('pagination', function () {
        return {
            restrict: 'AE',
            templateUrl: '/resources/components/pagination.html',
            transclude: true,
            link: function (scope, element, attrs) {
                var vm = scope.vm;
                /*scope.$watch(vm.params.perPage, function (value1, value2) {
                    console.log(value1);
                    console.log(value2);
                });*/
            }
        }
    });