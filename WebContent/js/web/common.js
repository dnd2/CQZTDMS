window.onunload = function gbg(){

	if(parent.$('inIframe')){
		//parent.$('inIframe').document.write("");
		//parent.$('inIframe').document.clear();
		//parent.$('inIframe').src = '';
		/*CollectGarbage();*/
	}
}
//===============记录用户点击的复选框start by chenyu=========================
// 分隔符
var checkSep = ",";
// 页面自定义获取checkbox的value的方法名
// cusGetClickValFun,请在需要自定获取clickedvalue的页面编写cusGetClickValFun方法
function saveCheckboxValue(checkObj){
	var hideCheckedNode = document.getElementById(hideCheckedId);
	if(hideCheckedNode){
		if(hideCheckedNode.value.indexOf(checkObj.value)==-1){
			hideCheckedNode.value = clearValue(hideCheckedNode.value+checkSep+checkObj.value,checkSep);
		}
	}
}

function removeCheckboxValue(checkObj){
	var hideCheckedNode = document.getElementById(hideCheckedId);
	if(hideCheckedNode){
		if(hideCheckedNode.value.indexOf(checkObj.value)!=-1){
			hideCheckedNode.value = clearValue(hideCheckedNode.value.replace(checkObj.value,''),checkSep);
		}
	}
}

//清除无用的符号
function clearValue(_str,_sub){
	if(_str&&_sub){
		var reg = new RegExp("^["+_sub+"]+|["+_sub+"]+$","g");
		_str = _str.replace(reg,'');
		while(_str.indexOf(_sub+_sub)>-1){
			_str = _str.replace(_sub+_sub,_sub);
		}
	}
	return _str;
}
//
function refreshCheckedNum(){
	var hideCheckedNode = document.getElementById(hideCheckedId);
	if(hideCheckedNode){
		var checkedNumNode = document.getElementById(checkedNumNodeId);
		if(checkedNumNode){
			var hideCheckedValuesStr = hideCheckedNode.value;
			if(hideCheckedValuesStr.length<1){
				checkedNumNode.innerHTML = '';
			} else {
				var hideCheckedValues = hideCheckedNode.value.split(checkSep);
				checkedNumNode.innerHTML = hideCheckedValues.length;
			}
			
		}
	}
}
//===============记录用户点击的复选框end by chenyu===========================
//==================记录用户选中复选框start by chenyu====================
function getCheckedParamValues() {
	var checkedNodeIds = new Array();
	checkedNodeIds[0] = hideCheckedId;
	checkedNodeIds[1] = hideCheckedRegionId;
	checkedNodeIds[2] = hideCheckedDealerId;
	checkedNodeIds[3] = hideCheckedMaterialId;
	checkedNodeIds[4] = hideCheckedMaterialGroupId;

	var params = '';
	try{
		if (typeof cusGetClickValFun != undefined && cusGetClickValFun instanceof Function) {
			cusGetClickValFun();
		}
	}catch(e){
		
	}
	for(var i = 0;i<checkedNodeIds.length;i++){
		params += getHideCheckedNodeValue(checkedNodeIds[i]);
	}
	return params;
}

function getHideCheckedNodeValue(_nodeName){
	var _checkedParam = '';
	if(_nodeName){
		var _checkedNode = document.getElementById(_nodeName);
		if (_checkedNode && _checkedNode.value
				&& _checkedNode.value.length > 0) {
			_checkedParam += "&" + _nodeName + "="
			+ encodeURI(_checkedNode.value);
		}
	}
	return _checkedParam;
}

// ==================记录用户选中复选框end by chenyu======================

/**
 * 复选框的多选
 */
function selectAll(checkObj,checkBoxName){ 
    //modified by pan.gaoming@infoservice.com.cn
    window.event.cancelBubble = true;
    //end
	var allChecks = document.getElementsByName(checkBoxName);
	if(checkObj.checked){
		for(var i = 0;i<allChecks.length;i++){
			allChecks[i].checked = true;
			saveCheckboxValue(allChecks[i]);
		}
	}else{
		for(var i = 0;i<allChecks.length;i++){
			allChecks[i].checked = false;
			removeCheckboxValue(allChecks[i]);
		}
	}
}
/**
 * 将选择框的某一项选中
 */
function selectOption(value,selectBoxName){
	var allSelects = $(selectBoxName);
	for(var i = 0;i<allSelects.length;i++){
		if(allSelects[i].value==value){
			allSelects[i].selected = true;
			break;
		}
	}
}
/**
 * 检查复选框
 */
function checkselectAllBox(checkObj,checkAllId,checkBoxName){
	var checkAllObj = document.getElementById(checkAllId);
	var allChecks = document.getElementsByName(checkBoxName);
	var allFlag = true;
	if(checkObj.checked){
		for(var i = 0;i<allChecks.length;i++){
			if(allChecks[i].checked){
				
			}else{
				checkAllObj.checked = false;
				allFlag = false;
				return;
			}
		}
		if(allFlag==true){
			checkAllObj.checked = true;
		}
	}else{
		checkAllObj.checked = false;
	}
}

