define(function(require, exports, module) {
	var view = Backbone.View.extend({
		el: "body",
		events: {
		},
		initialize: function() { /** 初始化 */
			this.render();
		},
		render: function() { /** 页面渲染 */
			this.initLi;
		},
		initLi:function(){
			alert(1111111111);
			$.ajax({
				type : "post",
        		async : false,
        		url :  "userMng/isManager",
        		success : function(result){
        			if(result.code==200){
        				if(result.body=='1'){//管理员
        					
        				}else{
        					$('#userMng').hide();
        				}
        			}else{
        				window.location.href="/login";
        			}
        		}
			});
		}
		
		
	});
	module.exports = view;
});