/**

 * 2009-09-18 Update By Chenliang
 */

/**

 * 提交前验证:
 * submitForm(formID) 返回true通过验证，验证失败并在相应input上显示提示框
 * 
 * <input type="text" name="number" datatype="0,is_digit,4" value="">
 * 标签多了一个datatype属性，该属性含三部分：
 * 1.是否可为空 1-允许 0-不允许
 * 2.数据类型，只能是下表中的值
 * 3.最大长度
 * 4.最小长度(可选)
 * 
 * 可用的数据类型：
 * is_vin 验证VIN
 * is_null 验证空
 * is_textarea 验证文本域
 * is_carno 验证车牌号
 * is_name 验证名称
 * is_fdjh 发动机号
 * is_datetime 验证日期格式 YYYY-MM-DD hh:mm
 * is_date 日期类型，可匹配group使用。例：group="date_begin,date_end" 
 *         date_begin开始时间的ID，date_end结束时间的ID 加了is_date和group会检查开始时间不能大于结束时间
 * is_double 正浮点数 decimal 指定小数点后几位 decimal="2"
 * is_wan 验证万
 * is_yuan  验证圆
 * is_digit  全部由数字组成
 * is_letter_cn 由字母和中文组成
 * is_digit_letter_cn 由数字字母和中文组成
 * is_digit_letter 由数字和字母组成
 * is_email 是否Email
 * is_noquotation 不能输入 ',%
 * is_phone 电话
 × is_docNumber 文档编号
 * is_task_pri
 * modify by wjb start
 * is_labercode 验证工时代码
 * is_date_now 日期不能大于当前时间(今日)
 * isDigit  判断数字：0不能开头
 * isMoney_4  判断税率: 0不能开头，可以有小数但最多是四位
 * modify by wjb end
 * isMoney  判断金额: 0不能开头，可以有小数但最多是两位 
 * isDigitAll输入正负数字和小数点
 *  * isInvoiceNo  判断发票号码 
 *   * isVehicleNo  判断车牌号后六位


*/

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g,"");
}

String.prototype.getByteLength = function () {
  return this.replace(/[^\x00-\xff]/g,"00").length;
}

Array.prototype.removee = function(s) {   
    for (var i = 0; i < this.length; i++) {   
        if (s == this[i])   
            this.splice(i, 1);   
    }   
}   

function Map() {
    this.keys = new Array();   
    this.data = new Object();   
       
    this.put = function(key, value) {   
        if(this.data[key] == null){   
            this.keys.push(key);   
        }   
        this.data[key] = value;   
    };   
       
    this.get = function(key) {   
        return this.data[key];   
    };   

    this.remove = function(key) {   
        this.keys.removee(key);   
        this.data[key] = null;   
    };
       
    this.isEmpty = function() {   
        return this.keys.length == 0;   
    };   
       
    this.size = function(){   
        return this.keys.length;   
    };
}

var m = new Map();

//if (window.attachEvent) {
//	window.attachEvent('onload',initPage);
//} else {
//	window.addEventListener("load",initPage, false);
//}

function checkDate(sd,ed) {
	var startDate = sd.replace(/-/g,"/");
	var endDate = ed.replace(/-/g,"/");  
	if(Date.parse(startDate) - Date.parse(endDate) > 0) {
		return false;
	}
	return true;
}

var ieVer = getIEVer();


function initPage() {
	createTip();
	addListener();
}


function getTip() {
	if(validateobjarr != null && validateobjarr.length >0) {
		for(var i=0; i<validateobjarr.length; i++) {
			if(document.getElementById(validateobjarr[i]).style.visibility == 'hidden') {
				return validateobjarr[i];
			}
		}
	}
	return null;
}

function clearTip() {
	if(validateobjarr != null && validateobjarr.length >0) 
	{
		for(var i=0; i<validateobjarr.length; i++) 
		{
			document.getElementById(validateobjarr[i]).style.visibility = 'hidden';
		}
	}
}

function getIEVer() {
	return parseFloat(navigator.appVersion.split("MSIE")[1]);
}

function killErrors() {
return true;
}
//window.onerror = killErrors;

var oldInputStyle;
var oldImgStyle;

function submitForm(form)
{
	if(!checkFormInput(form)) 
	{
		return false;
	} else if(!checkFormSelect(form)){
		return false;
	} 
	else
	{
		//added by andy.ten@tom.com
		var fjTab = $("fileUploadTab");
		if(fjTab)
		{
			var s = "";
			if("undefined" != typeof(fjTab.rows)){
				if(fjTab.rows.length > 1)
				{
					for(var i=1;i<fjTab.rows.length;i++)
					{
						var fjid = fjTab.rows[i].getAttribute("FJID");
						if(s && s.length > 0)
					  		s += "," + fjid;
						else
					   		s = fjid;
					}
					
				}
				if($("fjids"))
				   	$("fjids").value = s;
			}
		}
		return true;
	}
}

