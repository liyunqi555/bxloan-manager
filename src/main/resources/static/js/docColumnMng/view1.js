define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #signIn" : "signIn",
			"click #btn-add" : "addColumn",
			"click #btn-showTree": "ToggleTree"

		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			this.initTree();
			this.ToggleTree();
		/*	this.addColumn();*/
		},
		signIn : function(){
			
		},
		initTree : function() {
					/** 初始化树 */
		            var viewSelf = this;
		            $.fn.zTree.init($("#tree"), {
		                async: {
		                    enable: true,
		                    url: "/docColumnMng/getAllColumn"
		                },
		    			edit: {
		    				enable: true,
		    				showRemoveBtn: false,
		    				showRenameBtn: false
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
		                    radioType: "all"
		                },
		                callback: {
		                    onClick: function(event, treeId, treeNode) {
		                    	if(treeNode!=null&&treeNode.children!=null&&treeNode.children.length!=null&&treeNode.children.length>0){
		                    		$("#docColumnCdField").val("");
		                            $("#docColumnCdMask").val("");
		                            $("#docColumnCd").val("");
		                    		return false;
		                    	}else{
		                    		var id = treeNode.id;
		                        	var treeObj = $.fn.zTree.getZTreeObj(treeId);
		                        	var node = treeObj.getNodeByParam("id", id, null);
		                        	var parentNode = node.getParentNode();
		                            treeObj.expandNode(parentNode, true, false);
		                    		$("#docColumnCdField").val(treeNode.id);
		                            $("#docColumnCdMask").val(treeNode.name);
		                            $("#docColumnCd").val(treeNode.id);
		                            $("#controlZTree").toggle(300, function() {});
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
		                    onAsyncSuccess: function(event, treeId, treeNode, msg) {}
		                }
		            });
		        },
		        //增加栏目
		        addColumn: function (){
		        	   //$("#addColumnForm").resetForm();
		        	   $("#add-modal-form div.modal-header h4").html("<i class='ace-icon fa fa-plus'></i> 新增栏目");
		               $("#add-modal-form").modal("show");
		        },
		        ToggleTree: function() {
					/** 初始化树 */
		            var viewSelf = this;
		            $.fn.zTree.init($("#addTree"), {
		                async: {
		                    enable: true,
		                    url: "/docColumnMng/getAllColumn"
		                },
		    			edit: {
		    				enable: true,
		    				showRemoveBtn: false,
		    				showRenameBtn: false
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
		                    radioType: "all"
		                },
		                callback: {
		                    onClick: function(event, treeId, treeNode) {
		                    	if(treeNode!=null&&treeNode.children!=null&&treeNode.children.length!=null&&treeNode.children.length>0){
		                    		$("#docColumnCdField").val("");
		                            $("#docColumnCdMask").val("");
		                            $("#docColumnCd").val("");
		                    		return false;
		                    	}else{
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
		                                	
		                                } else if (($("#addControlZTree").attr("style")) == "display: display;") {
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
		                        $("#addControlZTree").setAttribute("style") = "display: display";
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
		                        	treeObj.checkNode(node,null,true,true);
		                        	var parentNode = node.getParentNode();
		                            treeObj.expandNode( node.id , true, false); 
		                            $("#docColumnCdMask").val(node.name);
		                            $("#docColumnCd").val(node.id);
		                        }
		                        
		                    }
		                }
		            });
		            $("#addControlZTree").toggle(300,
		                    function() {
		                        if (($("#addControlZTree").attr("style")) == "<i class='ace-icon fa fa-eye'></i>") {
		                            $("#btn-showTree")[0].innerHTML = "";
		                        }else{
		                        	$("#btn-showTree")[0].innerHTML = "<i class='ace-icon fa fa-eye'></i>";
		                        }
		                    });
		        }
	});
	module.exports = view;
});	