/*-------------------------- window.open 弹出效果 ----------------------*/
function openNewWindow(wName, width, height, url){
	window.open(url,wName,'width='+width+',height='+height+',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=yes,alwaysRaised=yes,depended=yes');
}
/*-------------------------- 为页面上的所有table加上序号一列 ----------------------*/
	function __initTd__() {
		var tableObj = document.getElementsByTagName("table");
		var __td__;
		for(out=0;out<tableObj.length;out++) {
			if(tableObj[out].className == "table_list") {
				for(i=0;i<tableObj[out].rows.length;i++) {
					__td__ = tableObj[out].rows[i].insertCell(0);//document.createElement(i<1?'th':'td');
					if(i<1) {
						__td__.style.background = "#DAE0EE"; 
						__td__.style.color = "#08327E";
						__td__.style.padding = "0px 2px 0px 2px";
					}
					__td__.style.borderLeft = "none";
					__td__.innerHTML = i < 1 ? "序号" : i;
					//tableObj[out].rows[i].appendChild(__td__);
				}
			}
		}
	}
/*--------------------------end-----------------------------------------------------*/
function setAllSelect(){
	document.write("-请选择-");
}

/*----------------------------客户管理的相关模块的公用js--add by zhangxianchao--------*/
//根据选择的客户类型变换主要信息
function changeCustomerType(obj,personal,corp){
	if(obj.value==personal){
		$("personalInput").style.display = "inline";
		$("companyInput").style.display = "none";
		showDetailMore2(personal,corp);
	}else{
		$("personalInput").style.display = "none";
		$("companyInput").style.display = "inline";
		showDetailMore2(personal,corp);
	}
}
//根据选择的客户类型变换主要信息
function changeCustomerType2(obj,personal,corp){
	if(obj.value==personal){
		$("personalInput").style.display = "inline";
		$("companyInput").style.display = "none";
	}else{
		$("personalInput").style.display = "none";
		$("companyInput").style.display = "inline";
	}
}
//根据选择的客户变换客户选择的内容。
function showCustomerMessage(obj){
	$('customerInput').style.display = "none";
	$('customerInfo').style.display = "inline";
}
//改变详细信息的显示样式
function showDetailMore2(personal,corp){
	var customertype = $F("cus_type");
	if(customertype==personal){
		if(document.getElementById("companyDetailMoreDiv").style.display=="inline"){
			document.getElementById("companyDetailMoreDiv").style.display="none";
			document.getElementById("personalDetailMoreDiv").style.display = "inline";
		}
	}else{
		if(document.getElementById("personalDetailMoreDiv").style.display=="inline"){
			document.getElementById("personalDetailMoreDiv").style.display = "none";
			document.getElementById("companyDetailMoreDiv").style.display="inline";
		}
	}
}
//弹出选择客户的公共页面
function openCustomerSearch(path){
	OpenHtmlWindow(path+"/common/CustomerManager/customerSearch.do?COMMAND=1",800,600);
}
//弹出查询条件选择客户的公共页面
function openQueryCustomerSearch(path,fun,actionName){
	OpenHtmlWindow(path+"/common/CustomerManager/customerSearch.do?COMMAND=1&fun="+fun+"&action_name="+actionName,800,600);
}
//弹出新增客户的公共页面
function addCustomerMore(path){
	//OpenHtmlWindow(path+"/common/CustomerManager/customerMoreAdd.do?COMMAND=1",800,600);
	openNewWindow("cust_more_add",840,600,path+"/common/CustomerManager/customerMoreAdd.do?COMMAND=1");
}
//生成显示客户更加详细的信息
function showCustomerMoreDetail(path){
	//OpenHtmlWindow(path+"/common/CustomerManager/customerMoreDetail.do?cust_id="+$F("get_cust_id")+"&operation_type="+$F("operation_type"),800,600);
	openNewWindow("",800,600,path+"/common/CustomerManager/customerMoreDetail.do?cust_id="+$("get_cust_id").value+"&operation_type="+$("operation_type").value);
}
//弹出修改客户的信息
function modifyCustomerInfo(path){
	//OpenHtmlWindow(path+"/common/CustomerManager/customerModify.do?COMMAND=1&cust_id="+$F("get_cust_id")+"&operation_type="+$F("operation_type"),800,600);
	openNewWindow("cust_more_modify",840,600,path+"/common/CustomerManager/customerModify.do?COMMAND=1&cust_id="+$("get_cust_id").value+"&operation_type="+$("operation_type").value);
}
//弹出联系人信息窗口
function openLinkManMore(path,rowNum){
	openNewWindow("link_man_more",720,300,path+"/common/CustomerManager/linkManMore.do?COMMAND=1&rowNum="+rowNum);
}
//弹出联系人详细信息窗口
function openLinkManMoreDetail(path,conId){
	openNewWindow("link_man_more",720,300,path+"/common/CustomerManager/linkManMore.do?COMMAND=2&con_id="+conId);
}
//弹出选择用户的公共页面
function openUserSearch(path){
	OpenHtmlWindow(path+"/common/UserManager/UserSearch.do",800,500);
}

