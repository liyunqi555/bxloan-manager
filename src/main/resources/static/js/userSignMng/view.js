define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset",
			"click button[role=docInfoList]":"docInfoList",//进入文章列表
			
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
					        {mData: "parentColumnName"},
					        {mData: null, mRender: function(data, type, rowdata) {
					        	var buttons ="";
						    	var buttons = " <button type='button' role='docInfoList'  data-field='"+rowdata.conditionField+ "' class='btn btn-xs btn-purple' title='文章列表'><i class='ace-icon fa fa-check bigger-120'></i></button> ";
			            		return buttons
					        }}
					    ]
			});
			viewSelf.dt = dt;
		},
		//查看事件
		docInfoList:function(e){
			var viewSelf = this;
			var $this = $(this);
			var columnId =  null;
			var conditionField = $(e.currentTarget).data('field') ;
			var type ="column";
			window.location.href ="docInfoMng/findDocInfoListById?id=&name=&conditionField="+conditionField+"&type="+type; 	
		}
	});
	module.exports = view;
});
