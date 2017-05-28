angular
    .module('svittsApp')
    .service('notificationService', function () {

        var service = this;

        var settings = {
            styling: 'bootstrap3',
            animate_speed: 'normal',
            delay: 3000
        };

        service.success = function (text) {
            settings.type = 'success';
            settings.title = 'Success';
            settings.text = text;
            return getNotification(settings);
        };

        service.error = function (text) {
            settings.type = 'error';
            settings.title = 'Error';
            settings.text = text;
            return getNotification(settings);
        };

        var getNotification = function (settings) {
            return $(function () {
                new PNotify(settings);
            });
        }

    });