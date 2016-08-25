'use strict';

angular.module('svittsApp').service('modalService', ['$uibModal', function ($uibModal) {

    var self = this;

    self.open = function (templateUrl, payload) {

        self.foo = "self";

        return $uibModal.open({
            templateUrl: templateUrl,
            controller: function ($scope, $uibModalInstance, payload) {

                $scope.acceptModal = function () {
                    $uibModalInstance.close(self.foo);
                };
                $scope.dismissModal = function () {
                    $uibModalInstance.dismiss('avbryt');
                };

            },
            resolve: {
                payload: function () {
                    return payload;
                }
            }
        });

    }

}]);












