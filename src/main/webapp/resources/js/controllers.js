angular.module('myApp').controller('ModalInstanceCtrl', ModalInstanceCtrl);

// 模态框
function ModalInstanceCtrl($http, $uibModalInstance, items) {
    var vm = this;
    vm.items = items;
    console.log(items);

    /*// 获取当前登录人已填写的信息
     $http.get('/activities/myActivities').then(function (response) {
     if (response && response.data) {
     vm.activities = response.data.list;
     }
     });*/

    vm.ok = function () {
        console.log("ok");
        $uibModalInstance.close(vm.items);
    };

    vm.cancel = function () {
        console.log("cancel");
        $uibModalInstance.dismiss('cancel');
    };
}
ModalInstanceCtrl.inject = ['$http', '$uibModalInstance', 'items'];

// 我的成绩单
function myScoreController($http, notification, progression) {
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
myScoreController.inject = ['$http', 'notification', 'progression'];

// 素质评定当前录入查看
function ActivityController($http, notification, progression) {
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
ActivityController.inject = ['$http', 'notification', 'progression'];


// 素质评定分数录入
function QualityEditController($http, notification, progression) {
    var vm = this;
    progression.start();

    vm.currentProjectId = 0;
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
            notification.log('先选择要添加的类目', {addnCls: 'humane-flatty-error'});
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
QualityEditController.inject = ['$http', 'notification', 'progression'];

// 素质评定汇总
function QualityController($http, notification, progression) {
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
QualityController.inject = ['$http', 'notification', 'progression'];

// 综测管理
function QualityManageController($http, $uibModal, notification, progression) {
    var vm = this;
    vm.btn = {
        show: true,
        text: "隐藏成绩"
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

    vm.showModifyDlg = function (scores, quality) {
        vm.items = [];
        if (scores) {
            scores.forEach(function (e) {
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
                }
            }
        });

        modalInstance.result.then(function (selectedItem) {
            // vm.selected = selectedItem;
        }, function () {

        });
    };
}
QualityManageController.inject = ['$http', '$uibModal', 'notification', 'progression'];


