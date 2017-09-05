
//字符串相关函数
//--------------------------------------
//字符创新增trim方法
//" sdfd sdf "返回值"sdfd sdf"
//--------------------------------------
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
function getSelectValue(obj){
	for(var i=0;i<obj.length;i++){
		if(obj[i].checked){
			return obj[i].value;
		}
	}
}
//--------------------------------------
//检查字符串是否为null
//obj:检查的对象
//返回值:不为空返回true
//--------------------------------------
function checkNull(obj,notice,select_flag){
	var str = null;
 	if(obj.value.trim().length==0){
 		if(select_flag==true){
			str = strAppend(str,"选择框："+notice+"不能为空");
		}else{
			str = strAppend(str,"输入框："+notice+"不能为空");
		}
	}
 	return str;
}
function strAppend(str,value){
	if(str!=null){
		if(value==null){
			return str;
		}else{
			return (str += value);
		}
	}else{
		if(value==null){
			return null;
		}else{
			return value;
		}
	}
}
function strAppendBr(str,value){
	if(str!=null){
		if(value==null){
			return str;
		}else{
			return (str +=("<br/>"+ value));
		}
	}else{
		if(value==null){
			return null;
		}else{
			return value;
		}
	}
}
function strAppendLast(value){
	var lastValue = null;
	if(value==null){
	}else{
		lastValue= strAppendBr("输入信息有误：",value);
	}
	if(lastValue!=null){
		MyAlert(lastValue);
		return false;
	}else{
		return true;
	}
}
function checkVINAll(obj,notice,select_flag){
	var str = null;
	var nullFlag = false;
	var nullStr = checkNull(obj,notice,select_flag);
	if(nullStr!=null){
		str = strAppend(str,nullStr);
		nullFlag = true;
	}
	if(!nullFlag){
		var formatStr = checkVINFormat(obj,notice);
		if(formatStr!=null){
			str = strAppend(str,formatStr);
		}
	}
	return str;
}
//验证编号的格式
function checkVINFormat(obj,notice){
	var str = null;
	var lengthFlag = false;
	if(obj.value.trim().length>0){
 		if(obj.value.trim().length!=17){
 			str = strAppend(str,"输入框："+notice+"长度必须为17位!");
 			lengthFlag = true;
		} 
 		var format = /^(([a-zA-Z]{1,}[0-9]{1,})|([0-9]{1,}[a-zA-Z]{1,}))$/;
		if (!format.test(obj.value.trim())){
			if(lengthFlag==true){
				str = strAppend(str,",格式只能为字母和数字的组合");
			}else{
				str = strAppend(str,"输入框："+notice+"格式只能为字母和数字的组合");
			}
		}
	}  
	return str;
}
//验证车牌号的格式
function checkLicenceFormat(obj,notice){
	var str = null;
	var lengthFlag = false;
	if(obj.value.trim().length>0){
 		if(obj.value.trim().length!=9){
 			str = strAppend(str,"输入框："+notice+"长度必须为9位!");
 			lengthFlag = true;
		} 
 		var format = /([A-Z]{1,}[0-9]{1,})|([0-9]{1,}[A-Z]{1,})/;
		if (!format.test(obj.value.trim())){
			if(lengthFlag==true){
				str = strAppend(str,",格式只能为大写字母和数字的组合");
			}else{
				str = strAppend(str,"输入框："+notice+"格式只能为字母和数字的组合");
			}
		}
	}  
	return str;
}
//检查说明的所有格式，并输出错误信息
function checkDescAll(obj,notice,select_flag,requireFlag,length){
	var str = null;
	var nullFlag = false;
	if(requireFlag==true){
		var nullStr = checkNull(obj,notice,select_flag);
		if(nullStr!=null){
			str = strAppend(str,nullStr);
			nullFlag = true;
		}
	}
	if(!nullFlag){
		var formatStr = checkDescFormat(obj,notice);
		if(formatStr!=null){
			str = strAppend(str,formatStr,length);
		}
	}
	return str;
}
//检查说明的格式
function checkDescFormat(obj,notice,length){
	if(length==undefined||length==null){
		length=200;
	}else{
		length= length;
	}
	var str = null;
	var lengthFlag = false;
	var l=0;
	if(obj.value.trim().length>0){
		for(var i=0;i<obj.value.trim().length;i++){
			if(obj.value.trim().charCodeAt(i)>255)l+=3;
			else l++;
		}
 		if(l>length){
 			str = strAppend(str,"输入框："+notice+"长度不能超过200，汉字长度不能超过66");
 			lengthFlag = true;
		} 
 		var format = /[#%&+=\/?<>'"]/;
		if (format.test(obj.value.trim())){
			if(lengthFlag==true){
				str = strAppend(str,",不能含有如下字符：#\"%&+=\/?<>'!");
			}else{
				str = strAppend(str,"输入框："+notice+"不能含有如下字符：#\"%&+=\/?<>'!");
			}
		}
	}  
	return str;
}
//验证数字格式的所有
function checkNumberAll(obj,len,notice,flag,select_flag){
	if(flag==true){
		if(obj.value.trim().length==0){
	 		if(select_flag==true){
	 			MyAlert(obj,"请选择"+notice+"!");
				return false;
			}else{
				MyAlert(obj,"请输入"+notice+"!");
				return false;
			}
		}else{
			if(obj.value.trim().length>0){
				 if(obj.value.length>len){
				 	MyAlert(obj,notice+"长度不能超过"+len+"位!");
					 return false;
				 }else{
				 	var reg=/^\d+$/
					if(reg.test(obj.value)){
						return true;
					}else{
						MyAlert(obj,notice+"只能为数字!");
						return false;
					}
				 }
 			}
		}
	}else if(flag==false){
		if(obj.value.trim().length>0){
			 if(obj.value.length>len){
			 	MyAlert(obj,notice+"长度不能超过"+len+"位!");
				 return false;
			 }else{
			 	var reg=/^\d+$/
				if(reg.test(obj.value)){
					return true;
				}else{
					MyAlert(obj,notice+"只能为数字!");
					return false;
				}
			 }
 		}
	}else{
		alert("参数不正确!");
	}
}	
//检查电话的所有有效性
function checkTelAll(obj,notice,flag,select_flag){
	if(flag==true){
		if(obj.value.trim().length==0){
	 		if(select_flag==true){
	 			MyAlert(obj,"请选择"+notice+"!");
				return false;
			}else{
				MyAlert(obj,"请输入"+notice+"!");
				return false;
			}
		}else{
			if(obj.value.trim().length>0){
				if(obj.value.trim().length>20){
					MyAlert(obj,notice+"电话长度不能超过20位!");
				 	return false;
				}else{
				  	var pattern=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	  				if(obj.value.trim().length!=0){
	  					if(pattern.test((obj.value.trim()))){
	  					 	return true;
	  					}else{
	  						MyAlert(obj,"请输入正确的"+notice+"!");
	   						return false;
	  					}
					}
  				}
			}
		}
	}else if(flag==false){
		if(obj.value.trim().length>0){
			if(obj.value.trim().length>20){
				MyAlert(obj,notice+"电话长度不能超过20位!");
			 	return false;
			}else{
			  	var pattern=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
  				if(obj.value.trim().length!=0){
  					if(pattern.test((obj.value.trim()))){
  					 	return true;
  					}else{
  						MyAlert(obj,"请输入正确的"+notice+"!");
   						return false;
  					}
				}
 			}
		}
	}else{
		alert("参数不正确!");
	}
}
//验证编号的长度和格式,编号的长度不能超过40
function checkId(obj,notice){
	if(obj.value.trim().length>0){
	 	if(obj.value.trim().length>40){
	 		MyAlert(obj,notice+"长度不能超过400位!");
		 	return false;
		 } else{
	 		var format = /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){0,}$/;
			if (format.test(obj.value.trim())){
				return true;
	   		}else{
	   			MyAlert(obj,notice+"中请输入以字母或数字开头，可带字母、数字、下划线和点的字符!");
	   			return false;
	  		 }
		 }     
 	}
}


