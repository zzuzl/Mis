var myApp = angular.module('myApp', ['ng-admin']);

myApp.config(['NgAdminConfigurationProvider', function (nga) {

    /********************* 主配置 **********************/
    var admin = nga.application('Mis')
        .baseApiUrl('http://mis.zzuzl.cn/'); // main API endpoint
    var student = nga.entity('students')
        .identifier(nga.field('schoolNum'));
    var project = nga.entity('projects');
    var item = nga.entity('items');
    admin.addEntity(student)
        .addEntity(project)
        .addEntity(item);

    /********************* student配置 **********************/
    student.listView()
        .title('学生列表')
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

    /********************* project配置 **********************/
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

    project.editionView()
        .title('修改信息')
        .fields(project.creationView().fields());

    project.showView()
        .title('详细信息')
        .fields(project.creationView().fields());

    // todo list
    // 1、小项目超出5个报错，notifitation not found
    // 2、列表点击标题不能排序的问题
    // 3、添加分数时防止分数超出范围

    /********************* 主页配置 **********************/
    admin.dashboard(nga.dashboard()
        .template('dashboard.html')
    );

    /********************* 菜单配置 **********************/
    admin.menu(nga.menu()
        .addChild(nga.menu(student).title('学生管理'))
        .icon('<span class="glyphicon glyphicon-tags"></span>')
        .addChild(nga.menu(project).title('项目管理'))
        .icon('<span class="glyphicon glyphicon-tags"></span>')
        .addChild(nga.menu().title('我的成绩单').link('/myScore')
            .active(function (path) {
                return path.indexOf('/myScore') === 0;
            }))
        .icon('<span class="glyphicon glyphicon-tags"></span>')
        .addChild(nga.menu().title('我的综测')
            .addChild(nga.menu().title('查看').link('/listActivity')
                .active(function (path) {
                    return path.indexOf('/listActivity') === 0;
                }))
            .addChild(nga.menu().title('修改').link('/qualityEdit')
                .active(function (path) {
                    return path.indexOf('/qualityEdit') === 0;
                }))
            .addChild(nga.menu().title('汇总').link('/myQuality')
                .active(function (path) {
                    return path.indexOf('/myQuality') === 0;
                }))
        )
        .icon('<span class="glyphicon glyphicon-tags"></span>')
    );

    nga.configure(admin);
}]);