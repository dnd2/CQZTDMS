<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<title>节能惠民上报</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
<!--
//厂商指导价与销售价格限制
function chkSales(actPrice, textValue, minPrice, maxPrice) {
	if(minPrice) {
		if(parseFloat(actPrice) < parseFloat(minPrice)) {
			MyAlert(textValue + "不能小于" + parseFloat(minPrice)) ;
			
			return false ;
		}
	}
	
	if(maxPrice) {
		if(parseFloat(actPrice) > parseFloat(maxPrice)) {
			MyAlert(textValue + "不能大于" + parseFloat(maxPrice)) ;
			
			return false ;
		}
	}
	
	return true ;
}
//-->
</script>
</head>
<BODY onload="initPage();loadcalendar();">
<div class="navigation">
<img src="../../img/nav.gif" />&nbsp;当前位置： 节能惠民管理&nbsp;&gt;&nbsp;节能惠民信息上报</div>  
  <form name="fm" >
  	<input type=hidden name='vin' id='vin' value='${map.VIN }'/>
  	<input type=hidden name='purChasedDate' id='purChasedDate' value='${map.PURCHASED_DATE }'/>
  	<input type=hidden name='modelName' id='modelName' value='${map.MODELNAME }'/>
  	<input type=hidden name='modelCode' id='modelCode' value='${map.MODELCODE }'/>
  	
  	<input type=hidden name='dealerName' id='dealerName' value='${map.DEALER_SHORTNAME }'/>
  	<input type=hidden name='ctmName' id='ctmName' value='${map.CTM_NAME }'/>
  	<input type=hidden name='ctmId' id='ctmId' value='${map.CTM_ID }'/>
  	<input type=hidden name='dealerId' id='dealerId' value='${map.DEALER_ID }'/>
  	<input type=hidden name='modelId' id='modelId' value='${map.MODEL_ID }'/>
  	<input type=hidden name='orgId' id='orgId' value='${map.ORG_ID }'/>
  	<input type=hidden name='salesId' id='salesId' value='${map.SALES_ID }'/>
  	<input type=hidden name='vichId' id='vichId' value='${map.VEHICLE_ID }'/>
  	<input type="hidden" name="linkTel1" id="linkTel1" class="middle_txt" value="${map.PHONE }" />
  	<input type="hidden" name="invNo" id="invNo" class="middle_txt" value="${map.INVOICE_NO}" />
    <table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 销售信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">VIN：</td>
			<td align="left" nowrap width="30%">${map.VIN }</td>
			<td align="right" nowrap width="15%">销售时间：</td>
			<td align="left" nowrap width="30%">${map.PURCHASED_DATE }
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">车型名称：</td>
			<td align="left" nowrap width="30%">${map.MODELNAME }</td>
			<td align="right" nowrap width="15%">车型代码：</td>
			<td align="left" nowrap width="30%">${map.MODELCODE }</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">发票号：</td>
			<td align="left" nowrap width="30%">
				<input type="text" onblur="javascript:checkNoFormat();" name="invoceNo" id="invoceNo" class="short_txt" value="" maxlength="8"/> &nbsp;<font color="red">*输入发票号后6位：NO123456</font>
			</td>
			<td align="right" nowrap width="15%">牌照号：</td>
			<td align="left" nowrap width="30%">
				<input maxlength="7" type="text" name="vichNo" id="vichNo" class="middle_txt" value="${map.VEHICLE_NO }" onBlur="chkVhlNo(this.value)" /> &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">厂商指导价：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="factoryPrice" id="factoryPrice" class="short_txt" value="${map.PRICE }" /> 元&nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%">销售价格：</td>
			<td align="left" nowrap width="30%"><input class="short_txt" type=text name='salePrice' id='salePrice' value='${map.PRICE }'/> 元&nbsp;<font color="red">*</font></td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">兑现金额：</td>
			<td align="left" nowrap width="30%"><input maxlength="4" class="short_txt" type=text name='payMoney' id='payMoney' readonly="readonly" value='3000'/> 元&nbsp;<font color="red">*</font></td>
	   		<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 经销商信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">经销商名称：</td>
			<td align="left" nowrap width="30%">${map.DEALER_NAME }</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">联系人：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="linkPerson1" id="linkPerson1" class="short_txt" value="${map.LINK_MAN }" /> &nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%">联系电话：</td>
			<td align="left" nowrap width="30%">
				<input type="text" onblur="javascript:checkPhoneFormat();" name="cellPhone" id="cellPhone" class="middle_txt" value="" maxlength="11" /><input maxlength="4" type="text" name="fixatePhoneHead" id="fixatePhoneHead" class="mini_txt" value="" style="display:none;width:30px" /><span id="shortLine" id="shortLine" style="display:none;">&nbsp;-&nbsp;</span><input type="text" maxlength="8" name="fixatePhoneBody" id="fixatePhoneBody" class="short_txt" value="" style="display:none;" /> &nbsp;<font color="red">*</font>
				<label for="fix">座机</label><input type="radio" name="dlrTell" id="fix" onclick="changeTell();" value="fix"><label for="move">手机</label><input type="radio" name="dlrTell" id="move" checked="checked" onclick="changeTell();" value="move">
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">行政区划代码：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="saleZipCode11" id="saleZipCode11" class="short_txt" maxlength="6" value="" disabled="disabled"/> &nbsp;<font color="red">*</font>
				<input type="hidden" name="saleZipCode1" id="saleZipCode1" value=""/>
			</td>
			<td align="right" nowrap width="15%">省：</td>
			<td align="left" nowrap width="30%"><select class="min_sel" id="dPro" name="dPro" onchange="_genCity(this,'dCity')"></select> 
				 &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
	   <td align="right" nowrap width="15%">市：</td>
			<td align="left" nowrap width="30%">
				<select class="min_sel" id="dCity" name="dCity" onchange="_genCity(this,'dTown')"></select>
				 &nbsp;<font color="red">*</font>
			</td>
		<td align="right" nowrap width="15%">县：</td>
			<td align="left" nowrap width="30%">
				<select class="min_sel" id="dTown" name="dTown" onchange="setDlrTownCode() ;" ></select>
				 &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">详细地址：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="address1" id="address1" class="long_txt" value="${map.DEALER_ADDRESS }" /> &nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1" bgcolor="91908E"  class="table_edit">
       <tr>
         <th colspan="6"  ><img class="nav" src="../../img/subNav.gif" /> 消费者信息</th>
       </tr>
	   <tr>
			<td align="right" nowrap width="15%">消费者名称：</td>
			<td align="left" nowrap width="30%">${map.CTM_NAME }</td>
			<td align="right" nowrap width="15%">个人/公司：</td>
			<td align="left" nowrap width="30%"><script type="text/javascript">writeItemValue(${map.CTM_TYPE });</script></td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">联系人：</td>
			<td align="left" nowrap width="30%">
						<input type="text" name="linkPerson2" id="linkPerson2" class="short_txt" value="${map.CTM_NAME }" /> &nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%">联系电话：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="linkTel2" id="linkTel2" class="middle_txt" value="${map.MAIN_PHONE }" /> &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">行政区划代码：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="saleZipCode22" id="saleZipCode22" class="short_txt" maxlength="6" value="${map.TOWN }" disabled="disabled"/> &nbsp;<font color="red">*</font>
				<input type="hidden" name="saleZipCode2" id="saleZipCode2" value="${map.TOWN }"/>
			</td>
	   		<td align="right" nowrap width="15%">省：</td>
			<td align="left" nowrap width="30%">
				<select class="min_sel" id="cPro" name="cPro" onchange="_genCity(this,'cCity')"></select> 
				 &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">市：</td>
			<td align="left" nowrap width="30%">
				<select class="min_sel" id="cCity" name="cCity" onchange="_genCity(this,'cTown')"></select>
				 &nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%">县：</td>
			<td align="left" nowrap width="30%">
				<select class="min_sel" id="cTown" name="cTown" onchange="javascript:setCtmTownCode();"></select> 
				 &nbsp;<font color="red">*</font>
			</td>
	   </tr>
	   <tr>
			<td align="right" nowrap width="15%">详细地址：</td>
			<td align="left" nowrap width="30%">
				<input type="text" name="address2" id="address2" class="long_txt" value="${map.CTM_ADDRESS }" /> &nbsp;<font color="red">*</font>
			</td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
	   </tr>
    </table>
	<br />
	
     <table class="table_edit">
		<tr> 
			<td align="right" nowrap width="15%">备注：</td>
			<td align="left" nowrap width="30%"><textarea name="remark" id="remark" cols="60" rows="3"></textarea></td>
			<td align="right" nowrap width="15%"></td>
			<td align="left" nowrap width="30%"></td>
		</tr>
		<tr > 
            <td height="12" align="center" colspan="4">
				<input type="button" id="btn_report" onclick="doReport();" class="normal_btn"  style="width=8%" value="提报"/>&nbsp;&nbsp;
				<input type="button" onClick="javascript:history.go(-1) ;" class="normal_btn"  style="width=8%" value="返回"/>
			</td>
		</tr>
    </table>
