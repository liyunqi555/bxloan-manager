define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
		},
		
		initialize: function() { /** 初始化 */
			console.log(1111);
		}

	});
	module.exports = view;
});