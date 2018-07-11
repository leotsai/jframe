var libs = require("./lib")
var appConfig = {

    getAppConfigs: function () {
        return {
            account: {
                login: [],
                register: [],
                findPassword: []
            }
        };
    }
}

module.exports = appConfig;