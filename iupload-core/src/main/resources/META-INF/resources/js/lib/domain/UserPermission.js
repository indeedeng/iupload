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
 function UserPermission() {
    this.hasRootAccess = false;
    this.writableRepositories = [];
    this.writableIndexes = {};

    this.isWriteAllowed = function (repositoryName, indexName) {
        if (this.hasRootAccess || this.writableRepositories.indexOf(repositoryName) >= 0) {
            return true;
        } else {
            if (!indexName || !this.writableIndexes.hasOwnProperty(repositoryName)) {
                return false;
            } else if (this.writableIndexes[repositoryName].indexOf(indexName) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}