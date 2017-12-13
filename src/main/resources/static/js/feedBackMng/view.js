define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset",
			"click button[role='remove']":"remove",
			"click button[role='detail']":"detail"
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
				sAjaxSource: "/feedBackMng/findByCondition",
				sPaginationType: "full_numbers",  
		        bProcessing: true,  
		        bServerSide: true,  
				fnServerParams: function(aoData){
					aoData.push({  
	                     name: "roleName",  
	                     value: $("#roleName").val()  
                    });
				},
				aoColumns: [
			        {mData: "id"},
			        {mData: "title"},
			        {mData: "content"},
			        {mData: "userName"},
			        {mData: "createTimeStr"}
			    ]
			});
			viewSelf.dt = dt;
		},
		query : function(){
			var viewSelf = this;
			viewSelf.dt.fnPageChange(0);
		},
        detail:function(e){ //查看按钮事件
        	var viewSelf = this;
        	viewSelf.initModal();
			var btnSelf = $(e.currentTarget);
			var roleId = btnSelf.data("id"); 
        	$("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-eye'></i> 查看来源");
        	$('#checkedId').val(roleId);
        	viewSelf.initModalData(roleId,'view');
        },
        initModal:function(){
        	$("#addForm")[0].reset();
        	$("#add-simple-submit").show();
        	$("#addRoleName").attr("disabled",false);
			$("#englishName").attr("disabled",false);
			$("#roleType").attr("disabled",false);
        },
        initModalData:function(id,type){
        	$.ajax({
				type : "post",
				url : "/docSourceMng/initModalData",
				data: {
					id : id
				},
				success : function(result){
					if(result.code=='200'){
						$('#name').val(result.body.name);
						$('#type').val(result.body.type);
						$('#ifSpecial').val(result.body.ifSpecial);
						$('#languageType').val(result.body.languageType);
						if(type=='view'){
							$("#name").attr("disabled",true);
							$("#type").attr("disabled",true);
							$("#ifSpecial").attr("disabled",true);
							$("#languageType").attr("disabled",true);
							$("#add-simple-submit").hide();
						}else{
                            $("#name").attr("disabled",false);
							$("#type").attr("disabled",false);
							$("#ifSpecial").attr("disabled",false);
							$("#languageType").attr("disabled",false);
						}
						$("#add-modal-form").modal("show");
					}else{
						utils.alert.warn(result.msg);
					}
				}
			});
        },
		reset:function(){//表单重置
			var viewSelf = this;
			$('#roleName').val("");
			viewSelf.dt.fnPageChange(0);
		},
		remove:function(e){
			var viewSelf = this;
			var btnSelf = $(e.currentTarget);
			var docSourceId = btnSelf.data("id");
			utils.button.confirm(btnSelf,function(result){
				if(!result){//取消
					return false;
				}
				$.ajax({
					type : "post",
					url : "/docSourceMng/deleteById",
					data: {
						docSourceId : docSourceId
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