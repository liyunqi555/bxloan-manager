define(function (require, exports, module){
	var rm = {
			rules: {
				name : {
					required : true
				},
				ifSpecial : {
					required : true,
				},
				docColumnCdMask : {
					required : true,
				}
			},
			messages : {
				name : {
					required : "该项必填！"
				},
				ifSpecial : {
					required : "该项必填！",
				},
				docColumnCdMask : {
					required :  "该项必填！",
				}
			}
		}
		module.exports = rm;
		
});
