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

    function post(url, data, success, error) {
        ajaxBody(url, data, success, error, 'POST');
    }

    function put(url, data, success, error) {
        ajaxBody(url, data, success, error, 'PUT');
    }

    function del(url, data, success, error) {
        ajaxBody(url, data, success, error, 'DELETE');
    }

    function delById(url, data, success, error) {
        if (!data.id) {
            console.error("调用删除接口参数错误，缺少id, url = " + url);
            return;
        }
        ajaxBody(url + "/" + data.id, success, error, 'DELETE');
    }

    function ajaxBody(url, data, success, error, method) {
        if ($.isFunction(data)) {
            method = error;
            error = success;
            success = data;
            data = undefined;
        }
        $.ajax({
            url: url,
            type: method || 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                resolve(res, success, error);
            },
            error: function (xhr, status, error) {
                // 处理错误响应
                error && error({code: xhr.status, msg: xhr.responseText});
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
    self.put = put;
    self.del = del;
    self.delById = delById;
    self.navigate2Login = () => window.location.href = '/login';
    self.navigate2Index = () => window.location.href = '/';
    self.login = (params, success, error) => post('/account/login', params, success, error);
    self.changePassword = (params, success, error) => post('/account/password', params, success, error);
    self.getAccount = (success, error) => get('/account', success, error);
    self.saveAccount = (params, success, error) => post('/account', params, success, error);
    self.state = (success, error) => get('/account/state', success, error);
    self.statisticsOrder = (success, error) => get('/order/statistics', success, error);
    self.deleteOrder = (params, success, error) => del('/order', params, success, error);
    self.getOrder = (params, success, error) => get('/public/order/get', params, success, error);
    self.checkOrder = (params, success, error) => get('/public/order/check', params, success, error);
    self.fulfillOrder = (params, success, error) => get('/order/fulfill', params, success, error);

    // 付款码相关接口
    self.savePayCode = (params, success, error) => post('/pay-code', params, success, error);
    self.modifyPayCode = (params, success, error) => put('/pay-code', params, success, error);
    self.deletePayCode = (params, success, error) => delById('/pay-code', params, success, error);
});