//验证编号的长度,编号的长度不能超过40
function checkIdLength(obj,notice){
	 if(obj.value.trim().length>40){
	 	MyAlert(obj,notice+"长度不能超过40位!");
		 return false;
	 }
	 else
	     return true;
}
//验证输入的字符只能为字母、数字、下划线和点组成!
function CheckIdFormat(obj,notice){
	if(obj.value.trim().length>0){
			var format = /^[a-zA-Z0-9]{1}([a-zA-Z0-9]|[._]){0,}$/;
			if (format.test(obj.value.trim())){
				return true;
	   		}else{
	   			MyAlert(obj,notice+"中请输入以字母或数字开头，可带字母、数字、下划线和点的字符!");
	   			return false;
	  		 }
	  }
}
//验证名称的长度和格式,名称的长度不能超过100位,汉字不能超过50个
function checkName(obj,notice){
	if(obj.value.trim().length>0){
		var l=0;
		for(var i=0;i<obj.value.trim().length;i++){
			if(obj.value.trim().charCodeAt(i)>255)l+=2;
			else l++;
		}
		if(l>100){
			MyAlert(obj,notice+"长度不能超过100位，汉字不能超过50个!");
			return false;
		}else{
			var format=/[#%&+=\/?<>'"]/;
			if(format.test(obj.value.trim())){
				MyAlert(obj,notice+"中请不要输入如下字符：#\"%&+=\/?<>'!");
				return false;
			}else{
				return true;
			}
		}
	}
}
//验证名称的长度,名称的长度不能超过100位,汉字不能超过50个
function checkNameLength(obj,notice){
	var l=0;
	for(var i=0;i<obj.value.trim().length;i++){
		if(obj.value.trim().charCodeAt(i)>255)l+=2;
		else l++;
	}
	if(l>100){
		MyAlert(obj,notice+"长度不能超过100位，汉字不能超过50个!");
		 return false;
	}
	else
	     return true;
}
 //验证输入的字符只能为字母、汉字、数字、下划线和点组成!
function CheckNameFormat(obj,notice){
	if(obj.value.trim().length>0){
		var format = /[#%&+=?<>'"!]/;
		if (format.test(obj.value.trim())){
			MyAlert(obj,notice+"中请不要输入如下字符：\n#\"%&+=?<>'!");
			return false;
	   }else{
	   		return true;
	   }
	}
}
//验证描述和地址的长度,名称的长度不能超过200位,汉字不能超过100个
function checkDescOrAdd(obj,notice){
	if(obj.value.trim().length>0){
		var l=0;
		for(var i=0;i<obj.value.trim().length;i++){
			if(obj.value.trim().charCodeAt(i)>255)l+=2;
			else l++;
		}
		if(l>200){
			 MyAlert(obj,notice+"长度不能超过200位，汉字不能超过100个!");
			 return false;
		}else{
			var pattern=/[#%&+=\/?<>'"]/;
			var arr=pattern.exec(obj.value.trim());
			if(arr==null){
				return true;
			}else{
				MyAlert(obj,notice+"中请不要输入如下字符：#\"%&+=\/?<>'!");
				return false;
			}
		}
	}  
}
//验证描述的长度,名称的长度不能超过200位,汉字不能超过100个
function checkDescLength(obj,notice){
	var l=0;
	for(var i=0;i<obj.value.trim().length;i++){
		if(obj.value.trim().charCodeAt(i)>255)l+=2;
		else l++;
	}
	if(l>200){
		 MyAlert(obj,notice+"长度不能超过200位，汉字不能超过100个!");
		 return false;
	}
	 else
	     return true;
}

//验证数字格式的长度
function checkNumberLength(obj,len,notice){
	 if(obj.value.length>len){
	 	MyAlert(obj,notice+"长度不能超过"+len+"位!");
		 return false;
	 }
	 else
	     return true;
}
//验证数字的格式和长度
function checkNumber(obj,len,notice){
	if(obj.value.trim().length>0){
		 if(obj.value.length>len){
		 	MyAlert(obj,notice+"长度不能超过"+len+"位!");
			 return false;
		 }else{
		 	var reg=/^\d+$/
			if(reg.test(obj.value)){
				return true;
			}else{
				MyAlert(obj,notice+"只能为数字!");
				return false;
			}
		 }
 	}
}

//------------------------------------
//检查是否为正整数
//------------------------------------
function isUnsignedInteger(obj,notice) {
	if(obj.value.trim().length>0){
		var reg=/^\d+$/
		if(reg.test(obj.value)){
			return true;
		}else{
			MyAlert(obj,notice+"只能为数字!");
			return false;
		}
	}
}
//------------------------------------
//数字判断函数
//------------------------------------
function isNumber(str) 
{
	var digits = "0123456789";
	var i = 0;
	if(str==null) return false;
	var sLength = str.length;
    if(sLength==0)  return false;
	while ((i < sLength))
	{
	var c = str.charAt(i);
	if (digits.indexOf(c) == -1) return false;
	i++;
	}
	return true;
}
//正浮点数
function isFloat(obj,notice) {
	if(obj.value.trim().length>0){
			  var pattern=/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
  			if(obj.value.trim().length!=0){
  				if(pattern.test((obj.value.trim()))){
  				 return true;
  				}else{
  					MyAlert(notice+"只能是正数,请重新输入!");
   					return false;
  				}
  		  }
	}
}
//取字符串长度，汉字占两个字节
function strlen(str) {   
    var len = 0;   
    for (var i = 0; i < str.length; i++) {   
        if (str.charCodeAt(i) > 255 || str.charCodeAt(i<0)) len += 2; else len ++;   
    }   
    return len;   
} 
//检查输入的电话
function isTel(obj,notice){
	if(obj.value.trim().length>0){
		if(obj.value.trim.length>20){
			MyAlert(obj,notice+"电话长度不能超过20位!");
		 	return false;
		}else{
			  var pattern=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
  			  if(obj.value.trim().length!=0){
  				if(pattern.test((obj.value.trim()))){
  				 return true;
  				}else{
  					MyAlert(obj,"请输入正确的"+notice+"!");
   					return false;
  				}
			}
  		}
	}
}
//检查输入的电话
function isTel2(obj,notice){
	if(obj.value.trim().length>0){
		if(obj.value.trim.length>20){
			MyAlert(notice+"电话长度不能超过20位!");
		 	return false;
		}else{
			  var pattern=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
  			  if(obj.value.trim().length!=0){
  				if(pattern.test((obj.value.trim()))){
  				 return true;
  				}else{
  					MyAlert("请输入正确的"+notice+"!");
   					return false;
  				}
			}
  		}
	}
}
//打开窗口的接口，滚动条默认为开启
function openSimpleWindow(url,name,width,height){
	window.open(encodeURI(url+"&abc="+new Date()),name,"menubar=no,toolbar=no,location=no,directories=no,status=no,width="+width+"px,height="+height+"px,left=80,top=20,scrollbars=yes") ;
}
function openWindowNoName(url,width,height){
	window.open(encodeURI(url+"&abc="+new Date()),"","menubar=no,toolbar=no,location=no,directories=no,status=no,width="+width+"px,height="+height+"px,left=80,top=20,scrollbars=yes") ;
}		

//打开一个showModelDialog，参数为url，name:窗口的名称，对于大小都采用默认，主要用来提示信息。
function openshowModalDialog(url,name){
	var sReturn = self.showModalDialog(encodeURI(url+"&abc="+new Date()),name,"dialogwidth:300px ;dialogheight:100px ;center:yes ;help:no ;resizable:no ;status:no ;scroll:no ;unadorned:yes");
	return sReturn;
}
//打开一个showModelDialog，参数为url,name为"",表示打开一个新的窗口
function openModalDialogNoName(url){
	var sReturn = self.showModalDialog(encodeURI(url+"&abc="+new Date()),"","dialogwidth:300px ;dialogheight:100px ;center:yes ;help:no ;resizable:no ;status:no ;scroll:no ;unadorned:yes");
	return sReturn;
}
//打开一个showModelDialog，参数为url,name：窗口的名称，widht:窗口的宽度，height:窗口的高度，scroll：是否出现滚动条，返回值为SReturn
function openNewModalDialog(url,name,width,height,scroll){
	if(scroll==true){
		var sReturn = self.showModalDialog(encodeURI(url+"&abc="+new Date()),name,"dialogwidth:"+width+"px ;dialogheight:"+height+"px ;center:yes ;help:no ;resizable:no ;status:no ;scroll:yes ;unadorned:yes");
		return sReturn;
	}else if(scroll==false){
		var sReturn = self.showModalDialog(encodeURI(url+"&abc="+new Date()),name,"dialogwidth:"+width+"px ;dialogheight:"+height+"px ;center:yes ;help:no ;resizable:no ;status:no ;scroll:no ;unadorned:yes");
		return sReturn;
	}else{
		alert("请检查参数是否正确");
		return false;
	}
}

function StringBuffer()
{
  this.__strings__ = new Array;
}
StringBuffer.prototype.append = function(str)
{
  this.__strings__.push(str);
  return this;
};
StringBuffer.prototype.toString = function(split)
{
	if(typeof split!="string"){
		split="";
	}
  return this.__strings__.join(split);
};	

function checkPwName(url){
    var playTaskName=document.getElementById("playTaskName").value.trim();
	var availableDiv=document.getElementById("availableDiv");
	availableDiv.style.display="none";
	if(playTaskName!=""){
		if(checkNameAll(document.getElementById("playTaskName"),"任务名称",true)==false){
			return false;
		}else{
				var parameter="pwName="+playTaskName;
				var checkDiv=document.getElementById("checkDiv");
				checkDiv.innerHTML="<font size='2' color='green'>"+"正在检查...</font>";
				sendAsynchronRequest(url,parameter,checkPwNameCallBack);
		}
	}else{
		return false;
	}
}

function checkPwNameCallBack(){
	//alert("readyState="+xmlHttp.readyState);
	if(xmlHttp.readyState==4){
		//alert("struts="+xmlHttp.status);
		if(xmlHttp.status==200){
			var xmlDoc=xmlHttp.responseText;
			var checkDiv=document.getElementById("checkDiv");
			var availableDiv=document.getElementById("availableDiv");
			var jsonContent = eval("(" + xmlDoc + ")");
			var content="";
			var availableName="";
			if(jsonContent.errorMessage!=undefined){
				if(jsonContent.availableName!=undefined){	
					var container =document.getElementById("availableDiv");
					var availableNamesString=jsonContent.availableName;
					var	availableNames=availableNamesString.split(",");
					if(availableNames.length>2){
						availableDiv.style.display="inline";
					var sb=new StringBuffer();
					sb.append("<SPAN ID='availableSpan'><SELECT ID='availableName' NAME='availableName' ONCHANGE='changePwName(this.value);'>");
					for(var i=0;i<availableNames.length-1;i++){
						sb.append(" <option value='"+availableNames[i]+"' >"+availableNames[i]+"</option>");
					}
					sb.append("</SELECT></SPAN>");
					container.innerHTML="<font size='2' color='red'>可以使用："+sb.toString()+"</font>";
					}
				}
				content=jsonContent.errorMessage;
				checkDiv.innerHTML="<font size='2' color='red'>"+content+"</font>";
				document.getElementById("nextButton").disabled="disabled";
			}
			if(jsonContent.successMessage!=undefined){
				content=jsonContent.successMessage;
				checkDiv.innerHTML="<font size='2' color='green'>"+content+"</font>";
			}
		}else{
			alert("");
			return false;
		}
	}
}
function changePwName(select){
	var playTaskName=document.getElementById("playTaskName");
	playTaskName.value=select;
	
}

//二手车项目==================================================
//isnull - 该字段可否为空
//验证用户姓名输入是否正确
function checkDMSUCName(instr,isnull,obj,notice){
	var returnstr = "-1";
	if(obj != undefined && obj.value != undefined && obj.value.trim().length>0){
		var format = /[#%&+=?<>'"!]/;
		if (format.test(obj.value.trim())){
			returnstr = "●"+notice+"中请不要输入特殊字符！";
		}
	}
	if(!isnull && obj.value.trim().length==0){
		returnstr = "●"+notice+"不能为空！";
	}
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}
	return returnstr;
}
//验证输入框输入长度和不能为空
function checkDMSUCText(instr,isnull,obj,len,notice){
	var returnstr = "-1";
	if(obj != undefined && obj.value != undefined && obj.value.trim().length>0){
		if(obj.value.trim().length>len){
			returnstr = "●"+notice + "长度不能超过"+len+"位!";
		}
	}
	if(!isnull && obj.value.trim().length==0){
		returnstr = "●"+notice+"不能为空！";
	}
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}
	return returnstr;
}

//验证描述的长度,名称的长度不能超过200位,汉字不能超过100个
function checkDMSUCLength(instr,isnull,obj,len,notice){
	var returnstr = "-1";
	var l=0;
	for(var i=0;i<obj.value.trim().length;i++){
		if(obj.value.trim().charCodeAt(i)>255)l+=3;
		else l++;
	}
	if(l>len){
		 returnstr="●"+notice+"长度不能超过"+len+"位，汉字不能超过"+parseInt(len/3)+"个!";
	}
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}
	return returnstr;
}
//验证用户查询电话输入是否正确
function checkDMSUCSelTel(instr,isnull,obj,notice){
	var returnstr = "-1";
	if(obj != undefined && obj.value != undefined && obj.value.trim().length>0){
		if(obj.value.trim().length>20){
			returnstr = "●"+notice + "长度不能超过20位!";
		}else{
			var pattern=/^[+]{0,1}(\d){0,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
  			if(!(pattern.test(obj.value.trim()))){
  				returnstr = "●"+"请输入正确的"+notice+"!";
  			}
  		}
	}
	if(!isnull && obj.value.trim().length==0){
		returnstr = "●"+notice+"不能为空！";
	}
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}
	return returnstr;
}

//验证用户查询时间输入/选择是否正确
function checkDMSUCSelDate(instr,startobj,endobj,notice){
	var returnstr = "-1";
	if(startobj.value != "" && endobj.value != ""){
		if(startobj.value > endobj.value){
			returnstr = "●"+notice + "查询开始时间不能大于结束时间!";
		}
	}

	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}
	return returnstr;
}

