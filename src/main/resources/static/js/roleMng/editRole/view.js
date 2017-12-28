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
			this.initUserTree();
			this.initChecked();
		},
		initChecked:function(){
			var roleId = $('#id').val();
			 $("#type option").each(function(){ //遍历全部option  
				  var value = $(this).val(); //获取option的value 
				  if(value==$('#roleType').val())  
					  $(this).attr("selected",true);
			  });  
			$.ajax({
            	type : "post",
				url : "/roleColumnSourceAssign/getCheckedColumn",
				data: {
					"roleId" : roleId
				},
				success : function(result){
					if(result.code=='200'){
						var ids = result.body;
						var treeObj = $.fn.zTree.getZTreeObj("ColumnZTree");
						treeObj.checkAllNodes(false);
						$.each(ids,function(key,value){
							var node = treeObj.getNodeByParam("id",
									value, null);
							if(!node.isParent)
								treeObj.checkNode(node, true, true);
						}); 
						
					}else{
						utils.alert.warn(result.msg);
					}
				}
            });
			
			$.ajax({
            	type : "post",
				url : "/roleColumnSourceAssign/getCheckedSource",
				data: {
					"roleId" : roleId
				},
				success : function(result){
					if(result.code=='200'){
						var ids = result.body;
						var treeObj = $.fn.zTree.getZTreeObj("SourceZTree");
						treeObj.checkAllNodes(false);
						$.each(ids,function(key,value){
							var node = treeObj.getNodeByParam("id",
									value, null);
							treeObj.checkNode(node, true, true);
						}); 
						
					}else{
						utils.alert.warn(result.msg);
					}
				}
            });
			
			$.ajax({
            	type : "post",
				url : "/roleColumnSourceAssign/getCheckedUser",
				data: {
					"roleId" : roleId
				},
				success : function(result){
					if(result.code=='200'){
						var ids = result.body;
						var treeObj = $.fn.zTree.getZTreeObj("UserZTree");
						treeObj.checkAllNodes(false);
						$.each(ids,function(key,value){
							var node = treeObj.getNodeByParam("id",
									value, null);
							treeObj.checkNode(node, true, true);
						}); 
						
					}else{
						utils.alert.warn(result.msg);
					}
				}
            });
			
			
			var operateType = $('#operateType').val();
			if(operateType=='view'){
				$('input').attr("readonly",true);
				$('select').attr("disabled",true);
				$('#save').hide();
			}
			
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
            	v2+=nodes2[i].id + ",";
            }
            
            var treeObj3=$.fn.zTree.getZTreeObj("UserZTree");
            nodes3=treeObj3.getCheckedNodes(true);
            var v3 = "";
            for(var i=0;i<nodes3.length;i++){
            	v3+=nodes3[i].id + ",";
            }
            var params = [];
            params.push("columnIds="+v1);
            params.push("sourceIds="+v2);
            params.push("userIds="+v3)
            params.push("operateType="+"edit");
            //校验
            if(v1==""){
            	utils.alert.warn("请选择栏目");
            	return false;
            }
            if(v2==""){
            	utils.alert.warn("请选择来源");
            	return false;
            }
            var addRoleName = $('#roleName').val();
            if(!addRoleName){
				utils.alert.warn("请录入角色名称");
				return false;
			}
			var englishName = $('#englishName').val();
			if(!englishName){
				utils.alert.warn("请录入角色英文名称");
				return false;
			}
            var formParams = $("#basicInfoForm").serialize();
            var data = params.join("&") + "&" + formParams;
            var url = "/roleMng/editRole";
            $.ajax({
            	type : "post",
				url : url,
				data: data,
				success : function(result){
					if(result.code=='200'){
						utils.alert.suc(result.msg,function(){
							window.location.href="/roleMng";
						});
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
					}
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
				
			});
			var treeObj = $.fn.zTree.getZTreeObj("SourceZTree");
		},
		initUserTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#UserZTree"), {
				async : {
					enable : true,
					url : "/userMng/getAllUser"
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
						name : "userName"
					}
				},
				view: {
					fontCss : {
						size:"2em"
					}
				}
				
			});
			var treeObj = $.fn.zTree.getZTreeObj("UserZTree");
		},
		
		
	});
	module.exports = view;
});