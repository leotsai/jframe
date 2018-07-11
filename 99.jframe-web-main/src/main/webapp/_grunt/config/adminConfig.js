var libs = require("./lib")
var AdminConfig = {

    getAdminConfigs: function () {
        return {
            home: {
                index: [libs.fileUpload, libs.webImageUploader, libs.cascadingSelector]
            },
            account: {
                login: [],
                register: [],
                findPassword: []
            },
            employee: {
                index: [libs.grid, libs.hashQuery],
                detail: [],
                edit: []
            }
        };
    }

}

module.exports = AdminConfig;