$(document).ready(function() {
	// -=============== dataTables本地化 =================-
	if ($.fn.dataTable) {
		$.extend(true, $.fn.dataTable.defaults, {
			"bProcessing": false, // 禁用正在处理提示(由于太丑)
	        "bSort": false, // 禁用客户端排序(没有必要)
	        "bServerSide": true, // 启用服务端模式
	    	"sServerMethod": "POST", // 解决参数乱码问题
	    	"oLanguage": {
				"sLengthMenu": "显示 _MENU_ 条记录",
				"sSearch": "查询：",
				"sInfo": "显示第 _START_ - _END_ 条记录，共 _TOTAL_ 条",
				"sInfoEmpty": " ",
				"sZeroRecords": "没有符合条件的记录",
				"sEmptyTable": "没有符合条件的记录",
				"oPaginate": {
                    "sFirst": " 首页  ",
                    "sPrevious": " 前一页  ",
                    "sNext": " 后一页  ",
                    "sLast": " 尾页  "
                }
				
			},
			"fnServerData1": function ( sUrl, aoData, fnCallback, oSettings ) {
				var _this = this;
				var _clone = function(origin){
					if(origin){
						var arr = new Array();
						$(origin).each(function(index,value){
							arr[index] = $.extend({},value);
						});
						return arr;
					}
					return undefined;
				}
				
				var _keys = ["sEcho","iColumns","sColumns","iDisplayStart","iDisplayLength"];
			
				if(_this.submitSearchFlag || _this.aoDataCache===undefined){
					_this.aoDataCache = _clone(aoData);
				}
				
				if(_this.aoDataCache){
					for(var index in _this.aoDataCache){
						var nv = _this.aoDataCache[index];
						if($.inArray(nv.name,_keys)>=0){
							$(aoData).each(function(i,_data){
								if(_data.name==nv.name){
									nv.value = _data.value;
								}
							});
						}
					}
				}
				var dataToSubmit = _this.aoDataCache;
				
				this.submitSearchFlag = false;
				oSettings.jqXHR = $.ajax( {
					"url":  sUrl,
					"data": dataToSubmit,
					"success": function (json) {
						if ( json.sError ) {
							oSettings.oApi._fnLog( oSettings, 0, json.sError );
						}
						
						$(oSettings.oInstance).trigger('xhr', [oSettings, json]);
						fnCallback( json );
					},
					"dataType": "json",
					"cache": false,
					"type": oSettings.sServerMethod,
					"error": function (xhr, error, thrown) {
						if ( error == "parsererror" ) {
							oSettings.oApi._fnLog( oSettings, 0, "DataTables warning: JSON data from "+
								"server could not be parsed. This is caused by a JSON formatting error." );
						}
					}
				} );
			}
            
		});
		
//		$.fn.dataTableExt.oApi.submitSearch = function(oSettings, mAction){
//			mAction = mAction || 0;
//			this.submitSearchFlag = true;
//        	this.fnPageChange(mAction);
//		}
		
	}
	//-=============== bootbox本地化 =================-
	if(bootbox){
		try {
			bootbox.setDefaults({
				locale : "zh_CN"
			});
		} catch (e) {
			// TODO: handle exception
		}
	}
	
	$(document).on("mouseover","button[title]",function(){
		//IE9/10 disabled 无效
		var $this=$(this);
		if($this.prop("disabled")){
			$this.data("title",$this.attr("title"));//保存title值
			$this.attr("title","");//清空title
			return ;
		}else{
			var title=$this.data("title");
			if(title){
				$this.attr("title",$this.data("title"));//重置title
				$this.removeData("title");
			}
		}
		//IE9/10 disabled 无效
		$(this).tooltip({
			//placement:"bottom",
			placement:function (context, source) {
				//sidebar中提示特殊处理位置，防止被遮盖
				if($(source).closest("div.sidebar").length>0){
					var offset = $(source).offset();
					if( parseInt(offset.left) < parseInt($("#sidebar").get(0).scrollWidth / 2) ) return 'right';
					return 'left';
				}
				return 'top';
			},
			html:true,
			template:'<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner" style="white-space:nowrap;"></div></div>'
		})
		$(this).tooltip("show");
	});
	
	//FIXME 此段应由css控制
	var sideDive = function(){
		var main_content = $(document).find("div[class='main-content']");
		if($("#sidebar")&&$("#sidebar").length>0){
		var i = setInterval(function(){
				if($("#sidebar").attr("class").indexOf("menu-min") >0){
					$(main_content).css({
						"margin-left" : "43px"
					});
				}else{
					$(main_content).removeAttr("style");
				}
			
		}, 100);
		}
	}
	sideDive();
	
if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function(elt /* , from */) {
		var len = this.length >>> 0;
		var from = Number(arguments[1]) || 0;
		from = (from < 0) ? Math.ceil(from) : Math
				.floor(from);
		if (from < 0)
			from += len;
		for (; from < len; from++) {
			if (from in this && this[from] === elt)
				return from;
		}
		return -1;
	};
}  
	
	$(document).on("click", "#singleSignOut", function() {
		open(location, '_self').close();
	});
	
});