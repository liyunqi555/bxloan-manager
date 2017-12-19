define(function(require, exports, module) {
	var utils = require("../common/utils.js");
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click button[role='btn-Query']":"query",
			"click button[role='btn-Reset']":"reset",
			"click button[role='btn-Export']":"exportExcel"
			
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			 /** 页面渲染 */
            this.initDataTables();
            if($('#type').val()=='1'){
            	$('#userName').attr("readonly",true);
            }
		},
		query : function(){
			var viewSelf = this;
			viewSelf.dt.fnPageChange(0);
		},
		exportExcel:function(){
    		var url='/userStoreMng/checkDownload';
			$.ajax({
				type:"get",
				url: url,
				data : {
					'userName' : $('#userName').val()
				},
				success:function(result){
					if(result.code=='200'){
						url='/userStoreMng/downloadExcel';
						var params=[];
						params.push("userName" + "=" +userName);
						if(params && params.length > 0) {
							url += ('?'+params.join('&'));
						}
						window.location.href = url;
					}else{
						utils.alert.warn(result.msg);
					}
				}
			});
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
			        {mData: "userName"},
			        {mData: "docName"},
			        {mData: "createTime"}
			    ]
			});
			viewSelf.dt = dt;
		}

	});
	module.exports = view;
});