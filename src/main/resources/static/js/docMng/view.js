define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #show-column-tree" : "toggleColumnTree",
			"click #show-source-tree" : "toggleSourceTree"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			this.initColumnTree();
			this.initSourceTree();
		},
		 toggleSourceTree: function() {
             $("#SourceTreeWarp").toggle(300);
         },
         toggleColumnTree: function() {
             $("#ColumnTreeWarp").toggle(300);
         },

		initColumnTree:function(){
				var viewSelf = this;
				$.fn.zTree.init($("#columnTree"), {
					async : {
						enable : true,
						url : "/userColumnSourceAssign/getAllColumn"
					},
					check: {
						enable: true,
						chkStyle : "radio",
						radioType : "all"
					},
					data : {
						simpleData : {
							enable : true,
							idKey : "id",
							pIdKey : "parentId"
						},
						key : {
							name : "name"
						}
					},
					view: {
						fontCss : {
							size:"2em"
						}
					},
					callback : {
						onCheck : function(event, treeId, treeNode) {
							var columnId = treeNode.id;
							var columnName = treeNode.name;
							$('#columnId').val(columnId);
							$('#column').val(columnName);
							
							
						}
					}
				});
		},
		
		initSourceTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#sourceTree"), {
				async : {
					enable : true,
					url : "/userColumnSourceAssign/getAllSource"
				},
				check: {
					enable: true,
					chkStyle : "radio",
					radioType : "all"
				},
				data : {
					simpleData : {
						enable : true,
						idKey : "id"
					},
					key : {
						name : "name"
					}
				},
				view: {
					fontCss : {
						size:"2em"
					}
				},
				callback : {
					onCheck : function(event, treeId, treeNode) {
						var sourceId = treeNode.id;
						var sourceName = treeNode.name;
						$('#sourceId').val(sourceId);
						$('#source').val(sourceName);
						
						
					}
				}
				
			});
			var treeObj = $.fn.zTree.getZTreeObj("sourceTree");
	}
		
		
		
	});
	module.exports = view;
});