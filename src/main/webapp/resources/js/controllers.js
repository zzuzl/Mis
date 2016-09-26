// 导航栏controller
function NavController($http, $window) {
    var vm = this;

    vm.student = $window.localStorage.getItem('student');
    vm.logout = function () {
        $http.post('/students/logout')
            .then(function (response) {
                $window.localStorage.removeItem('student');
                $window.location.href = "/login";
            });
    };
}
NavController.$inject = ['$http', '$window'];

// 菜单controller
function MenuController($location) {
    var vm = this;

    vm.active = function (text) {
        return $location.url().indexOf(text) === 0;
    };
}
MenuController.$inject = ['$location'];

// 主页controller
function DashboardController($http, $timeout) {
    var vm = this;
    // 获取最近30天的登录记录
    vm.series = ['人次', '人数'];
    vm.labels = [];
    vm.loginData = [[], []];

    vm.load = function () {
        $http.get('/students/loginRecords/30').then(function (response) {
            if (response.data && response.data.data) {
                vm.labels = [];
                vm.loginData = [[], []];
                for (key in response.data.data.map) {
                    var obj = response.data.data.map[key];
                    vm.labels.push(key);
                    vm.loginData[0].push(obj.recordCount);
                    vm.loginData[1].push(obj.personCount);
                }
            }
        });
    };

    vm.load();

    vm.onClick = function (points, evt) {
        console.log(points, evt);
    };

    var gap = 10 * 60 * 1000;

    // 每10秒更新一次数据
    $timeout(function () {
        vm.load();
    }, gap);
}
DashboardController.$inject = ['$http', '$timeout'];

// 学生列表
function StudentListController(progression, studentService, Notification, zlDlg) {
    var vm = this;
    vm.params = {
        page: 1,
        perPage: 10
    };

    // 为了在controller中调用指令的方法
    vm.control = function (func) {
        vm.callback = func;
    };

    vm.list = function (_params, callback) {
        progression.start();
        if (_params) {
            vm.params.page = _params.page;
            vm.params.perPage = _params.perPage;
        }
        studentService.list(vm.params, function (res) {
            vm.students = res.list;
            progression.done();
            if (callback) {
                callback(res);
            }
        });
    };

    vm.delete = function (schoolNum) {
        zlDlg.confirm('确定要删除吗?', function (result) {
            if (result) {
                studentService.remove(schoolNum, function (res) {
                    if (res.success) {
                        Notification.success("删除成功");
                        vm.callback();
                    } else {
                        Notification.error(res.error);
                    }
                })
            }
        });
    }
}
StudentListController.inject = ['progression', 'studentService', 'Notification', 'zlDlg'];

// 学生详细
function StudentDetailController(progression, studentService, $stateParams, Notification, $scope, $filter) {
    var vm = this;

    vm.detail = function (schoolNum) {
        progression.start();
        studentService.detail(schoolNum, function (res) {
            vm.student = res;
            if (vm.student.birthday) {
                vm.student.birthday = $filter('date')(vm.student.birthday, 'yyyy-MM-dd', 'zh_CN')
            }
            if (vm.student.entranceDate) {
                vm.student.entranceDate = $filter('date')(vm.student.entranceDate, 'yyyy-MM-dd', 'zh_CN')
            }
            progression.done();
        });
    };

    if ($stateParams.schoolNum) {
        vm.detail($stateParams.schoolNum);
    }

    vm.onSubmit = function () {
        if ($scope.form.$invalid) {
            // Notification.error("存在不合法的数据");
        } else {
            progression.start();
            studentService.update(vm.student, function (res) {
                if (res.success) {
                    Notification.success('修改成功');
                } else {
                    Notification.error(res.error);
                }
                progression.done();
            });
        }
    };
}
StudentDetailController.inject = ['progression', 'studentService', ' $stateParams', 'Notification', '$scope', '$filter'];

// 项目列表
function ProjectListController(progression, projectService) {
    var vm = this;
    vm.params = {
        page: 1,
        perPage: 10
    };

    // 为了在controller中调用指令的方法
    vm.control = function (func) {
        vm.callback = func;
    };

    vm.list = function (_params, callback) {
        progression.start();
        if (_params) {
            vm.params.page = _params.page;
            vm.params.perPage = _params.perPage;
        }
        projectService.list(vm.params, function (res) {
            vm.projects = res.list;
            progression.done();
            if (callback) {
                callback(res);
            }
        });
    };
}
ProjectListController.inject = ['progression', 'projectService'];

