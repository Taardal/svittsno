'use strict';

angular.module('svittsApp').service('modalService', ['$uibModal', function ($uibModal) {

    var self = this;

    self.open = function () {
        console.log("Opening modal...");
        return $uibModal.open({
            templateUrl: 'app/common/modal/modal.template.html',
            controller: function () {
                console.log("service controller");
            }
        });
    }

}]);