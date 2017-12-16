define(function (require, exports, module){
	var rm = {
			rules: {
				title : {
					required : true
				},
				cnTitle : {
					required : true
				},
				classification : {
					required : true
				},
				groupName : {
					required : true
				},
				website : {
					required : true
				},
				keyword : {
					required : true
				},
				columnName : {
					required : true
				},
				sourceName : {
					required : true,
				},
				ifTop : {
					required : true,
				},
				summary : {
					required : true,
				},
				body : {
					required : true,
				},
				cnBoty : {
					required : true,
				}
			},
			messages : {
				title : {
					required :  "该项必填！"
				},
				cnTitle : {
					required :  "该项必填！"
				},
				classification : {
					required :  "该项必填！"
				},
				groupName : {
					required :  "该项必填！"
				},
				website : {
					required :  "该项必填！"
				},
				keyword : {
					required :  "该项必填！"
				},
				columnName : {
					required :  "该项必填！"
				},
				sourceName : {
					required :  "该项必填！",
				},
				ifTop : {
					required :  "该项必填！",
				},
				summary : {
					required :  "该项必填！",
				},
				body : {
					required :  "该项必填！",
				},
				cnBoty : {
					required :  "该项必填！",
				}
			}
		}
		module.exports = rm;
		
});
