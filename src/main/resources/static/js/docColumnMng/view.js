define(function(require, exports, module) {
/*	var mergedBillView = require('./mergedBill/view');
*/	var view = Backbone.View.extend({

	el: "body",
	events: {  
		"click #button-query": "query",//查询
		"click #button-reset":"resetBills",//重置
		"click #btn-add" : "addColumn",//增加栏目
		"click #btn-showTree": "ToggleTree",//初始化树菜单
		"click button[role=btn-detail-del]" : "delDetail",//删除
		"click button[role=edit]" : "edit",//编辑
		"click button[role=detail]" : "detail"//查看
			
	},
	initialize: function() {
		this.render();
/*			this.$bizAccountListModal=$('#bizAccountListModal');
	this.mergedBillView=new mergedBillView();*/	
	},
	//渲染
	render: function() {
		var viewSelf = this;
		this.initDtTable();
	//	this.initPage();
	},
    initPage:function(){
    	$(document).on("click","#isChecked",function(e){
    		var bills = $("#tb_bills").find(":checkbox[name='bills']");
    		if($("#isChecked").is(":checked")){
    			bills.prop("checked",true);
    		}else{
    			bills.prop("checked",false);
    		}
    	});
    	
    },
	//查询
	query : function(){
		var viewSelf = this;
      	var seachData=[];
			var $form=$("form[role='']");
			seachData.push({name:"name",value:$form.find("select[name='name']").val()});
			viewSelf.dt.oSearchData=seachData;
			viewSelf.dt.fnPageChange(0);//执行查询
	},
	//重置
	resetBills:function(){
		var viewSelf = this;
		var $form=$("form[role='searchForm']");
		$form.resetForm();
	},
	/*		mergeAccount:function() {
			var $form=$("form[role='searchForm']");
			var projectType = $form.find("select[name='projectType']").val();
			var orgId = $form.find("select[name='orgId']").val();
			if(orgId==''){
				utils.alert.warn("<strong>"+ "请选择需要合并单据的所属机构类型！" +"</strong>");
				return false;
			}
			window.location.href = $$ctx + 'mergeAccounting/addMergeAccounting'+"/"+projectType+"/"+orgId;
		},*/

	    //增加栏目
	    addColumn: function (){
	    	$("#add-modal-form").modal("show");
	    	 $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增栏目");
	            return false;
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
				        		var edit = "<button type='button' role='edit' data-id='" + rowdata.id + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit'></i></button> ";
	                    		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye'></i></button> ";
	                    		buttons+= edit+view;
	                       		return buttons;
			        	}else{
			        		var buttons = "";
			        		var edit = "<button type='button' role='edit' data-id='" +rowdata.id + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit'></i></button> ";
			        		var view = " <button type='button' role='detail' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye'></i></button> ";
			        		var deleteView = "<button type='button' role='btn-detail-del' data-id='" +rowdata.id + "'  class='btn btn-xs btn-info' title='删除' ><i class='ace-icon fa fa-trash-o'></i></button> ";
                    		buttons+= edit+view+deleteView;
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
							name : "name",
							value : $("form[role='searchForm']").find("select[name='name']").val()
						});
		    		}
				}
			});
			viewSelf.dt = dt;
		},
		//删除栏目
		delDetail : function(e) {
			var viewSelf = this;
			$(document).on("click","button[role='btn-detail-del']",function(e){
	    		var $this = $(this);
				var id = $(this).attr("data-id");
				console.log(id);
				bootbox.confirm("确定删除吗？", function(result) {
					if (result) {
						$.get( "docColumnMng/deleteColumn/" + id, function(result) {
							if (result.success=='500') {
								return bootbox.alert(result.msg || "删除失败，请刷新页面后再试");
							}
						});
					}
				});
			});
			$(this).dt.fnPageChange(0);
		},
	    edit:function(e){ //编辑按钮事件
        	var viewSelf = this;
        	viewSelf.initModal();
        	var btnSelf = $(e.currentTarget);
        	var id = btnSelf.data("id"); 
        	$("#addForm").resetForm();
        	$("#add-modal-form").modal("show");
        	 $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增栏目");
        	 $('#checkedId').val(id);
        	 $('#operateType').val('edit');
        	 viewSelf.initModalData(roleId,'edit');
        },
        initModalData:function(id,type){
        	$.ajax({
				type : "post",
				url : "docColumnMng/deleteColumn",
				data: {
					id : id
				},
				success : function(result){
					console.log(result);
					if(result.code=='200'){
						$('#addRoleName').val(result.body.roleName);
						$('#englishName').val(result.body.englishName);
						$('#roleType').val(result.body.type);
						if(type=='view'){
							$("#addRoleName").attr("disabled",true);
							$("#englishName").attr("disabled",true);
							$("#roleType").attr("disabled",true);
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
                    enable: false,
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