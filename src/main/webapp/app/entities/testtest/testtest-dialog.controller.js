(function() {
    'use strict';

    angular
        .module('channelApp')
        .controller('TesttestDialogController', TesttestDialogController);

    TesttestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Testtest'];

    function TesttestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Testtest) {
        var vm = this;

        vm.testtest = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.testtest.id !== null) {
                Testtest.update(vm.testtest, onSaveSuccess, onSaveError);
            } else {
                Testtest.save(vm.testtest, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('channelApp:testtestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
