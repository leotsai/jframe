var outputRoot = '../../../../target/jframe-web-main-mvc/modules/';
var outputSource = '../modules/';
var folder = '../_js-source/';
var libs = {
    listPager: folder + "_libs/listpager.js",
    grid: folder + "_libs/Grid.js",
    fileUpload: folder + "_libs/FileUpload.js",
    loginManager: folder + "_libs/LoginManager.js",
    cascadingSelector: folder + "_libs/CascadingSelector.js",
    hashQuery: folder + "_libs/HashQuery.js",
    app: {
        weixinRecharger: folder + "_libs/app/WeixinRecharger.js",
        weixinJsSdk: folder + "_libs/app/WeixinJsSdk.js",
        likereader: folder + "_libs/app/LikeReader.js",
        touchManager: folder + "_libs/app/TouchManager.js",
        slideAction: folder + "_libs/app/SlideAction.js",
        locationPicker: folder + "_libs/app/LocationPicker.js",
        numbersInput: folder + "_libs/app/numbersInput.js",
        smsCaptchaSender: folder + "_libs/app/SmsCaptchaSender.js",
        listScroller: folder + "_libs/app/ListScroller.js",
        multiAjaxCaller: folder + "_libs/app/MultiAjaxCaller.js"
    },
    admin: {
        weixinMenu: folder + "_libs/admin/WeixinMenuManager.js"
    },
    pc: {
        slider: folder + "_libs/pc/slider.js",
        iphonePreviewer: folder + "_libs/pc/IphonePreviewer.js",
        imageZoomer: folder + "_Libs/pc/ImageZoomer.js",
        timeframeSelector: folder + "_libs/pc/TimeframeSelector.js",
        pageDataPointViewer: folder + "_libs/pc/PageDataPointViewer.js",
        combobox: folder + "_libs/pc/Combobox.js"
    }
};

libs.webImageUploader = [libs.fileUpload, folder + "_libs/WebImageUploader.js"];
libs.vueGrid = [libs.grid, folder + "_libs/VueGrid.js"];
libs.vueListPager = [libs.listPager, folder + "_libs/vuelistpager.js"];
libs.app.loopshow = [libs.app.touchManager, folder + "_libs/app/LoopShow.js"];
libs.app.weixinImageUploader = [libs.app.weixinJsSdk, folder + "_libs/app/WeixinImageUploader.js"];
libs.pc.imageViewer = [libs.pc.slider, libs.pc.imageZoomer, folder + "_libs/pc/ImageViewer.js"];


function getDemoConfigs() {
    return {
        home: {
            index: [libs.fileUpload, libs.webImageUploader, libs.cascadingSelector]
        },
        account: {
            index: [],
            login: [libs.loginManager],
            register: [libs.fileUpload, libs.webImageUploader]
        }
    };
}
function getImportedLibs(libList) {
    var list = [];

    function addAll(itemOrList) {
        if (itemOrList instanceof Array) {
            for (var i = 0; i < itemOrList.length; i++) {
                addAll(itemOrList[i]);
            }
        } else {
            if (list.indexOf(itemOrList) === -1) {
                list.push(itemOrList);
            }
        }
    }
    addAll(libList);
    return list;
}
module.exports = function (grunt) {
    var pages = {
        demo: getDemoConfigs()
    };

    var concat = {};
    for (var areaName in pages) {
        var area = pages[areaName];
        for (var moduleName in area) {
            var module = area[moduleName];
            for (var page in module) {
                var taskName = areaName + '_' + moduleName + '_' + page;
                var output1 = outputRoot + areaName + '/js/' + moduleName + '/' + page + '.js';
                var output2 = outputSource + areaName + '/js/' + moduleName + '/' + page + '.js';
                var inputs = getImportedLibs(module[page]);
                inputs.push(folder + areaName + '/' + moduleName + '/' + page + '/*.js');
                var task = {files: {}};
                task.files[output1] = inputs;
                task.files[output2] = inputs;
                concat[taskName] = task;
            }
        }
    }

    var coreFolder = folder + '_core/*.js';

    function subFolder(name) {
        return folder + '_core/' + name + '/*.js';
    }
    var core = {files: {}};
    core.files[outputRoot + "demo/js/core.js"] = [coreFolder, subFolder("_pc")];

    core.files[outputSource + "demo/js/core.js"] = [coreFolder, subFolder("_pc")];

    concat.core = core;

    var options = {
        concat: concat,
        uglify: concat,
        watch: {
            debug: {
                files: [
                    folder + '*.js',
                    folder + '*/*.js',
                    folder + '*/*/*.js',
                    folder + '*/*/*/*.js',
                    folder + '*/*/*/*/*.js',
                    folder + '*/*/*/*/*/*.js',
                    folder + '*/*/*/*/*/*/*.js'],
                tasks: ['debug']
            }
        }
    };
    grunt.initConfig(options);

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('debug', ['concat']);
    grunt.registerTask('release', ['uglify']);
};