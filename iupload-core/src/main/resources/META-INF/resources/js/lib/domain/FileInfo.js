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