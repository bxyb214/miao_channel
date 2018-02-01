(function() {
    'use strict';

    angular
        .module('channelApp')
        .controller('TesttestDetailController', TesttestDetailController);

    TesttestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Testtest'];

    function TesttestDetailController($scope, $rootScope, $stateParams, previousState, entity, Testtest) {
        var vm = this;

        vm.testtest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('channelApp:testtestUpdate', function(event, result) {
            vm.testtest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
