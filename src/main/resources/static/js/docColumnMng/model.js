
define(function(require, exports, module) {
	var model = Backbone.Model.extend({
		initialize : function() {
		},
		submitForm : function($form,userIds,sourceIds ,callback) {
			$form.ajaxSubmit(function(r) {
				callback(r);
			});
		}
	});
    module.exports = model;
});