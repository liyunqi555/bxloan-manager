define(function(require, exports, module) {
	var model = require("./model");
	var rm = require("./rm");
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
		"click button[role=detailDocInfo]" : "detailDocInfo",//查看
		"click button[role=btn-preview]" : "preview"//文章预览
			
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
		seachData.push({name:"columnId",value:$form.find("#sColumnId").val()});
		seachData.push({name:"sourceId",value:$form.find("#sSourceId").val()});
		seachData.push({name:"conditionField",value:$form.find("#conditionField").val()});
		seachData.push({name:"columnName",value:$form.find(":text[name='sColumnName']").val()});
		seachData.push({name:"sourceName",value:$form.find(":text[name='sSourceName']").val()});
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
		$("#add-modal-form").modal("show");
		$("#addDocInfoForm").resetForm();
		$("#form-field-0").val("");
	    $('#columnId').val($('#sColumnId').val());
	    $('#sourceId').val($('#sSourceId').val());
	    $('#docSourceField').val($('#sSourceId').val());
	    $('#docColumnField').val($('#sColumnId').val());
	    $('#sourceName').val($('#sSourceName').val());
	    $('#columnName').val($('#sColumnName').val());
	    $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 创建文档");
	    return false;
	},
	saveDocInfo:function(){
		var viewSelf = this;
		var $form=$("form[role='addDocInfoForm']");
		  $form.validate({
				rules: rm.rules,
	            messages: rm.messages,
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
		var conditionField = $('#conditionField').val();
		var sourceId = $('#sSourceId').val();
		var sourceName = $('#sSourceName').val();
		var columnName = $('#sColumnName').val();
		var keyword = $('#keyword').val();
		var init= false;
		var dt = $("#tb-list").dataTable({
			bFilter:false,
			bSort:false,
			sPaginationType: "full_numbers",  
			data : {
      			"conditionField":conditionField,
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
	        		var preview = "<button type='button' role='btn-preview' data-id='" +rowdata.id + "'  class='btn btn-xs btn-success' title='文章预览' ><i class='ace-icon fa fa-eye fa-trash-o bigger-120'></i></button> ";
            		buttons+= edit+view+deleteView+preview;
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
						name : "conditionField",
						value :  conditionField
					});
	    			aoData.push({
						name : "sourceId",
						value :  sourceId
					});
	    			aoData.push({
						name : "columnName",
						value :  $('#sColumnName').val()
					});
	    			aoData.push({
						name : "sourceName",
						value :  $('#sSolumnName').val()
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
		var $this = $(this);
		var btnSelf = $(e.currentTarget);
		var id = btnSelf.data("id");
		utils.button.confirm(btnSelf,function(result){
			if(!result){//取消
				return false;
			}
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
		},'是否确认删除？');		
	},
	
	//查看事件
	detailDocInfo:function(e){
		 var $form=$("form[role='addDocInfoForm']");
		var viewSelf = this;
		$(document).on("click","button[role=detailDocInfo]",function(e){
			$("#addDocInfoForm").resetForm();
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
    	$("#addDocInfoForm").resetForm();
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
						$.each($("#addDocInfoForm").find("input[type='text'],input[type='hidden'], select,textarea"), function() {
							$(this).val(obj[$(this).attr("name")]);
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

    preview :function(e){
    	var viewSelf = this;
		var $this = $(this);
		var docId =  $(e.currentTarget).data('id');
		window.location.href="/docInfo/article/"+docId;
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