//生成爱好
function generateFavor(code,hobby,flag){
	var _codeData_ = codeData;
	var data = new Array();
	var k =0;
	var splits = null;
	if(hobby!=undefined&&hobby!=""){
		splits = hobby.split(",");
	}
	if(flag!=undefined&&flag=="true"){
		flag = true;
	}else if(flag!=undefined&&flag=="false"){
		flag = false;
	}
    data.push('<table border=0><TBODY>');
    var rowFlag = false;
    for(var i=0;i<_codeData_.length;i++){
    	if(_codeData_[i].type == code){
    		if(k%6==0){
    			data.push('<tr>');
    			rowFlag = false;
    		}
    		var temp = '<td width="115"><input type="checkbox"';
    		if(splits!=null){
	    		for(var j=0;j<splits.length;j++){
	    			temp +=(codeData[i].codeId == splits[j] ? "checked" : "");
	    		}
    		}
    		if(!flag){
    			temp+=' disabled="disabled" ';
    		}
    		temp+=' name="personal_favor"   value = '+_codeData_[i].codeId+'/>'+_codeData_[i].codeDesc+'</td>';
    		data.push(temp);
    		if((k+1)%6==0){
    			data.push('</tr>');
    			rowFlag = true;
     		}
    		k++;
    	}
    }
    if(!rowFlag){
    	data.push('</tr>');
    }
   data.push('</TBODY></table>');
  document.write(data.join(''));
}
//生成了解渠道
function generateKnowChannel(code,channel,flag,otherRemark){
	var _codeData_ = codeData;
	var data = new Array();
	var k =0;
	var splits = null;
	if(channel!=undefined&&channel!=""){
		splits = channel.split(",");
	}
	if(flag!=undefined&&flag=="true"){
		flag = true;
	}else if(flag!=undefined&&flag=="false"){
		flag = false;
	}
    data.push('<table border=0><TBODY>');
    var rowFlag = false;
    for(var i=0;i<_codeData_.length;i++){
    	if(_codeData_[i].type == code){
    		if(k%6==0){
    			data.push('<tr>');
    			rowFlag = false;
    		}
    		if(_codeData_[i].codeId=="20781010"){
    			var temp = '<td width="250" colspan="3"><input type="checkbox"';
        		if(splits!=null){
    	    		for(var j=0;j<splits.length;j++){
    	    			temp +=(codeData[i].codeId == splits[j] ? "checked" : "");
    	    		}
        		}
        		if(!flag){
        			temp+=' disabled="disabled" ';
        		}
    			temp+=' name="personal_channel" id = "otherChannel" onclick = "doOtherInput(this,\'otherDesc\');"  value = '+_codeData_[i].codeId+'/>'+_codeData_[i].codeDesc;
    			temp+= ' <input type="text" name="otherDesc" disabled="disabled" id="otherDesc" class="mid_txt"  datatype="1,is_noquotation,60" value="'+(otherRemark==undefined?'':otherRemark)+'"/>';
    			temp+= '</td>';
    		}else{
    			var temp = '<td width="115"><input type="checkbox"';
        		if(splits!=null){
    	    		for(var j=0;j<splits.length;j++){
    	    			temp +=(codeData[i].codeId == splits[j] ? "checked" : "");
    	    		}
        		}
        		if(!flag){
        			temp+=' disabled="disabled" ';
        		}
    			temp+=' name="personal_channel"   value = '+_codeData_[i].codeId+'/>'+_codeData_[i].codeDesc+'</td>';
    		}
    		data.push(temp);
    		if((k+1)%6==0){
    			data.push('</tr>');
    			rowFlag = true;
     		}
    		k++;
    	}
    }
    if(!rowFlag){
    	data.push('</tr>');
    }
   data.push('</TBODY></table>');
  document.write(data.join(''));
}
//生成过户手续
function generateTransferProcedure(code,flag,remarkArray,currentYear){
	var _codeData_ = codeData;
	var data = new Array();
	var k =0;
	if(flag!=undefined&&flag==true){
		flag = true;
	}else if(flag!=undefined&&flag==false){
		flag = false;
	}
    data.push('<table border=0><TBODY>');
    var rowFlag = false;
    for(var i=0;i<_codeData_.length;i++){
    	if(_codeData_[i].type == code){
    		if(k%3==0){
    			data.push('<tr>');
    			rowFlag = false;
    		}
    		
    		if(_codeData_[i].codeId=="20261001"||_codeData_[i].codeId=="20261002"||_codeData_[i].codeId=="20261003"||_codeData_[i].codeId=="20261007"){
    			var temp = '<td width="33%" style="text-indent: 10px;"><input type="checkbox"';
    			var remark = "";
	    		for(var j=0;j<remarkArray.length;j++){
	    			if(codeData[i].codeId == remarkArray[j].type){
	    				temp += " checked ";
	    				remark = remarkArray[j].remark;
	    			}
	    		}
        		if(!flag){
        			temp+=' disabled="disabled" ';
        		}
    			temp+=' name="transferPro" onclick="changeYearSpanVisible(this);"  value = '+_codeData_[i].codeId+'/>'+_codeData_[i].codeDesc;
    			if(_codeData_[i].codeId=="20261001"){
    				temp+=generateToDateYearMonth2(parseInt(currentYear),remark,_codeData_[i].codeId,flag);
    			}else{
    				temp+=generateToDateYearMonth(parseInt(currentYear),remark,_codeData_[i].codeId,flag);
    			}
    		}else{
    			var temp = '<td width="33%" style="text-indent: 10px;"><input type="checkbox"';
    			for(var j=0;j<remarkArray.length;j++){
	    			temp +=(codeData[i].codeId == remarkArray[j].type ? "checked" : "");
	    		}
        		if(!flag){
        			temp+=' disabled="disabled" ';
        		}
    			temp+=' name="transferPro"   value = '+_codeData_[i].codeId+'/>'+_codeData_[i].codeDesc+'</td>';
    		}
    		data.push(temp);
    		if((k+1)%3==0){
    			data.push('</tr>');
    			rowFlag = true;
     		}
    		k++;
    	}
    }
    if(!rowFlag){
    	data.push('</tr>');
    }
   data.push('</TBODY></table>');
   return data.join('');
}
//生成到期日框
function generateToDateYearMonth(currentYear,selectValue,parent,flag){
	var temp = "";
	if(selectValue!=""){
		temp+='<span id="span'+parent+'" >到期日';
	}else{
		temp+='<span id="span'+parent+'" style="visibility: hidden;">到期日';
	}
	temp+= generateYearMonth(currentYear,selectValue,flag);
	temp+= '<input type = "hidden" name = "selectNum" value = "'+parent+'" />'
	temp+='</span>';
	return temp;
}
//生成到期日框
function generateToDateYearMonth2(currentYear,selectValue,parent,flag){
	var temp = "";
	if(selectValue!=""){
		temp+='<span id="span'+parent+'" >';
	}else{
		temp+='<span id="span'+parent+'" style="visibility: hidden;">';
	}
	temp+= generateYearMonth2(currentYear,selectValue,flag);
	temp+= '<input type = "hidden" name = "selectNum" value = "'+parent+'" />'
	temp+='</span>';
	return temp;
}
function changeYearSpanVisible(obj){
	var objValue = obj.value;
	var value = objValue.substr(0,(objValue.length-1));
	if(obj.checked){
		$("span"+value).style.visibility="visible";
	}else{
		$("span"+value).style.visibility="hidden";
	}
}
//生成年月选择框
function generateYearMonth(currentYear,selectValue,flag){
	var selectYear ;
	var selectMonth;
	if(selectValue!=""){
		selectYear = parseInt(selectValue.substring(0,4));
		selectMonth = parseInt(selectValue.substring(4));
	}
	
	var temp ="";
	if(!flag){
		temp+=' &nbsp;<select  name="proYear" disabled="disabled"  >';
	}else{
		temp+=' &nbsp;<select  name="proYear"  >';
	}
	for(var i=2008;i<currentYear+12;i++){
		if(i==selectYear){
			temp+=' <option value="'+i+'" selected = "selected" >'+i+'</option>';
		}else{
			temp+=' <option value="'+i+'"  >'+i+'</option>';
		}
	}
	temp+=' </select>';
    temp+='年&nbsp;';
   
    if(!flag){
    	 temp+='<select  name="proMonth"  disabled="disabled">';
	}else{
		 temp+='<select  name="proMonth" >';
	}
    for(var i=1;i<=12;i++){
    	if(i==selectMonth){
			temp+=' <option value="'+i+'" selected = "selected" >'+i+'</option>';
		}else{
			temp+=' <option value="'+i+'"  >'+i+'</option>';
		}
    }
   temp+='</select> 月&nbsp; '
   return temp;
}
//生成年月选择框
function generateYearMonth2(currentYear,selectValue,flag){
	var selectYear ;
	var selectMonth;
	if(selectValue!=""){
		selectYear = parseInt(selectValue.substring(0,4));
		selectMonth = parseInt(selectValue.substring(4));
	}
	
	var temp ="";
	if(!flag){
		temp+=' &nbsp;<select  name="proYear" disabled="disabled"  >';
	}else{
		temp+=' &nbsp;<select  name="proYear"  >';
	}
	for(var i=(currentYear-9);i<=currentYear;i++){
		if(i==selectYear){
			temp+=' <option value="'+i+'" selected = "selected" >'+i+'</option>';
		}else{
			temp+=' <option value="'+i+'"  >'+i+'</option>';
		}
	}
	temp+=' </select>';
    temp+='年&nbsp;';
   
    if(!flag){
    	 temp+='<select  name="proMonth"  disabled="disabled">';
	}else{
		 temp+='<select  name="proMonth" >';
	}
    for(var i=1;i<=12;i++){
    	if(i==selectMonth){
			temp+=' <option value="'+i+'" selected = "selected" >'+i+'</option>';
		}else{
			temp+=' <option value="'+i+'"  >'+i+'</option>';
		}
    }
   temp+='</select> 月&nbsp; '
   return temp;
}
function doOtherInput(obj,name){
	if(obj){
		if(obj.checked){
			$(name).disabled="";
		}else{
			$(name).disabled="true";
		}
	}
}
//显示详细信息
function showDetailMessage(obj){
	if(obj==undefined||obj==null){
		return "";
	}else{
		return obj;
	}
}
//显示详细信息
function showDetailMessage1(obj){
	if(obj==undefined||obj==null||obj=='null'){
		return "";
	}else{
		return obj;
	}
}
//生成单选框的字符串
function generateRadio(obj){
	return String.format("<input type='radio' value='"+obj+"' name='selectRadio'/>");
}
//生成客户查询的单选择框
function generateCustRadio(value, metaData, record){
	return String.format("<input type='radio' value='"+record.data.custId+"' name='selectRadio' onclick = 'showCustIntent(this);'/><input type='hidden' value='"+record.data.custName+"' name='selectNameRadio'/>");
}
//生成用户查询的单选择框
function generateUserCheckBox(value, metaData, record){
	return String.format("<input type='checkbox' value='"+record.data.userId+"' name='selectCheck'/><input type='hidden' value='"+record.data.name+"' name='selectNameCheck'/>");
}
//关闭窗口
function closePage(){
	window.close();
}
//对来访对象进行覆值。
function givenVistorValue(cus_name,cus_sex,link_way,areaCode,tel){
	var cust = {};
	if(cus_name!=undefined&&cus_name!="null"&&cus_name!=""){
		cust.cus_name = cus_name;
	}
	if(cus_sex!=undefined&&cus_sex!="null"&&cus_sex!=""){
		cust.cus_sex = cus_sex;
	}
	if(link_way!=undefined&&link_way!="null"&&link_way!=""){
		cust.link_way = link_way;
	}
	var linkTel = null;
	if(areaCode!=undefined&&areaCode!="null"&&areaCode!=""){
		linkTel = areaCode;
	}
	if(tel!=undefined&&tel!="null"&&tel!=""){
		if(linkTel!=null){
			linkTel = linkTel+"-"+tel;
		}else{
			linkTel = tel;
		}
	}
	if(linkTel!=null){
		cust.linkTel = linkTel;
	}
	cust.linkType = estimateLinkType(cust);
	generateCutomerInfo(cust);
}
//判断客户的主要联系方式类型
function estimateLinkType(obj){
	if(obj.link_way!=undefined){
		return "20551001";
	}
	if(obj.linkTel!=undefined){
		return "20551002";
	}
}
//根据覆值进行客户信息的覆值
function generateCutomerInfo(obj){
	if(obj.cus_name!=undefined){
		$('cus_name_back').value = obj.cus_name;
		$('cus_name').value = obj.cus_name;
	}
	if(obj.cus_sex!=undefined){
		doSelected('personal_sex',obj.cus_sex);
		$('cus_sex_back').value = obj.cus_sex;
	}
	if(obj.link_way!=undefined){
		$('link_way').value = obj.link_way;
		$('link_way_back').value = obj.link_way;
	}
	if(obj.linkTel!=undefined){
		if($('link_way').value==""){
			$('link_way').value = obj.linkTel;
		}
		$('cus_tel_back').value = obj.linkTel;
	}
	if(obj.linkType!=undefined){
		doSelected('link_Way_Type',obj.linkType);
		$('link_type_back').value = obj.linkType;
	}
	changeVilidateFun($('link_way'));
}
function doSelected(id,value){
	var obj = $(id);
	for(var i =0;i<obj.length;i++){
		if(obj[i].value==value){
			obj[i].selected = true;
			break;
		}
	}
}
function doSelected2(obj,value){
	for(var i =0;i<obj.length;i++){
		if(obj[i].value==value){
			obj[i].selected = true;
			break;
		}
	}
}
function custBaseGivenValue(personal){
//	//客户类型
//	doSelected('cus_type',$('inIframe').contentWindow.$('cus_type').value);
//	//客户名称
//	$('cus_name').value = $('inIframe').contentWindow.$('cus_name').value;
//	$('link_way').value = $('inIframe').contentWindow.$('link_way').value;
//	$('personal_birthDay').value = $('inIframe').contentWindow.$('personal_birthDay').value;
//	$('personal_mark').value = $('inIframe').contentWindow.$('personal_mark').value;
//	$('company_charactor').value = $('inIframe').contentWindow.$('company_charactor').value;
//	$('company_dicide_positon').value = $('inIframe').contentWindow.$('company_dicide_positon').value;
//	$('company_remark').value = $('inIframe').contentWindow.$('company_remark').value;
//	$('operation_type').value = $('inIframe').contentWindow.$('operation_type').value;
//	doSelected('link_Way_Type',$('inIframe').contentWindow.$('link_Way_Type').value);
//	doSelected('personal_industry_way',$('inIframe').contentWindow.$('personal_industry_way').value);
//	doSelected('company_industry_way',$('inIframe').contentWindow.$('company_industry_way').value);
//	doSelected('company_industry_way',$('inIframe').contentWindow.$('company_industry_way').value);
	
	//客户类型
	doSelected('cus_type',window.opener.$('cus_type').value);
	//客户名称
	$('cus_name').value = window.opener.$('cus_name').value;
	$('link_way').value = window.opener.$('link_way').value;
	$('personal_birthDay').value = window.opener.$('personal_birthDay').value;
	$('personal_mark').value = window.opener.$('personal_mark').value;
	$('company_charactor').value = window.opener.$('company_charactor').value;
	$('company_dicide_positon').value = window.opener.$('company_dicide_positon').value;
	$('company_remark').value = window.opener.$('company_remark').value;
	$('operation_type').value = window.opener.$('operation_type').value;
	doSelected('link_Way_Type',window.opener.$('link_Way_Type').value);
	doSelected('personal_industry_way',window.opener.$('personal_industry_way').value);
	doSelected('company_industry_way',window.opener.$('company_industry_way').value);
	doSelected('personal_sex',window.opener.$('personal_sex').value);
	//联系人信息
	if($F("cus_type")==personal){
		document.getElementsByName("linkMan_name")[0].value = $('cus_name').value;
		document.getElementsByName("isPrimaryLinkMan")[0].checked = true;
		doSelected2(document.getElementsByName("linkMan_type")[0],'20571002');
		doSelected2(document.getElementsByName("linkMan_sex")[0],$F("personal_sex"));
		var obj = $("link_Way_Type");
		if(obj.value=='20551001'){
			document.getElementsByName("linkMan_mobile")[0].value = $('link_way').value;
			document.getElementsByName("linkMan_tel")[0].value = window.opener.$('cus_tel_back').value;
		}
		if(obj.value=='20551002'){
			document.getElementsByName("linkMan_tel")[0].value = $('link_way').value;
			document.getElementsByName("linkMan_mobile")[0].value = window.opener.$('link_way_back').value;
		}
		if(obj.value=='20551003'){
			document.getElementsByName("linkMan_fami_tel")[0].value = $('link_way').value;
			document.getElementsByName("linkMan_mobile")[0].value = window.opener.$('link_way_back').value;
		}
		if(obj.value=='20551004'){
			document.getElementsByName("linkMan_email")[0].value = $('link_way').value;
			document.getElementsByName("linkMan_mobile")[0].value = window.opener.$('link_way_back').value;
			document.getElementsByName("linkMan_tel")[0].value = window.opener.$('cus_tel_back').value;
		}
	}else{
		document.getElementsByName("linkMan_name")[0].value = window.opener.$('cus_name_back').value;
		document.getElementsByName("isPrimaryLinkMan")[0].checked = true;
		doSelected2(document.getElementsByName("linkMan_type")[0],'20571002');
		doSelected2(document.getElementsByName("linkMan_sex")[0],window.opener.$('cus_sex_back').value);
		document.getElementsByName("linkMan_mobile")[0].value = window.opener.$('link_way_back').value;
		document.getElementsByName("linkMan_tel")[0].value = window.opener.$('cus_tel_back').value;
	}
}
function setOperatonType(value){
	$("operation_type").value = value;
}
function setIntentionFlag(){
	$("intention_flag").value = true;
}
function getCommonCustId(){
	return $F("get_cust_id");
}
function getCommonCustName(){
	return $F("get_cust_name");
}
function getActionName(){
	return $F("cust_action_name");
}
function setActionName(value){
	$("cust_action_name").value = value;
}
//生成选择框
function genSelByValueBox(id,obj,selectedKey,setAll,_class_,_script_,nullFlag){ //根据type生成下拉框
	var str = "";
	str += "<select id='" + id + "' name='" + id +"' class='"+ _class_ +"' " + _script_ ;
	if(nullFlag!=undefined&&nullFlag==true){
		str += " datatype='0,is_null,200' ";
	}
	str += " > ";
	if(setAll){
		str += genDefaultOpt();
	}
	for(var i=0;i<obj.length;i++){
		str += "<option " + (obj[i].value == selectedKey ? "selected" : "") + " value='" + obj[i].value + "' title = '"+obj[i].name+"' >" + obj[i].name + "</option>";
	}
	str += "</select>";
	return str;
}
/*--------------------------------end----------------------------------------------*/
//根据联系方式类型变换数据的校验规则
function changeVilidateFun(obj){
	if(obj.value=='20551001'){
		$("link_way").datatype = "0,is_digit,11,11";
	}
	if(obj.value=='20551002'||obj.value=='20551003'){
		$("link_way").datatype = "0,is_phone,15,7";
	}
	if(obj.value=='20551004'){
		$("link_way").datatype = "0,is_email,50";
	}
}
function showOrgs(path){ //公用模块代理商选择页面
	OpenHtmlWindow(path+'/common/OrgMng/queryOrgs.do',700,450);
}


