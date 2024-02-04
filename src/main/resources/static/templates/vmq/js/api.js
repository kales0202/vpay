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
    function get(url, data, callback) {
        if ($.isFunction(data)) {
            callback = data;
            data = undefined;
        }
        $.get(url, data, function (res) {
            resolve(res, callback);
        });
    }

    function post(url, data, callback) {
        ajaxBody(url, data, callback, 'POST');
    }

    function del(url, data, callback) {
        ajaxBody(url, data, callback, 'DELETE');
    }

    function ajaxBody(url, data, callback, method) {
        if ($.isFunction(data)) {
            callback = data;
            data = undefined;
        }
        $.ajax({
            url: url,
            type: method || 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                resolve(res, callback);
            },
            error: function (xhr, status, error) {
                // 处理错误响应
            }
        });
    }

    function resolve(res, callback) {
        if (res.code !== 0) {
            switch (res.code) {
                case -2: // 未登录
                    window.location.href = "/login";
                    break;
                default:
            }
            layer.msg(res.msg);
            return;
        }
        callback && callback(res.data);
    }

    self.get = get;
    self.post = post;
    self.del = del;

    self.login = (params, callback) => post('/admin/login', params, callback);
    self.statisticsOrder = (callback) => get('/order/statistics', callback);
    self.deleteOrder = (params, callback) => del('/order', params, callback);
});