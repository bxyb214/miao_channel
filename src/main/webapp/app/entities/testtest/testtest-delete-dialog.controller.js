(function() {
    'use strict';

    angular
        .module('channelApp')
        .controller('TesttestDeleteController',TesttestDeleteController);

    TesttestDeleteController.$inject = ['$uibModalInstance', 'entity', 'Testtest'];

    function TesttestDeleteController($uibModalInstance, entity, Testtest) {
        var vm = this;

        vm.testtest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Testtest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
