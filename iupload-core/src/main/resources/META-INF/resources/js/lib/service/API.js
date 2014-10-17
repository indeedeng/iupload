/*
 * Copyright (C) 2014 Indeed Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
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