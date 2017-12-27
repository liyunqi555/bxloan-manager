define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
		},
		query : function(){
			var viewSelf = this;
			viewSelf.dt.fnPageChange(0);
		},		
		reset : function(){
			var viewSelf = this;
			$('#columnName').val('');
			viewSelf.dt.fnPageChange(0);
		},
		initDataTables : function(){
			var viewSelf = this;
			var dt = $("#tb-list").dataTable({
				bFilter:false,
				bSort:false,
				sAjaxSource: "/userSign/findByCondition",
				sPaginationType: "full_numbers",  
		        bProcessing: true,  
		        bServerSide: true,  
				fnServerParams: function(aoData){
					aoData.push({  
	                     name: "columnName",  
	                     value: $("#columnName").val()  
                    });
				},
				aoColumns: [
			        {mData: "columnName"},
			        {mData: "level"},
			        {mData: "parentColumnName"}
			    ]
			});
			viewSelf.dt = dt;
		}

	});
	module.exports = view;
});