</form>
<script type="text/javascript" > 
function chkVhlNo(StrValue) {
	var reg =  /[^\u0000-\u00FF]\w{6}/ ;
	var flag = reg.exec(StrValue) ;
	
	if(!flag) {
		MyAlert("提醒：<font color=\"red\">牌照号第一位不是汉字或位数不满七位！</font>") ;
	}
}
//设置客户行政区划代码
function setCtmTownCode(){
	var sTown = $('cTown').value ;
	var sCity = $('cCity').value ;
	var iLen = sTown.length ;
	
	if(iLen <= 6) {
		$('saleZipCode2').value = sTown ;
		$('saleZipCode22').value = sTown ;
	} else {
		iLen = sCity.length ;
		
		if(iLen <= 6) {
			$('saleZipCode2').value = $('cCity').value ;
			$('saleZipCode22').value = $('cCity').value ;
		} else {
			$('saleZipCode2').value = $('cPro').value ;
			$('saleZipCode22').value = $('cPro').value ;
		}
	}
}

function setDlrTownCode() {
	var sTown = $('dTown').value ;
	var sCity = $('dCity').value ;
	var iLen = sTown.length ;
	
	if(iLen <= 6) {
		$('saleZipCode1').value = sTown ;
		$('saleZipCode11').value = sTown ;
	} else {
		iLen = sCity.length ;
		
		if(iLen <= 6) {
			$('saleZipCode1').value = $('dCity').value ; 
			$('saleZipCode11').value = $('dCity').value ;
		} else {
			$('saleZipCode1').value = $('dPro').value ; 
			$('saleZipCode11').value = $('dPro').value ;
		}
	}
}