function showDealer(path){ //公用模块代理商选择页面
	OpenHtmlWindow(path+'/common/OrgMng/queryDealer.do',800,450);
}
/*
**代理商多选页面，可以选择多个代理商
**add by wangwenhu 2010-03-17
*/
function showDealers(path){ //公用模块代理商选择页面,多选
	OpenHtmlWindow(path+'/common/OrgMng/queryDealers.do',800,450);
}
function showUpload(path){
	OpenHtmlWindow(path+'/commonUpload.jsp',800,450);
}
function showUpload2(path){
	OpenHtmlWindow(path+'/commonUpload2.jsp',800,450);
}
function showUpload_guangxuan(path,index,index2,index3,sdetial_id,type){
	OpenHtmlWindow(path+'/commonUpload_guangxuan.jsp?index='+index+'&index2='+index2+'&index3='+index3+'&sdetial_id='+sdetial_id+'&type='+type,800,450);
}
//新闻附件上传JS 2013-01-24 韩晓宇
function showNewsUpload(path) {
	OpenHtmlWindow(path+'/newsCommonUpload.jsp',800,450);
}
//新增二级经销商上传附件
function showDealerUpload(path) {
	OpenHtmlWindow(path+'/DealerCommonUpload.jsp',800,450);
}
//车辆验收附件上传JS 2014-07-03 郑志强
function showVehicleUpload(index,path) {
	OpenHtmlWindow(path+'/vehicleCommonUpload.jsp?index='+index,800,450);
}

