(function() {
    'use strict';
    angular
        .module('channelApp')
        .factory('Testtest', Testtest);

    Testtest.$inject = ['$resource'];

    function Testtest ($resource) {
        var resourceUrl =  'api/testtests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
