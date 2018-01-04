define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var selectedStr = "";
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #selectAll" : "selectAll",
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset",
			"click button[role='btn-Export']":"exportExcel",
			"click button[role='detail']":"detail"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
			this.getCheckBox();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
            if($('#type').val()=='1'){
            	$('#userName').attr("readonly",true);
            }
		},
		detail:function(e){ //查看按钮事件
        	var viewSelf = this;
			var btnSelf = $(e.currentTarget);
			var roleId = btnSelf.data("id"); 
			window.location.href="/docInfo/article/"+roleId;
        },
		getCheckBox : function(){
			var viewSelf = this;
			$(document).on("change","input[type='checkbox'][name='doc_id']",function() {
				var arr = $("input[type='checkbox'][name='doc_id']");
				for(var i = 0; i < arr.length; i++){
					if(!$(arr[i]).prop("checked")){
						$("#selectAll").prop("checked",false);
						break;
					}
				}
				if(i == arr.length){
					$("#selectAll").prop("checked",true); 
				}
				if($(this).prop("checked")){
					var str = $(this).val();
					if (selectedStr.indexOf(str+ ",") == -1) {
						selectedStr += str+ ",";
					}
				}else if(!$(this).prop("checked")){
					var str = $(this).val() ;
					if (selectedStr.indexOf(str+ ",") != -1) {
						selectedStr = selectedStr.replace(str+ ",", "");
					}
				}
				
			});
		},
		selectAll : function(){
			if($("#selectAll").prop("checked")){
				$("input[type='checkbox'][name='doc_id']").prop("checked",true);
				var arr = $("input[type='checkbox'][name='doc_id']");
				for(var i = 0; i < arr.length; i++){
					var str = $(arr[i]).val()+",";
					if (selectedStr.indexOf(str) == -1) {
						selectedStr += str;
					}
				}
			}else{
				$("input[type='checkbox'][name='doc_id']").prop("checked",false);
				var arr = $("input[type='checkbox'][name='doc_id']");
				for(var i = 0; i < arr.length; i++){
					var str = $(arr[i]).val()+",";
					if (selectedStr.indexOf(str) != -1) {
						selectedStr = selectedStr.replace(str, "");
					}
				}
			}
		},
		query : function(){
			var viewSelf = this;
			viewSelf.dt.fnPageChange(0);
		},		
		reset : function(){
			var viewSelf = this;
			$('#userName').val('');
			viewSelf.dt.fnPageChange(0);
		},
		exportExcel:function(){
			if(selectedStr==null||selectedStr==""){
				utils.alert.warn("请选择数据！");
				return false;
			}
			url='/userStoreMng/downloadExcel';
			var params=[];
			params.push("selectedStr" + "=" +selectedStr);
			if(params && params.length > 0) {
				url += ('?'+params.join('&'));
			}
			window.location.href = url;
		},
		initDataTables : function(){
			var viewSelf = this;
			var dt = $("#tb-list").dataTable({
				bFilter:false,
				bSort:false,
				sAjaxSource: "/userStoreMng/findByCondition",
				sPaginationType: "full_numbers",  
		        bProcessing: true,  
		        bServerSide: true,  
				fnServerParams: function(aoData){
					aoData.push({  
	                     name: "userName",  
	                     value: $("#userName").val()  
                    });
				},
				aoColumns: [
					{mData: null,mRender : function(data, type, row){
						var html=[];
	    	    		html.push(' <div class="form-group"><input type="checkbox" name="doc_id"  value=' + row.id+ '><div>');

						return html.join(',');
					}},
			        {mData: "userName"},
			        {mData: "docName"},
			        {mData: "createTime"},
			        {mData: null, mRender: function(data, type, rowdata) {
		    	    	var operation = 
						"<div class='btn-group'>" + 
							"<button data-id='" + rowdata.docId + "' class='btn btn-xs btn-yellow' role='detail' data-toggle='tooltip' data-placement='bottom' title='查看'>" +
								"<i class='ace-icon fa fa-eye'></i></button>" +
						"</div>";
		    	    	return operation;
		    		}}
			    ]
			});
			viewSelf.dt = dt;
		}

	});
	module.exports = view;
});