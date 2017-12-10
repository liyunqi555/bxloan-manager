define(function(require, exports, module) {
	var utils = require("../common/utils.js");
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
			this.initRoleTree();
		},
		save:function(){
			var treeObj1=$.fn.zTree.getZTreeObj("ColumnZTree");
            nodes1=treeObj1.getCheckedNodes(true);
            var v1 = "";
            for(var i=0;i<nodes1.length;i++){
            	v1+=nodes1[i].id + ",";
            }
            if(v1==""){
            	utils.alert.warn("请选择栏目");
            	return false;
            }
            
            var treeObj2=$.fn.zTree.getZTreeObj("SourceZTree");
            nodes2=treeObj2.getCheckedNodes(true);
            var v2 = "";
            for(var i=0;i<nodes2.length;i++){
            	v2+=nodes1[i].id + ",";
            }
            if(v1==""){
            	utils.alert.warn("请选择来源");
            	return false;
            }
            var roleId = $('#checkedId').val();
            if(!roleId){
            	utils.alert.warn("请选择角色"); 
            	return false;
            }
            $.ajax({
            	type : "post",
				url : "/roleMng/allocateToRole",
				data: {
					"columnIds" : v1,
					"sourceIds" : v2,
					"roleIdId" : roleId
				},
				success : function(result){
					if(result.code=='200'){
						utils.alert.suc(result.msg);
					}else{
						utils.alert.warn(result.msg);
					}
				}
            })
		},
		initColumnTree:function(){
				var viewSelf = this;
				$.fn.zTree.init($("#ColumnZTree"), {
					async : {
						enable : true,
						url : "/userColumnSourceAssign/getAllColumn"
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
		initRoleTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#RoleZTree"), {
				async : {
					enable : true,
					url : "/userColumnSourceAssign/getAllRole"
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
						name : "roleName"
					}
				},
				view: {
					fontCss : {
						size:"2em"
					}
				},
				callback : {
					onCheck : function(event, treeId, treeNode) {
						var roleId = treeNode.id;
						$.ajax({
			            	type : "post",
							url : "/roleColumnSourceAssign/getCheckedColumn",
							data: {
								"role" : roleId
							},
							success : function(result){
								if(result.code=='200'){
									var ids = result.body;
									var treeObj = $.fn.zTree.getZTreeObj("ColumnZTree");
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
						
						$('#checkedId').val(roleId);
					}
				}
				
			});
		},
		initSourceTree:function(){
			var viewSelf = this;
			$.fn.zTree.init($("#SourceZTree"), {
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