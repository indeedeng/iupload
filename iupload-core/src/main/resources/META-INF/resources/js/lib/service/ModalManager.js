function ModalManager() {
    this.showModal = false;
    this.showModalProgress = false;
    this.modalMessage = 'Uploading ...';
    this.modalProgress = 0.;
    this.showError = false;
    this.errorMessage = "";
    this.searchQuery = "";
    this.userPermission = new UserPermission();


    this.openErrorDialog = function (msg) {
        this.showError = true;
        this.errorMessage = msg;
        this.closeModalDialog();
    };

    this.closeErrorDialog = function () {
        this.showError = false;
    };

    this.openModalDialog = function (msg, withProgress) {
        this.showModal = true;
        this.showModalProgress = withProgress;
        this.modalMessage = msg;
    };

    this.isModalShown = function () {
        return this.showModal;
    };

    this.closeModalDialog = function () {
        this.showModal = false;
    };

    this.setModalMessage = function (message) {
        this.modalMessage = message;
    };

    this.setModalProgress = function (progress) {
        this.modalProgress = progress;
    };
}

iupload.service('ModalManager', ModalManager);