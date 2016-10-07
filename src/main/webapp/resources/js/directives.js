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
    })
    .directive('autoComplete', function () {
        return {
            restrict: 'E',
            template: "<input type='text' class='form-control' name='keyword' id='autocomplete' placeholder='输入学号查找'/>",
            transclude: true,
            link: function (scope, element) {
                $('#autocomplete').autocomplete({
                    serviceUrl: '/students/blurSearch',
                    paramName: 'keyword',
                    dataType: 'JSON',
                    maxHeight: 300,
                    onSelect: function (suggestion) {
                        // 获取已有权限
                        scope.vm.getResource(suggestion.data);
                    },
                    transformResult: function (response, originalQuery) {
                        var result = {
                            suggestions: []
                        };

                        if (response.suggestions) {
                            for (var i = 0; i < response.suggestions.length; i++) {
                                result.suggestions.push({
                                    value: response.suggestions[i].name + ' (' + response.suggestions[i].schoolNum + ')',
                                    data: response.suggestions[i].schoolNum
                                });
                            }
                        }
                        return result;
                    }
                });
            }
        }
    });