function showCompany(path){ //公用模块代理商选择页面
	OpenHtmlWindow(path+'/common/OrgMng/queryCompany.do',800,450);
}

function showModel(path,funcname){ //公用模块车型选择页面
	OpenHtmlWindow(path+'/common/ModelMng/queryPro.do?funcname='+funcname,800,450);
}

function showDealerTree(path){ //公用模块代理商选择页面
	OpenHtmlWindow(path+'/common/OrgMng/queryDealerTree.do',700,450);
}



function showCarRefitItem(path){ //整备信息详细明细查询方法
	OpenHtmlWindow(path,500,300);
}

function showBrandSeries(path){ //公用模块厂家车系选择页面
	OpenHtmlWindow(path+'/dialog/DiaSeries.jsp',700,450);
}
function showBrandSeriesByDlr(path){ //公用模块厂家车系选择页面(根据DealerId和品牌的关系进行过滤)
	OpenHtmlWindow(path+'/dialog/DiaSeriesByDlr.jsp',700,450);
}
//车牌选择公共模块
function queryCarLicense(path,carlicense){
	OpenHtmlWindow(path+"/common/CommonUtil/allLisenceQuery.do?COMMAND=1&fun="+carlicense,800,600);
}
//车辆发布列表缩略图查看模块
function queryCarPic(path,vhclId){
	OpenHtmlWindow(path+"/carsell/carIssuance/CarIssuanceAction/showPic.do?COMMAND=1&vhclId="+vhclId,800,600);
}
//销售车辆公共选择接口
function selectVhcl(path,vhclId){
	if(vhclId!=undefined){
		OpenHtmlWindow(path+"/carsell/sellOrder/NewSaleOrder/querySellVhcl.do?COMMAND=1&vhclId="+vhclId,800,600);
	}else{
		OpenHtmlWindow(path+"/carsell/sellOrder/NewSaleOrder/querySellVhcl.do?COMMAND=1",800,600);
	}
}
// 车辆发布显示图片公共方法
function showpic(value){
	   queryCarPic('<%=path %>',value);
	}
