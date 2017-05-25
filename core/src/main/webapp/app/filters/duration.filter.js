angular
    .module("svittsApp")
    .filter("duration", function () {
        return function (durationInMinutes) {
            if (durationInMinutes) {
                var hours = Math.floor(durationInMinutes / 60);
                var minutes = durationInMinutes % 60;
                return hours + "h " + minutes + "min";
            } else {
                return "";
            }
        }
    });
