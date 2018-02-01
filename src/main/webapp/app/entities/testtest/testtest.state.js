(function() {
    'use strict';

    angular
        .module('channelApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('testtest', {
            parent: 'entity',
            url: '/testtest?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'channelApp.testtest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/testtest/testtests.html',
                    controller: 'TesttestController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('testtest');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('testtest-detail', {
            parent: 'testtest',
            url: '/testtest/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'channelApp.testtest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/testtest/testtest-detail.html',
                    controller: 'TesttestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('testtest');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Testtest', function($stateParams, Testtest) {
                    return Testtest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'testtest',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('testtest-detail.edit', {
            parent: 'testtest-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testtest/testtest-dialog.html',
                    controller: 'TesttestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Testtest', function(Testtest) {
                            return Testtest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('testtest.new', {
            parent: 'testtest',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testtest/testtest-dialog.html',
                    controller: 'TesttestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('testtest', null, { reload: 'testtest' });
                }, function() {
                    $state.go('testtest');
                });
            }]
        })
        .state('testtest.edit', {
            parent: 'testtest',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testtest/testtest-dialog.html',
                    controller: 'TesttestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Testtest', function(Testtest) {
                            return Testtest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('testtest', null, { reload: 'testtest' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('testtest.delete', {
            parent: 'testtest',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/testtest/testtest-delete-dialog.html',
                    controller: 'TesttestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Testtest', function(Testtest) {
                            return Testtest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('testtest', null, { reload: 'testtest' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
