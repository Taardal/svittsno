(function () {
    window.swaggerUi = new SwaggerUi({
        url: "/api/v1/swagger.json",
        dom_id: "swagger-ui-container",
        supportedSubmitMethods: ['get', 'post', 'put', 'delete'],
        docExpansion: "list",
        apisSorter: "alpha",
        showRequestHeaders: false,
        validatorUrl: null
    });
    window.swaggerUi.load();
}());