function setMyPhone() {
	var phoneFlag = "${phoneType}" ;
	
	if(phoneFlag == "fix") {
		$("fix").checked = "checked" ;
		
		var aTel = "${map.PHONE }".split("-") ;
		$("fixatePhoneHead").value = aTel[0] ;
		$("fixatePhoneBody").value = aTel[1] ;
	} else {
		$("move").checked = "checked" ;
		$("cellPhone").value = "${map.PHONE }" ;
	}
}
genLocSel('dPro','dCity','dTown','${map.PROVINCE_ID}','${map.CITY_ID}','${map.TOWN_ID}'); // 加载省份城市和县
genLocSel('cPro','cCity','cTown','${map.PROVINCE}','${map.CITY}','${map.TOWN}'); // 加载省份城市和县
setCtmTownCode() ;
setDlrTownCode() ;
setMyPhone() ;
changeTell();

	//页面初始化（去掉空格）
	function initPage(){
		$('address1').value = $('address1').value.replace(/[ ]/g,""); 
		$('address2').value = $('address2').value.replace(/[ ]/g,"");
		var  originalVal = $('invNo').value;
		if(originalVal!=''||originalVal!=null){
			$('invoceNo').value = 'NO'+originalVal.toString().substring(0,6);
		}else{
			$('invoceNo').value = 'NO';
		}	
	}


	function doReport(){
		
		if(!chkSales(document.getElementById('factoryPrice').value, '厂商指导价', 20000, 200000)) return false ;
		if(!chkSales(document.getElementById('salePrice').value, '销售价格', 20000, 200000)) return false ;
			
		if($('invoceNo').value==''||$('invoceNo').value==null){
			MyAlert('请输入发票号！');
			return;
		}
		if(!checkNoFormat()){
			return;
		}
		if(!checkSpeWord()){
			return;
		}
		if($('vichNo').value==''||$('vichNo').value==null){
			MyAlert('请输入牌照号！');
			return;
		}
		if($('factoryPrice').value==''||$('factoryPrice').value==null){
			MyAlert('请输入厂产指导价！');
			return;
		}
		if($('salePrice').value==''||$('salePrice').value==null){
			MyAlert('请输入销售价格！');
			return;
		}
		//验证兑现金额
		if(!limitPayMoney()){
			return;
		}
		if($('linkPerson1').value==''||$('linkPerson1').value==null){
			MyAlert('请输入经销商联系人！');
			return;
		}
		//先设置电话再判断
		setPhoneNo();
		if($('linkTel1').value==''||$('linkTel1').value==null){
			MyAlert('请输入经销商联系电话！');
			return;
		}
		//验证电话格式
		if(!checkPhoneFormat){
			return;
		}
		if($('dPro').value==''||$('dPro').value==null){
			MyAlert('请输入经销商所属省份！');
			return;
		}
		if($('dCity').value==''||$('dCity').value==null){
			MyAlert('请输入经销商所属城市！');
			return;
		}
		if($('dTown').value==''||$('dTown').value==null){
			MyAlert('请输入经销商所属县！');
			return;
		}
		if($('saleZipCode1').value==''||$('saleZipCode1').value==null){
			MyAlert('经销商行政区划代码错误！');
			return;
		}
		if($('address2').value==''||$('address2').value==null){
			MyAlert('请输入经销地址！');
			return;
		}
		if($('linkPerson2').value==''||$('linkPerson2').value==null){
			MyAlert('请输入客户联系人！');
			return;
		}
		if($('linkTel2').value==''||$('linkTel2').value==null){
			MyAlert('请输入客户联系电话！');
			return;
		}
		
		if($('cPro').value==''||$('cPro').value==null){
			MyAlert('请输入客户所属省份！');
			return;
		}
		if($('cCity').value==''||$('cCity').value==null){
			MyAlert('请输入客户所属城市！');
			return;
		}
		if($('cTown').value==''||$('cTown').value==null){
			MyAlert('请输入客户所属县！');
			return;
		}
		if($('saleZipCode2').value==''||$('saleZipCode2').value==null){
			MyAlert('客户行政区划代码错误！');
			return;
		}
        if($('address2').value==''||$('address2').value==null){
			MyAlert('请输入客户地址！');
			return;
		}
		document.getElementById("btn_report").disabled = true ;
		MyConfirm("确认对该车辆实行节能惠政策上报！",doReportConfirm);
		//window.location.href="<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConReport.do?vehicleId="+vehicleId;;
	}  
	//上报提示
	function doReportConfirm(){
		var url="<%=contextPath %>/conservation/EnergyConservationDlr/vehicleEnergyConReport.json?COMMAND=1";
		makeNomalFormCall(url,showEnergyNo,"fm");
	}
	function showEnergyNo(json){
		var returnStr = json.returnStr ;
		
		if(returnStr == "error") {
			MyAlert("对应牌照号已上报,请核对后输入!") ;
			
			document.getElementById("btn_report").disabled = false ;
		} else {
			window.location.href="<%=contextPath%>/conservation/EnergyConservationDlr/vehicleEnergyConApply.do";
			MyAlert("申请成功！") ;
		}
	}
	
	function MyselfAlertForFun(info,fun){
		 var owner = getTopWinRef();
		 try{
		  _myDialogInit();  
		  owner.getElementById('dialog_content_div').innerHTML='\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
		  owner.getElementById('dialog_alert_info').innerHTML=info;
		  owner.getElementById('dialog_alert_button').onclick=function(){
			try{
			 if(fun){
			  fun();
			 }
			}finally{
			 _hide();
			}
		  };
		  _setSize(300, 200); 
		  _show();
		 }catch(e){
		  MyAlert('MyAlert : '+ e.name+'='+e.message);
		 }finally{
		  owner=null;
		 }
		}
	function _myDialogInit(sWidth ,sHeight ,sTop ,sLeft){//初始化
	    var owner = null;
		owner = getTopWinRef();
		if( owner.getElementById('dialog_div')==null ){
			var div = owner.createElement('div');
			div.setAttribute('id','dialog_div');
			owner.getElementsByTagName('body').item(0).appendChild(div);
			
			_crtBackGlassDiv(sWidth ,sHeight ,sTop ,sLeft);
			_crtBackIFrame(sWidth ,sHeight ,sTop ,sLeft); //add by zhaoli
			_crtModalDiv(sWidth ,sHeight ,sTop ,sLeft);
			_crtBackGroudDiv();
			_crtContentDiv();
			_crtContentFrame();
		} 
		if(owner.getElementById('dialog_modal_div')!=null){
			owner.getElementById('dialog_modal_div').onclick = function(){}
		}
		owner =null;
	}
	//验证发票位数据
	function checkNoFormat(){
		var val = $('invoceNo').value;	
		var no = val.substring(0, 2);
		if(val.length!=8||no!='NO'||isNaN(val.substring(2,val.length))){
			//$('invoceNo').value="";
			MyAlert('发票格式不对');
			return false;
		}else{
			return true;
		}
	}
	//验证车牌号
	function checkVehiclNo(){
	
		var vehNo = $('vichNo').value;
		
		var reg = new RegExp("[\\u4e00-\\u9fae0][A-Z](\\d|[A-Z]){5}");
        if (!reg.test(vehNo)) {
            MyAlert("车牌号格式不正确！例：渝A888U0");
        }

	}
	//过滤特殊符号
	function checkSpeWord(){ 
		var strInput = $('vichNo').value;
		var forbidChar = new Array(",","-","/","\\","'","%","\""); 
		for (var i = 0;i < forbidChar.length ; i++){ 
		  if(strInput.indexOf(forbidChar[i]) >= 0){ 
		            MyAlert("您输入的信息: "+strInput+" 中含有非法字符: "+forbidChar[i]+" 请更正！"); 
		      return false; 
		  } 
		} 
		return true; 
	} 

	//手机座机切换
	function changePhone(){
		var cellPhone = $('cellPhone');
		var fixatePhoneHead = $('fixatePhoneHead');
		var shortLine = $('shortLine');
		var fixatePhoneBody = $('fixatePhoneBody');
		if(cellPhone.style.display==''){
			cellPhone.style.display = 'none';
			fixatePhoneHead.style.display = '';
			shortLine.style.display = '';
			fixatePhoneBody.style.display = '';
		}else{
			cellPhone.style.display = '';
			fixatePhoneHead.style.display = 'none';
			shortLine.style.display = 'none';
			fixatePhoneBody.style.display = 'none';
		}
	}
	
	function changeTell() {
		var cellPhone = $('cellPhone');
		var fixatePhoneHead = $('fixatePhoneHead');
		var shortLine = $('shortLine');
		var fixatePhoneBody = $('fixatePhoneBody');

		if(document.getElementById("fix").checked) {
			cellPhone.style.display = 'none';
			fixatePhoneHead.style.display = '';
			shortLine.style.display = '';
			fixatePhoneBody.style.display = '';
		} else if(document.getElementById("move").checked){
			cellPhone.style.display = '';
			fixatePhoneHead.style.display = 'none';
			shortLine.style.display = 'none';
			fixatePhoneBody.style.display = 'none';
		}
	}
	//设置电话
	function setPhoneNo(){
		var cellPhone = $('cellPhone');
		var fixatePhoneHead = $('fixatePhoneHead');
		var shortLine = $('shortLine');
		var fixatePhoneBody = $('fixatePhoneBody');
		var linkTel1 = $('linkTel1');
		if(cellPhone.style.display==''){
			linkTel1.value = cellPhone.value;
		}else{
			linkTel1.value = fixatePhoneHead.value+'-'+fixatePhoneBody.value;
		}
	}	
	//验证电话格式
	function checkPhoneFormat(){
		var cellPhone = $('cellPhone');
		var fixatePhoneHead = $('fixatePhoneHead');
		var fixatePhoneBody = $('fixatePhoneBody');
		if(cellPhone.style.display==''){
			if(cellPhone.value.length!=11){
				MyAlert("手机号码长度不是11位！");
				return false;
			}
			if(isNaN(cellPhone.value)){
				MyAlert("手机号码只能输入数字！");
				return false;
			}
		}else{
			if(fixatePhoneHead.value.length!=3){
				MyAlert("区号长度不是3位！");
				return false;
			}
			if(isNaN(fixatePhoneHead.value)){
				MyAlert("区号只能输入数字！");
				return false;
			}
			if(fixatePhoneBody.value.length!=8){
				MyAlert("座机长度不是8位！");
				return false;
			}
			
			if(isNaN(fixatePhoneBody.value)){
				MyAlert("座机只能输入数字！");
				return false;
			}
		}
	}
	/*
	//兑现时间限制
	function limitPayDate(){
		
		  var obj= $('payDate').value;
		  var arr=obj.split("-");
		  var oldtime=new Date(arr[0],arr[1],arr[2]);
		  var oldtimes=oldtime.getTime();
		  
		  var now=new Date();
		  var now2 = new Date(now.getYear(),now.getMonth()+1,now.getDate());
		  var newtime=now2.getTime();
		  if(newtime>oldtimes)
		   {
		    	MyAlert('兑现时间不能小于当前时间！');
		    	return false;
		   } else{
		   		return true;
		   }
	}*/
	//兑现金额限制
	function limitPayMoney(){
		var payMoney = $('payMoney');
		if(payMoney.value>3000){
			MyAlert('兑现金额不能超过3000元！');
			
			return false;
		}else{
			return true;
		}
	}
</SCRIPT>
</BODY>
</html>