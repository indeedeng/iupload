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
 iupload.controller('FileController', ['$scope', '$location', 'API', 'ModalManager', function ($scope, $location, API, modalManager) {

    $scope.files = [];
    $scope.indexes = [];
    $scope.repositories = [];
    $scope.currentRepositoryName = false;
    $scope.currentIndexName = false;
    $scope.modalManager = modalManager;


    var handleAjaxError = function (data) {
        if (data.hasOwnProperty("errors") && data.errors.hasOwnProperty("message")) {
            $scope.modalManager.openErrorDialog(data.errors.message);
        } else {
            $scope.modalManager.openErrorDialog(data.errorThrown);
        }
    };
    API.setDefaultErrorHandler(handleAjaxError);

    var findRepositoryByName = function (repositoryName) {
        var result = $scope.repositories.filter(function (repository) {
            return repository.name == repositoryName
        });
        if (result.length > 0) {
            return result[0];
        } else {
            return null;
        }
    };

    var findIndexByName = function (indexName) {
        var result = $scope.indexes.filter(function (index) {
            return index.name == indexName
        });
        if (result.length > 0) {
            return result[0];
        } else {
            return null;
        }
    };

    $scope.selectRepository = function (repositoryName) {
        if ($scope.repositories.length == 0) {
            return;
        }
        if (!repositoryName) {
            repositoryName = $scope.repositories[0].name;
        }
        var oldRepository = findRepositoryByName($scope.currentRepositoryName);
        if (oldRepository) {
            oldRepository.active = false;
        }
        var newRepository = findRepositoryByName(repositoryName);
        newRepository.active = true;
        $scope.currentRepositoryName = repositoryName;
        $scope.updateIndexList(repositoryName, false);
    };

    $scope.selectIndex = function (indexName) {
        if ($scope.indexes.length == 0) {
            return;
        }
        if (!indexName) {
            indexName = $scope.indexes[0].getName();
        }
        var oldIndex = findIndexByName($scope.currentIndexName);
        if (oldIndex) {
            oldIndex.active = false;
        }
        var newIndex = findIndexByName(indexName);
        if (!newIndex) {
            newIndex = $scope.indexes[0];
        }
        newIndex.active = true;
        $scope.currentIndexName = newIndex.getName();
        $scope.updateFileList($scope.currentRepositoryName, $scope.currentIndexName);
    };

    $scope.updateUserPermission = function () {
        API.get('./user/permission/')
            .success(function (data) {
                $scope.userPermission = angular.extend(new UserPermission(), data.body.permission);
            });
    };

    $scope.updateRepositoryList = function () {
        $scope.modalManager.openModalDialog("Loading repositories ...", false);
        API.get('./repository/')
            .success(function (data) {
                $scope.repositories = [];
                angular.forEach(data.body.repositories, function (repositoryJson) {
                    $scope.repositories.push(angular.extend(new IndexRepository(), repositoryJson))
                });
                $scope.modalManager.closeModalDialog();
                $scope.selectRepository($scope.currentRepositoryName);
            });
    };

    $scope.updateIndexList = function (repositoryName) {
        $scope.modalManager.openModalDialog("Loading indexes ...", false);
        API.get('./repository/' + repositoryName + '/index/')
            .success(function (data) {
                $scope.indexes = [];
                angular.forEach(data.body.indexes, function (indexJson) {
                    $scope.indexes.push(angular.extend(new Index(), indexJson))
                });
                $scope.modalManager.closeModalDialog();
                $scope.selectIndex($scope.currentIndexName);
            });
    };

    $scope.createIndex = function (repositoryName, indexName) {
        if (!indexName) {
            return;
        }
        if (confirm("Are you sure to create an index named '" + indexName + "'?")) {
            API.post('./repository/' + repositoryName + '/index/',
                jQuery.param({name: indexName}),
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function () {
                    $scope.updateIndexList(repositoryName);
                    $scope.indexName = "";
                });
        }
    };

    $scope.deleteIndex = function (repositoryName, indexName) {
        if (confirm("Are you sure to delete the index named '" + indexName + "'?")) {
            API.delete('./repository/' + repositoryName + '/index/' + indexName + "/")
                .success(function () {
                    $scope.updateIndexList(repositoryName);
                });
        }
    };

    $scope.updateFileList = function (repoName, indexName) {
        $scope.modalManager.openModalDialog("Loading files ...", false);
        API.get('./repository/' + repoName + '/index/' + indexName + '/file/')
            .success(function (data) {
                $scope.files = [];
                angular.forEach(data.body.files, function (fileInfoJson) {
                    $scope.files.push(angular.extend(new FileInfo(), fileInfoJson));
                });
                $scope.modalManager.closeModalDialog();
                $location.path("/" + $scope.currentRepositoryName + "/" + $scope.currentIndexName);
            });
    };

    $scope.getMatchedFiles = function (query) {
        if (query) {
            query = query.trim();
            return jQuery.grep($scope.files, function (file) {
                return file.name.toLowerCase().indexOf(query.toLowerCase()) != -1;
            });
        } else {
            return $scope.files;
        }
    };

    $scope.deleteFile = function (repoName, indexName, fileStatus, fileName) {
        API.delete('./repository/' + repoName + '/index/' + indexName + '/file/' + fileStatus + "/" + fileName + "/")
            .success(function () {
                $scope.updateFileList($scope.currentRepositoryName, $scope.currentIndexName);
            });
    };

    $scope.downloadFile = function (repoName, indexName, fileStatus, fileName) {
        location.href = './repository/' + repoName + '/index/' + indexName + '/file/' + fileStatus + "/" + fileName + "/";
    };

    var urlParams = PathUtil.parse($location.path(), "/:repositoryName/:indexName");
    $scope.currentRepositoryName = urlParams["repositoryName"];
    $scope.currentIndexName = urlParams["indexName"];
    $scope.updateRepositoryList();
    $scope.updateUserPermission();
}]);