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
 var iupload = angular.module("iupload", []);

(function ($) {
    $(document).ready(function () {

        function getAngularScope() {
            return angular.element($("body")).scope();
        }

        $("#fileUpload").fileupload({
            dataType: 'json',
            done: function (e) {
                $('#progress').fadeOut();
                var $scope = getAngularScope();
                $scope.updateFileList($scope.currentRepositoryName, $scope.currentIndexName);
                $scope.modalManager.closeModalDialog();
            },
            error: function (jqXHR) {
                var $scope = getAngularScope();
                $scope.modalManager.openErrorDialog(jqXHR.responseJSON.errors.message);
                $scope.$apply();
            },
            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                var $s = getAngularScope();
                if (progress == 100) {
                    $s.modalManager.setModalProgress(100);
                    $s.modalManager.setModalMessage("Uploading");
                } else {
                    $s.modalManager.setModalProgress(progress);
                }
                $s.$apply();
            }
        }).bind('fileuploadsubmit', function (e, data) {
            var $s = getAngularScope();
            if (! $s.currentIndexName || $s.currentIndexName == "") {
                e.preventDefault();
                $s.modalManager.openErrorDialog("Please create or select a dataset before uploading.");
                $s.$apply();
                return;
            }
            data.url = "./repository/" + $s.currentRepositoryName + "/index/" + $s.currentIndexName + "/file/";
            $s.modalManager.openModalDialog("Uploading ...", true);
            $s.$apply();
        });
    });
})(jQuery);