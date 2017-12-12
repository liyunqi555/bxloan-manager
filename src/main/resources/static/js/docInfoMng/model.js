define(function(require, exports, module) {
	var model = Backbone.Model.extend({
		initialize: function() {
		},
		/** 获取合并单据信息，用于页面数据显示 */
		getBillCountInfo : function(data, callback) {
			$.post($$ctx + "mergeAccounting/getBillCountInfo", data, function(obj) {
				callback(obj);
			}).error(function(){bootbox.alert("查看详细失败,请稍后再试");});
		}
	});
	module.exports = model;
});