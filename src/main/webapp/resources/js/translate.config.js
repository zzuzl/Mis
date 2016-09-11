angular.module('myApp')
    .config(['$translateProvider', function ($translateProvider) {
        $translateProvider.translations('zh-CN', {
            'BACK': '返回',
            'DELETE': '删除',
            'CREATE': '新建',
            'EDIT': '修改',
            'EXPORT': '导出',
            'ADD_FILTER': '添加查询',
            'SEE_RELATED': 'Voir les {{ entityName }} liés',
            'LIST': '列表',
            'SHOW': '查看',
            'SAVE': '保存',
            'N_SELECTED': '{{ length }} 已选择',
            'ARE_YOU_SURE': '确定？',
            'YES': '是',
            'NO': '否',
            'FILTER_VALUES': '过滤值',
            'CLOSE': '关闭',
            'CLEAR': '清空',
            'CURRENT': '当前',
            'REMOVE': '移除',
            'ADD_NEW': '添加 {{ name }}',
            'BROWSE': '浏览',
            'N_COMPLETE': '{{ progress }}% 已完成',
            'CREATE_NEW': '新建',
            'SUBMIT': '提交',
            'SAVE_CHANGES': '保存修改',
            'BATCH_DELETE_SUCCESS': '批量删除成功',
            'DELETE_SUCCESS': '删除成功',
            'ERROR_MESSAGE': '错误 (code: {{ status }})',
            'INVALID_FORM': '无效的数据',
            'CREATION_SUCCESS': '新建成功',
            'EDITION_SUCCESS': '修改成功',
            'ACTIONS': '操作',
            'PAGINATION': '<strong>{{ begin }}</strong> - <strong>{{ end }}</strong> 共 <strong>{{ total }}</strong>',
            'NO_PAGINATION': '无分页',
            'PREVIOUS': '上一页',
            'NEXT': '下一页',
            'DETAIL': '详细',
            'STATE_CHANGE_ERROR': '页面跳转错误: {{ message }}',
            'NOT_FOUND': '页面未找到 404',
            'NOT_FOUND_DETAILS': '未找到请求的数据'
        });
        $translateProvider.preferredLanguage('zh-CN');
    }]);