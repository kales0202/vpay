// 闭包执行一个立即定义的匿名函数
!function (factory) {
    // 模块化引入的名字/通过script标签引入的，挂在window对象上的名字
    var moduleName = 'Util';

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
    function formatTime(time) {
        time = time || new Date();
        if (typeof time === 'number') {
            time = new Date(time);
        }
        return time.toISOString().slice(0, 19).replace('T', ' ');
    }

    self.formatTime = formatTime;
});