function showTip(handle,msg,tipid) {
    document.getElementById(handle.id).attachEvent('onkeydown',  function() {
    	if(event.keyCode != 9) {
    		hideTip(tipid);
    	}
    });
 var pos = getPosition(handle);
 var t = pos.top;
 var l = pos.left;

 
 mytop = t;
 if(msg.indexOf("CL") > 0) {
 	mytop = mytop - msg.split("CL")[1];
 	msg = msg.split("CL")[0];
 } else {
 	if(msg.length > 0 && msg.length <= 10) {
	 	mytop = mytop -18;
	 } else if(msg.length >= 11 && msg.length <= 16) {
	 	mytop = mytop -30;
	 } else if(msg.length > 16) {
	 	mytop = mytop -40;
	 }
 }

 document.getElementById(tipid).style.position="absolute";  
 document.getElementById(tipid).style.left=l+60;
 document.getElementById(tipid).style.top=mytop-6;
 document.getElementById(tipid+"_msg").innerHTML="<font color='red'>"+msg+"</font>";
 document.getElementById(tipid).style.visibility = 'visible';
 if(ieVer <= 6) {
 	HideOverSels(tipid);
 }
 if(validateConfig.timeOut > 0) {
 	var timer = window.setTimeout("hideTip('"+tipid+"');", validateConfig.timeOut);
 	m.put(tipid,timer);
 }
 document.getElementById(handle.id).focus();
}

function addListener() {
	if(validateConfig.isOnBlur) 
	{
		var inputList = document.body.getElementsByTagName("input");
	    for(var i=0;i<inputList.length;i++) 
	    {
	    	if(inputList[i].getAttribute("datatype") != null) 
	    	{
	    		var temp = null;
	    		if(inputList[i].id)
	    		    temp = inputList[i].id;
	    		else
	    		{
	    			alert("input 框中没有写明ID属性");
	    			return false;
	    		}
	    		if (window.attachEvent) 
	    		{
	    			//modified by andy.ten@tom.com
					document.getElementById(temp).onblur = function()
					{
							myonblur(this.id);
							if(this.blurback && this.blurback == "true")
								blurBack(this.id);
					}
					//end
				} else 
				{
					//document.getElementById(temp).addEventListener("blur",alert(rrr), false);
				}
	    	}
	   }
		var textareaList = document.body.getElementsByTagName("textarea");
	    for(var j=0;j<textareaList.length;j++) {
	    	if(textareaList[j].getAttribute("datatype") != null) {
	    		var temp = textareaList[j].id;
	    		if (window.attachEvent) {
					document.getElementById(temp).onblur = function() { myonblur(this.id) }
				} else {
					//document.getElementById(temp).addEventListener("blur",alert(rrr), false);
				}
	    	}
	   }
	}
}

function blurBack(obj)
{
}
function myonblur(obj) {
	checkInput(obj);
}

function showErrMsg(oid,errmsg,y) {
	var tipid = getTip();
	showTip(document.getElementById(oid),errmsg+"CL"+y,tipid);
}

