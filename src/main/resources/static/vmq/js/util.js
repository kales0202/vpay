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
    Date.prototype.format = function (format) {
        /*
         * eg:format="YYYY-MM-dd hh:mm:ss";
         */
        var o = {
            "M+": this.getMonth() + 1, // month
            "d+": this.getDate(), // day
            "H+": this.getHours(), // hour
            "m+": this.getMinutes(), // minute
            "s+": this.getSeconds(), // second
            "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
            "S": this.getMilliseconds()
            // millisecond
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }

    function formatTime(time) {
        if (!time) {
            return '';
        }
        try {
            if (typeof time === 'number') {
                time = new Date(time);
            } else if (typeof time === 'string') {
                time = new Date(parseInt(time));
            }
            return time.format('yyyy-MM-dd HH:mm:ss');
        } catch (e) {
            console.error('时间格式化出错, time = ' + time, e);
            return '';
        }
    }

    /**
     * 将指定格式的日期时间字符串转换为时间戳
     * @param {string} time - 日期时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     * @return {number} 时间戳 (单位：毫秒)
     */
    function toTimestamp(time) {
        // 检查输入字符串是否符合预期格式
        var regex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;
        if (!regex.test(time)) {
            throw new Error('日期时间字符串格式不正确，应为"yyyy-MM-dd HH:mm:ss"');
        }

        // 解析日期时间字符串为Date对象
        var parts = time.split(/[- :]/);
        var year = parseInt(parts[0], 10);
        var month = parseInt(parts[1], 10) - 1; // 月份是从0开始的
        var day = parseInt(parts[2], 10);
        var hour = parseInt(parts[3], 10);
        var minute = parseInt(parts[4], 10);
        var second = parseInt(parts[5], 10);

        var date = new Date(year, month, day, hour, minute, second);

        // 返回时间戳
        return date.getTime();
    }


    var ZXingReader;

    function decodeQRCode(url, callback) {
        if (!ZXingReader) {
            ZXingReader = new window.ZXingBrowser.BrowserQRCodeReader();
        }
        ZXingReader.decodeFromImageUrl(url).then(callback).catch((err) => {
            console.error(err)
        })
    }

    var ZxingWriter;

    function encodeQRCode($container, text) {
        if (!ZxingWriter) {
            ZxingWriter = new window.ZXingBrowser.BrowserQRCodeSvgWriter();
        }

        if (!$container || $container.length === 0 || !text) {
            $container.attr("data-code", "");
            $container.html("");
            return;
        }
        var element = ZxingWriter.write(text, 200, 200, null);
        $container.attr("data-code", text);
        $container.html(element);
    }

    function getObjectURL(file) {
        let url = null;
        if (window.createObjectURL !== undefined) { // basic
            url = window.createObjectURL(file);
        } else if (window.webkitURL !== undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file);
        } else if (window.URL !== undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file);
        }
        return url;
    }

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)
            return decodeURI(r[2]);
        return null;
    }

    self.formatTime = formatTime;
    self.toTimestamp = toTimestamp;
    self.decodeQRCode = decodeQRCode;
    self.encodeQRCode = encodeQRCode;
    self.getObjectURL = getObjectURL;
    self.getQueryString = getQueryString;
});