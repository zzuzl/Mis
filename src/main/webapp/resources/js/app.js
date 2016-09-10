var myApp = angular.module('myApp', ['ng-admin']);
myApp.config(['NgAdminConfigurationProvider', 'RestangularProvider', function (nga, RestangularProvider) {

    /********************* 主配置 **********************/
    var admin = nga.application('Mis')
        .baseApiUrl('http://mis.zzuzl.cn/'); // main API endpoint
    var student = nga.entity('students')
        .identifier(nga.field('schoolNum'));
    student.listView().fields([
        nga.field('name'),
        nga.field('schoolNum')
    ]);

    admin.addEntity(student);
    nga.configure(admin);

    /********************* 主页配置 **********************/
    admin.dashboard(nga.dashboard()
        .template('dashboard.html')
    );

    /********************* 菜单配置 **********************/
    admin.menu(nga.menu()
        .addChild(nga.menu(student)
            .title('学生管理'))
        .icon('<span class="glyphicon glyphicon-tags"></span>')
    );

    /********************* 配置getList返回格式 **********************/
    RestangularProvider.addResponseInterceptor(function (data, operation, what, url, response) {
        if (operation == "getList") {
            response.totalCount = data.totalItem;
            data = data.list;
        }
        return data;
    });
}]);