function checkInput(valid) {
	var tipid = getTip();
	if(tipid==null) {
		return false;
	}
	var issele = null;
	document.getElementById(valid).value = document.getElementById(valid).value.trim();
	var tmptypeStr = document.getElementById(valid).datatype;
	var str = tmptypeStr.split(",");
	var nullAble = str[0];
	var typeStr = str[1];
	var maxLength = str[2];
	 
	var errm = document.getElementById(valid).errmsg;
	 
	 minLength = "0";
	 if (str.length == 4) {
		 minLength = str[3];
	 }
	 if(document.getElementById(valid).value==null || document.getElementById(valid).value=="") {
		  if (nullAble == "0") {
				hideTip(tipid);
				if(document.getElementById(valid).hint==null || document.getElementById(valid).hint=="") {
					showTip(document.getElementById(valid),"此处不能为空.",tipid);
					issele == null ? issele = document.getElementById(valid).id : "";
				}
	      }
	  } else {
	  	if(typeStr == "is_yuan" || typeStr == "is_wan") {
	  		maxLength = 1000;
	  	}
	  	if(typeStr == "is_phone") {
	  		maxLength = 15;
	  	}
	  
		  if(typeStr == "is_date") {
		  	if(document.getElementById(valid).group) {
		  		var gr = document.getElementById(valid).group;
		  		var sdd = gr.split(",");
		  		var startd = document.getElementById(sdd[0]).value;
		  		var endd = document.getElementById(sdd[1]).value;
		  		if(startd != null && startd != "" && endd != null && endd != "") {
		  			if(!checkDate(startd,endd)) {
		  				hideTip(tipid);
						showTip(document.getElementById(sdd[1]),"开始时间不能大于结束时间.",tipid);
						issele == null ? (issele = document.getElementById(valid).id) : "";
						rest = false;
		  			}
		  		}
		  	}
		  }
		  //wjb add by 2010-07-12 begin
		  if(typeStr == "is_date_now") {
		  	if(document.getElementById(valid)) {
		  		var valDate = document.getElementById(valid).value;
		  		var nowDate = (new Date()).Format("yyyy-MM-dd");
		  		if(valDate != null && valDate != "" && nowDate != null && nowDate != "") {
		  			if(!checkDate(valDate,nowDate)) {
		  				hideTip(tipid);
						showTip(document.getElementById(valid),"不能大于当前日期.",tipid);
						issele == null ? (issele = document.getElementById(valid).id) : "";
						rest = false;
		  			}
		  		}
		  	}
		  	if(document.getElementById(valid).group) {
		  		var gr = document.getElementById(valid).group;
		  		var sdd = gr.split(",");
		  		var startd = document.getElementById(sdd[0]).value;
		  		var endd = document.getElementById(sdd[1]).value;
		  		if(startd != null && startd != "" && endd != null && endd != "") {
		  			if(!checkDate(startd,endd)) {
		  				hideTip(tipid);
						showTip(document.getElementById(sdd[0]),"开始时间不能大于结束时间.",tipid);
						issele == null ? (issele = document.getElementById(valid).id) : "";
						rest = false;
		  			}
		  		}
		  	}
		  }		  
		  //wjb add by 2010-07-12 end
		  if(typeStr == "is_textarea") {
		  	if(document.getElementById(valid).length > maxLength) {
		  		hideTip(tipid);
				showTip(document.getElementById(valid),"不能超过"+maxLength+"个字符.",tipid);
				issele == null ? (issele = document.getElementById(valid).id) : "";
				rest = false;
		  	}
		  }
		  
		  if(typeStr == "is_double") {
		  	var controlObj = document.getElementById(valid);
		  	if(document.getElementById(valid).decimal) {
		  		var tempMsg=eval(""+typeStr+"(controlObj);");
		  		if(tempMsg == true) {
		  			var tt2 = checkDecimal(document.getElementById(valid).value,document.getElementById(valid).decimal);
		  			if(tt2 != true) {
		  				hideTip(tipid);
					  showTip(document.getElementById(valid),""+tt2,tipid);
					  issele == null ? (issele = document.getElementById(valid).id) : "";
					  rest = false;
		  			}
		  		} else {
		  			  hideTip(tipid);
					  showTip(document.getElementById(valid),""+tempMsg,tipid);
					  issele == null ? (issele = document.getElementById(valid).id) : "";
					  rest = false;
		  		}
		  	}
		  }
							  
		  if (document.getElementById(valid).value.length < minLength) {
			  	hideTip(tipid);
				showTip(document.getElementById(valid),"不能少于"+minLength+"个字符.",tipid);
				issele == null ? issele = document.getElementById(valid).id : "";
		  } else if (document.getElementById(valid).value.length > maxLength) {
				hideTip(tipid);
				showTip(document.getElementById(valid),"不能超过"+maxLength+"个字符.",tipid);
				issele == null ? issele = document.getElementById(valid).id : "";
		  } else {
		  	var controlObj = document.getElementById(valid);
			  var tempMsg=eval(""+typeStr+"(controlObj);");
			  if (tempMsg!=true) {
				  hideTip(tipid);
				  if(errm != null && errm != "") {
				  	showTip(document.getElementById(valid),errm,tipid);
				  } else {
				  	showTip(document.getElementById(valid),""+tempMsg,tipid);
				  }
				  issele == null ? issele = document.getElementById(valid).id : "";
			  }
		  }
	  }
}

