define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
			"click #login-btn" : "signIn"
		},
		initialize: function() { /** 初始化 */
		},
		render: function() { /** 页面渲染 */
		},
		signIn : function(){
			var userName = $('#Username').val();
			var password = $('#password').val();
			if(!userName){
				alert("请输入用户名！");
				return false;
			}
			if(!password){
				alert("请输入密码！");
				return false;
			}
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
        				window.location.href="index";
        			}else{
        				alert(result.msg);
        			}
        		}
        	});
		}
		
	});
	module.exports = view;
});