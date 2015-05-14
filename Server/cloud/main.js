
Parse.Cloud.define("Push", function(request, response) {
  var query = new Parse.Query(Parse.Installation);
  query.equalTo('installationId', request.params.uuid);
  Parse.Push.send({
       where : query,
       data: {
        alert : request.params.message,
    }
  }, {
    success: function () {
        response.success("success");
    },
    error: function (error) {
        response.error(error);
    }
  });
});