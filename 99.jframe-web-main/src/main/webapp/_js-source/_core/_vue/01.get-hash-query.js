/**
 * Created by Leo on 2018/4/19.
 */
(function () {
    mvcApp.vue = {
        getHashQuery: function () {
            var query = {};
            var path = location + "";
            if (path.indexOf("#") === -1) {
                return query;
            }
            path = path.substr(path.indexOf('#') + 1);
            if (path.indexOf("?") === -1) {
                return query;
            }
            path = path.substr(path.indexOf('?') + 1);
            if (path.indexOf("#") > -1) {
                path = path.substr(0, path.indexOf("#"));
            }
            var keyValues = path.split('&');
            for (var i = 0; i < keyValues.length; i++) {
                var keyValue = keyValues[i].split('=');
                query[keyValue[0]] = keyValue[1];
            }
            return query;
        }
    };
})();