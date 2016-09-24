/*var myApp = angular.module('myApp', ['ng-admin', 'ui.bootstrap']);

 myApp.config(['NgAdminConfigurationProvider', function (nga) {
 student.showView()
 .title('详细信息')
 .fields([
 nga.field('schoolNum').label('学号'),
 nga.field('name').label('姓名'),
 nga.field('grade').label('年级'),
 nga.field('sex', 'choice').label('性别')
 .choices([
 {label: '男', value: '男'},
 {label: '女', value: '女'}
 ]),
 nga.field('classCode').label('班号'),
 nga.field('birthday', 'date').label('出生日期'),
 nga.field('nation').label('民族'),
 nga.field('politicalStatus').label('政治面貌'),
 nga.field('idNo').label('身份证号'),
 nga.field('accountNo').label('光大卡号'),
 nga.field('originAddress').label('生源地'),
 nga.field('homeAddress').label('家庭住址'),
 nga.field('entranceDate', 'date').label('入学日期'),
 nga.field('schoolYear', 'number').label('学制'),
 nga.field('residence', 'choice').label('户口')
 .choices([
 {label: '农村户口', value: '农村户口'},
 {label: '城市户口', value: '城市户口'}
 ]),
 nga.field('phone', 'number').label('手机号'),
 nga.field('qq', 'number').label('QQ号'),
 nga.field('email', 'email').label('邮箱'),
 nga.field('parentPhone', 'number').label('家庭电话'),
 nga.field('dormNo').label('宿舍号'),
 nga.field('direction').label('专业方向')
 ]);

 student.editionView()
 .title('修改信息')
 .fields([
 nga.field('schoolNum').label('学号').editable(false),
 nga.field('name').label('姓名').editable(false),
 nga.field('grade').label('年级').editable(false),
 nga.field('sex', 'choice').label('性别').editable(false)
 .choices([
 {label: '男', value: '男'},
 {label: '女', value: '女'}
 ]),
 nga.field('classCode').label('班号').editable(false),
 nga.field('birthday', 'date').label('出生日期'),
 nga.field('nation').label('民族'),
 nga.field('politicalStatus').label('政治面貌'),
 nga.field('idNo').label('身份证号'),
 nga.field('accountNo').label('光大卡号'),
 nga.field('originAddress').label('生源地'),
 nga.field('homeAddress').label('家庭住址'),
 nga.field('entranceDate', 'date').label('入学日期'),
 nga.field('schoolYear', 'number').label('学制'),
 nga.field('residence', 'choice').label('户口')
 .choices([
 {label: '农村户口', value: '农村户口'},
 {label: '城市户口', value: '城市户口'}
 ])
 .attributes({placeholder: '选择户口类型'}),
 nga.field('phone', 'number').label('手机号')
 .validation({minlength: 6, maxlength: 11}),
 nga.field('qq', 'number').label('QQ号')
 .validation({minlength: 6, maxlength: 12}),
 nga.field('email', 'email').label('邮箱'),
 nga.field('parentPhone', 'number').label('家庭电话')
 .validation({minlength: 6, maxlength: 11}),
 nga.field('dormNo').label('宿舍号'),
 nga.field('direction', 'choice').label('专业方向')
 .choices([
 {label: '系统开发方向', value: '1'},
 {label: '企业管理方向', value: '0'}
 ])
 .attributes({placeholder: '选择专业方向'})
 ]);

 project.listView()
 .title('项目列表')
 .infinitePagination(true)
 .fields([
 nga.field('id').label('id'),
 nga.field('title').label('标题'),
 nga.field('majorCode').label('专业'),
 nga.field('grade').label('年级'),
 nga.field('year').label('年份'),
 nga.field('maxScore').label('最大分值'),
 nga.field('minScore').label('最小分值')
 ])
 .sortField('year')
 .sortDir('DESC')
 .listActions(['show', 'edit', 'delete']);

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
// 2、编写数据表格指令，封装列表请求

angular.module('myApp', ['ui.router', 'ui-notification', 'ui.bootstrap'])
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
            templateUrl: '/resources/pages/dashboard.html'
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
        }).state('projects', {
            url: '/projects',
            template: '<ui-view/>',
            abstract: true
        }).state('projects.list', {
            url: '/list',
            templateUrl: '/resources/pages/projects/list.html',
            controller: 'StudentController as vm'
        }).state('qualityManage', {
            url: '/qualityManage',
            templateUrl: '/resources/pages/qualityManage.html',
            controller: 'QualityManageController as vm'
        });

        $urlRouterProvider.otherwise('/dashboard');
    });