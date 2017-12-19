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
			this.initDate();
			this.initRole();
		},
		initRole:function(){
			var roleIds=$("#roleIds");
			$.ajax({
				async:false,
				url: "/roleMng/getAllRoles",
				success:function(obj){
					$.each(obj,function(index, val){
						roleIds.append("<option value='"+val.id+"'>"+val.roleName+"</option>");
        			});
				}
			});
		},
		initDate:function(){
			$('#birthday').datepicker({
				format : 'yyyy-mm-dd', 
				clearBtn:true,
				autoclose:true
			}).on("changeDate",function(ev){
				$("#birthday").datepicker("setBirthday",ev.date?ev.date:"");
				
			});
			$('#startTime').datepicker({
				format : 'yyyy-mm-dd', 
				clearBtn:true,
				autoclose:true
			}).on("changeDate",function(ev){
				$("#startTime").datepicker("setStartTime",ev.date?ev.date:"");
				
			});
			$('#endTime').datepicker({
				format : 'yyyy-mm-dd', 
				clearBtn:true,
				autoclose:true
			}).on("changeDate",function(ev){
				$("#endTime").datepicker("setEndTime",ev.date?ev.date:"");
				
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
            	v2+=nodes2[i].id + ",";
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
            if(!(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test($('#email').val()))){
            	utils.alert.warn("请录入正确的邮箱格式，如:zhangsan@163.com");
            	return false;
            }
            if(!$('#telephone').val()||!(/^1[2|3|5|7|8|9]\d{9}$/.test($('#telephone').val()))){
            	utils.alert.warn("请录入正确的联系电话");
            	return false;
            }
            if(!$('#birthday').val()){
            	utils.alert.warn("请录入出生日期");
            	return false;
            }
            if(!$('#roleIds').val()){
            	utils.alert.warn("请选择角色");
            	return false;
            }
            if(!$('#startTime').val()){
            	utils.alert.warn("请录入有效期起始时间");
            	return false;
            }
            if(!$('#endTime').val()){
            	utils.alert.warn("请录入有效期截止时间");
            	return false;
            }
            if($('#endTime').val()<=$('#startTime').val()){
            	utils.alert.warn("有效期截止时间不可小于起始时间");
            	return false;
            }
            var formParams = $("#basicInfoForm").serialize();
            var data = params.join("&") + "&" + formParams;
            var url = "/userMng/editUser";
            $.ajax({
            	type : "post",
				url : url,
				data: data,
				success : function(result){
					if(result.code=='200'){
						utils.alert.suc(result.msg);
						window.location.href="/userMng";
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