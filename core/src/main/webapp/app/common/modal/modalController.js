'use strict';

// angular.module('svittsApp').controller('modalController', function ($scope, $uibModal) {
//
//     $scope.open = function (data) {
//         var modalInstance = $uibModal.open({
//             templateUrl: 'app/common/modal/modal.template.html',
//             controller: 'modalInstanceController',
//             resolve: {
//                 items: function () {
//                     return data;
//                 }
//             }
//         });
//         modalInstance.result.then(function (selectedItem) {
//             $scope.selected = selectedItem;
//         });
//     };
//
// });
//
// angular.module('svittsApp').controller('modalInstanceController', function ($scope, $uibModalInstance, items) {
//
//     $scope.items = items;
//     $scope.selected = {
//         item: $scope.items[0]
//     };
//
//     $scope.ok = function () {
//         $uibModalInstance.close($scope.selected.item);
//     };
//
//     $scope.cancel = function () {
//         $uibModalInstance.dismiss('cancel');
//     };
//
// });