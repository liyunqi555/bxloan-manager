define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
			$("div.page-header h1").html($('#title').val());
			$("#page_body").html($('#body').val());
			$("img").css("width", "100%");
		}
	});
	module.exports = view;
});