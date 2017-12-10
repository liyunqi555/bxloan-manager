define(function(require, exports, module) {
	var utils = require("../../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #save":"save"
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			this.initColumnTree();
			this.initSourceTree();
			//this.initDate();
		},
		initDate:function(){
			$('#birthday').datepicker({
				format : 'yyyy-mm-dd', 
				clearBtn:true,
				autoclose:true
			}).on("changeDate",function(ev){
				$("#birthday").datepicker("setBirthday",ev.date?ev.date:"");
				
			});
		},
		save:function(){
			var treeObj1=$.fn.zTree.getZTreeObj("ColumnZTree");
            nodes1=treeObj1.getCheckedNodes(true);
            var v1 = "";
            for(var i=0;i<nodes1.length;i++){
            	v1+=nodes1[i].id + ",";
            }
            
            var treeObj2=$.fn.zTree.getZTreeObj("SourceZTree");
            nodes2=treeObj2.getCheckedNodes(true);
            var v2 = "";
            for(var i=0;i<nodes2.length;i++){
            	v2+=nodes1[i].id + ",";
            }
            var params = [];
            params.push("columnIds="+v1);
            params.push("sourceIds="+v2);
            params.push("operateType="+"add");
            //校验
            if(v1==""){
            	utils.alert.warn("请选择栏目");
            	return false;
            }
            if(v2==""){
            	utils.alert.warn("请选择来源");
            	return false;
            }
            if(!$('#userName').val()){
            	utils.alert.warn("请录入用户名");
            	return false;
            }
            if(!$('#nickName').val()){
            	utils.alert.warn("请录入昵称");
            	return false;
            }
            if(!$('#password').val()){
            	utils.alert.warn("请录入密码");
            	return false;
            }
            if(!$('#email').val()){
            	utils.alert.warn("请录入邮箱");
            	return false;
            }
            if(!$('#officePhone').val()){
            	utils.alert.warn("请录入办公电话");
            	return false;
            }
            if(!$('#telephone').val()){
            	utils.alert.warn("请录入联系电话");
            	return false;
            }
            if(!$('#birthday').val()){
            	utils.alert.warn("请录入生日");
            	return false;
            }
            if(!$('#roleIds').val()){
            	utils.alert.warn("请选择角色");
            	return false;
            }
            var formParams = $("#basicInfoForm").serialize();
            var data = params.join("&") + "&" + formParams;
            console.log(data);
            var url = "/userMng/editUser";
            $.ajax({
            	type : "post",
				url : url,
				data: data,
				success : function(result){
					if(result.code=='200'){
						utils.alert.suc(result.msg);
					}else{
						utils.alert.warn(result.msg);
					}
				}
            });
		},
		initColumnTree:function(){
				var viewSelf = this;
				$.fn.zTree.init($("#ColumnZTree"), {
					async : {
						enable : true,
						url : "/userMng/getAllColumn"
					},
					check: {
						enable: true
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
					callback : {
						onAsyncSuccess : function(event, treeId, treeNode, msg) {
							var treeObj = $.fn.zTree.getZTreeObj(treeId);
							var node = treeObj.getNodeByParam("id",
										1, null);
							treeObj.checkNode(node, true, true);
						},
						onCheck : function(event, treeId, treeNode) {
							
						}
					}
					
					/*callback : {
						onclick : function(event, treeId, treeNode) {
							alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
						},
						onCheck : function(event, treeId, treeNode) {
							 alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
							$("#industryTypeCdField__").val(treeNode.industryTypeCd);
							$("#industryCdMask_").val(treeNode.industryTypeName);
							$("#industryCd_").val(treeNode.industryTypeCd);
						},
						beforeCheck : function(treeId, treeNode) {
							//点击radio时赋值
							$("#industryTypeCdField_").val(treeNode.industryTypeCd);
	                        $("#industryCd_").val(treeNode.industryTypeCd);
							//return !treeNode.isParent;
						},
						onAsyncSuccess : function(event, treeId, treeNode, msg) {
							//var industryTypeCd = $("#industryTypeCdField_").val();
							//if (industryTypeCd !== "") {
								var node = treeObj.getNodeByParam("i",
										industryTypeCd, null);
								treeObj.checkNode(node, true, true);
								var parentNode = node.getParentNode();
								treeObj.expandNode(parentNode, true, false);
								$("#industryCdMask_").val(node.industryTypeName);
								$("#industryCd_").val(node.industryTypeCd);
							//}
						}
					}*/
				});
		},
		initSourceTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#SourceZTree"), {
				async : {
					enable : true,
					url : "/userMng/getAllSource"
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
				/*callback : {
					onclick : function(event, treeId, treeNode) {
						alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
					},
					onCheck : function(event, treeId, treeNode) {
						 alert(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
						$("#industryTypeCdField__").val(treeNode.industryTypeCd);
						$("#industryCdMask_").val(treeNode.industryTypeName);
						$("#industryCd_").val(treeNode.industryTypeCd);
					},
					beforeCheck : function(treeId, treeNode) {
						//点击radio时赋值
						$("#industryTypeCdField_").val(treeNode.industryTypeCd);
                        $("#industryCd_").val(treeNode.industryTypeCd);
						//return !treeNode.isParent;
					},
					onAsyncSuccess : function(event, treeId, treeNode, msg) {
						//var industryTypeCd = $("#industryTypeCdField_").val();
						//if (industryTypeCd !== "") {
							var node = treeObj.getNodeByParam("i",
									industryTypeCd, null);
							treeObj.checkNode(node, true, true);
							var parentNode = node.getParentNode();
							treeObj.expandNode(parentNode, true, false);
							$("#industryCdMask_").val(node.industryTypeName);
							$("#industryCd_").val(node.industryTypeCd);
						//}
					}
				}*/
			});
			var treeObj = $.fn.zTree.getZTreeObj("SourceZTree");
	},
		initSourceDataTable:function(){
			var viewSelf = this;
			var dt = $("#tbSourceList").dataTable({
				bFilter:false,
				bServerSide: true,
				bInfo:false,
				bLengthChange:false,
				bAutoWidth : true,
				sAjaxSource: "userMng/getSourceDataTable",
				fnServerParams: function(aoData){
				},
				aoColumns: [
			        {mData:null,mRender:function(data, type, rowdata){
		    	    	var html=[];
		    	    	html.push(' <div class="btn-group">');
	    	    		html.push(' <input type="checkbox"  data-id=' + rowdata.id+ ' role="check"/>');
		    	    	html.push(' </div>')
		    	    	return html.join("");
		    	    }},
			        
			        {mData: "name"}
			       
			    ],
			    fnDrawCallback:function(){
			    	$('#selectAll').prop("checked",false);
			    }
			});
			viewSelf.dt = dt;
		}
		
		
	});
	module.exports = view;
});