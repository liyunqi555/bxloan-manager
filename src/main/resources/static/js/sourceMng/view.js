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
			"click button[role='detail']":"detail",
			"click button[role=docInfoList]":"docInfoList",
			"click #add-simple-submit":"save"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
		},
		save : function(){
			var viewSelf = this;
			var name = $('#name').val();
			if(!name){
				utils.alert.warn("请录入来源名称");
				return false;
			}

			var $form=$("form[name='addForm']");
			var type = $form.find("select[name='type']").val();
            if(!type){
                utils.alert.warn("请选择来源类型");
                return false;
            }
            var ifSpecial = $form.find("select[name='ifSpecial']").val();
            if(!ifSpecial){
                utils.alert.warn("请选择是否特殊来源");
                return false;
            }
            var languageType = $form.find("select[name='languageType']").val();
            if(!languageType){
                utils.alert.warn("请选择语言类型");
                return false;
            }
			var url = "/docSourceMng/save";
			 $.ajax({
					type : "post",
					url : url,
					data: {
						name : name,
						type : type,
						ifSpecial : ifSpecial,
						languageType:languageType,
						id : $('#checkedId').val()
					},
					success : function(result){
						if(result.code=='200'){
							$("#add-modal-form").modal("hide");
							utils.alert.suc(result.msg);
							viewSelf.dt.fnPageChange(0);
						}else{
							utils.alert.warn(result.msg);
						}
					}
				});
		},
		initDataTables : function(){

			var viewSelf = this;
			var dt = $("#tb-list").dataTable({
				bFilter:false,
				bSort:false,
				sAjaxSource: "/docSourceMng/findByCondition",
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
			        {mData: "name"},
			        {mData: "type",mRender: function(data, type, rowdata) {
                            return data==1?'国内':'国外';
                     }},
			        {mData: "ifSpecial",mRender: function(data, type, rowdata) {
                            return data==1?'是':'否';
                    }},
			        {mData: "languageType",mRender: function(data, type, rowdata) {
                              return data==1?'中文':'其它';
                    }},
			        {mData: null, mRender: function(data, type, rowdata) {
		    	    	var operation = 
						"<div class='btn-group'>" + 
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-info' role='edit' data-toggle='tooltip' data-placement='bottom' title='编辑'>" +
								"<i class='ace-icon fa fa-edit'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-yellow' role='detail' data-toggle='tooltip' data-placement='bottom' title='查看'>" +
								"<i class='ace-icon fa fa-eye'></i></button>" +
							"<button data-id='" + rowdata.id + "' class='btn btn-xs btn-danger' role='remove' data-toggle='tooltip' data-placement='bottom' title='删除'>" +
								"<i class='ace-icon fa fa-trash-o' title='删除'></i></button>" +
		                	" <button type='button' role='docInfoList' data-id='" +rowdata.id + "' data-name='"+rowdata.name+ "'  class='btn btn-xs btn-purple' title='文章列表'><i class='ace-icon fa fa-check bigger-120'></i></button> "+
	

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
	    add: function() { // 新增按钮事件
	    	this.initModal();
	    	$("#name").attr("disabled",false);
            $("#type").attr("disabled",false);
            $("#ifSpecial").attr("disabled",false);
            $("#languageType").attr("disabled",false);
            $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增来源");
            $("#add-modal-form").modal("show");
            $('#checkedId').val('');
            $('#operateType').val('');
        },
        edit:function(e){ //编辑按钮事件
        	var viewSelf = this;
        	viewSelf.initModal();
        	var btnSelf = $(e.currentTarget);
        	var roleId = btnSelf.data("id"); 
        	$("#addForm")[0].reset();
        	$("#add-simple-submit").show();
        	 $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-edit'></i> 编辑来源");
        	 $('#checkedId').val(roleId);
        	 $('#operateType').val('edit');
        	 viewSelf.initModalData(roleId,'edit');
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
		},
		//查看事件
		docInfoList:function(e){
			var viewSelf = this;
			var $this = $(this);
			var sourceId =  null;
			id =  $(e.currentTarget).data('id');
			name =  $(e.currentTarget).data('name');
			var type="source"
			window.location.href ="docInfoMng/findDocInfoListById?id="+id+"&name="+name+"&type="+type; 	
		}
	});
	module.exports = view;
});