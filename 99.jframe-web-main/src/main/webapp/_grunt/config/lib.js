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

module.exports = libs;