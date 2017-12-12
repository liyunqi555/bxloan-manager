define(function(require, exports, module) {
	var model = require("./model");
	var utils = require("common/utils");
	var view = Backbone.View.extend({
	el: "body",
	events: {  
		"click button[role=btn-Query]": "query",//查询
		"click button[role=btn-Reset]":"resetBills",//重置
		"click button[role=btn-Add]" : "addDocInfo",//新增
		"click #btn-columnTree": "initColumnTree",//初始化树菜单
		"click #btn-sourceTree": "initSourceTree",//初始化树菜单
		"click button[role=btn-delDocInfo]" : "delDocInfo",//删除
		"click button[role=editDocInfo]" : "editDocInfo",//编辑
		"click button[role=detailDocInfo]" : "detailDocInfo"//查看
			
	},
	initialize: function() {
		this.model = new model();''
		this.render();
	},
	//渲染
	render: function() {
		var viewSelf = this;
		this.initDtTable();
		this.saveDocInfo();
	},
	//查询
	query : function(){
		var viewSelf = this;
      	var seachData=[];
		var $form=$("form[role='searchForm']");
		seachData.push({name:"columnId",value:$form.find(":text[name='sColumnId']").val()});
		seachData.push({name:"columnName",value:$form.find(":text[name='sColumnName']").val()});
		seachData.push({name:"keyword",value:$form.find(":text[name='keyword']").val()});
		viewSelf.dt.oSearchData=seachData;
		viewSelf.dt.fnPageChange(0);//执行查询
	},
	//重置
	resetBills:function(){
		var viewSelf = this;
		var $form=$("form[role='searchForm']");
		$form.resetForm();
		viewSelf.dt.oSearchData = null;	
		//执行查询
		viewSelf.dt.fnPageChange(0);
	},
	//创建文章
	addDocInfo: function (){
		var viewSelf = this;
		$("#add-modal-form").resetForm();
	    $('#columnId').val($('#sColumnId').val());
	    $('#docColumnField').val($('#sColumnId').val());
	    $("#add-modal-form").modal("show");
	    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 创建文档");
	    return false;
	},
	saveDocInfo:function(){
		var viewSelf = this;
		var $form=$("form[role='addDocInfoForm']");
		  $form.validate({
			  submitHandler: function(form) {
				var formSelf = $(form);
				viewSelf.model.submitForm($form, function(result) {
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
	//初始化列表
	initDtTable: function() {
		var viewSelf = this;
		var columnId = $('#sColumnId').val();
		var columnName = $('#sColumnName').val();
		var keyword = $('#keyword').val();
		var sourceId = $('#sourceId').val();
		var init= false;
		var dt = $("#tb-list").dataTable({
			bFilter:false,
			bSort:false,
			sPaginationType: "full_numbers",  
			data : {
      			"columnId":columnId,
      			"columnName":columnName,
      			"keyword":keyword,
      			"sourceId":sourceId
      			
      		},
			sAjaxSource : "/docInfoMng/findDocInfoList",
			aoColumns : [
		        {mData: "title", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "classification", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "groupName", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "website", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "keyword", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: "summary", mRender: function(data, type, rowdata) {
		        	if ($.trim(data) == '') {
		                 return "";
		            }
		        	return data;
		        }},
		        {mData: null, mRender: function(data, type, rowdata) {
	        		var buttons = "";
	        		var edit = "<button type='button' role='editDocInfo' data-id='" +rowdata.id + "'"+"data-name='"+rowdata.name+ "'data-parentId='"+rowdata.parentId  + "'"+"data-parentName='"+rowdata.parentName +"'"+"data-ifSpecial='"+rowdata.ifSpecial + "'  class='btn btn-xs btn-info' title='修改' ><i class='ace-icon fa fa-edit bigger-120'></i></button> ";
	        		var view = " <button type='button' role='detailDocInfo' data-id='" +rowdata.id + "'  class='btn btn-xs btn-yellow' title='查看'><i class='ace-icon fa fa-eye bigger-120'></i></button> ";
	        		var deleteView = "<button type='button' role='btn-delDocInfo' data-id='" +rowdata.id + "'  class='btn btn-xs btn-danger' title='删除' ><i class='ace-icon fa fa-trash-o bigger-120'></i></button> ";
            		buttons+= edit+view+deleteView;
            		return buttons;
	        	
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
						name : "columnId",
						value :  columnId
					});
	    			aoData.push({
						name : "columnName",
						value :  $('#sColumnName').val()
					});
	    			aoData.push({
						name : "keyword",
						value :  $('#keyword').val()
					});
	    		}
			}
		});
		viewSelf.dt = dt;
	},	
	//删除文章
	delDocInfo : function(e) {
		var viewSelf = this;
		$(document).on("click","button[role='btn-delDocInfo']",function(e){
				var $this = $(this);
				var id = $(this).attr("data-id");
				bootbox.confirm("确定删除吗？", function(result) {
					if(!result){//取消
						return false;
					}
					if (result) {
						$.ajax({
							type : "post",
							url : "/docInfoMng/deleteDocInfo",
							data: {
								id : id
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
					}
				});
			});
	},
	//查看事件
	detailDocInfo:function(e){
		 var $form=$("form[role='addDocInfoForm']");
		var viewSelf = this;
		$(document).on("click","button[role=detailDocInfo]",function(e){
			$("#add-modal-form").resetForm();
			$("#add-modal-form").modal("show");
			$("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 查看文章");
			var $this = $(this);
			var id = $(this).attr("data-id");
			$("input").attr("disabled",true);
			$("select").attr("disabled",true);
			$("textarea").attr("disabled",true);
			$("#add-simple-submit").hide();
		    viewSelf.initModalData(id,'view');
		});
	},
	 //编辑按钮事件
	editDocInfo:function(e){
    	var viewSelf = this;
    	$("#add-modal-form").resetForm();
    	$(document).on("click","button[role=editDocInfo]",function(e){
    		$ ("select").unbind ("change");
	    	var btnSelf = $(e.currentTarget);
	    	var $this = $(this);
			var id = $(this).attr("data-id");
			$("#add-simple-submit").hide();
		    $("#add-modal-form").modal("show");
		    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 编辑文章");
	    	 $('#addName').val(name);
	    	 $("input[name='id']").val(id);
	    	 $('#operate').val('edit');
	    	 viewSelf.initModal();
	    	 viewSelf.initModalData(id,'edit');
    	});
	},
    initModal:function(){
	    var form=$("form[role='addDocInfoForm']");
	    var validator = $(form).validate();
		validator.resetForm();
		$("input").attr("disabled",false);
		$("select").attr("disabled",false);
		$("textarea").attr("disabled",false);
		$("#add-simple-submit").show();
    },
    initModalData:function(id,type){
    	$.ajax({
			type : "post",
			url : "/docInfoMng/getDocInfoOne",
			data: {
				id : id
			},
			success : function(result){
				if(result.code=='200'){
					  var form=$("form[role='addDocInfoForm']");
					    var validator = $(form).validate();
						validator.resetForm();
						var obj = result.body;
						$.each($("#addDocInfoForm").find("input[type='text'], select"), function() {
							$(this).val(obj[$(this).attr("name")]);
						});
						$.each($("#addDocInfoForm").find("textarea"),function(){
							$(this).html(obj[$(this).attr("name")]);
						});
						$("#add-modal-form").modal("show");
						if(type=='view'){
							$("input").attr("disabled",true);
							$("select").attr("disabled",true);
							$("textarea").attr("disabled",true);
							$("#add-simple-submit").hide();
					}
				}else{
					utils.alert.warn(result.msg);
				}
			}
		});
    },
	initColumnTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#columnTree"), {
				async : {
					enable : true,
					url : "/userColumnSourceAssign/getAllColumn"
				},
	             view:{//表示tree的显示状态
	                 selectMulti:false//表示多选
	             },
				check: {
					enable: true,
					chkStyle : "radio",
					radioType : "level"
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
				callback : {
					onCheck : function(event, treeId, treeNode) {
						var id = treeNode.id;
						if(0==id){
							return false ;
						}
                     	var treeObj = $.fn.zTree.getZTreeObj(treeId);
                     	var node = treeObj.getNodeByParam("id", id, null);
                     	var parentNode = node.getParentNode();
                     	var columnName = node.name; 
                        treeObj.expandNode(parentNode, true, false);
                        $('#docColumnField').val(id);
						$('#columnId').val(id);
						$('#columnName').val(columnName);
					    $("#addcolumnZTree").toggle(300,
					    		function() {
		                            if (($("#addcolumnZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
		                             	
		                             } else if (($("#addcolumnZTree").attr("style")) == "display: none;") {
		                                 $("#btn-columnTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
		                             } else {
		                                 $("#btn-columnTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
		                             }
		                         });
					}
				},
                onAsyncSuccess: function(event, treeId, treeNode, msg) {
                 	var treeObj = $.fn.zTree.getZTreeObj(treeId);
                 	var docColumnField = $("#docColumnField").val(); 
                 	console.log(docColumnField);
                     if (docColumnField !== ""&&docColumnField !==null) {
                    	var node = treeObj.getNodeByParam("id", docColumnCdField, null);
                       	var parentNode = node.getParentNode();
                        treeObj.expandNode( parentNode , true, false); 
                        this.$('#columnId').val(node.id);
 						$('.columnName').val(node.name);
                     }
                     
                 }
			});
		    $("#addcolumnZTree").toggle(300,
		    		function() {
                        if (($("#addcolumnZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
                         	
                         } else if (($("#addcolumnZTree").attr("style")) == "display: none;") {
                             $("#btn-columnTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
                         } else {
                             $("#btn-columnTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
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
					$('#sourceName').val(sourceName);
					$('#docSourceField').val(sourceName);
				    $("#addsourceZTree").toggle(300,
				    		function() {
	                            if (($("#addsourceZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
	                             	
	                             } else if (($("#addsourceZTree").attr("style")) == "display: none;") {
	                                 $("#btn-sourceTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
	                             } else {
	                                 $("#btn-sourceTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
	                             }
	                         });
				}
			}
			
		});
	    $("#addsourceZTree").toggle(300,
	    		function() {
                    if (($("#addsourceZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
                     	
                     } else if (($("#addsourceZTree").attr("style")) == "display: none;") {
                         $("#btn-sourceTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
                     } else {
                         $("#btn-sourceTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
                     }
                 });
}
	
	

});
	module.exports = view;	
	   
});