// 闭包执行一个立即定义的匿名函数
!function (factory) {
    // 模块化引入的名字/通过script标签引入的，挂在window对象上的名字
    var moduleName = 'API';

    // factory是一个函数，下面的self就是他的参数
    // Support three module loading scenarios
    if (typeof require === 'function' && typeof exports === 'object' && typeof module === 'object') {
        // [1] CommonJS/Node.js
        // [1] 支持在module.exports.abc,或者直接exports.abc
        var target = module[moduleName] || exports; // module.exports is for Node.js
        factory(target);
    } else if (typeof define === 'function' && define['amd']) {
        // [2] AMD 规范
        define([moduleName], factory);
    } else if (typeof define === 'function' && define['cmd']) {
        // [3] CMD 规范
        define(moduleName, function (require, exports, module) {
            module.exports = factory;
        });
    } else {
        // [4] No module loader (plain <script> tag) - put directly in global namespace
        factory(window[moduleName] = {});
    }
}(function (self, server) {
    function get(url, data, success, error) {
        if ($.isFunction(data)) {
            error = success;
            success = data;
            data = undefined;
        }
        $.get(url, data, function (res) {
            resolve(res, success, error);
        });
    }

    function post(url, data, success) {
        ajaxBody(url, data, success, 'POST');
    }

    function del(url, data, success) {
        ajaxBody(url, data, success, 'DELETE');
    }

    function ajaxBody(url, data, success, method) {
        if ($.isFunction(data)) {
            success = data;
            data = undefined;
        }
        $.ajax({
            url: url,
            type: method || 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                resolve(res, success);
            },
            error: function (xhr, status, error) {
                // 处理错误响应
            }
        });
    }

    function resolve(res, success, error) {
        if (res.code !== 0) {
            switch (res.code) {
                case -2: // 未登录
                    API.navigate2Login();
                    break;
                default:
            }
            if (error) {
                error(res);
            } else {
                layer.msg(res.msg);
            }
            return;
        }
        success && success(res.data);
    }

    self.get = get;
    self.post = post;
    self.del = del;
    self.navigate2Login = () => window.location.href = '/login';
    self.navigate2Index = () => window.location.href = '/';
    self.login = (params, success) => post('/account/login', params, success);
    self.changePassword = (params, success) => post('/account/password', params, success);
    self.getAccount = (success) => get('/account', success);
    self.saveAccount = (params, success) => post('/account', params, success);
    self.state = (success) => get('/account/state', success);
    self.statisticsOrder = (success) => get('/order/statistics', success);
    self.deleteOrder = (params, success) => del('/order', params, success);
    self.getOrder = (params, success) => get('/public/order/get', params, success);
    self.checkOrder = (params, success, error) => get('/public/order/check', params, success, error);
    self.fulfillOrder = (params, success, error) => get('/order/fulfill', params, success, error);
});
