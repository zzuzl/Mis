angular.module('myApp')
// 自定义分页指令
    .directive('pagination', function () {
        return {
            restrict: 'AE',
            templateUrl: '/resources/components/pagination.html',
            transclude: true,
            scope: {
                params: '=params',
                load: '=load',
                control: '=control'
            },
            controller: ['$scope', function PaginationController($scope) {
                var max = 10;

                $scope.loadData = function () {
                    $scope.load($scope.params, function (res) {
                        $scope.res = res;

                        $scope.pageArr = [];
                        // 如果不足10页则直接显示
                        if (res.totalPage <= max) {
                            for (i = 1; i <= res.totalPage; i++) {
                                $scope.pageArr.push(i);
                            }
                        } else {
                            if (res.page <= max / 2) {
                                for (i = 1; i <= res.totalPage; i++) {
                                    $scope.pageArr.push(i);
                                }
                            } else {
                                if (res.totalPage > max + max / 2) {
                                    for (i = res.page - max / 2; i <= res.page + max / 2; i++) {
                                        $scope.pageArr.push(i);
                                    }
                                } else {
                                    for (i = res.totalPage - max; i <= res.totalPage; i++) {
                                        $scope.pageArr.push(i);
                                    }
                                }
                            }
                        }
                    });
                };

                $scope.changePage = function (p) {
                    $scope.params.page = p;
                    $scope.loadData();
                };

                $scope.addPage = function (num) {
                    var result = $scope.params.page + num;
                    console.log(result);
                    if (result >= 1 && result <= $scope.res.totalPage) {
                        $scope.params.page = result;
                        $scope.loadData();
                    }
                };

                $scope.control($scope.loadData);
                $scope.loadData();
            }]
        }
    });