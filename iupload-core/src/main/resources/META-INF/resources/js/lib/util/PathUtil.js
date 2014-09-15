var PathUtil = {
    parse: function (path, pattern) {
        var values = path.match(/(\w+)/g);
        var keys = pattern.match(/(\w+)/g);
        var result = {};
        if (!values || !pattern || values.length != keys.length) {
            return {};
        }
        for (var i in keys) {
            result[keys[i]] = values[i];
        }
        return result;
    }
};