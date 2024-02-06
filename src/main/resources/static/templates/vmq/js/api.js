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
                    window.location.href = "/login";
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

    // 补单失败时执行的回调
    function fulfillOrderError(res) {
        if (res.code === -5) { // 补单失败执行回调
            layer.alert(res.data || 'null', {icon: 1}, function (i) {
                layer.close(i);
            });
            return;
        }
        // 其它异常直接提示即可
        layer.msg(res.msg);
    }

    self.get = get;
    self.post = post;
    self.del = del;
    self.login = (params, success) => post('/admin/login', params, success);
    self.account = (success) => get('/admin/account', success);
    self.state = (success) => get('/admin/state', success);
    self.statisticsOrder = (success) => get('/order/statistics', success);
    self.deleteOrder = (params, success) => del('/order', params, success);
    self.fulfillOrder = (params, success) => get('/order/fulfill', params, success, fulfillOrderError);
});