//年、月、日下拉框联动    lax --add-- 2009-09-04
function initDate(year,month,day)   
{   
 //每个月的初始天数   
    MonDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];   
    //当前的年份   
    var y = new Date().getFullYear();   
    //当前的月份   
    var m = new Date().getMonth()+1; //javascript月份为0-11   
    //当前的天   
    var d = new Date().getDate();   
 //以今年为准，向后2年，填充年份下拉框   
    for (var i = y; i < (y+1); i++){   
    		year.options.add(new Option(i+"年",i));   
	}   
 //选中今年   
 // year.value=y;   
    //填充月份下拉框   
    for (var i = 1; i <= 12; i++){  
    	if(i < 10){
    		month.options.add(new Option("0"+i+"月","0"+i));
    	}else{
    		month.options.add(new Option(i+"月",i));
    	}
	}   
 //选中当月   
 //month.value = m;   
 //获得当月的初始化天数   
 var n = MonDays[m-1];   
 //如果为2月，天数加1   
 if (m == 2 && isLeapYear(year.options[year.selectedIndex].value))   
    n++;   
 //填充日期下拉框   
 // createDay(n,day);    
 //选中当日   
 // day.value = new Date().getDate();   
}
function change(year,month,day) //年月变化，改变日   
{   
     var y = year.options[year.selectedIndex].value;   
     var m = month.options[month.selectedIndex].value;    
     var n = MonDays[m - 1];   
     if ( m ==2){
       		n++;
       }
     //createDay(n,day)
}   
function change1(year,month,day) //年月变化，改变日   
{
     var y = year.options[year.selectedIndex].value;   
     var m = month.options[month.selectedIndex].value;
     var n = MonDays[m - 1];   
     if ( m ==2 && isLeapYear(y)){   
         n++;   
     }   
     createDay(n,day);   
}
function createDay(n,day) //填充日期下拉框 
{
    //清空下拉框
     clearOptions(day);
     //几天，就写入几项   
 day.options.add(new Option("-请选择-",""));
     for(var i=1; i<=n; i++){
    	 if(i < 10){
    		 day.options.add(new Option("0"+i,"0"+i)); 
    	 }else{
    		 day.options.add(new Option(i,i));  
    	 }  
     }   
}
function clearOptions(ctl)//删除下拉框中的所有选项 
{   
   for(var i=ctl.options.length-1; i>=0; i--){   
        	ctl.remove(i);
         }
}
function isLeapYear(year)//判断是否闰年
{
    return( year%4==0 || (year%100 ==0 && year%400 == 0));
}
//获得颜色代码
function getColorCode(color){
	var groupcolor = "";
	if(color=='20011001'){
		groupcolor='red';
	}
	else if(color=='20011002'){
		groupcolor='#FF9900';
	}
	else if(color=='20011003'){
		groupcolor='#FFFF00';
	}
	else if(color=='20011004'){
		groupcolor='#00FF00';
	}
	else if(color=='20011005'){
		groupcolor='#66CCCC';
	}
	else if(color=='20011006'){
		groupcolor='#0000FF';
	}
	else if(color=='20011007'){
		groupcolor='#990099';
	}
	else if(color=='20011008'){
		groupcolor='#FFFFFF';
	}
	return groupcolor;
}
//获得背景颜色代码
function getColorStyleCode(color){
	var groupcolor;
	var colorCode = getColorCode(color);
	groupcolor = 'background:'+colorCode+';';
	return groupcolor;
}
//获得字体颜色代码
function getFontColorStyleCode(color,con){
	var groupcolor;
	var colorCode = getColorCode(color);
	groupcolor = '<font style="color:'+colorCode+'; "><strong>'+con+'</strong></font>';
	return groupcolor;
}
//--------联系人姓名不能重复---------//
function chklinkManNames(){
	var linkManNames = document.getElementsByName("linkMan_name");
	var NUM = linkManNames.length;
	/*冒泡法排序*/
　　 for(i=0; i<NUM-1; i++){ /*外循环：控制比较趟数*/
　　		for(j=NUM-1; j>i; j--) /*内循环：进行每趟比较*/
		　　 if(linkManNames[j].value==linkManNames[j-1].value) /*如果data[j]大于data[j-1],交换两者的位置*/
		　　 {
				alert("联系人姓名不能重复!");
				return false;
		　　 }
	}
	return true;
}
//公用模块申请单位选择页面
function showApply(path){ 
	OpenHtmlWindow(path+'/feedbackmng/apply/CommonQueryApply/queryApply.do',800,450);
}
//钱转换带千位符
function formatCurrency(num,metadata,record) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + '' + num + '.' + cents);
}

/*子表中鼠标上下键移动
 * add by andyzhou
 * 2013-5-30
 */
function jumpCursor(event){
	var obj = Event.element(event);
	var objs = document.getElementsByName(obj.name);		
	if(objs.length == null){
		var iLine = -1;
	}
	for(var i=0;i<objs.length;i++){
		if(objs[i]==obj){
			var iLine = i;
		}
	}
	var iKeyCode = event.keyCode;
	if(iKeyCode == 38){
		var nextobj=objs[iLine-1];
		nextobj.select();
		nextobj.focus();
	}		
	if(iKeyCode == 40){
		var nextobj=objs[iLine+1];
		nextobj.select();
		nextobj.focus();
	}
}






	