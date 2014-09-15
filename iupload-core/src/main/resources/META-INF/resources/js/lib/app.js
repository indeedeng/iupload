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
            data.url = "./repository/" + $s.currentRepositoryName + "/index/" + $s.currentIndexName + "/file/";
            $s.modalManager.openModalDialog("Uploading ...", true);
            $s.$apply();
        });
    });
})(jQuery);