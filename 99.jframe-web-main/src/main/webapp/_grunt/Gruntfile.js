var outputRoot = '../../../../target/jframe-web-main-mvc/modules/';
var outputSource = '../modules/';
var folder = '../_js-source/';

var adminConfig = require("./config/adminConfig");
var appConfig = require("./config/appConfig");
var pcConfig = require("./config/pcConfig");

module.exports = function (grunt) {
    var pages = {
        admin: adminConfig.getAdminConfigs(),
        app: appConfig.getAppConfigs(),
        pcConfig: pcConfig.getPcConfigs()
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
    core.files[outputRoot + "admin/js/core.js"] = [coreFolder, subFolder("_pc")];
    core.files[outputRoot + "app/js/core.js"] = [coreFolder, subFolder("_weixin")];

    core.files[outputSource + "admin/js/core.js"] = [coreFolder, subFolder("_pc")];
    core.files[outputSource + "app/js/core.js"] = [coreFolder, subFolder("_weixin")];

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