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