// 模态框
function ModalInstanceCtrl($uibModalInstance, item, quality, items) {
    var vm = this;
    var row = 1;
    vm.items = items;
    vm.item = item;
    vm.quality = quality;
    vm._items = angular.copy(items);

    vm._items.forEach(function (e) {
        e.row = row++;
    });

    vm.removeItem = function (index) {
        for (var i = 0; i < vm._items.length; i++) {
            if (vm._items[i].row === index) {
                vm._items.splice(i, 1);
                break;
            }
        }
    };

    vm.addItem = function () {
        vm._items.push({
            item: {
                id: item.id
            },
            student: {
                name: quality.name,
                schoolNum: quality.schoolNum
            },
            score: 0,
            row: row++
        });
    };

    vm.ok = function () {
        $uibModalInstance.close(vm._items);
    };

    vm.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
}
ModalInstanceCtrl.inject = ['$uibModalInstance', 'items'];


// 我的成绩单
function MyScoreController($http, progression) {
    var vm = this;
    progression.start();
    $http.get('/students/myScore').then(function (response) {
        if (response && response.data) {
            vm.firstTermScore = {
                title: response.data.data.scores[0].term,
                scores: response.data.data.scores[0].scores
            };
            vm.secondTermScore = {
                title: response.data.data.scores[1].term,
                scores: response.data.data.scores[1].scores
            };
            progression.done();
        }
    });
}
MyScoreController.inject = ['$http', 'progression'];

// 素质评定当前录入查看
function ActivityController($http, progression) {
    var vm = this;
    progression.start();

    // 获取当前登录人已填写的信息
    $http.get('/activities/myActivities').then(function (response) {
        if (response && response.data) {
            vm.activities = response.data.list;
        }
        progression.done();
    });
}
ActivityController.inject = ['$http', 'progression'];

// 素质评定分数录入
function QualityEditController($scope, $http, Notification, progression) {
    var vm = this;
    progression.start();

    vm.currentProjectId = 0;
    vm.projects = [{
        id: 0,
        title: '==请选择=='
    }];
    vm.currentItem = undefined;
    vm.myItems = [];
    var index = 1;
    // 获取分类信息
    $http.get('/projects/items').then(function (response) {
        if (response && response.data) {
            vm.projects = response.data.list;
            vm.currentProjectId = vm.projects[0].id;
            vm.currentItems = vm.projects[0].itemList;

            vm.allItems = [];
            vm.projects.forEach(function (e) {
                e.itemList.forEach(function (i) {
                    vm.allItems.push(i);
                });
            });
        }
        progression.done();
    });

    // 获取当前登录人已填写的信息
    $http.get('/activities/myActivities').then(function (response) {
        if (response && response.data) {
            vm.myItems = response.data.list;
            vm.myItems.forEach(function (e) {
                e.row = index++;
            });
        }
        progression.done();
    });

    // 添加一行
    vm.addRow = function () {
        if (vm.currentProjectId > 0 && vm.currentItem) {
            var obj = JSON.parse(vm.currentItem);
            vm.myItems.push({
                row: index++,
                item: {
                    id: obj.id,
                    title: obj.title
                }
            });
        } else {
            Notification('先选择要添加的类目');
        }
    };

    // 移除一行
    vm.removeRow = function (row) {
        for (var i = 0; i < vm.myItems.length; i++) {
            if (vm.myItems[i].row == row) {
                vm.myItems.splice(i, 1);
            }
        }
    };

    // 切换子项目列表
    vm.changeItems = function () {
        for (var i = 0; i < vm.projects.length; i++) {
            if (vm.projects[i].id == vm.currentProjectId) {
                vm.currentItems = vm.projects[i].itemList;
                break;
            }
        }
    };

    // 提交
    vm.submitActivity = function () {
        progression.start();
        $http.post('/activities', {
            json: JSON.stringify(vm.myItems)
        }).then(function (response) {
            progression.done();
        });
    };
}
QualityEditController.inject = ['$scope', '$http', 'Notification', 'progression'];

