define(function(require, exports, module) {
    var _self = this;
    var utils = _u = {};
    _u.str = {
        substringBefore: function(str, seperator) {
            return str.substring(0, str.indexOf(seperator));
        },
        substringBeforeLast: function(str, seperator) {
            return str.substring(0, str.lastIndexOf(seperator));
        },
        substringAfter: function(str, seperator) {
            return str.substring(str.indexOf(seperator) + seperator.length);
        },
        substringAfterLast: function(str, seperator) {
            return str.substring(str.lastIndexOf(seperator) + seperator.length);
        },
        contains: function(str, word) {
            return str.indexOf(word) > -1;
        },
        notContains: function(str, word) {
            return ! this.contains(str, word);
        },
        /**
         * 获得字符串实际长度，中文2，英文1
         * @param str
         * @returns {Number}
         * TODO
         */
        getLength:function (str) {
            ///<summary>获得字符串实际长度，中文2，英文1</summary>
            ///<param name="str">要获得长度的字符串</param>
            var realLength = 0, len = str.length, charCode = -1;
            for (var i = 0; i < len; i++) {
                charCode = str.charCodeAt(i);
                if (charCode >= 0 && charCode <= 128) realLength += 1;
                else realLength += 2;
            }
            return realLength;
        },
        /**
         * js截取字符串，中英文都能用<br/>
         * 如果给定的字符串大于指定长度，截取指定长度返回，否者返回源字符串
         * @param str	需要截取的字符串 
         * @param len	需要截取的长度 
         * @returns
         */
        subStrForEllipsis:function cutstr(str, len) {
        	if(!str){
        		return '';
        	}
            var str_length = 0;
            var str_len = 0;
            str_cut = new String();
            str_len = str.length;
            for (var i = 0; i < str_len; i++) {
                a = str.charAt(i);
                str_length++;
                if (escape(a).length > 4) {
                    //中文字符的长度经编码之后大于4  
                    str_length++;
                }
                if(i+1 == str_len){
                	return str;
                }
                str_cut = str_cut.concat(a);
                if (str_length >= len) {
                    str_cut = str_cut.concat("...");
                    return str_cut;
                }
            }
            //如果给定字符串小于指定长度，则返回源字符串；  
            if (str_length < len) {
                return str;
            }
            return str;
        }
    };
    _u.fileupload = {
        init: function(fileUploadElementId, allow) {
            $("#" + fileUploadElementId).ace_file_input({
                no_file: "请上传文件..",
                btn_choose: "选择",
                btn_change: "重选",
                droppable: false,
                onchange: null,
                thumbnail: false,
                before_change: function(files, dropped) {
                    return _u.fileupload._filter(files, allow);
                }
            });
        },
        _filter: function(files, allow) {
            var uploadFileName = files[0].name;
            var suffix = _u.str.substringAfterLast(uploadFileName, ".");
            if (_u.str.contains(uploadFileName, ".")) { // 判断是否包含.
                if (_u.str.contains(allow, suffix)) { // 判断是否在允许的后缀名范围内
                    return true;
                } else {
                    bootbox.alert("只允许上传后缀名为" + allow + "的文件！");
                }
            } else {
                bootbox.alert("上传文件必须包含后缀名！");
            }
            return false;
        }
    };
    _u.dd = {
        initDataDict: function(codeTypes, callback) {
            $.post($$ctx + "loadDataDict", {
                codeTypes: codeTypes
            },
            function(dataDict) {
                callback(dataDict);
            });
        },
        translateDict: function(codeType, codeValue, callback) {
            $.ajax({
            	  type: "POST",
            	  url: $$ctx + "loadDataDict/translate",
            	  dataType: "text",
            	  data:{
                      codeType: codeType,
                      codeValue: codeValue
                  },
                  async:false,
                  success: function(dictName){
                	  callback(dictName);
                  },
                  error: function(request) {
                      _u.alert.err("加载字典表名称错误");
                  }
        	});
        },
        getCodeVal: function(codeType, codeKey, callback) {
            $.ajax({
	          	  type: "POST",
	          	  url: $$ctx + "loadDataDict/getCodeVal",
	          	  dataType: "text",
	          	  data: {
	          		  codeType: codeType,
	          		  codeKey: codeKey
	              },
	              async: false,
	              success: function(dictVal){
	                callback(dictVal);
	              },
                  error: function(request) {
                      _u.alert.err("加载字典表值错误");
                  }
            });
        }
    };

    _u.forms = {
        putValueToForm: function(dataToPut, formSelector) { // 将json数据填充到表单
            $.each(dataToPut,
            function(k, v) {
                var checkBoxes = $(formSelector).find("input[type='checkbox'][name='" + k + "']");
                var radios = $(formSelector).find("input[type='radio'][name='" + k + "']");

                if (checkBoxes[0] || radios[0]) {
                    if (v) {
                        $.each(v.toString().split(","),
                        function(i, val) {
                            if (val) {
                                var check = $(formSelector).find("input[type='checkbox'][name='" + k + "'][value='" + val + "']")[0] || $(formSelector).find("input[type='radio'][name='" + k + "'][value='" + val + "']")[0];
                                if (check) {
                                    check.checked = true;
                                }
                            }
                        });
                    }
                } else {
                    $(formSelector).find("input[name='" + k + "']").val(v);
                    $(formSelector).find("select[name='" + k + "']").val(v);
                    $(formSelector).find("textarea[name='" + k + "']").val(v);
                }
            });
        },
        disableForm: function(formSelector) {
            $(formSelector).find("input[type='text']").attr("readonly", "readonly");
            $(formSelector).find("input[type='checkbox']").attr("disabled", "disabled");
            $(formSelector).find("input[type='radio']").attr("disabled", "disabled");
            $(formSelector).find("select").attr("disabled", "disabled");
        }
    };
    _u.button = {
        loading: function(name) {
            var selector = name;
            $(selector).button('loading');
            setTimeout(function() {
                $(selector).button('reset');
            },
            5000);
        },
        ban: function(selector) {
            $(selector).button('loading');
        },
        reset: function(selector) {
            $(selector).button('reset');
        },
        confirm: function(selector, func,msg) {
            var $this = $(selector);
            if (bootbox.confirm({
                message: msg?msg:"确定要删除此条数据吗 ?",
                buttons: {
                    confirm: {
                        label: "<i class='ace-icon fa fa-trash-o bigger-110'></i> 确定",
                        className: "btn-danger btn-sm"
                    },
                    cancel: {
                        label: "<i class='ace-icon fa fa-times bigger-110'></i> 取消",
                        className: "btn-warning btn-sm"
                    }
                },
                callback: function(result) {
                    func(result);
                }
            }));
        }
    };
    _u.alert = {
        warn: function(content, callback) {
            var _content = '<span class="badge badge-transparent" >' + '<i class="ace-icon fa fa-exclamation-triangle orange" style="font-size: 400% !important;"></i>' + '</span>' + content;
            bootbox.alert(_content, callback);
        },
        suc: function(content, callback) {
            var _content = '<span class="badge badge-transparent" >' + '<i class="ace-icon fa fa-check-square green" style="font-size: 400% !important;"></i>' + '</span>' + content;
            bootbox.alert(_content, callback);
        },
        err: function(content, callback) {
            var _content = '<span class="badge badge-transparent" >' + '<i class="ace-icon fa fa-times-circle red" style="font-size: 400% !important;"></i>' + '</span>' + content;
            bootbox.alert(_content, callback);
        }
    };
    _u.datatable = {
        fresh: function(selector) {
            var datatable = $(selector).dataTable();
            var nowPage = $(selector).siblings("div.row").find("li.active a").text();
            var aa = $(selector).siblings("div.row").find("li.active a");
            var trs = $(selector + " tbody tr").length - 1;
            if (trs > 0) {
                datatable.fnDraw();
                aa.click();
            } else {
                datatable.fnDraw();
                aa.parent().prev().click();
            }
            return datatable;
        }
    };
    _u.upload = {
        createThead: function(r) {
            if (r != null) {
                var num = parseInt(r.count);
                var turn = 3;
                var thead3 = "<thead><th width='5%'>选择</td><th width='23%'>上传内容</td><th width='5%'>数量</td><th width='5%'>选择</td><th width='23%'>上传内容</td><th width='5%'>数量</td><th width='5%'>选择</td><th width='23%'>上传内容</td><th width='5%'>数量</td></thead>";
                var thead2 = "<thead><th width='10%'>选择</td><th width='30%'>上传内容</td><th width='10%'>数量</td><th width='10%'>选择</td><th width='30%'>上传内容</td><th width='10%'>数量</td></thead>";
                var thead1 = "<thead><th width='15%'>选择</td><th width='50%'>上传内容</td><th >数量</td></thead>";
                if (num >= 3 && num != 4) {
                    $("#tb_doc_selector").prepend(thead3);
                } else if (num == 4 || num == 2) {
                    $("#tb_doc_selector").prepend(thead2).attr("style", "width:75%;margin:0 auto");;
                    turn = 2;
                } else if (num < 2) {
                    $("#tb_doc_selector").prepend(thead1).attr("style", "width:50%;margin:0 auto");
                    turn = 1;
                }

                var $tbody = $("#uploadTbody");
                var count = 0;
                var total = 0;
                var html = "<tr>";
                var map = r.custMap;
                for (var key in map) {
                    total++;
                    count++;
                    var str = "<td>" + "<input type='radio' name='uploadDoc' value='" + key + "' />" + "</td>" + "<td>" + map[key] + "</td>" + "<td><span name='custSpan_" + key + "'" + ">" + "</span></td>";
                    html += str;
                    if (count == turn || total == r.count) {
                        html += "</tr>";
                        $tbody.html($tbody.html() + html);
                        count = 0;
                        html = "<tr>";
                    }
                }
            }
        },
        beforeUpload: function(url, uploadParams) {
            var fromData = null;
            $.ajax({
                cache: true,
                type: "POST",
                url: $$ctx + url,
                data: uploadParams,
                async: false,
                error: function(request) {
                    _u.alert.err("初始化上传报错,错误" + request.status);
                },
                success: function(r) {
                    fromData = r;
                }
            });
            return fromData;
        },
        initUploadify: function(formData, path, btn, onStart, onOneSuc, onQueueEnd, modalDiv) {
            var viewSelf = this;
            var queueNum = 15;
            if (typeof modalDiv == "number" && modalDiv < 15) {
                queueNum = modalDiv;
            }
            $(btn).uploadify({
                // debug: true,
                swf: $$ctx + "static/assets/js/uploadify/uploadify.swf",
                uploader: // "http://172.16.49.33:8080/RmiServer/mutilFileuploadAction.action",
                $(path).val() + "/uploadFileAction.action?SYS_FLAG=bxloan",
                // $("#uploadPathField").val() +
                // "/mutilFileuploadAction.action?SYS_FLAG=bxloan",
                buttonText: "请选择文件",
                auto: false,
                fileObjName: "file",
                // 必须为file
                removeTimeout: 1,
                queueSizeLimit: queueNum,
                // 必须为file
                fileTypeExts: "*.doc; *.docx; *.xls; *.xlsx; *.pdf; *.jpg; *.png; *.gif; *.rar;",
                fileTypeDesc: "请选择",
                formData: formData,
                onSelectError: function(file, errorCode, errorMsg) {
                    switch (errorCode) {
                    case - 100 : utils.alert.warn("上传的文件数量已经超出系统限制");
                        break;
                    case - 110 : utils.alert.warn("文件 大小超出系统限制的" + $('#uploadFile').uploadify('settings', 'fileSizeLimit') + "大小！");
                        break;
                    case - 120 : utils.alert.warn("文件 [" + file.name + "] 大小异常！");
                        break;
                    case - 130 : utils.alert.warn("文件 [" + file.name + "] 类型不正确！");
                        break;
                    default:
                        break;
                    }
                },
               /* onFallback: function() {
                    utils.alert.warn("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。");
                    return false;
                },*/
                onUploadStart: function(file) {
                    if (onStart) {
                        onStart(file, formData);
                    }
                    $(btn).uploadify("settings", "formData", formData);
                    $(btn).uploadify("disable", true);
                },
                onUploadSuccess: function(file, data, response) {
                    if (response) {
                        var obj = eval('(' + data + ')');
                        if (obj.success) {
                            if (onOneSuc) {
                                onOneSuc(obj);
                            }
                        } else {
                            if (modalDiv && typeof modalDiv == "string") {
                                // $(modalDiv).modal("hide");
                            } else {
                                // $("#add-modal-formWd").modal("hide");
                            }
                            _u.alert.err("<strong>" + obj.msg + "</strong>", function(){
                            	$("#documentType").val("");	//清空选中的文档类型
                            });
                        }
                    }
                    $(btn).uploadify("disable", false); // 按钮还原
                },
                onQueueComplete: function(queueData) {
                    if (onQueueEnd) {
                        onQueueEnd(queueData);
                    }
                    if ($("input[name='uploadDoc'][type='radio']:checked")[0]) {
                        $("input[name='uploadDoc'][type='radio']:checked").removeAttr("checked");
                    }
                    $(btn).uploadify('cancel', '*'); // 清空队列
                },
                onUploadError: function(file, errorCode, errorMsg, errorString) {
                    $(btn).uploadify("disable", false); // 按钮还原
                }
            });
        },
        refreshSelectorTable: function(url, params) {
            $.post($$ctx + url, params,
            function(r) {
                var $spans = $("#tb_doc_selector span");
                $spans.each(function(i, e) {
                    $(e).text("0");
                });
                if (r != null) {
                    for (var count = 0; count < r.length; count++) {
                        var $span = $("#tb_doc_selector").find("span[name= 'custSpan_" + r[count][0] + "']");
                        if ($span) {
                            $span.text(r[count][1]);
                        }
                    }
                }
            });
        },
        changeDocumentType: function(docType, allDocType) {
            switch (docType+'') {
            case "44":
                {
                    return "45";
                    break;
                }
            case "45":
                {
                    return "06";
                    break;
                }
            case "51":
                {
                    return "40";
                    break;
                }
            case "52":
                {
                    return "41";
                    break;
                }
            case "53":
                {
                    return "42";
                    break;
                }
            case "54":
                {
                    return "44";
                    break;
                }
            case "57":
                {
                    return "13";
                    break;
                }
            case "58":
                {
                    return "14";
                    break;
                } // 14
            case "59":
                {
                    return "15";
                    break;
                }
            case "60":
                {
                    return "16";
                    break;
                }
            case "61":
                {
                    return "17";
                    break;
                }
            case "63":
                {
                    return "29";
                    break;
                }
            case "74"://放款核实
            {
            	return "43";//其他
            	break;
            }
            case "75"://放款合影照片
            {
            	return "43";//其他
            	break;
            }
            default:
                {
                    if ($.inArray(allDocType, ["005", "006"]) > 0) {
                        return "02";
                    } else if ($.inArray(allDocType, ["007", "008", "009", "010", "011"]) > 0) {
                        return "44";
                    } else {
                        return "44";
                    }
                }
            }
        },
        changeAllDocType: function(taskStageCode) {
            switch (parseInt(taskStageCode)) {
            case 100311:
                {
                    return "007";
                    break;
                } // 易贷评审
            case 100312:
                {
                    return "009";
                    break;
                } // 易贷初审
            case 100314:
	            {
	            	return "012";
	            	break;
	            } // 易贷合同
            case 100315:
                {
                    return "010";
                    break;
                } // 易贷落实放款
            case 100411:
                {
                    return "008";
                    break;
                } // 贷款审查
            case 100412:
	            {
	            	return "009";
	            	break;
	            } // 微贷初审
            case 100417:
	            {
	            	return "012";
	            	break;
	            } // 微贷合同
            case 100419:
                {
                    return "011";
                    break;
                } // 微贷落实放款
            case 100711:
        		{
               		return "008";
               		break;
        		} //授信审批流程-贷款审查
            case 100712:
            	{
	                return "009";
	                break;
            	} //授信审批流程-初审环节
            case 100716:
	            {
	            	return "012";
	            	break;
	            } //授信审批流程-制定电子合同
        	case 100718:
	            {
	                return "011";
	                break;
	            } //授信审批流程-签订合同
        	case 100811:
	             {
	                 return "008";
	                 break;
	             } //授信下借款 贷款审查
            case 100812:
	            {
	            	return "009";
	            	break;
	            } // 授信借款 初审
            case 100813:
	            {
	            	return "012";
	            	break;
	            } // 授信借款 签订合同
            case 100815:
	             {
	                 return "011";
	                 break;
	             } //授信借款  落实放款条件
            case 101227:
            	{
            		return "019";
            		break;
            	} //小贷普通业务  签订合同	
            case 102027:
        	{
        		return "019";
        		break;
        	} //类典当普通业务  签订合同	
            case 101229:
	            {
	                return "018";
	                break;
	            } //小贷普通业务  落实放款条件
            case 102029:
            {
                return "018";
                break;
            } //类典当业务  落实放款条件
            case 101211:
            	{
            		return "022";
            		break;
            	} //小贷普通业务  业务总监复核
            case 102011:
        	{
        		return "022";
        		break;
        	} //类典当普通业务  业务总监复核
            case 103511:
        	{
        		return "022";
        		break;
        	} //房抵贷普通业务  审批师审查
            case 103611:
        	{
        		return "022";
        		break;
        	} //房抵贷授信业务  审批师审查
            case 101212:
        		{
        			return "023";
        			break;
        		} //小贷普通业务  风险经理初审
            case 102012:
    		{
    			return "023";
    			break;
    		} //类典当普通业务  风险经理初审
            case 101213:
            	{
            		return "024";
            		break;
            	} //小贷普通业务  贷审会委员审批
            case 102013:
        	{
        		return "024";
        		break;
        	} //类典当普通业务  贷审会委员审批
            case 101214:
	        	{
	        		return "025";
	        		break;
	        	} //小贷普通业务  拟写贷审会批复
            case 102014:
        	{
        		return "025";
        		break;
        	} //类典当普通业务  拟写贷审会批复
            case 101215:
	            {
	            	return "026";
	            	break;
	            } //小贷普通业务  总经理审批
            case 102015:
            {
            	return "026";
            	break;
            } //类典当普通业务  总经理审批
            case 101218:
	            {
	            	return "027";
	            	break;
	            } //小贷普通业务  安排贷审会
            case 102017:
            {
            	return "27";
            	break;
            } //类典当制定合同
            case 103514:
            {
            	return "27";
            	break;
            } //房抵贷 面签公证抵押登记
            case 103614:
            {
            	return "27";
            	break;
            } //房抵贷授信  面签公证抵押登记
            case 102018:
            {
            	return "027";
            	break;
            } //类典当普通业务  安排贷审会
	            {
	            	return "028";
	            	break;
	            } //小贷普通业务  小贷事业部贷审会委员审批
            case 102019:
            {
            	return "028";
            	break;
            } //类典当普通业务  小贷事业部贷审会委员审批
            case 101220:
	            {
	            	return "029";
	            	break;
	            } //小贷普通业务  安排总部联席会
            case 102020:
            {
            	return "033";
            	break;
            } //类典当普通业务  安排总部联席会
            case 101221:
	            {
	            	return "030";
	            	break;
	            } //小贷普通业务  总部联席会委员审批
            case 102021:
            {
            	return "033";
            	break;
            } //类典当普通业务  总部联席会委员审批
            case 101222:
	            {
	            	return "031";
	            	break;
	            } //小贷普通业务  批复结论录入
            case 102022:
            {
            	return "033";
            	break;
            } //类典当普通业务  批复结论录入
            case 101223:
	            {
	            	return "032";
	            	break;
	            } //小贷普通业务  复核批复
            case 102023:
            {
            	return "033";
            	break;
            } //类典当普通业务  复核批复
            case 101224:
	            {
	            	return "033";
	            	break;
	            } //小贷普通业务  总部分管领导审批
            case 102024:
            {
            	return "033";
            	break;
            } //类典当普通业务  总部分管领导审批
            case 101225:
	            {
	            	return "034";
	            	break;
	            } //小贷普通业务  总部总经理审批
            case 102025:
            {
            	return "033";
            	break;
            } //类典当普通业务  总部总经理审批
            case 103513:
            {
            	return "033";
            	break;
            }
            case 103613:
            {
            	return "033";
            	break;
            } 
            case 101226:
	            {
	            	return "035";
	            	break;
	            } //小贷普通业务  下发批复
            case 102026:
            {
            	return "033";
            	break;
            } //类典当普通业务  下发批复
            case 101228:
	            {
	            	return "036";
	            	break;
	            } //小贷普通业务  总经理审核合同
            case 102028:
            {
            	return "033";
            	break;
            } //小贷普通业务  总经理审核合同
            case 101230:
	            {
	            	return "037";
	            	break;
	            } //小贷普通业务  审核贷款条件
            case 102030:
            {
            	return "201";
            	break;
            } //类典当普通业务  审核贷款条件
            case 103515:
            {
            	return "033";
            	break;
            } //房抵贷 普通业务  放款审核他项归档
            case 103615:
            {
            	return "033";
            	break;
            } //房抵贷 授信业务  放款审核他项归档
            case 101232:
            {
            	return "061";
            	break;
            } //小贷普通业务  放款中心电话核查
            case 102032:
            {
            	return "061";
            	break;
            } //类典当普通业务  放款中心电话核查
            case 103517:
            {
            	return "061";
            	break;
            } //房抵贷普通业务  签批放款
            case 103617:
            {
            	return "061";
            	break;
            } //房抵贷普通业务  签批放款
            case 101233:
            {
            	return "062";
            	break;
            } //小贷普通业务  放款中心初审
            case 102033:
            {
            	return "062";
            	break;
            } //类典当普通业务  放款中心初审
            case 103512:
            {
            	return "062";
            	break;
            } //房抵贷普通业务   风险总监审批
            case 103612:
            {
            	return "062";
            	break;
            } //房抵贷普通业务   风险总监审批
            case 101234:
            {
            	return "063";
            	break;
            } //小贷普通业务  放款中心复核
            case 102034:
            {
            	return "063";
            	break;
            } //类典当普通业务  放款中心复核
            case 101235:
            {
            	return "064";
            	break;
            } //小贷普通业务 确认放款 
            case 102035:
            {
            	return "033";
            	break;
            } //类典当普通业务 确认放款   
            case 101526:
            {
            	return "038";
            	break;
            } //小贷授信业务  签订合同
            case 101612:
            {
            	return "039";
            	break;
            } //小贷授信下借款  签订合同
            case 101527:
            {
            	return "040";
            	break;
            } //小贷授信落实放款
            case 101614:
            {
            	return "041";
            	break;
            } //小贷授信下借款落实放款
        case 101511:
    		{
    			return "043";
    			break;
    		} //小贷授信业务  风险经理初审
        case 101512:
        	{
        		return "044";
        		break;
        	} //小贷授信业务  贷审会委员审批
        case 101513:
        	{
        		return "045";
        		break;
        	} //小贷授信业务  拟写贷审会批复
        case 101514:
            {
            	return "046";
            	break;
            } //小贷授信业务  总经理审批
        case 101517:
            {
            	return "047";
            	break;
            } //小贷授信业务  安排贷审会
        case 101518:
            {
            	return "048";
            	break;
            } //小贷授信业务  小贷事业部贷审会委员审批
        case 101519:
            {
            	return "049";
            	break;
            } //小贷授信业务  安排总部联席会
        case 101520:
            {
            	return "050";
            	break;
            } //小贷授信业务  总部联席会委员审批
        case 101521:
            {
            	return "051";
            	break;
            } //小贷授信业务  批复结论录入
        case 101522:
            {
            	return "052";
            	break;
            } //小贷授信业务  复核批复
        case 101523:
            {
            	return "053";
            	break;
            } //小贷授信业务  总部分管领导审批
        case 101524:
            {
            	return "054";
            	break;
            } //小贷授信业务  总部总经理审批
        case 101525:
            {
            	return "055";
            	break;
            } //小贷授信业务  下发批复
        case 101528:
            {
            	return "056";
            	break;
            } //小贷授信业务  总经理审核合同
        case 101529:
            {
            	return "057";
            	break;
            } //小贷授信业务  审核贷款条件
        case 101531:
        {
        	return "065";
        	break;
        } //小贷授信业务  放款中心电话核查    
            
            
        case 101611:
	        {
	        	return "058";
	        	break;
	        } //小贷授信下借款  放款条件审批
	    case 101613:
	        {
	        	return "059";
	        	break;
	        } //小贷授信下借款  总经理审核合同
	    case 101615:
	        {
	        	return "060";
	        	break;
	        } //小贷授信下借款  审核放款条件
            }
        }
    };
    _u.num = {
        thousands: function(selector) {
            var value = $(selector).val();
            if (!value) {
                value = selector;
            }
            var thousands = "";
            if (value && typeof value === "string") {
                thousands = (parseFloat(value.split(",").join("")) + "");
            }
            var num = parseFloat(thousands);
            if (!isNaN(num)) {
                thousands = thousands.replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
            } else {
                thousands = value;
            }
            $(selector).val(thousands);
            return thousands;
        },
        thousandsFormat: function(val) {//千分位格式化
        	var value=val
            if (!value) {
                value = val;
            }
            var thousands = "";
            if (value && typeof value === "string") {
                thousands = (parseFloat(value.split(",").join("")) + "");
            }
            var num = parseFloat(thousands);
            if (!isNaN(num)) {
                thousands = thousands.replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
            } else {
                thousands = value;
            }
            return thousands;
        },
        normal: function(value) {
           return  _u.number.toNum(value);
        },
        numberFormat: function() {
            var viewSelf = this;
            $.each($(document).find(".formatNum"),
            function(i, e) {
                var mask = this;
                var hidden = "<input type='hidden' name='" + $(mask).attr("name") + "' value='" + utils.num.normal($(mask).val()) + "' />";
                if (!$(mask).siblings("input:hidden[name='" + $(mask).attr("name") + "']")) {
                    $(mask).parent().append(hidden);
                }

                var $form = $(mask).closest("form");

                $(mask).on("blur",
                function(e) {
                    var nElement = $(this).parents("div .input-group")[0];
                    var _hidden = $(nElement).find("input[type='hidden']")[0];
                    var num = viewSelf.thousands(mask);
                    $(_hidden).val(viewSelf.normal($(mask).val()));
                    $form.validate().element($(_hidden));
                });

                $(document).ajaxStop(function() {
                    $(mask).removeAttr("name");
                    $(mask).blur();
                });
            });
        },
        tableFormat: function(selector) { // 对表格内的纯数字列加逗号
            $(selector).find("td:parent").each(function(i, e) {
                var regex = /^(\d+)(\.?)\d+$/;
                if (regex.exec(e.innerHTML)) {
                    var formatNum = utils.num.thousands(e.innerHTML);
                    e.innerHTML = formatNum;
                }
            });
        },
        colsFormat: function(selector, cols) { // 对表格内指定数字列加逗号，索引从1开始
            for (var i in cols) {
                if (!isNaN(cols[i])) {
                    $(selector).find("tr td:nth-child(" + cols[i] + ")").each(function(i, e) {
                        var regex = /^\d+$/;
                        if (regex.exec(e.innerHTML)) {
                            var formatNum = utils.num.thousands(e.innerHTML);
                            e.innerHTML = formatNum;
                        }
                    });
                }
            }
        }
    };
    _u.number={
		commafy:function(num) {   
			//1.先去除空格,判断是否空值和非数   
			num = num + "";   
			num = num.replace(/[ ]/g, ""); //去除空格 
			if (isNaN(num)){  
		    	return '';   
		    }   
		    //2.针对是否有小数点，分情况处理   
		    var index = num.indexOf(".");   
		    if (index==-1) {//无小数点   
		      var reg = /(-?\d+)(\d{3})/;   
		        while (reg.test(num)) {   
		        num = num.replace(reg, "$1,$2");   
		        }   
		    } else {   
		        var intPart = num.substring(0, index);   
		        var pointPart = num.substring(index + 1, num.length);   
		        var reg = /(-?\d+)(\d{3})/;   
		        while (reg.test(intPart)) {   
		        intPart = intPart.replace(reg, "$1,$2");   
		        }   
		       num = intPart +"."+ pointPart;   
		    } 
			return num; 
		},
		delcommafy:function(num){  
			   num += "";
			   num = num.replace(/[ ]/g, "");//去除空格  
			   num = num.replace(/,/gi,'');  
			   return num;  
		},
		round:function(v,e){
			var t=1;
			for(;e>0;t*=10,e--);
			for(;e<0;t/=10,e++);
			return Math.round(v*t)/t;
		},
		/**
		 * 金额转换
		 * @param val 值
		 * @param defaultVal 默认值
		 * @returns 无defaultVal时，返回''
		 */
		toAmt:function(val,defaultVal){
			var _val=val+'';//转换为字符串
			_val = this.delcommafy(_val);//去掉逗号及空格
			if(typeof(defaultVal)!=undefined&&defaultVal!=null){
				_val=!isNaN(_val)?_val:((isNaN(defaultVal)?0:defaultVal)+'');//设置默认值
			}
			if(isNaN(_val)){
				return '';
			}
			//首先转换为数值类型
			return this.commafy(this.toFixed(_val,2));
		},
		/**
		 * 转数字
		 * @param v	待转换的值
		 * @returns
		 */
		toNum:function(v){
			v=this.delcommafy(v);
			if(v&&!isNaN(v)){
				v=parseFloat(v);
			}else{
				v=0;
			}
			return isNaN(v)?0:v;
		},
		/**
		 * 数字保留几位小数
		 * @param v	待格式化数值
		 * @param n	格式化位数
		 * @returns
		 */
		toFixed:function(v,n){
			if(!v){
				return 0.00;
			}
			return parseFloat(v).toFixed(n);
		}
    }

    /**
	 * 时间对象的格式化;
	 */
    Date.prototype.format = function(format) {
        var o = {
            "M+": this.getMonth() + 1,
            // month
            "d+": this.getDate(),
            // day
            "h+": this.getHours(),
            // hour
            "m+": this.getMinutes(),
            // minute
            "s+": this.getSeconds(),
            // second
            "q+": Math.floor((this.getMonth() + 3) / 3),
            // quarter
            "S": this.getMilliseconds()
            // millisecond
        };
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    };
    _u.date = {
        formatDate: function(value) {
            if (typeof(value) == 'undefined' || $.trim(value) == '') {
                return "";
            }
            return new Date(value).format("yyyy-MM-dd");
        },
        formatDateTime: function(value) {
            if (typeof(value) == 'undefined' || $.trim(value) == '') {
                return "";
            }
            return new Date(value).format("yyyy-MM-dd HH:mm:ss");
        },
        timestampToDate: function(timestamp,format) {//时间戳转换成日期格式
    		var viewSelf = this;
    		var timezone = 8;//时区
            var time = new Date(parseInt(timestamp) + parseInt(timezone) * 60 * 60 * 1000);
            var ymdhis = "";
            ymdhis += time.getUTCFullYear() + "-";
            ymdhis += (time.getUTCMonth()+1) + "-";
            ymdhis += time.getUTCDate();
            ymdhis += " " + time.getUTCHours() + ":";
            ymdhis += time.getUTCMinutes() + ":";
            ymdhis += time.getUTCSeconds();
            return new Date(Date.parse(ymdhis.replace(/-/g, "/"))).format(format);
        }

    };
    _u.validate = {
    	removeCss : function(form){
    		var validator = $(form).validate();
    		validator.resetForm();
    		$(form).find("input,select,textarea").closest("div.form-group").removeClass('has-error');
    	}	
    };
    //字符串转布尔
    _u.parseBool=function( _input ){
        if( typeof _input == 'string' ){
            _input = _input.replace( /[\s]/g, '' ).toLowerCase();
            if( _input && ( _input == 'false' 
                            || _input == '0' 
                            || _input == 'null'
                            || _input == 'undefined'
           )) _input = false;
           else if( _input ) _input = true;
        }
        return !!_input;
    };
    _u.downloadFile=function(url){
		var gotoLink = document.createElement('a');
		gotoLink.style.display = 'none';
		gotoLink.href = url;
	    document.body.appendChild(gotoLink);
	    gotoLink.click();
	    document.body.removeChild(gotoLink);
	},
	//add by wangpeng on 2016-03-21（关闭toggle的方法）
	_u.closeToggle=function(href){
		$target = $("a[href='#"+href+"']");
    	//toggle变灰
    	$target.addClass("collapsed");
    	//右侧箭头样式改为向左
    	var $i = $target.find("i[class='pull-right ace-icon fa fa-chevron-down']");
    	$i.removeClass("fa-chevron-down").addClass("fa-chevron-left");
    	//相邻节点高度调整
    	var $div = $target.parent();
    	$div.next().removeClass("collapse in").addClass("collapse");
	},
	/**
	 * 序列化当前元素下的所有表单
	 * return a=1&b=2
	 */
	_u.serializeParams=function($selector){//
		var params=[];
		$selector.find(":hidden,:text,:radio,select,textarea").each(function(){
			var type=this.type;//input域类型
			var $this=$(this);
			var name=$this.attr('name');
			var checkboxVals={};
			switch(type){
				case 'radio'://radio特殊处理
					if(name&&!$this.is(':disabled')&&$this.is(':checked')){
						params.push(name+'='+$this.val());
					}
					break;
				case 'checkbox'://checkbox特殊处理
					if(name&&!$this.is(':disabled')&&$this.is(':checked')){
						if(!checkboxVals[name]){
							checkboxVals[name] = [];
						}
						checkboxVals[name].push($this.val());
					}
					break;
				default:
					if(name&&!$this.is(':disabled')){
						params.push(name+'='+$this.val());
					}
			}
			//checkbox转为逗号隔开的字符串
			for(var k in checkboxVals){
				params.push(k+'='+checkboxVals[k].join(','));
			}
		});
		return params.join('&');
	},
	/**
	 * 把身份证 拆分为： 出生年月日、年龄、性别
	 */
	_u.idcardGetInfo=function(value){
    	var _idcard = value;
    	var array = [] ,birth = '',age = '', sex = 0;
    	
		var myDate = new Date();  
	    var month = myDate.getMonth() + 1;  
	    var day = myDate.getDate();
    	if(_idcard.length==15){
    		 birth = "19"+_idcard.substring(6, 8) + "/" + _idcard.substring(8, 10) + "/" + _idcard.substring(10, 12);
    		 sex = _idcard.substring(14,15)%2;
 	         age = myDate.getFullYear() - 1900 - _idcard.substring(6, 8) - 1;  
	        if (_idcard.substring(8, 10) < month || (_idcard.substring(8, 10) == month && _idcard.substring(10, 12) <= day)) {  
	            age++;
	        }
    	}else if(_idcard.length==18){
    		 birth = _idcard.substring(6, 10) + "/" + _idcard.substring(10, 12) + "/" + _idcard.substring(12, 14);
    		 sex = _idcard.substring(16,17)%2;
 	         age = myDate.getFullYear() - _idcard.substring(6, 10) - 1;  
	        if (_idcard.substring(10, 12) < month || (_idcard.substring(10, 12) == month && _idcard.substring(12, 14) <= day)) {
	            age++;  
	        }
    	}
    	array.push(birth,age,sex);
    	return array;
	},
	
    module.exports = utils;
});