function checkFormInput(handle)
{
	  if(validateobjarr.length == 0) 
	  {
		  return true;
	  }
	  clearTip();
	  //modified by and.ten@tom.com 解决handle参数可能是form id字符串，也可能是form对象情况 begin
	  var controlList;
	  if(typeof(handle) == "object")
	  {
	     controlList = handle.getElementsByTagName("INPUT");
	  }else
	  {
	     var form = document.getElementById(handle);
	     controlList = form.getElementsByTagName("INPUT");
	  }
	  //end
	  var controlObj;
	  var rest = true;
	  var issele = null; // select标记ID=
	  for(var i=0;i<controlList.length;i++)
	  {
	  		var tipid = getTip();
	  		if(tipid==null) {
	  			return false;
	  		}
		   controlObj = controlList[i];
		   if(isControlVisible(controlObj)) {
				if(controlObj.type=='text' || controlObj.type=='password' || controlObj.type=='textarea' || controlObj.type=='select-one' || controlObj.type=='file' || controlObj.type=='checkbox') {
					controlObj.value = controlObj.value.trim();
					 if(controlObj.datatype!=null) {
					 	if(controlObj.type=='checkbox' && controlObj.checked == false) {
					 		showTip(controlObj,"此处不能为空.",tipid);
					 		issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
							rest = false;
							continue;
					 	} else if(controlObj.type=='checkbox' && controlObj.checked == true) {
					 		rest = true;
							continue;
					 	}
					 	
						  tmptypeStr = controlObj.datatype;
						  str = tmptypeStr.split(",");
						  nullAble = str[0];
						  typeStr = str[1];
						  maxLength = str[2];
						  errm = controlObj.errmsg;
						  
						  minLength = "0";
						  if (str.length == 4) {
							  minLength = str[3];
						  }
						  
    					  if(controlObj.value==null || controlObj.value=="") {
     						  if (nullAble == "0") {
	                    			hideTip(tipid);
									if(controlObj.hint==null || controlObj.hint=="") {
										if(controlObj.type=='select-one' && getIEVer() <= 6) {
											showTip(controlObj,"此处不能为空.CL30",tipid);
										} else {
											showTip(controlObj,"此处不能为空.",tipid);
										}
										issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
										rest = false;
										continue;
									}
						      }
						  } else {
						  	if(typeStr == "is_yuan" || typeStr == "is_wan") {
						  		maxLength = 1000;
						  	}
						  	if(typeStr == "is_phone") {
						  		maxLength = 15;
						  	}
						  
							  if(typeStr == "is_date") {
							  	if(controlObj.group) {
							  		var gr = controlObj.group;
							  		var sdd = gr.split(",");
							  		var startd = document.getElementById(sdd[0]).value;
							  		var endd = document.getElementById(sdd[1]).value;
							  		if(startd != null && startd != "" && endd != null && endd != "") {
							  			if(!checkDate(startd,endd)) {
							  				hideTip(tipid);
											showTip(document.getElementById(sdd[0]),"开始时间不能大于结束时间.",tipid);
											issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
											rest = false;
											continue;
							  			}
							  		}
							  	}
							  }
							  //wjb add by 2010-07-12 验证日期不能大于当前日期 begin
							  if(typeStr == "is_date_now") {
						  		var valDate = controlObj.value;
						  		var nowDate = (new Date()).Format("yyyy-MM-dd");
						  		if(valDate != null && valDate != "" && nowDate != null && nowDate != "") {
						  			if(!checkDate(valDate,nowDate)) {
						  				hideTip(tipid);
										showTip(controlObj,"不能大于当前日期.",tipid);
										issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
										rest = false;
										continue;
						  			}
						  		}
							  }		  
							  //wjb add by 2010-07-12 end							  
							  
							  if(typeStr == "is_textarea") {
							  	if(controlObj.value.length > maxLength) {
							  		hideTip(tipid);
									showTip(controlObj,"不能超过"+maxLength+"个字符.",tipid);
									issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
									rest = false;
									continue;
							  	}
							  }
							  
							  if(typeStr == "is_double") {
							  	if(controlObj.decimal) {
							  		var tempMsg=eval(""+typeStr+"(controlObj);");
							  		if(tempMsg == true) {
							  			var tt2 = checkDecimal(controlObj.value,controlObj.decimal);
							  			if(tt2 != true) {
							  			  hideTip(tipid);
										  showTip(controlObj,""+tt2,tipid);
										  issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
										  rest = false;
										  continue;
							  			}
							  		} else {
							  			  hideTip(tipid);
										  showTip(controlObj,""+tempMsg,tipid);
										  issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
										  rest = false;
										  continue;
							  		}
							  	}
							  }
							  		  	
							  if (controlObj.value.length < minLength) {
								  	hideTip(tipid);
									showTip(controlObj,"不能少于"+minLength+"个字符.",tipid);
									issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
									rest = false;
									continue;
							  } else if (controlObj.value.length > maxLength) {
									hideTip(tipid);
									showTip(controlObj,"不能超过"+maxLength+"个字符.",tipid);
									issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
									rest = false;
									continue;
							  } else {
								  var tempMsg=eval(""+typeStr+"(controlObj);");
								  if (tempMsg!=true) {
									  hideTip(tipid);
									  if(errm != null && errm != "") {
									  	showTip(controlObj,errm,tipid);
									  } else {
									  	showTip(controlObj,""+tempMsg,tipid);
									  }
									  issele == null ? (document.getElementById(controlObj).select(),issele = controlObj.id) : "";
									  rest = false;
									  continue;
								  }
							  }
						  }
					 }
				}
		   }
	  }
	  return rest;
}

function checkFormSelect(handle){
	if(validateobjarr.length == 0){
		return true;
	}
	clearTip();
	var controlList;
	if(typeof(handle) == "object"){
	   controlList = handle.getElementsByTagName("SELECT");
	}else{
	   var form = document.getElementById(handle);
	   controlList = form.getElementsByTagName("SELECT");
	}
	//end
	var controlObj;
	var rest = true;
	for(var i=0;i<controlList.length;i++){
			var tipid = getTip();
			if(tipid==null) {
				return false;
			}
		 controlObj = controlList[i];
		 if(isControlVisible(controlObj)) {
			 var tmptypeStr = controlObj.datatype;
			 if(tmptypeStr){
				 str = tmptypeStr.split(",");
				 nullAble = str[0];
				 typeStr = str[1];
				 maxLength = str[2];
				 errm = controlObj.errmsg;
				 minLength = "0";
				 if (str.length == 4) {
					 minLength = str[3];
				 }
				 if(controlObj.value==null || controlObj.value=="") {
					 hideTip(tipid);
					 showTip(controlObj,"此处不能为空.",tipid);
					 rest = false;
					 continue;
				 }
			 }
		 }
	}
	return rest;
}

