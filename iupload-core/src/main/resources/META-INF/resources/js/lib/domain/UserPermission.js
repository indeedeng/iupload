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