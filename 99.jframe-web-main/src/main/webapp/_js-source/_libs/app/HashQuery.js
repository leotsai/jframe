(function() {
    if (window.HashQuery) {
        return;
    }
    window.HashQuery = function() {

    };

    HashQuery.prototype = {
        parseFromLocation: function() {
            if (location.hash === '' || location.hash.length === 1) {
                return;
            }
            var properties = location.hash.substr(1).split('|');
            var index = 0;
            for (var p in this) {
                if (!this.hasOwnProperty(p) || typeof this[p] != 'string') {
                    continue;
                }

                if (index < properties.length) {
                    this[p] = properties[index];
                    if (this[p] === '-') {
                        this[p] = '';
                    }
                }
                index++;
            }
        },
        updateLocation: function() {
            var properties = [];
            for (var p in this) {
                if (!this.hasOwnProperty(p) || typeof this[p] != 'string') {
                    continue;
                }
                var value = this[p];
                properties.push(value === '' ? '-' : value);
            }
            var url = location.origin + location.pathname + location.search + "#" + properties.join('|');
            location.replace(url);
        }
    };
})();