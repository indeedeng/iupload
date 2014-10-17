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
 function FileInfo() {
    this.name = "";
    this.status = "";
    this.size = 0;
    this.lastModifiedAt = 0;

    this.getName = function () {
        return this.name;
    };

    this.getStatus = function () {
        return this.status;
    };

    this.getSize = function () {
        return this.size;
    };

    this.getLastModifiedAt = function () {
        return this.lastModifiedAt;
    };

    this.getLabelClass = function () {
        switch (this.status) {
            case "indexing":
                return "label-default";
            case "indexed":
                return "label-primary";
            case "failed":
                return "label-danger";
        }
    };
}