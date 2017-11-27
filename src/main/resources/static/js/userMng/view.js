define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
		},
		initDataTables : function(){
			var viewSelf = this;
			var dt = $("#tb-list").dataTable({
				bFilter:false,
				bSort:false,
				sAjaxSource: "/userMng/findByCondition",
				sPaginationType: "full_numbers",  
		        bProcessing: true,  
		        bServerSide: true,  
				fnServerParams: function(aoData){
					aoData.push({  
	                     name: "customerName",  
	                     value: $("#customerName").val()  
                    });
				},
				aoColumns: [
			        {mData: "id"},
			        {mData: "userName"},
			        {mData: "nickName"},
			        {mData: "email"},
			        {mData: "telephone"},
			        /*{mData: null, mRender: function(data, type, rowdata) {
		    	    	var operation = 
						"<div class='btn-group'>" + 
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-info' role='editMngPrivilege' data-toggle='tooltip' data-placement='bottom' title='编辑'>" +
								"<i class='ace-icon fa fa-edit'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-yellow' role='detailMngPrivilege' data-toggle='tooltip' data-placement='bottom' title='查看'>" +
								"<i class='ace-icon fa fa-eye'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-danger' role='removeMngPrivilege' data-toggle='tooltip' data-placement='bottom' title='删除'>" +
								"<i class='ace-icon fa fa-trash-o' title='删除'></i>" +
						"</div>";
		    	    	return operation;
		    		}}*/
			    ]
			});
			viewSelf.dt = dt;
		}
	});
	module.exports = view;
});