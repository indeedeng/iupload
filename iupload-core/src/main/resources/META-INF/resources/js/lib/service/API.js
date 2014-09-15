function API($http) {
    var defaultErrorHandler = function () {
    };
    var defaultSuccessHandler = function () {
    };

    this.setDefaultSuccessHandler = function (callback) {
        defaultSuccessHandler = callback;
    };

    this.setDefaultErrorHandler = function (callback) {
        defaultErrorHandler = callback;
    };

    this.get = function (url, config) {
        return $http.get(url, config)
            .success(defaultSuccessHandler)
            .error(defaultErrorHandler);
    };

    this.post = function (url, data, config) {
        return $http.post(url, data, config)
            .success(defaultSuccessHandler)
            .error(defaultErrorHandler);
    };

    this.delete = function (url) {
        return $http.delete(url)
            .success(defaultSuccessHandler)
            .error(defaultErrorHandler);
    };

}

iupload.service('API', ['$http', API]);