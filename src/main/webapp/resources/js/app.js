var myApp = angular.module('myApp', ['ng-admin']);
myApp.config(['NgAdminConfigurationProvider', 'RestangularProvider', function (nga, RestangularProvider) {

    /********************* 主配置 **********************/
    // create an admin application
    var admin = nga.application('Mis')
        .baseApiUrl('http://mis.zzuzl.cn/'); // main API endpoint
    // create a user entity
    // the API endpoint for this entity will be 'http://jsonplaceholder.typicode.com/users/:id
    var student = nga.entity('students');
    // set the fields of the user entity list view
    student.listView().fields([
        nga.field('name'),
        nga.field('schoolNum')
    ]);
    student.identifier(nga.field('schoolNum'));

    // add the user entity to the admin application
    admin.addEntity(student);
    // attach the admin application to the DOM and execute it
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
        console.log(data);
        return data;
    });


}]);