// 素质评定汇总
function QualityController($http, Notification, progression) {
    var vm = this;
    progression.start();

    // 获取当前登录人已填写的信息
    $http.get('/activities/myActivities').then(function (response) {
        if (response && response.data) {
            vm.activities = response.data.list;
        }
        progression.done();
    });

    progression.start();
    $http.get('/students/myScore').then(function (response) {
        if (response && response.data) {
            vm.firstTermScore = {
                title: response.data.data.scores[0].term,
                scores: response.data.data.scores[0].scores
            };
            vm.secondTermScore = {
                title: response.data.data.scores[1].term,
                scores: response.data.data.scores[1].scores
            };
            progression.done();
        }
    });
}
QualityController.inject = ['$http', 'Notification', 'progression'];

// 综测管理
function QualityManageController($http, $uibModal, progression) {
    var vm = this;
    vm.conf = {
        showScore: true,
        showDetail: true
    };
    progression.start();

    // 获取所有学生已填写的信息
    $http.get('/activities/qualities').then(function (response) {
        if (response && response.data) {
            vm.qualities = response.data.list;
            vm.firstSet = response.data.data.firstSet;
            vm.secondSet = response.data.data.secondSet;
            vm.qualities.forEach(function (e) {
                if (e.list && e.list.length > 0) {
                    e.firstTerm = e.list[0];
                }
                if (e.list && e.list.length > 1) {
                    e.secondTerm = e.list[1];
                }
            });
        }
        progression.done();
    });

    progression.start();
    // 获取分类信息
    $http.get('/projects/items').then(function (response) {
        if (response && response.data) {
            var arr = response.data.list;
            vm.allItems = [];

            for (var i = 0; i < arr.length; i++) {
                if (arr[i].itemList.length < 1) {
                    arr.splice(i--, 1);
                } else {
                    arr[i].itemList.forEach(function (e) {
                        vm.allItems.push(e);
                    });
                }
            }
            vm.projects = arr;

            // 按item分类显示
            vm.groupWithItem();
        }
        progression.done();
    });

    vm.groupWithItem = function () {
        // 获取本专业学生的活动分数
        $http.get('/activities/majorActivities').then(function (response) {
            if (response && response.data) {
                var arr = response.data.list;
                vm.allItems.forEach(function (item) {
                    if (!item.scores) {
                        item.scores = [];
                    }
                    arr.forEach(function (e) {
                        if (e.item.id === item.id) {
                            item.scores.push(e);
                        }
                    });
                });
            }
        });
    };

    vm.showOrHide = function () {
        if (vm.btn.show) {
            vm.btn.show = false;
            vm.btn.text = '显示成绩';
        } else {
            vm.btn.show = true;
            vm.btn.text = '隐藏成绩';
        }
    };

    vm.showModifyDlg = function (item, quality) {
        vm.items = [];
        if (item.scores) {
            item.scores.forEach(function (e) {
                if (e.student.schoolNum === quality.schoolNum) {
                    vm.items.push(e);
                }
            });
        }

        // 显示dlg
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'model.html',
            controller: 'ModalInstanceCtrl',
            controllerAs: 'vm',
            resolve: {
                items: function () {
                    return vm.items;
                },
                quality: function () {
                    return quality;
                },
                item: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function (_items) {
            $http.post('/activities/manage', {
                json: JSON.stringify(_items),
                schoolNum: quality.schoolNum,
                itemId: item.id
            }).then(function (response) {
                if (response.data && item.scores) {
                    for (var i = 0; i < item.scores.length; i++) {
                        if (item.scores[i].student.schoolNum === quality.schoolNum) {
                            item.scores.splice(i, 1);
                            i--;
                        }
                    }
                    if (response.data.list) {
                        response.data.list.forEach(function (e) {
                            item.scores.push(e);
                        });
                    }
                }
            });
        }, function () {

        });
    };
}
QualityManageController.inject = ['$http', '$uibModal', 'progression'];

angular.module('myApp')
    .controller('NavController', NavController)
    .controller('MenuController', MenuController)
    .controller('DashboardController', DashboardController)
    .controller('MyScoreController', MyScoreController)
    .controller('ActivityController', ActivityController)
    .controller('QualityEditController', QualityEditController)
    .controller('QualityController', QualityController)
    .controller('StudentListController', StudentListController)
    .controller('StudentDetailController', StudentDetailController)
    .controller('ProjectListController', ProjectListController)
    .controller('QualityManageController', QualityManageController)
    .controller('ModalInstanceCtrl', ModalInstanceCtrl);