function handleKeyDown(event) {
  hideTip();
}

function hideTip(tipid) {
	if(validateConfig.timeOut > 0) {
		var val = m.get(tipid);
 		window.clearTimeout(val);
 		m.remove(val);
 	}
  document.getElementById(tipid).style.visibility = 'hidden';
  if(ieVer <= 6) {
  	ShowOverSels(tipid);
  }
}

function HideOverSels(objID)
{
    var sels = document.getElementsByTagName('select');
    for (var i = 0; i < sels.length; i++)
      if (Obj1OverObj2(document.all[objID], sels[i])) {
      	sels[i].style.visibility = 'hidden';
      }
}

function ShowOverSels(objID)
{
    var sels = document.getElementsByTagName('select');
    for (var i = 0; i < sels.length; i++)
      if (Obj1OverObj2(document.all[objID], sels[i]))
        sels[i].style.visibility = 'visible';
}

function Obj1OverObj2(obj1, obj2)
{
  var pos1 = getPosition(obj1)
  var pos2 = getPosition(obj2)
  var result = true;
  var obj1Left = pos1.left - window.document.body.scrollLeft;
  var obj1Top = pos1.top - window.document.body.scrollTop;
  var obj1Right = obj1Left + obj1.offsetWidth;
  var obj1Bottom = obj1Top + obj1.offsetHeight;
  var obj2Left = pos2.left - window.document.body.scrollLeft;
  var obj2Top = pos2.top - window.document.body.scrollTop;
  var obj2Right = obj2Left + obj2.offsetWidth;
  var obj2Bottom = obj2Top + obj2.offsetHeight;
  if (obj1Right <= obj2Left || obj1Bottom <= obj2Top ||
      obj1Left >= obj2Right || obj1Top >= obj2Bottom)
    result = false;
  return result;
}

function getPosition(Obj)
{
    var sumTop=0;
    var sumLeft=0;    
    while(Obj!=null && Obj!=window.document.body) {
      sumTop+=Obj.offsetTop;
      if(Obj.tagName.toLowerCase()=='div') {
        sumTop-=Obj.scrollTop;
      }
      sumLeft+=Obj.offsetLeft;
      Obj=Obj.offsetParent;
    }
 return {left:sumLeft,top:sumTop}
}

function isControlVisible(handle) {
  var retValue = true;
  while(handle.tagName.toLowerCase()!='form' && handle.style.display.toLowerCase()!='none') {
    handle = handle.parentNode;
  }
  if(handle.style.display=='none') retValue = false;
  return retValue;
}

function pdDate(st,ed) {
	var startDate=document.getElementById(st).value;   
	startDate=startDate.replace(/-/g,"/");
	var endDate=document.getElementById(ed).value;
	endDate=endDate.replace(/-/g,"/");
	if(Date.parse(startDate)-Date.parse(endDate)>0){   
		return false;   
	}
	return true;
}

//0-1.判断字符是否是数字
   function is_digitC(Character){
	   if(!(Character>='0'&&Character<='9')) {
         return false;       
       } else return true;
 }
//0-2.判断字符是否是字母
   function is_alfaC(Character){
	   if(!((Character>='a'&&Character<='z')||(Character>='A'&&Character<='Z'))) {
         return false;       
       } else return true;
}
//0-3.判断字符是否是线
   function is_lineC(Character){
	   if(!((Character=='-')||(Character=='_'))) {
         return false;       
       } else return true;
}
//0-4.判断字符是否是中文//  /^[\u4E00-\u9FA5]*$/，/^[chr(0xa1)-chr(0xff)]+$/，"/^[".chr(0xa1)."-".chr(0xff)."]+$/"
   function is_cnC(Character){
	   var pattern = /^[\u4E00-\u9FA5]{0,200}$/;  
	   if(!pattern.exec(Character)) {
         return false;       
       } else return true;
}

//判断是否全部由大写字母组成
  function is_upper(handle){
        var inputString=handle.value;
        for (var i = 0; i < inputString.length; i++){ 
		   checkCharacter=inputString.charAt(i);
		   if(checkCharacter>='Z'||checkCharacter<='A')
		       {
			     return "不能输入字符\""+checkCharacter+"\"";
		       }	 	    
		}
		return true;
 }
 
