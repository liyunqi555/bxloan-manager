define(function(require, exports, module) {
	var model = require("./model");
	var utils = require("common/utils");
	var mergedBillView = require('docInfoMng/view');
	var view = Backbone.View.extend({
	el: "body",
	events: {  
		"click #button-query": "query",//查询
		"click #button-reset":"resetBills",//重置
		"click #btn-add" : "addColumn",//增加栏目弹出框
		"click #btn-showTree": "ToggleTree",//初始化树菜单
		"click button[role=btn-detail-del]" : "delDetail",//删除
		"click button[role=edit]" : "edit",//编辑
		"click button[role=detail]" : "detail",//查看
		"click button[role=docInfoList]":"docInfoList",//查看
		"click #btn-docInfoView":"initdocInfoView" //进入文章列表
			
			
	},
	initialize: function() {
		this.model = new model();''
		this.render();
	},
	//渲染
	render: function() {
		var viewSelf = this;
		this.initDtTable();
		this.saveColumn();
	},
	//查询
	query : function(){
		var viewSelf = this;
      	var seachData=[];
			var $form=$("form[role='searchForm']");
			seachData.push({name:"docColumName",value:$form.find(":text[name='docColumName']").val()});
			console.log($form.find(":text[name='docColumName']").val());
			viewSelf.dt.oSearchData=seachData;
			viewSelf.dt.fnPageChange(0);//执行查询
	},
	//重置
	resetBills:function(){
		var viewSelf = this;
		var $form=$("form[role='searchForm']");
		$form.resetForm();
	},
	initdocInfoView : function (){
		
	},
	//新增栏目
	addColumn: function (){
		var viewSelf = this;
	    var form=$("form[role='addColumnForm']");
	    var validator = $(form).validate();
		validator.resetForm();
	    $("#add-modal-form").modal("show");
	    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增栏目");
	    return false;
	},
	saveColumn:function(){
		var viewSelf = this;
		var $form=$("form[role='addColumnForm']");
		  $form.validate({
			  submitHandler: function(form) {
				var formSelf = $(form);
				viewSelf.model.submitForm($form, function(result) {
					console.log(result.code);
						if (result.code=='200') {
							$("#add-modal-form").modal("hide");
							formSelf.resetForm();
							viewSelf.dt.fnDraw(); // 重新加载表格中的数据
						} else {
							utils.alert.err("<strong>" + result.msg + "</strong>");
						}
					});
			  	}
			});
	},
	initDtTable: function() {  //查询导入的记录
		var viewSelf = this;
		var init= true;
		var dt = $("#tb_columnList").dataTable({
			bFilter:false,
			bSort:false,
			sPaginationType: "full_numbers",  
			sAjaxSource : "/docColumnMng/findColumnList",
			aoColumns : [
		        {mData: "order", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "name", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "creatorName", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "ifSpecial", mRender: function(data, type, rowdata) {
		        		if(rowdata.ifSpecial == '1'){
		        			return "是";
		        		}else{
		        			return "否";
		        		}
		        }},
		        {mData: "level", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "parentName", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: null, mRender: function(data, type, rowdata) {
		        	if( rowdata.id == '0') {
			        		var buttons = "";
			        		var edit = "<button type='button' role='edit' data-id='" +rowdata.id + "'"+"date-name='"+rowdata.name + "'"+"date-parentName='"+rowdata.parentName +"'"+"date-ifSpecial='"+rowdata.ifSpecial + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit bigger-120' ></i></button> ";
                    		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye bigger-120'></i></button> ";
                    		buttons+= edit+view;
                       		return buttons;
		        	}else{
		        		var buttons = "";
		        		var edit = "<button type='button' role='edit' data-id='" +rowdata.id + "'"+"date-name='"+rowdata.name + "'"+"date-parentName='"+rowdata.parentName +"'"+"date-ifSpecial='"+rowdata.ifSpecial + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit bigger-120'></i></button> ";
		        		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye bigger-120'></i></button> ";
		        		var deleteView = "<button type='button' role='btn-detail-del' data-id='" +rowdata.id + "'  class='btn btn-xs btn-danger' title='删除' ><i class='ace-icon fa fa-trash-o bigger-120'></i></button> ";
                		var list = " <button type='button' role='docInfoList' data-id='" +rowdata.id + "'  class='btn btn-xs btn-purple' title='文章列表'><i class='ace-icon fa fa-check bigger-120'></i></button> ";
                		buttons+= edit+view+deleteView+list;
                		return buttons;
		        	}
		        	
	    	}}],	
	    	/**传参*/
			fnServerParams : function(aoData) {
				var dtSelf=this;
	    		if(dtSelf.oSearchData){//增加查询参数
	    			$.each(dtSelf.oSearchData,function(i,v){
	    				aoData.push(v);
	    			})
	    		}
	    		//默认的查询参数
	    		else{
	    			aoData.push({
						name : "docColumName",
						value : $("form[role='searchForm']").find(":text[name='docColumName']").val()
					});
	    		}
			}
		});
		viewSelf.dt = dt;
	},
	//查看事件
	docInfoList:function(e){
		var viewSelf = this;
		$(document).on("click","button[role=docInfoList]",function(e){
		    var $this = $(this);
			var id = $(this).attr("data-id");
			window.location.href ='docInfoMng/main'+"/"+id;
		});
	},
	//删除栏目
	delDetail : function(e) {
		var viewSelf = this;
		$(document).on("click","button[role='btn-detail-del']",function(e){
    		var $this = $(this);
			var id = $(this).attr("data-id");
			console.log(id);
				if (result) {
					$.get( "docColumnMng/deleteColumn/" + id, function(result) {
						if (result.success=='500') {
							return bootbox.alert(result.msg || "删除失败，请刷新页面后再试");
						}
						viewSelf.dt.fnPageChange(0);
					});
				}
		});
	},
	//查看事件
	detail:function(e){
		var viewSelf = this;
		viewSelf.initModal();
			$(document).on("click","button[role=detail]",function(e){
			$("#add-modal-form").resetForm();
		    $("#add-modal-form").modal("show");
		    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 查看栏目");
		    var $this = $(this);
			var id = $(this).attr("data-id");
			$("#btn-showTree").attr("disabled",true);
	    	viewSelf.initModalData(id,'view');
		});
	},
	 //编辑按钮事件
	edit:function(e){
    	var viewSelf = this;
    	$("#add-modal-form").resetForm();
    	$(document).on("click","button[role=edit]",function(e){
    		$ ("select").unbind ("change");
	    	var btnSelf = $(e.currentTarget);
	    	var $this = $(this);
			var id = $(this).attr("data-id");
	    	var name = $(this).attr("data-name");
	    	var parentName = $(this).attr("data-parentName");
	    	var ifSpecial = $(this).attr("data-ifSpecial");
			$("#add-simple-submit").hide();
		    $("#add-modal-form").modal("show");
		    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 编辑栏目");
	    	 $('#addName').val(name);
	    	 $('#docColumnCdMask').val(parentName);
	    	 $('#ifSpecial').val(ifSpecial);
	    	 $('#docColumnId').val(id);
	    	 $("input[name='id']").val(id);
	    	 $('#operate').val('edit');
	    	 viewSelf.initModal();
	    	 viewSelf.initModalData(id,'edit');
    	});
	},
    initModal:function(){
	    var form=$("form[role='addColumnForm']");
	    var validator = $(form).validate();
		validator.resetForm();
    	$("#add-modal-form").show();
    	$("#addName").attr("disabled",false);
		$("#ifSpecial").attr("disabled",false);
		$("#docColumnCdMask").attr("disabled",false);
		$("#btn-showTree").attr("disabled",false);
    },
    initModalData:function(id,type){
    	$.ajax({
			type : "post",
			url : "/docColumnMng/getColumnOne",
			data: {
				id : id
			},
			success : function(result){
				if(result.code=='200'){
					  var form=$("form[role='addColumnForm']");
					    var validator = $(form).validate();
						validator.resetForm();
			    	 $('#addName').val(result.body.name);
			    	 $('#docColumnCdMask').val(result.body.parentName);
			    	 $('#ifSpecial').val(result.body.ifSpecial);
			    	 $("input[name='id']").val(id);
			    	 $("input[name='parentId']").val(id);
			    	 $('#docColumnId').val(result.body.id);
					if(type=='view'){
						$("#addName").attr("disabled",true);
						$("#ifSpecial").attr("disabled",true);
						$("#docColumnCdMask").attr("disabled",true);
						$("#add-simple-submit").hide();
					}
					$("#add-modal-form").modal("show");
				}else{
					utils.alert.warn(result.msg);
				}
			}
		});
    },
		//加载树菜单
     ToggleTree: function() {
			/** 初始化树 */
         var viewSelf = this;
         $.fn.zTree.init($("#addTree"), {
             async: {
                 enable: true,
                 url: "/docColumnMng/getAllColumn"
             },
             view:{//表示tree的显示状态
                 selectMulti:true//表示多选
             },
             data: {
                 simpleData: {
                     enable: true,
                     idKey: "id",
                     pIdKey: "parentId"
                 },
                 key: {
                     name: "name"
                 }
             },
             check: {
                 enable: true,
                 chkStyle: "checkbox",
                 radioType: "level"
             },
             callback: {
                 onClick: function(event, treeId, treeNode) {
                /* 	if(treeNode!=null&&treeNode.children!=null&&treeNode.children.length!=null&&treeNode.children.length>0){
                 		$("#docColumnCdField").val("");
                         $("#docColumnCdMask").val("");
                         $("#docColumnCd").val("");
                 		return false;
                 	}else{*/
                 		var id = treeNode.id;
                     	var treeObj = $.fn.zTree.getZTreeObj(treeId);
                     	var node = treeObj.getNodeByParam("id", id, null);
                     	var parentNode = node.getParentNode();
                         treeObj.expandNode(parentNode, true, false);
                 		$("#docColumnCdField").val(treeNode.id);
                         $("#docColumnCdMask").val(treeNode.name);
                         $("#docColumnCd").val(treeNode.id);
                         $("#addControlZTree").toggle(300,
                         function() {
                            if (($("#addControlZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
                             	
                             } else if (($("#addControlZTree").attr("style")) == "display: none;") {
                                 $("#btn-showTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
                             } else {
                                 $("#btn-showTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
                             }
                         });
                 	}
                 },
                 onCheck: function(event, treeId, treeNode) {
                 	$("#docColumnCdField").val(treeNode.id);
                     $("#docColumnCdMask").val(treeNode.name);
                     $("#docColumnCd").val(treeNode.id);
                 },
                 beforeCheck: function(treeId, treeNode) {
                 	//点击radio时赋值
                 	$("#docColumnCdField").val(treeNode.id);
                     $("#docColumnCd").val(treeNode.id);
                     return ! treeNode.isParent;
                 },
                 onAsyncSuccess: function(event, treeId, treeNode, msg) {
                 	var treeObj = $.fn.zTree.getZTreeObj(treeId);
                 	var docColumnCdField = $("#docColumnCdField").val();
                     if (docColumnCdField !== ""&&docColumnCdField !==null) {
                     	var node = treeObj.getNodeByParam("id", docColumnCdField, null);
/*                        	treeObj.checkNode(node,null,true,true);
*/                        	var parentNode = node.getParentNode();
                         treeObj.expandNode( node.id , true, false); 
                         $("#docColumnCdMask").val(node.name);
                         $("#docColumnCd").val(node.id);
                     }
                     
                 }
             //}
         });
         $("#addControlZTree").toggle(300,
                 function() {
		                if (($("#addControlZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
		                	
		                } else if (($("#addControlZTree").attr("style")) == "display: none;") {
		                    $("#btn-showTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
		                } else {
		                    $("#btn-showTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
		                }
         });
     }

});
	module.exports = view;	
	   
});