//验证录入数字（手机）的格式和长度
function checkDMSUCNumber(instr,isnull,obj,len,notice){
	var returnstr = "-1";
	if(obj.value.trim().length>0){
		 if(obj.value.length>len){
		 	returnstr = "●"+notice+"长度不能超过"+len+"位!";
		 }else{
		 	var reg=/^\d+$/
			if(!reg.test(obj.value))
				returnstr = "●"+notice+"只能为数字!";
		}
	 }
	if(!isnull && obj.value.trim().length==0){
		returnstr = "●"+notice+"不能为空！";
	}	 
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}	 		 
 	return returnstr;
}

//验证录入数字小数点后两位
function checkDMSUCNumber2(instr,isnull,obj,len,notice){
	var returnstr = "-1";
	if(obj.value.trim().length>0){
		if(obj.value.length>len){
		 	returnstr = "●"+notice+"长度不能超过"+len+"位!";
		 }else{
		 	var reg= /^[0-9]+\.?[0-9]{0,2}$/
			if(!reg.test(obj.value))
				returnstr = "●"+notice+"只能是整数或浮点数且小数点后最多能输入两位!";
		 }
	 }
	if(!isnull && obj.value.trim().length==0){
		returnstr = "●"+notice+"不能为空！";
	}	 
	if(returnstr != "-1"){
		returnstr = ( instr == "-1" ? returnstr : instr + "<BR>" + returnstr);
	}else{
		returnstr = instr;
	}	 		 
 	return returnstr;
}
//验证编号的格式
function checkDMSUVINFormat(instr,isnull,obj,notice){
	var str = "-1";
	var lengthFlag = false;
	if(obj.value.trim().length>0){
 		if(obj.value.trim().length!=17){
 			str = strAppend(""+notice+"长度必须为17位!");
 			lengthFlag = true;
		} 
 		var format = /([a-zA-Z]{1,}[0-9]{1,})|([0-9]{1,}[a-zA-Z]{1,})/;
		if (!format.test(obj.value.trim())){
			if(lengthFlag==true){
				str = strAppend(""+notice+"格式只能为字母和数字的组合");
			}else{
				str = strAppend(""+notice+"格式只能为字母和数字的组合");
			}
		}
	} 	 
	if(str != "-1"){
		str = ( instr == "-1" ? str : instr + "<BR>" + str);
	}else{
		str = instr;
	}		 
	return str;
}
//验证车牌号的格式
function checkDMSULicenceFormat(instr,isnull,obj,notice){
	var str = "-1";
	var lengthFlag = false;
	if(obj.value.trim().length>0){
 		if(obj.value.trim().length!=9){
 			str = strAppend(""+notice+"长度必须为9位!");
 			lengthFlag = true;
		} 
 		var format = /([A-Z]{1,}[0-9]{1,})|([0-9]{1,}[A-Z]{1,})/;
		if (!format.test(obj.value.trim())){
			if(lengthFlag==true){
				str = strAppend(""+notice+"格式只能为大写字母和数字的组合");
			}else{
				str = strAppend(""+notice+"格式只能为字母和数字的组合");
			}
		}
	} 	 
	if(str != "-1"){
		str = ( instr == "-1" ? str : instr + "<BR>" + str);
	}else{
		str = instr;
	}		 
	return str;
}
//------------------------------------
//检查是否为整数(包括正，负 ，0)
// ranjian add 2013.4.23
//------------------------------------
function isInteger(obj,notice) {
	if(obj.value.trim().length>0){
		var reg=/^(-|\+)?\d+$/;
		if(reg.test(obj.value)){
			return true;
		}else{
			MyAlert(notice+"只能为整数!");
			return false;
		}
	}
}