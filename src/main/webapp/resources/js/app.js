var myApp = angular.module('myApp', ['ng-admin']);

myApp.config(['NgAdminConfigurationProvider', function (nga) {

    /********************* 主配置 **********************/
    var admin = nga.application('Mis')
        .baseApiUrl('http://mis.zzuzl.cn/'); // main API endpoint
    var student = nga.entity('students')
        .identifier(nga.field('schoolNum'));
    admin.addEntity(student);

    /********************* student配置 **********************/
    student.listView()
        .title('学生列表')
        .description('所有学生列表')
        .infinitePagination(false) // 下拉刷新
        .fields([
            nga.field('schoolNum').label('学号'),
            nga.field('name').label('姓名'),
            nga.field('grade').label('年级'),
            nga.field('sex').label('性别'),
            nga.field('classCode').label('班号')
        ])
        .sortField('schoolNum')
        .sortDir('ASC')
        .listActions(['show', 'edit', 'delete'])
        .filters([
            nga.field('schoolNum')
                .label('学号')
                .pinned(true),
            nga.field('name')
                .label('姓名')
                .pinned(true)
        ]);

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

    nga.configure(admin);
}]);