define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset",
			"click button[role='btn-Add']":"add",
			"click button[role='remove']":"remove",
			"click button[role='edit']":"edit",
			"click button[role='detail']":"detail"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
		},
		edit:function(e){
			var viewSelf = this;
			var btnSelf = $(e.currentTarget);
			var userId = btnSelf.data("id"); 
			window.location.href="/userMng/edit/"+"edit"+"/"+userId;
		},
		detail:function(e){
			var viewSelf = this;
			var btnSelf = $(e.currentTarget);
			var userId = btnSelf.data("id"); 
			window.location.href="/userMng/edit/"+"view"+"/"+userId;
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
	                     name: "userName",  
	                     value: $("#userName").val()  
                    });
				},
				aoColumns: [
			        {mData: "id"},
			        {mData: "userName"},
			        {mData: "nickName"},
			        {mData: "email"},
			        {mData: "telephone"},
			        {mData: null, mRender: function(data, type, rowdata) {
		    	    	var operation = 
						"<div class='btn-group'>" + 
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-info' role='edit' data-toggle='tooltip' data-placement='bottom' title='编辑'>" +
								"<i class='ace-icon fa fa-edit'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-yellow' role='detail' data-toggle='tooltip' data-placement='bottom' title='查看'>" +
								"<i class='ace-icon fa fa-eye'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-danger' role='remove' data-toggle='tooltip' data-placement='bottom' title='删除'>" +
								"<i class='ace-icon fa fa-trash-o' title='删除'></i>" +
						"</div>";
		    	    	return operation;
		    		}}
			    ]
			});
			viewSelf.dt = dt;
		},
		query : function(){
			var viewSelf = this;
			viewSelf.dt.fnPageChange(0);
		},
		add : function(){
			window.location.href="userMng/addUser";
		},
		reset:function(){//表单重置
			var viewSelf = this;
			$('#userName').val("");
			viewSelf.dt.fnPageChange(0);
		},
		remove:function(e){
			var viewSelf = this;
			var btnSelf = $(e.currentTarget);
			var userId = btnSelf.data("id"); 
			utils.button.confirm(btnSelf,function(result){
				if(!result){//取消
					return false;
				}
				$.ajax({
					type : "post",
					url : "/userMng/deleteById",
					data: {
						userId : userId
					},
					success : function(result){
						if(result.code=='200'){
							utils.alert.suc(result.msg);
							viewSelf.dt.fnPageChange(0);
						}else{
							utils.alert.warn(result.msg);
						}
					}
				});
			},'是否确认删除？');
			
		}

	});
	module.exports = view;
});