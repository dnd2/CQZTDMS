<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
	%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		doCusChange();
	}
</script>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
</div>
<form name="fm" id="fm">
   <input type="hidden" id="aaaa" >
	<table class="table_edit" id="vehicleInfo">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td width="22%"><input name="vin" id="vin" type="text" class="middle_txt" 
			 onblur="queryVehicle()"/><font color="red">*</font></td>
			<td width="10%" align="right">发动机号：<input type="hidden" id="engineNo" name="engineNo"/></td>
			<td width="22%" align="left"><input name="engineNo1" id="engineNo1" type="text" class="middle_txt" readonly="readonly"/></td>
			<td width="12%" align="right">牌照号：<input type="hidden" id="vehicleNo" name="vehicleNo"/></td>
			<td width="24%"></td>
		</tr>
		<tr>
			<td align="right">产地：<input type="hidden" id="yieldly" name="yieldly"/></td>
			<td></td>
			<td width="10%" align="right">车系：<input type="hidden" name="seriesId" id="seriesId"/></td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">车型：<input type="hidden" name="modelId" id="modelId"/></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">购车日期：<input type="hidden" id="purchasedDate" name="purchasedDate"/></td>
			<td align="left"></td>
			<td align="right">行驶里程：<input type="hidden" id="mileage" name="mileage"/></td>
			<td></td>
			<td align="right">保养次数：<input type="hidden" id="freeTimes" name="freeTimes"/></td>
			<td></td>
		</tr>
		<tr>
			<td width="10%" align="right">车主姓名：<input type="hidden" id="ctmId" name="ctmId"/><input type="hidden" id="ctmName" name="ctmName"/><input type="hidden" id="ctmPhone" name="ctmPhone"/><input type="hidden" id="ctmAddress" name="ctmAddress"/><!-- 车主id --></td>
			<td></td>
			<td width="10%" align="right">车主电话：</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">三包策略代码：<input type="hidden" id="gameId" name="gameId"/><!-- 策略id --></td>
			<td></td>
		</tr>
		<tr>
			<td width="12%" align="right" nowrap="nowrap">三包规则代码：<!-- 三包规则代码 --></td>
			<td></td>
		</tr>
		<tr>
			<td width="10%" align="right">车主地址：<!-- 车主地址 --></td>
			<td></td>
		</tr>
		<th colspan="6" align="left"><img class="nav" src="../../../img/subNav.gif" />申请内容</th>
		<tr>
			<td align="right">申请类型：</td>
		  	<td align="left" colspan="5">
		  		<script type="text/javascript">
        			genSelBoxExp("changeType",<%=Constant.VEHICLE_CHANGE_TYPE%>,"",true,"short_sel","","false",'<%=Constant.VEHICLE_CHANGE_TYPE_04%>');
        		</script>
		  	</td>
		</tr>
		<tbody id="mileage_body" style="display:none"> <!-- 行驶里程变更 -->
        <tr>
        	<td align="right" nowrap="nowrap">提报错误单据选择：</td>
       		<td align="left" nowrap="nowrap"><input name="errorRoCode" id="errorRoCode" value="" type="text" class="middle_txt" readonly/><font color="red">*</font>
       		<input type="button" class="mini_btn" onclick="openItem();" value="..." />
       		<input type="button" class="normal_btn" onclick="clrRecord();" value="清除" /></td>
       		<td align="right" nowrap="nowrap">变更后数据：</td>
       		<td align="left"><input name="cmileage" id="cmileage" type="text" class="middle_txt" /><font color="red">*</font></td>
       		<td></td>
       		<td></td>
       	</tr>
        <tr>
          <td align="right">经销商名称：<input type="hidden" id="troDealerCode" name="troDealerCode"/></td>
          <td align="left" id="roDealerCode"></td>
          <td align="right">单据里程：<input type="hidden" id="troMileage" name="troMileage"/></td>
          <td align="left" id="roInMileage"></td>
          <td align="right" nowrap="nowrap">可变更最小里程：<input type="hidden" id="tcMileage" name="tcMileage"/></td>
          <td align="left" id="cInMileage"></td>
          <!--
          <td align="right">单据保养次数：<input type="hidden" id="troFreeTimes" name="troFreeTimes"/></td>
          <td align="left" id="roFreeTimes"></td>
           -->
        </tr>
        <tr>
          <!-- 
          <td align="right" nowrap="nowrap">可变更保养次数：<input type="hidden" id="tcFreeTimes" name="tcFreeTimes"/></td>
          <td align="left" id="cFreeTimes"></td>
           -->
          <td></td>
          <td></td>
        </tr>
        </tbody><!-- 行驶里程变更结束 -->
        
        <tbody id="freetimes_body" style="display:none"> <!-- 保养次数变更 -->
        <tr>
            <!-- 
        	<td align="right" nowrap="nowrap">提报错误单据选择：</td>
       		<td align="left" nowrap="nowrap" id="tderrorRoCode"><input name="errorRoCode" id="errorRoCode" value="" type="text" class="middle_txt" readonly/><font color="red">*</font>
       		<input type="button" class="mini_btn" onclick="openItem();" value="..." />
       		<input type="button" class="normal_btn" onclick="clrRecord();" value="清除" /></td>
       		 -->
       		<td align="right" nowrap="nowrap">变更后数据：</td>
       		<td align="left"><input name="cfree_times" id="cfree_times" type="text" class="middle_txt" onblur="queryOrder()"/><font color="red">*</font></td>
       		<td align="right" nowrap="nowrap">提报错误单据：</td>
       		<td align="left" nowrap="nowrap"><input name="ferrorRoCode" id="ferrorRoCode" value="" type="text" class="middle_txt" readonly/>
       		<td></td>
       		<td></td>
       	</tr>
        <tr>
          <td align="right">经销商名称：<input type="hidden" id="ftroDealerCode" name="ftroDealerCode"/></td>
          <td align="left" id="froDealerCode"></td>
          <!-- 
          <td align="right">单据里程：<input type="hidden" id="troMileage" name="troMileage"/></td>
          <td align="left" id="roInMileage"></td>
           -->
          <td align="right">单据保养次数：<input type="hidden" id="troFreeTimes" name="troFreeTimes"/></td>
          <td align="left" id="roFreeTimes"></td>
          <td align="right" nowrap="nowrap">可变更保养次数：<input type="hidden" id="tcFreeTimes" name="tcFreeTimes"/></td>
          <td align="left" id="cFreeTimes"></td>
        </tr>
        <!-- 
        <tr>
          <td align="right" nowrap="nowrap">可变更最小里程：<input type="hidden" id="tcMileage" name="tcMileage"/></td>
          <td align="left" id="cInMileage"></td>
          <td></td>
          <td></td>
        </tr>
         -->
        </tbody><!-- 保养次数变更结束 -->
        <!-- 购车日期变更 开始 -->
		<tbody id="purchase_date_chg"  style="display:none">
			<tr>
				<td align="right">变更后的日期：</td>
				<td>
				<input name="c_purchase_date" type="text" datetype="0,isnull" class="middle_txt" id="t1" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't1', false);" /> <font color="red">*</font>
				</td>
			</tr>
		</tbody>   
		<!-- 购车日期变更 结束 -->
		<!-- 车主信息变更 开始 -->
		<tbody id="ctm_info_chg"  style="display:none">
			<tr>
				<td align="right">用户姓名：</td>
				<td>
					<input type="text" name="c_ctm_name" id="c_ctm_name" class="middle_txt" datatype="1,is_textarea,50"/><font color="red">*</font>
				</td>
				<td align="right">用户电话：</td>
				<td>
					<input type="text" name="c_ctm_phone" id="c_ctm_phone" class="middle_txt" datatype="1,is_phone,15"/><font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right">用户地址：</td>
				<td colspan="3">
					<input type="text" name="c_ctm_address" id="c_ctm_address" class="maxlong_txt" datatype="1,is_textarea,150"/><font color="red">*</font>
				</td>
			</tr>
		</tbody>   
		<!-- 车主信息变更 结束 -->
        <tbody id="remark_body" style="display:none"><!-- 备注 -->
       	<tr>
          	<td align="right">备注：</td>
 			<td colspan="5" align="left"><textarea id="remark" name="remark" cols="120" rows="3" ></textarea><font color="red">*</font></td>
        </tr>
        <tr id="check_2">
          	<td colspan="6" align="left">
            <input type="checkbox" id="sure"/>
         	<font color="red"> * 此次变更与用户仔细核对，所有资料真实，特此向错误工单制作方提出投诉申请</font></td>
        </tr>
        </tbody><!-- 备注结束 -->
	</table>
	 <!-- 添加附件 开始  -->
        <table id="add_file" style="display:none" width="100%" class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
	    		<tr>
	        		<th>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/><font color="red">*</font>
					</th>
				</tr>
				<tr>
    				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  					<%if(fileList!=null){for(int i=0;i<fileList.size();i++) { %>
	 					 <script type="text/javascript">
	 	 					 addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>');
	 	 				</script>
					<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		<table width=100% border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="12" align=left width="33%">&nbsp;</td>
             	<td height="12" align=center width="33%">
                	<input type="button" onClick="doSave();" class="normal_btn"  style="width=8%" value="保存"/>
                	&nbsp;
                	<input type="button" onclick="doSubmit();" class="normal_btn"  style="width=8%" value="上报"/>
                	&nbsp;
					<input type="button" onClick="javascript:history.go(-1);" class="normal_btn"  style="width=8%" value="返回"/>
    			</td>
            	<td height="12" align=center width="33%">
      			</td>
			</tr>
		</table>
	</form>
</body>
</html>
<script type="text/javascript" >
function queryVehicle() {
	var vin = document.getElementById("vin").value;
	 if(vin.length ==18){
       vin = vin.substring(0,vin.length-1);
        var strAscii = new Array();//用于接收ASCII码
		var res = "";
		for(var i = 0 ; i < vin.length ; i++ ){
		strAscii[i] = parseInt(vin.charCodeAt(i))-1;//只能把字符串中的字符一个一个的解码
		res+=String.fromCharCode(strAscii[i]);  //完成后，转化为为字母或者数字
		}
		document.getElementById("vin").value=res;
		vin = res;
        }
	var pattern=/^([A-Z]|[0-9]){17,17}$/;
	if(pattern.exec(vin)) {
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicle.json?vin='+vin;
		makeNomalFormCall(url,showVehicle,'fm');
	} else {
		MyAlert("不是有效的VIN格式");
	}
}
function showVehicle(json) {
	if (!json.vehicle) {
		MyAlert("没有找到对应的VIN");
		return;
	}
	var vehicleInfo = document.getElementById("vehicleInfo");
	var vehicle = json.vehicle;
	//vehicleInfo.rows[1].cells[3].innerText = checkNull(vehicle.ENGINE_NO);//发动机号
	document.getElementById("engineNo").value = checkNull(vehicle.ENGINE_NO);
	document.getElementById("engineNo1").value = checkNull(vehicle.ENGINE_NO);
	vehicleInfo.rows[1].cells[5].innerText = checkNull(vehicle.VEHICLE_NO);//牌照号
	document.getElementById("vehicleNo").value = checkNull(vehicle.VEHICLE_NO);
	vehicleInfo.rows[2].cells[1].innerText = checkNull(vehicle.YIELDLY);//产地
	document.getElementById("yieldly").value = checkNull(vehicle.Y_ID);
	vehicleInfo.rows[2].cells[3].innerText = checkNull(vehicle.SERIES_CODE);//车系
	document.getElementById("seriesId").value = checkNull(vehicle.SERIES_ID);
	vehicleInfo.rows[2].cells[5].innerText = checkNull(vehicle.MODEL_CODE);//车型
	document.getElementById("modelId").value = checkNull(vehicle.MODEL_ID);
	vehicleInfo.rows[3].cells[1].innerText = checkNull(vehicle.PURCHASED_DATE);//购车日期
	document.getElementById("purchasedDate").value = checkNull(vehicle.PURCHASED_DATE);
	vehicleInfo.rows[3].cells[3].innerText = checkNull(vehicle.MILEAGE);//行驶里程
	document.getElementById("mileage").value = checkNull(vehicle.MILEAGE);
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	document.getElementById("freeTimes").value = checkNull(vehicle.FREE_TIMES);
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	document.getElementById("ctmId").value = checkNull(vehicle.CTM_ID);//车主ID
	document.getElementById("ctmName").value = checkNull(vehicle.CTM_NAME);//老车主姓名
	document.getElementById("ctmPhone").value = checkNull(vehicle.MAIN_PHONE);//老车主电话
	document.getElementById("ctmAddress").value = checkNull(vehicle.ADDRESS);//老车主地址
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	//document.getElementById("mainPhone").value = checkNull(vehicle.MAIN_PHONE);
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	document.getElementById("gameId").value = checkNull(vehicle.GAME_ID);//三包策略ID
	vehicleInfo.rows[5].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[6].cells[1].innerText = checkNull(vehicle.ADDRESS);//车主地址
	
	//根据车辆的保养次数算出保养次数的上下限
	var freeTimes = checkNull(vehicle.FREE_TIMES);  //车辆的保养次数
    document.getElementById("aaaa").value = freeTimes;
	var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
	var date = $('purchasedDate').value ;
	if(date=='' || date=='null' || date.trim() == '' || date == null){
		MyAlert('该车未做实销或实销已退车，不予做车主信息变更与购车日期变更！');
	}     
}

function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}

function doCusChange(){
	var selectType = document.getElementById("changeType").value;	
	var date = $('purchasedDate').value ;
	if (selectType == '') {
		document.getElementById("mileage_body").style.display = "none";//行驶里程
		document.getElementById("freetimes_body").style.display = "none";//保养次数
		document.getElementById("remark_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		clearApplyData();
	}
	if (selectType == 13141001) {//行驶里程变更
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	else if (selectType == 13141002) {//保养次数变更
		document.getElementById("freetimes_body").style.display = "inline";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	//购车时间变更
	else if(selectType == 13141003) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		document.getElementById("purchase_date_chg").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'block' ;
		$('check_2').style.display = 'none' ;
		$('span1').innerHTML = '(必须上传发票照片 ) ' ;
		if(date=='' || date=='null' || date.trim() == '' || date == null){
			MyAlert('该车未做实销或实销已退车，不予做此变更！');
		}		 
		clearApplyData();
	}
	//三包策略变更
	else if(selectType == 13141004) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "inline";
		document.getElementById("ctm_info_chg").style.display = "none";
		$('add_file').style.display = 'none' ;
		$('check_2').style.display = 'block' ;
		clearApplyData();
	}
	//车主信息变更
	else if(selectType == 13141005) {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("freetimes_body").style.display = "none";
		document.getElementById("purchase_date_chg").style.display = "none";
		document.getElementById("remark_body").style.display = "inline";
		//document.getElementById("tdwrGames").style.display = "none";
		document.getElementById("ctm_info_chg").style.display = "block";
		$('add_file').style.display = 'block' ;
		$('check_2').style.display = 'none' ;
		$('span1').innerHTML = '(必须上传行车执照或发票照片 ) ' ;
		if(date=='' || date=='null' || date.trim() == '' || date == null){
			MyAlert('该车未做实销或实销已退车，不予做此变更！');
		}
		clearApplyData();
	}
}
//onchange下拉框的时候 清空原来的选择
function clearApplyData() {
	document.getElementById("cmileage").value = "";//变更后数据 变更里程
	document.getElementById("cfree_times").value="";//变更后数据 保养次数
	document.getElementById("errorRoCode").value="";//变更里程选择的单据
	document.getElementById("ferrorRoCode").value="";//变更保养次数的单据
	document.getElementById("roDealerCode").innerText = '';//变更里程的经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("froDealerCode").innerText = '';//变更保养次数经销商代码
	document.getElementById("ftroDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	//这里不清空可变保养次数,可变保养次数是在选择VIN的时候带出来的
	//document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	//document.getElementById("tcFreeTimes").value = "";
	document.getElementById("remark").value = '';//备注
	$('c_purchase_date').value = '' ;
	$('c_ctm_name').value = '' ;
	$('c_ctm_phone').value = '' ;
	$('c_ctm_address').value = '' ;
}

//保存
function doSave() {
	if (!validate()) {
		MyAlert("带*为必填项");
		return;
	}
	if (!compareEngineNo()) {
		return;
	}
	if (!validateValue()) {
		return;
	}
	if(!saveCheck()){
	return;
	}
	var cmileage = document.getElementById("cmileage").value;
	var pattern = /^(\d){0,100}$/ ;
	if(!pattern.exec(cmileage)){
		MyAlert('里程只能输入正整数！');
		return ;
	}
	
	//fm.action = url;
	//fm.method = "post";
	//fm.submit();
	MyConfirm("是否保存？",confirmAdd2,[]);
	
}
function confirmAdd2(){
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doSave.json?addFlag=0';
	makeNomalFormCall(url,handleDoSave,'fm');
}
function handleDoSave(json) {
	if(json.msgs!=""){
		MyAlert(json.msgs);
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/vehicleInfoChangeApplyInit.do';
	fm.action = url;
	fm.method = "post";
	fm.submit();
	}else{
	MyAlert("操作失败!");
	}
	
}
//上报
function doSubmit() {
	if (!validate()) {
		MyAlert("带*为必填项");
		return false;
	}
	if (!compareEngineNo()) {
		return;
	} 
	if (!validateValue()) {
		return;
	}
	if(!saveCheck()){
		return;
	}
	var cmileage = document.getElementById("cmileage").value;
	var pattern = /^(\d){0,100}$/ ;
	if(!pattern.exec(cmileage)){
		MyAlert('里程只能输入正整数！');
		return ;
	}
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doSave.json?addFlag=1';
	//fm.action = url;
	//fm.method = "post";
	//fm.submit();
	makeNomalFormCall(url,handleDoSave,'fm');
}

//清空经销商
function clrDlr(dealerId, dealerCode) {
	document.getElementById(dealerId).value="";
	document.getElementById(dealerCode).value="";
}

function openItem() {
	var vin = document.getElementById("vin").value;
	var selectType = document.getElementById("changeType").value;
	var url = "<%=contextPath%>/jsp/vehicleInfoManage/apply/showOrder.jsp?vin="+vin+"&selectType="+selectType;
	OpenHtmlWindow(url,800,500);
}

//清空选择的错误单据
function clrRecord() {
	document.getElementById("cmileage").value = "";//变更后数据 变更里程
	document.getElementById("cfree_times").value="";//变更后数据 保养次数
	document.getElementById("errorRoCode").value="";//变更里程选择的单据
	document.getElementById("ferrorRoCode").value="";//变更保养次数的单据
	document.getElementById("roDealerCode").innerText = '';//变更里程的经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("froDealerCode").innerText = '';//变更保养次数经销商代码
	document.getElementById("ftroDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	//这里不清空可变保养次数,可变保养次数是在选择VIN的时候带出来的
	//document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	//document.getElementById("tcFreeTimes").value = "";
}

function validate() {
	var vin = document.getElementById("vin").value;
	if (!vin) {
		return fasle;
	}
	//var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	//engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");
	//if (!engineNo1) {
	//	return false;
	//}
	var changeType = document.getElementById("changeType").value;
	if (changeType == 13141001) {
		var errorRoCode = document.getElementById('errorRoCode').value;//错误单据号
    	if(''== errorRoCode ||null == errorRoCode) {
        	MyAlert('错误单据号不能为空');
        	return false;
    	}
		var cmileage = document.getElementById("cmileage").value;
		if (!cmileage) {
			return false;
		}
	} else if (changeType == 13141002) {
		var cfreeTimes = document.getElementById("cfree_times").value;
		if (!cfreeTimes) {
			return false;
		}
	} else if (changeType == 13141003) {
		var cpurchasedDate = document.getElementById("c_purchase_date").value;
		if (!cpurchasedDate) {
			return false;
		}
		if(!$('uploadFileId')){
			return false;
		}
	} else if (changeType == 13141005) {
		var cCtmName = document.getElementById("c_ctm_name").value;
		var cCtmPhone = $('c_ctm_phone').value ;
		var cCtmAddress = $('c_ctm_address').value ;
		if (!cCtmName || !cCtmPhone || !cCtmAddress) {
			return false;
		}
		if(!$('uploadFileId')){
			return false;
		}
	}
	/*
	var errorDealerCode = document.getElementById("errorDealerCode").value;
	if (!errorDealerCode) {
		return false;
	}
	*/
	var remark = document.getElementById("remark").value;
	if (!remark.trim()) {
	MyAlert("请填写备注!");
		return false;
	}
	var sure = document.getElementById("sure").checked;//确认checkBox
	if(changeType!=13141003 && changeType!=13141005){
		if (!sure) {
			return false;
		}
	}
	return true;
}
//比较VIN带出来的发动机号和手动输入的发动机号是否相同
function compareEngineNo() {
	var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");//发动机号变成大写 去掉所有空格
	var engineNo = document.getElementById("engineNo").value;//输入VIN带出来的发动机号,隐藏域里面
	//if (engineNo1 != engineNo) {
	//	MyAlert("VIN和发动机号不符");
	//	return false;
	//}
	return true;
}
function saveCheck(){
	var cfree_times = document.getElementById("cfree_times").value;	
	var freeTimes = document.getElementById("freeTimes").value;
	var selectType = document.getElementById("changeType").value;
	if(selectType == <%=Constant.VEHICLE_CHANGE_TYPE_02%> && parseInt(cfree_times)<parseInt(freeTimes)&&$('ferrorRoCode').value==""){
		MyAlert('保养次数由大变小时,错误单据不能为空!');
			return false;
	}else {
	return true;
	}
}
//根据变更类型判断输入的值是否合法
function validateValue() {
	var selectType = document.getElementById("changeType").value;
	var reg = /^\d+$/;
	//行驶里程变更	
	if (selectType == <%=Constant.VEHICLE_CHANGE_TYPE_01%>) {
	    var cmileage = document.getElementById("cmileage").value;
		var mileage = document.getElementById('mileage').value;
		var tcMileage = document.getElementById("tcMileage").value;
		if(!reg.test(cmileage)){
			MyAlert('变更后数据必须输入数字');
			document.getElementById("cmileage").value="";
		 	return false;
		}
		if(parseInt(cmileage) < parseInt(tcMileage)){
			MyAlert('行驶里程必须大于最小可变里程');
		  	return false;
		}
	}
	//保养次数变更
	else if(selectType == <%=Constant.VEHICLE_CHANGE_TYPE_02%>) {
	
		var cfree_times = document.getElementById("cfree_times").value;	
		if (!reg.test(cfree_times)) {
			MyAlert('变更后数据必须输入数字');
			document.getElementById("cfree_times").value="";	
			return false;
		} 
		//var roFreeTimes = document.getElementById("roFreeTimes").innerText;
		var freeTimes = document.getElementById("freeTimes").value;
		if (freeTimes >= 0) {//如果保养次数为0  修改保养次数为0或者1
			if(freeTimes==0){
				MyAlert('保养次数为0的不需要变更');
				document.getElementById("cfree_times").value="";	
				return false;
			}
		
		
		   var freeTimes = document.getElementById("aaaa").value;
		   var minFreeTimes = parseInt(freeTimes) - 1 < 0 ? 0 : parseInt(freeTimes) - 1;//最小保养次数
		  var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
		
		   
		
			 if (! ( minFreeTimes<= parseInt(cfree_times) && parseInt(cfree_times)  <=  maxFreeTimes ) ) {
				MyAlert('必须在指定范围类');
				return false;
			} 
			return true;
		} else if (cfree_times < 0) {
			MyAlert('保养次数不能小于0');
			return false;
		} 
		
		var minF = parseInt(freeTimes) - 1;//最小保养次数
		var maxF = parseInt(freeTimes) + 1;//最大保养次数
		if (cfree_times == minF) {//修改保养次数为 -1 正负1 || (cfree_times == maxF
			return true;
		} else {
			MyAlert('保养次数只能填' + (parseInt(freeTimes) - 1));//+ '或' + (parseInt(freeTimes) + 1));
			return false;
		}
		
	}
	else if(selectType == <%=Constant.VEHICLE_CHANGE_TYPE_03%> || selectType == <%=Constant.VEHICLE_CHANGE_TYPE_05%>){
		var date = $('purchasedDate').value ;
		if(date=='' || date=='null' || date.trim() == '' || date == null){
			MyAlert('该车未做实销或已做实销退车，不予做此变更！');
			return false ;
		}
	}
	return true;
}

function showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage, dealerName) {
	document.getElementById("errorRoCode").value = roNo;//选定的工单编号
	document.getElementById("roDealerCode").innerText = dealerName;//经销商名称
	document.getElementById("troDealerCode").value = dealerCode;//经销商代码
	document.getElementById("roInMileage").innerText = inMileage;//单据里程
	document.getElementById("troMileage").value = inMileage;
	document.getElementById("tcMileage").value = nextInMileage;//可变更最小里程
	document.getElementById("cInMileage").innerText =  nextInMileage;
	
	document.getElementById("roFreeTimes").innerText = freeTimes;//保养次数
	
	document.getElementById("aaaa").value = freeTimes;
	document.getElementById("troFreeTimes").value = freeTimes;
	var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
	
}
//根据vin和保养次数查询保养类的工单
function queryOrder() {
	if (!validateValue()) {
		return;
	}
	//先清除原来工单的数据
	document.getElementById("ftroDealerCode").value = '';
	document.getElementById("froDealerCode").innerText = '';
	document.getElementById("ferrorRoCode").value = '';
	document.getElementById("troFreeTimes").value = '';
	document.getElementById("roFreeTimes").innerText = '';
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryOrderByFreeTimes.json';
	makeNomalFormCall(url,handleQueryOrder,'fm');
}

function handleQueryOrder(json) {
	var data = json.ps;
	document.getElementById("ftroDealerCode").value = data.DEALER_CODE;
	document.getElementById("froDealerCode").innerText = data.DEALER_NAME;
	document.getElementById("ferrorRoCode").value = data.RO_NO;
	document.getElementById("troFreeTimes").value = data.FREE_TIMES;
	document.getElementById("roFreeTimes").innerText = data.FREE_TIMES;
}

</script>
