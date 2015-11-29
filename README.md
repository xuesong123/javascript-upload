javascript-upload
===================

javascript upload

这是一个js的上传组件，它只提供最基本的上传功能，不包括任何UI组件。所有的UI交互细节可以完全自定制。

适用的需求：
如果你只需要一个基本的上传组件，它不需要包括任何的UI，你需要完全自定义交互细节，那么它适合你。
如果你只是需要一个上传组件，并且需要一套已经设计好人交互流程，那么它不适合你。

支持的功能：
1. h5上传和flash上传
2. 分段上传, 仅h5支持, flash不支持
3. 图片预览, h5&flash

特点：
1. 极简的API设计, 全部事件只有5个
2. UI交互完全自定制(这也意味着你必须自己设计你的交互过程)


API Example
===================
var fileUpload = FileUploadFactory.create({
    "id": "uploadButton", // 页面dom元素或者元素的id
    "accept": "*",        // 允许选择的文件类型
    "multiple": true,     // 是否允许多选
    "partSize": 5 * 10485760, // 分段上传每段的大小
    "dataType": "json",       // 返回的数据类型
    "swfURL": "/h5/flash/uploader.swf?a=3", // flash文件的地址
    "select": function(files) {
        // 当用户选择文件之后调用
        for(var i = 0; i < files.length; i++) {
            var file = files[i];
            var extension = file.extension;

            // 可以分别使用如下两种交互
            // 1. 立即上传
            // this.upload(file.id, "/upload/test1.html", "fileData", {"test1": "a1", "test2": "a2"});

            // 2. 显示预览图, load成功之后调用complete, 必须在complete里面显示预览图
            // this.load(file.id);
        }
    },
    "complete": function(file) {
        // 当调用load加载数据成功之后调用, 显示预览图, flash方式也支持该方法
        new Image().src = this.getDataURL(file.id);
    },
    "progress": function(fileId, loaded, total, speed, seconds) {
        // 上传进度回调
        // 参数含义: 文件ID, 已经上传, 文件总大小, 速度, 用时
    },
    "success": function(file, data) {
        // 上传成功调用
    },
    "error": function(file) {
        // 上传成功调用
    }
});
