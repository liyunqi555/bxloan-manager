define(function(require, exports, module) {
	var model = require("./model");
	var rm = require("./rm");
	var utils = require("common/utils");
	//var mergedBillView = require('docInfoMng/view');
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
		"click button[role=docInfoList]":"docInfoList",//进入文章列表
		"click #add-simple-submit":"saveColumn",
		"change #conditionType" : "ifConditionType",//更改模式
		"click #titleKeyWord":"titleKeyWord",
		"click #sourceKeyWord":"sourceKeyWord",
		"click #bodyKeyWord":"bodyKeyWord",
		"click #or":"ifOr",
		"click #and":"ifAnd"
	},
	initialize: function() {
		this.model = new model();
		this.render();
	},
	//渲染
	render: function() {
		var viewSelf = this;
		this.initDtTable();
		//this.initModal();
		this.initUserTree();
		this.initSourceTree();
		this.ifConditionType();
		this.formValidate();
	},
	//查询
	query : function(){
		var viewSelf = this;
      	var seachData=[];
			var $form=$("form[role='searchForm']");
			seachData.push({name:"docColumName",value:$form.find(":text[name='docColumName']").val()});
			viewSelf.dt.oSearchData=seachData;
			viewSelf.dt.fnPageChange(0);//执行查询
	},
	//重置
	resetBills:function(){
		var viewSelf = this;
		$("form[role='searchForm']").resetForm();
		viewSelf.dt.oSearchData = null;
		//执行查询
		viewSelf.dt.fnPageChange(0);
	},
	//新增栏目
	addColumn: function (){
		var viewSelf = this;
		var treeObj1 = $.fn.zTree.getZTreeObj("userZTree");
		treeObj1.checkAllNodes(false);
		var nodes1 = treeObj1.getNodes();
		for (var i=0, l=nodes1.length; i < l; i++) {
			treeObj1.setChkDisabled(nodes1[i], false);
		}
		var treeObj2 = $.fn.zTree.getZTreeObj("sourceZTree");
		treeObj2.checkAllNodes(false);
		var nodes2 = treeObj2.getNodes();
		for (var i=0, l=nodes2.length; i < l; i++) {
			treeObj2.setChkDisabled(nodes2[i], false);
		}
		 $("input[name='id']").val("");
		 $("#docColumnId").val("");
		$("#addColumnForm").resetForm();
	    $("#add-modal-form").modal("show");
	    viewSelf.initModal();
	    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增栏目");
	    return false;
	},
	ifConditionType : function() {
		$('#conditionField').val('');
		if($('#conditionType').val() == '1') {
			$("#titleKeyWord").css("display","none");
			$("#sourceKeyWord").css("display","none");
	        $("#bodyKeyWord").css("display","none");
           /* $("#or").css("display","none");
            $("#and").css("display","none");*/
		} else {
			$("#titleKeyWord").removeAttr("style");
			$("#sourceKeyWord").removeAttr("style");
	        $("#bodyKeyWord").removeAttr("style");
/*			$("#or").removeAttr("style");
	        $("#and").removeAttr("style");*/
		}
	},
	titleKeyWord:function(){
		$('#conditionField').val($('#conditionField').val()+"标题关键字");
		/*if(null==$('#conditionField').val()||""==$('#conditionField').val()){
		}else{
			$('#conditionField').val($('#conditionField').val()+"\n 标题关键字 =");
		}*/
	},
	sourceKeyWord:function(){
		$('#conditionField').val($('#conditionField').val()+"来源关键字");
	/*	if(null==$('#conditionField').val()||""==$('#conditionField').val()){
		}else{
			$('#conditionField').val($('#conditionField').val()+"\n来源关键字 =");
		}*/
	},
	bodyKeyWord:function(){
		$('#conditionField').val($('#conditionField').val()+"文章关键字 ");
		/*if(null==$('#conditionField').val()||""==$('#conditionField').val()){
		}else{
			$('#conditionField').val($('#conditionField').val()+"\n文章关键字 =");
		}*/
	},
	ifOr:function(){
		if(null==$('#conditionField').val()||""==$('#conditionField').val()){
			utils.alert.err("<strong> 逻辑运算不可在首位</strong>");
			return ;
		}else{
			$('#conditionField').val($('#conditionField').val()+"\n or");
		}
	},
	ifAnd:function(){
		if(null==$('#conditionField').val()||""==$('#conditionField').val()){
			utils.alert.err("<strong> 逻辑运算不可在首位</strong>");
			return ;
		}else{
			$('#conditionField').val($('#conditionField').val()+"\n and ");
		}
	},
	formValidate:function(){
        $("#addColumnForm").validate({
            rules: rm.rules,
            messages: rm.messages,
            errorPlacement: function(error, element) {
                if (element.is(":radio")) error.appendTo(element.parent().next().next());
                else if (element.is(":checkbox")) error.appendTo(element.next());
                else if (element.parent().hasClass("input-group")) error.appendTo(element.parent().parent());
                else error.appendTo(element.parent());
            }
        });
    
	},
	saveColumn: function(e) {
		var viewSelf = this;
		var btnSubmit=$(e.currentTarget);
		var $form=$("form[role='addColumnForm']");
		var treeObj1=$.fn.zTree.getZTreeObj("userZTree");
        nodes1=treeObj1.getCheckedNodes(true);
        var userIds = "";
        for(var i=0;i<nodes1.length;i++){
        	userIds+=nodes1[i].id + ",";
        }
        if(userIds==""){
        	utils.alert.warn("请选择角色或用户");
        	utils.button.reset(btnSubmit);//启用按钮
        	return false;
        }
        
        var treeObj2=$.fn.zTree.getZTreeObj("sourceZTree");
        nodes2=treeObj2.getCheckedNodes(true);
        var sourceIds = "";
        for(var i=0;i<nodes2.length;i++){
        	sourceIds+=nodes2[i].id + ",";
        }
        if(sourceIds==""){
        	utils.alert.warn("请选择来源");
        	utils.button.reset(btnSubmit);//启用按钮
        	return false;
        }
       if (!$("#addColumnForm").valid()) {
        	utils.button.reset(btnSubmit);//启用按钮
        	return ;	
        }
    	utils.button.confirm(btnSubmit,function(result){
			if(result){
				$.ajax({
                    cache: false,
                    type: "POST",
                    url: "/docColumnMng/addColumn?userIds=" + userIds+"&sourceIds="+sourceIds,
                    data:$form.serialize(),
                    success: function(result) {
                    	if(result.code=='200') {
                    		$("#add-modal-form").modal("hide");
                    		$form.resetForm();
                    		viewSelf.dt.fnDraw(); // 重新加载表格中的数据
                    		utils.button.reset(btnSubmit);//启用按钮
                    	} else {
                    		utils.button.reset(btnSubmit);//启用按钮
                    		utils.alert.err("<strong>" + result.msg + "</strong>");
                    	}
                    }
				 }); //ajax end
			}else{
				utils.button.reset(btnSubmit);//启用按钮
			}
		},"您确定要提交保存吗?");
    },
	initDtTable: function() {  //查询导入的记录
		var viewSelf = this;
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
			        		var edit = "<button type='button' role='edit' data-id='" +rowdata.id + "'"+"data-name='"+rowdata.name + "'"+"date-parentId='"+rowdata.parentId +"date-parentName='"+rowdata.parentName +"'"+"date-ifSpecial='"+rowdata.ifSpecial + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit bigger-120' ></i></button> ";
                    		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye bigger-120'></i></button> ";
                    		buttons+= edit+view;
                       		return buttons;
		        	}else{
		        		var buttons = "";
		        		var edit = "<button type='button' role='edit' data-id='" +rowdata.id + "' data-name='"+rowdata.name+ "'data-parentId='"+rowdata.parentId  + "'"+"data-parentName='"+rowdata.parentName +"'"+"data-ifSpecial='"+rowdata.ifSpecial + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit bigger-120'></i></button> ";
		        		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye bigger-120'></i></button> ";
		        		var deleteView = "<button type='button' role='btn-detail-del' data-id='" +rowdata.id + "'  class='btn btn-xs btn-danger' title='删除' ><i class='ace-icon fa fa-trash-o bigger-120'></i></button> ";
                		var list = " <button type='button' role='docInfoList' data-id='" +rowdata.id + "' data-name='"+rowdata.name+ "' data-field='"+rowdata.conditionField+ "' class='btn btn-xs btn-purple' title='文章列表'><i class='ace-icon fa fa-check bigger-120'></i></button> ";
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
		var $this = $(this);
		var columnId =  null;
		var id =  $(e.currentTarget).data('id');
		var name =  $(e.currentTarget).data('name');
		var conditionField = $(e.currentTarget).data('field') ;
		utils.alert.suc(conditionField);
		var type ="column";
		window.location.href ="docInfoMng/findDocInfoListById?id="+id+"&name="+name+"&conditionField="+conditionField+"&type="+type; 	
	},
	//删除栏目
	delDetail : function(e) {
		var viewSelf = this;
		var $this = $(this);
		var btnSelf = $(e.currentTarget);
		var id = btnSelf.data("id");

		utils.button.confirm(btnSelf,function(result){
			if(!result){//取消
				return false;
			}
			$.ajax({
				type : "post",
				url : "/docColumnMng/deleteColumn",
				data: {
					id : id
				},
				success : function(result){
					if(result.code=='200'){
						viewSelf.dt.fnPageChange(0);
						utils.alert.suc(result.msg);
					}else{
						utils.alert.warn(result.msg);
					}
				}
			});
		},'是否确认删除？');		
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
	    	var parentId = $(this).attr("data-parentId");
	    	var ifSpecial = $(this).attr("data-ifSpecial");
		    $("#add-modal-form").modal("show");
		    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 编辑栏目");
	    	 $('#addName').val(name);
	    	 $('#docColumnCdMask').val(parentName);
	    	 $('#ifSpecial').val(ifSpecial);
	    	 $('#docColumnId').val(parentId);
	    	 $("input[name='id']").val(id);
	    	 $("input[name='parentId']").val(parentId);
	    	 $('#operate').val('edit');
	    	 viewSelf.initModal();
	    	 viewSelf.initModalData(id,'edit');
    	});
	},
    initModal:function(){
	    var form=$("form[role='addColumnForm']");
		$("#addColumnForm").resetForm();
		console.log(form);
		$("input").attr("disabled",false);
		$("select").attr("disabled",false);
		$("textarea").attr("disabled",false);
		$("#btn-showTree").attr("disabled",false);
		$("#add-simple-submit").show();
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
					$.each($("#addDocInfoForm").find("input[type='text'],input[type='hidden'], select,textarea"), function() {
							$(this).val(obj[$(this).attr("name")]);
					});
			    	 $('#addName').val(result.body.name);
			    	 $('#docColumnCdMask').val(result.body.parentName);
			    	 $('#ifSpecial').val(result.body.ifSpecial);
			    	 $("input[name='id']").val(id);
			    	 $("input[name='parentId']").val(result.body.parentId);
			    	 $('#docColumnId').val(result.body.id);
			    	 $('#conditionField').val(result.body.conditionField);
			     	 $('#conditionType').val(result.body.conditionType);
			     	 
					if(type=='view'){
						var treeObj = $.fn.zTree.getZTreeObj("sourceZTree");
						var nodes = treeObj.getSelectedNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.setChkDisabled(nodes[i], true);
						}
						$("#addName").attr("disabled",true);
						$("#ifSpecial").attr("disabled",true);
						$("#docColumnCdMask").attr("disabled",true);
						 $('#conditionField').attr("disabled",true);
				     	 $('#conditionType').attr("disabled",true);
				     	$('#sourceZTree').attr("disabled",true);
				     	$("input").attr("disabled",true);
						$("select").attr("disabled",true);
						$("textarea").attr("disabled",true);
						$("#add-simple-submit").hide();
					}
					$("#add-modal-form").modal("show");
				}else{
					utils.alert.warn(result.msg);
				}
			}
		});
		$.ajax({
        	type : "post",
			url : "/docColumnMng/getSource",
			data: {
				"id" : id
			},
			success : function(result){
				if(result.code=='200'){
					var ids = result.body;
					console.log(ids);
					var treeObj = $.fn.zTree.getZTreeObj("sourceZTree");
					treeObj.checkAllNodes(false);
					$.each(ids,function(key,value){
						var node = treeObj.getNodeByParam("id",value, null);
						treeObj.checkNode(node, true, true);
					}); 
					
					if(type=='view'){
						var nodes = treeObj.getNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.setChkDisabled(nodes[i], true);
						}
					}else{
						var nodes = treeObj.getNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.setChkDisabled(nodes[i], false);
						}
						
					}
					
				}else{
					utils.alert.warn(result.msg);
				}
			}
        });
		
		$.ajax({
        	type : "post",
			url : "/docColumnMng/getUser",
			data: {
				"id" : id
			},
			success : function(result){
				if(result.code=='200'){
					var ids = result.body;
					var treeObj = $.fn.zTree.getZTreeObj("userZTree");
					treeObj.checkAllNodes(false);
					$.each(ids,function(key,value){
						var node = treeObj.getNodeByParam("id",
								value, null);
						treeObj.checkNode(node, true, true);
						/*if(null!=node.isParent&&!node.isParent)
						treeObj.checkNode(node, true, true);
						if(type=='view'){
							node.checked=false
						}*/
					}); 
					if(type=='view'){
						var nodes = treeObj.getNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.setChkDisabled(nodes[i], true);
						}
					}else{
						var nodes = treeObj.getNodes();
						for (var i=0, l=nodes.length; i < l; i++) {
							treeObj.setChkDisabled(nodes[i], false);
						}
						
					}

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
                 chkStyle: "radio",
                 radioType: "level"
             },
             callback: {
                 onCheck: function(event, treeId, treeNode) {
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
     },
		initUserTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#userZTree"), {
				async : {
					enable : true,
					url : "/userColumnSourceAssign/getAllUser"
				},
				check: {
					enable: true,
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
				callback : {}
				
			});
			var treeObj = $.fn.zTree.getZTreeObj("userZTree");
		},
		initSourceTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#sourceZTree"), {
				async : {
					enable : true,
					url : "/userColumnSourceAssign/getAllSource"
				},
				check: {
					enable: true
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
				}
			});
			var treeObj = $.fn.zTree.getZTreeObj("sourceZTree");
	}

});
	module.exports = view;	
	   
});
