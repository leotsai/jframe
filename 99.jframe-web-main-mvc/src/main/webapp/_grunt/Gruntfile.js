var outputRoot = '../modules/';
var folder = '../_js-source/';
var libs = {
    listPager: folder + "_libs/listpager.js",
    grid: folder + "_libs/Grid.js",
    fileUpload: folder + "_libs/FileUpload.js",
    webImageUploader: folder + "_libs/WebImageUploader.js",
    arrayExtensions: folder + "_libs/ArrayExtensions.js",
    dateExtensions: folder + "_libs/DateExtensions.js",
    app: {
        weixinRecharger: folder + "_libs/app/WeixinRecharger.js",
        weixinJsSdk: folder + "_libs/app/WeixinJsSdk.js",
        weixinImageUploader: folder + "_libs/app/WeixinImageUploader.js",
        likereader: folder + "_libs/app/LikeReader.js",
        loopshow: folder + "_libs/app/LoopShow.js"
    },
    admin: {
        weixinMenu: folder + "_libs/admin/WeixinMenuManager.js"
    },
    pc: {
        slider: folder + "_libs/pc/slider.js",
        iphonePreviewer: folder + "_libs/pc/IphonePreviewer.js",
        imageZoomer: folder + "_Libs/pc/ImageZoomer.js",
        imageViewer: folder + "_libs/pc/ImageViewer.js",
        timeframeSelector: folder + "_libs/pc/TimeframeSelector.js"
    }
};

function getAdminConfigs() {
    return {
        weixin: {
            menuManager: [libs.admin.weixinMenu],
            "qr-generate": [],
            "get-resource": [libs.grid],
            accessToken: []
        },
        user: {
            index: [libs.grid]
        },
        role: {
            index: [libs.grid]
        },
        banner: {
            index: [libs.fileUpload, libs.webImageUploader, libs.grid]
        },
        setting: {
            index: []
        },
        article: {
            index: [libs.grid, libs.pc.iphonePreviewer, libs.fileUpload, libs.webImageUploader],
            editor: []
        },
        message: {
            index: [libs.grid],
            weixin: [libs.grid]
        },
        datachange: {
            index: [libs.dateExtensions, libs.pc.timeframeSelector]
        },
        body: {
            index: [libs.fileUpload, libs.webImageUploader]
        },
        therapy: {
            index: [libs.grid]
        }
    };
}


function getAppConfigs() {
    return {
        account: {
            login: [],
            "validate-login-key": [],
            "login-by-password": [],
            changePassword:[]
        },
        home: {
            index: [libs.app.loopshow]
        },
        profile: {
            index: [],
            manage: [],
            managePhoto: [libs.app.weixinJsSdk, libs.app.weixinImageUploader]
        },
        article: {
            index: [libs.listPager],
            details: [libs.app.likereader, libs.app.weixinJsSdk]
        },
        therapy: {
            index: []
        },
        order: {
            details: [libs.app.weixinJsSdk, libs.app.weixinRecharger],
            paid: []
        }
    };
};

function getPublicConfigs() {
    return {
        home: {
            index: []
        },
        account: {
            index: [],
            login: [],
            register: [libs.fileUpload, libs.webImageUploader]
        }
    };
};

module.exports = function (grunt) {

    var pages = {
        admin: getAdminConfigs(),
        app: getAppConfigs(),
        'pc': getPublicConfigs()
    };

    var concat = {};
    for (var areaName in pages) {
        var area = pages[areaName];
        for (var moduleName in area) {
            var module = area[moduleName];
            for (var page in module) {
                var taskName = areaName + '_' + moduleName + '_' + page;
                var output = outputRoot + areaName + '/js/' + moduleName + '/' + page + '.js';
                var inputs = module[page];
                inputs.push(folder + areaName + '/' + moduleName + '/' + page + '/*.js');
                var task = { files: {} };
                task.files[output] = inputs;
                concat[taskName] = task;
            }
        }
    }

    var core = { files: {} };
    core.files[outputRoot + "pc/js/core.js"] = [folder + '_core/*.js', folder + '_core/pc/*.js'];
    core.files[outputRoot + "admin/js/core.js"] = [folder + '_core/*.js', folder + '_core/pc/*.js'];
    core.files[outputRoot + "app/js/core.js"] = [folder + '_core/*.js', folder + '_core/app/*.js'];
    core.files[outputRoot + "admin/js/libs.ImageViewer.js"] = [libs.pc.slider, libs.pc.imageZoomer, libs.pc.imageViewer];

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