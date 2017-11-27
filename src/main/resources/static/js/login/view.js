define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #signIn" : "signIn"
		},
		initialize: function() { /** 初始化 */
		},
		render: function() { /** 页面渲染 */
		},
		signIn : function(){
			var userName = $('#Username').val();
			var password = $('#Password').val();
			console.log(userName);
			$.ajax({
        		type : "post",
        		data : {
        			"userName" : userName,
        			"password" : password
        		},
        		async : false,
        		url :  "/loginPost",
        		success : function(result){
        			console.log(result.code);
        			if(result.code==200){
        				window.location.href="userMng";
        			}else{
        				alert(result.msg);
        			}
        		}
        	});
		}
		
	});
	module.exports = view;
});