//判断是否全部由(除'和\外的字符)组成
    function is_all(handle){
        var inputString=handle.value;
        for (var i = 0; i < inputString.length; i++){ 
		   checkCharacter=inputString.charAt(i);
		   if((checkCharacter=='\'')||(checkCharacter=='\\'))
		       {
			     return "不能输入字符\""+ checkCharacter+"\".";
		       }	      
		}
		return true;
}
    function is_address(handle) {
    	 var pattern = /^[-#，。a-zA-Z0-9\u4E00-\u9FA5]{0,200}$/;
         if (!pattern.exec(handle.value)) return "不能输入数字、-、#、，、。、字母和中文以外的字符.";
         return true;
    }
function my_phone(handle){
	var pattern = /^[-,0-9]{0,25}$/;
    if (!pattern.exec(handle.value)) return "不能输入数字逗号横线以外的字符.";
    return true;
}
//判断是否是由数字'('')'和'-'组成phone
function is_phone(handle){
		var inputString=handle.value;
		for (var i = 0; i < inputString.length; i++){
		    checkCharacter=inputString.charAt(i)
            if((is_digitC(checkCharacter))||(checkCharacter=='(')||(checkCharacter==')')||(checkCharacter=='-'))
		    {
			    //alert("合法");
				//合法字符;数字'('')'和'-';
		    }
			else
			{
				 // alert("非法");
				return "不能输入字符\""+ checkCharacter+"\"."; 
			}
		}
		return true;
 }

//以下为输入过程中输入内容即时验证函数
//必须输入是数字
function inputNumber(handle,keyCode) {
  if(!((keyCode>=48&&keyCode<=57)||(keyCode>=96&&keyCode<=105))) {
 window.event.returnValue=false;
 return "必须输入数字!即如下字符:<br>1234567890";
  } else return true;
}
//必须输入字母
function inputLetter(handle,keyCode) {
  if(!((keyCode>=97&&keyCode<=122)||(keyCode>=65&&keyCode<=90))) {
 window.event.returnValue=false;
 return "必须输入大小写字母!即如下字符:<br>abcdefghijklmnopqrstuvwxyz<br>ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  } else return true;
}
//必须输入所有可见字符
function inputVisible(handle,keyCode) {
 var pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.@><,-[]{}?/+=|\\\'\":;~!#$%()`";
 var keyValue=String.fromCharCode(keyCode);
 if(keyCode==190) keyValue = ".";
 if(keyCode==189) keyValue = "-";
 if(keyCode==188) keyValue = "<";
 if(keyCode==219) keyValue = "[";
 if(keyCode==221) keyValue = "]";
 if(keyCode==191) keyValue = "?";
 if(keyCode==187) keyValue = "+";
 if(keyCode==220) keyValue = "|";
 if(keyCode==222) keyValue = "'";
 if(keyCode==186) keyValue = ";";
 if(keyCode==192) keyValue = "~";
 if(pattern.indexOf(keyValue)!=-1) {
  window.event.returnValue=true;
  return true;
 }else{
  window.event.returnValue=false;
  return "必须输入可见字符!即如下字符:<br>ABCDEFGHIJKLMNOPQRSTUVWXYZ<br> abcdefghijklmnopqrstuvwxyz<br>0123456789.@><,-[]{}?/+=|\\\'\":<br>;~!#$%()`";
 }
}
//必须输入字母与数字
function inputNormal(handle,keyCode) {
 var pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 var keyValue=String.fromCharCode(keyCode);
 if(pattern.indexOf(keyValue)!=-1) {
  window.event.returnValue=true;
  return true;
 }else{
  window.event.returnValue=false;
  return "必须输入可见字符!即如下字符:<br>ABCDEFGHIJKLMNOPQRSTUVWXYZ<br>abcdefghijklmnopqrstuvwxyz<br>0123456789";
 }
}

//判断是否是正双精度型
 function is_double(handle){
    var pattern = /^\d+(\.\d+)?$/;
    if (!pattern.exec(handle.value)) return "数据无效.";
    return true;
 }
 
 function is_date(handle){
	 var pattern=/^\d{4}(-)(1[012]|0?[1-9]){1,2}(-)([12]\d|3[01]|0?[1-9]){1,2}$/;
	 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM-DD."
	  return true;
 }
 
  function is_date_now(handle){
	 var pattern=/^\d{4}(-)(1[012]|0?[1-9]){1,2}(-)([12]\d|3[01]|0?[1-9]){1,2}$/;
	 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM-DD."
	  return true;
 }
 
  function is_date3(handle){
	 var pattern=/^(-)(1[012]|0?[1-9]){1,2}(-)([12]\d|3[01]|0?[1-9]){1,2}$/;
	 if (!pattern.exec(handle.value)) return "日期格式MM-DD."
	  return true;
 }
  //ranj 2013 10 9
  function is_date_not_day(handle){
		 var pattern=/^\d{4}(-)(1[012]|0?[1-9]){1,2}$/;
		 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM."
		  return true;
  }
 function is_vin(handle){
	 var pattern=/^([A-Z]|[0-9]){17,17}$/;
	 if (!pattern.exec(handle.value)) return "必须为17位大写字母和数字组合."
	  return true;
 }
 
 function is_yuan(handle){
	 var pattern=/^[0-9]{1,8}(|\.[0-9]{1,2})$/;
	 if (!pattern.exec(handle.value)) return "数据格式错误."
	  return true;
 }
 
 function is_wan(handle){
	 var pattern=/^[0-9]{1,4}(|\.[0-9]{1,4})$/;
	 if (!pattern.exec(handle.value)) return "数据格式错误."
	  return true;
 }
 
 function is_fdjh(handle){
	 var pattern=/^[A-Z0-9*]{0,200}$/;
	 if (!pattern.exec(handle.value)) return "数据格式错误."
	  return true;
 }
 
 function is_carno(handle){
	 var pattern=/^[a-zA-Z0-9-·\u4E00-\u9FA5]{0,200}$/;
	 if (!pattern.exec(handle.value)) return "车牌号格式错误."
	  return true;
 }
 
 function is_null(handle){
	 if(handle.value == null || handle.value == "") {
	 	return "此处不能为空.";
	 }
	  return true;
 }
 
 function is_textarea(handle){
	  return true;
 }
 
 function is_datetime(handle){
 	var pattern=/^(\d{4})-(\d{1,2})-(\d{1,2}) (\d{1,2}):(\d{1,2}):*(\d{0,2})$/;
	 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM-DD hh:mm.CL27"
	  return true;
 }
 
 function is_task_pri(handle){
	 var pattern=/^\d+$/;
	 if (!pattern.exec(handle.value)) return "日期格式YYYY-MM-DD hh:mm.CL27"
	 return true;
 }
//判断金额:0不能开头，可以有小数但最多是两位 isMoney
//add by liubo
 function isMoney(handle) {
	 var pattern = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
     if (!pattern.exec(handle.value)){
    	 return "只能输入数字,0不能开头,小数最多两位";
     }else{
    	 return true;
     }
 }
 //判断金额不能为0，0不能开头，可以有小数最对是两位 
 function is_money_z(handle){
	 var pattern = /^((([1-9]\d*)(\.\d{1,2})?)|(0\.\d{1,2}))$/;
	 if(!pattern.exec(handle.value)){
		 return "只能输入数字,0不能开头，小数最多两位,且不能为0";
	 }else{
		 return true;
	 }
 }
//金额格式可以为12,000.00
//add by zuoxiangjian
 function isBigMoney(handle){
	 var pattern = /^[\-\+]?([0-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)(\.\d{1,2})?$/;
	 if(!pattern.exec(handle.value)){
		return "只能输入数字,0不能开头,小数最多两位" ;
	 } else{
		return true ;
	 }
 }
 
//判断税率:0不能开头，可以有小数但最多是4位
//add by wjb
 function isMoney_4(handle) {
	 var pattern = /^(([1-9]\d*)|0)(\.\d{1,4})?$/;
     if (!pattern.exec(handle.value)){
    	 return "只能输入数字,0不能开头,小数最多四位";
     }else{
    	 return true;
     }
 }
 
//判断是否是由数字组成
 function is_digit(handle){
	var pattern = /^(\d){0,100}$/; 
    if (!pattern.exec(handle.value)) return "不能输入数字以外的字符.";
    return true;
 }
 //判断数字：0不能开头
 //add by wjb
  function isDigit(handle){
	var pattern = /^\+?[1-9][0-9]*$/; 
    if (!pattern.exec(handle.value)) return "只能输入非零的正整数.";
    return true;
 }
  //输入正负数字和小数点
  //add by ranj
 function isDigitAll(handle){
 	var pattern = /^[-+]?[0-9]+(\.[0-9]+)?$/; 
     if (!pattern.exec(handle.value)) return "只能输入正负数字和小数点";
     return true;
  }
 //判断是否是由数字和'-'组成
 function is_digit_line(handle){
	var pattern = /^(\d|[-]){0,100}$/; 
    if (!pattern.exec(handle.value)) return "不能输入数字和'-'以外的字符.";
    return true;
 }

//判断是否字母和中文
 function is_letter_cn(handle){
  var pattern = /^[a-zA-Z\u4E00-\u9FA5]{0,200}$/;
    if (!pattern.exec(handle.value)) return "不能输入字母和中文以外的字符."
    return true;
 }
 
//判断是否是字母 数字 中文
    function is_digit_letter_cn(handle){
        var pattern = /^[a-zA-Z0-9\u4E00-\u9FA5]{0,200}$/;
        if (!pattern.exec(handle.value)) return "不能输入数字、字母和中文以外的字符.";
        return true;
    }
    
//判断是否名称
    function is_name(handle){
        var pattern = /^[a-zA-Z0-9-_\u4E00-\u9FA5]{0,200}$/;
        if (!pattern.exec(handle.value)) return "不能输入数字、字母、-、_和中文以外的字符.";
        return true;
    }
	
//判断是否是数字和字母
    function is_digit_letter(handle){
        var pattern = /^([a-z]|[A-Z]|[0-9]){0,200}$/;
        if (!pattern.exec(handle.value)) return "不能输入数字和字母以外的字符.";
        return true;
 }
    
    function is_digit_letter2(handle){
    	var pattern = /^([A-Z]|[0-9]){0,200}$/;
    	if (!pattern.exec(handle.value)) return "只能输入大写字母和数字.";
    	return true;
    }

 //判断是否是数字和字母
    function is_letter(handle){
        var pattern = /^([a-z]|[A-Z]){0,200}$/;
        if (!pattern.exec(handle.value)) return "不能输入字母以外的字符.";
        return true;
 }
 //判断索赔工时代码
 	function is_labercode(handle){
 	    var pattern = /^[A-Z]{2}\w{7}$/;
        if (!pattern.exec(handle.value)) return "工时代码定义不符合规则.";
        return true;
 	}
 	
 //判断是否文档编号
 function is_docNumber(handle) {
        var pattern = /^[a-zA-Z0-9_-]{0,200}$/;
        if (!pattern.exec(handle.value)) return "不能输入数字、字母、-、_以外的字符.";
        return true;
 }
 
//判断是否是email
 function is_email(handle){
  var pattern = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  if (!pattern.exec(handle.value)) {
  	return "邮件格式不正确.";
  }
  return true;
 /* var pattern = /^[A-Za-z_]+([\w-\.]+)([A-Za-z]+)@((\w[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})$/i;
    if (!pattern.exec(handle.value)) return "邮件格式不正确.";
    return true;*/
 }
 
//页面URL
    function is_pageurl(handle){
        /*var pattern = /^[a-zA-Z0-9-_,./\u4E00-\u9FA5]{0,200}$/;
        if (!pattern.exec(handle.value)) return "此URL无效.";*/
        return true;
    }
	//密码
    function is_password(handle){
        return true;
    }
//目录路径
    function is_dirctoryurl(handle){
        var pattern = /^[a-zA-Z0-9-_,.\\u4E00-\u9FA5]{0,200}$/;
        if (!pattern.exec(handle.value)) return "此路径无效.";
        return true;
    }
      
//禁用单引号
    function is_noquotation(handle){
	  var pattern = /(['%])/; 
    if (pattern.exec(handle.value)) return "不能输入'或%号.";
    return true;
    }
   	
//检测小数点位数
function checkDecimal(val,decimalLen)
{
  if(val.indexOf('.')>0)
  {
    val=val.substr(val.indexOf('.')+1,val.length);
    if ((val.length) <= decimalLen){
      return true;
    }
    return "小数点位数不能超过"+decimalLen+"位.";
  } else {
	  return true;
  }
}
function is_special(handle){
	var pattern = /^[\u4e00-\u9fa5\da-zA-Z\-\/,()]+$/;
    if (!pattern.exec(handle.value)) return "不能输入数字、字母、中文和-,()/以外的字符.";
    return true;	
	
}

// 对Date的扩展，将 Date 转化为指定格式的String 
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function(fmt) 
{ 
  var o = { 
    "M+" : this.getMonth()+1,                 //月份 
    "d+" : this.getDate(),                    //日 
    "h+" : this.getHours(),                   //小时 
    "m+" : this.getMinutes(),                 //分 
    "s+" : this.getSeconds(),                 //秒 
    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
    "S"  : this.getMilliseconds()             //毫秒 
  }; 
  if(/(y+)/.test(fmt)) 
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o) 
    if(new RegExp("("+ k +")").test(fmt)) 
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
  return fmt; 
}
//比较用户所选时间和系统时间的大小，
//如果用户所选时间小于系统时间，返回ture，否则返回false
//sel_date：用户所选时间
//fmt：时间格式
function checkSys_Sel_Date(sel_date,fmt){
	var flag = "";
	var sys_date = document.getElementById("sys_date__").value;
	var date_array = sys_date.split(","); 
	var yyyy = date_array[0];
	var MM = date_array[1];
	var dd = date_array[2];
	var hh = date_array[3];
	var mm = date_array[4];
	var ss = date_array[5];
	var  date=new Date(yyyy,MM-1,dd,hh,mm,ss);
	if(fmt){
		var date_format = date.Format(fmt);
	}else{
		var date_format = date.Format("yyyy-MM-dd");
	}
	if(date_format < sel_date){
		flag = false;
	}else{
		flag = true;
	}
	return flag;
}
function btnDisable(){

    $$('input[type="button"]').each(function(button) {
        button.disabled = true;
    });

}

function moneyValidate(money){
	if(""==money || "undefined"==typeof(money))
		return true;
	var t = parseFloat(money);
	if(money=='0'){
		money='0.0';
	}
	var i = money.indexOf(".");
	var len = money.length;
	var s = len-i;
	var zero = money.indexOf("0");
	if(!isNaN(t) && (i==-1 || s<=3) && t>=0){
		if(zero == 0 ){
			if(i != 1){
				return false;
			}
		}
		return true;
	}
	return false;
}

function btnEable(){

    $$('input[type="button"]').each(function(button) {
            button.disabled = false;
    });

}

//判断发票号
function isInvoiceNo(handle){
	var pattern = /^\+?[N][O][0-9]{6}$/; 
   if (!pattern.exec(handle.value)) return "只能输入NO开头然后输入六数字.";
   return true;
}

//判断车牌号后六位
function isVehicleNo(handle){
	var pattern = /^[A-Z][A-Z0-9]{5}$/; 
   if (!pattern.exec(handle.value)) return "只能输入大写字母开头的六位字母和数字.";
   return true;
} 