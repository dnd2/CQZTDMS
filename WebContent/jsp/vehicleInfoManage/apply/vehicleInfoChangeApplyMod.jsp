<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); 
List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		loadVehicleChangeInfo();//查询车辆变更详细信息
	}
</script>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆信息变更申请
<form name="fm" id="fm">
	<table class="table_edit" id="vehicleInfo">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<input type="hidden" id="id" value="${ID}"/>
		<tr>
			<td align="right" >VIN：<input type="hidden" name="vin" id="vin"/></td>
			<td id="vin" name="vin"></td>
			<td align="right">发动机号：</td>
			<td></td>
			<td align="right">牌照号：</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">产地：</td>
			<td></td>
			<td align="right">车系：</td>
			<td></td>
			<td align="right">车型：</td>
			<td nowrap="nowrap"></td>
		</tr>
		<tr>
			<td align="right">购车日期：</td>
			<td id="p_date"></td>
			<td align="right">行驶里程：<input type="hidden" id="MILEAGE" name="MILEAGE" /></td>
			<td></td>
			<td align="right">保养次数：<input type="hidden" id="freeTimes" name="freeTimes" /></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">车主姓名：</td>
			<td></td>
			<td align="right">车主电话：</td>
			<td></td>
			<td align="right" nowrap="nowrap">三包策略代码：</td>
			<td></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">三包规则代码：<!-- 三包规则代码 --></td>
			<td></td>
		</tr>
		<tr>
			<td align="right">车主地址：<!-- 车主地址 --></td>
			<td></td>
		</tr>
		<th colspan="6" align="left"><img class="nav" src="../../../img/subNav.gif" />申请内容</th>
		<tr>
			<td align="right" nowrap="nowrap">申请类型：</td>
		  	<td align="left" colspan="1">
		  		<script type="text/javascript">
        			genSelBoxExp("changeType2",<%=Constant.VEHICLE_CHANGE_TYPE%>,'',false,"short_sel","","false",'<%=Constant.VEHICLE_CHANGE_TYPE_04%>');
        		</script>
        		<input name="changeType" type="hidden" id="changeType" value=""/>
		  	</td>
		  	<td align="right">申请时间：</td>
		  		<td align="left" id="myDateShow"></td>
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
				<input name="c_purchase_date" type="text" class="middle_txt" id="t1" readonly="readonly"/> 
				<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 't1', false);" /> 
				</td>
			</tr>
		</tbody>   
		<!-- 购车日期变更 结束 -->
		<!-- 车主信息变更 开始 -->
		<tbody id="ctm_info_chg"  style="display:none">
			<tr>
				<td align="right">用户姓名：</td>
				<td>
					<input type="text" name="c_ctm_name" id="c_ctm_name" class="middle_txt" datatype="0,is_textarea,50"/>
				</td>
				<td align="right">用户电话：</td>
				<td>
					<input type="text" name="c_ctm_phone" id="c_ctm_phone" class="middle_txt" datatype="0,is_phone,15"/>
				</td>
			</tr>
			<tr>
				<td align="right">用户地址：</td>
				<td colspan="3">
					<input type="text" name="c_ctm_address" id="c_ctm_address" class="maxlong_txt" datatype="0,is_textarea,150"/>
				</td>
			</tr>
		</tbody>   
		<!-- 车主信息变更 结束 -->    
        <tbody id="remark_body" style="dispaly:none"><!-- 备注 -->
       	<tr>
          	<td align="right">备注：</td>
 			<td colspan="5" align="left"><textarea id="remark" name="remark" cols="120" rows="3" ></textarea><font color="red">*</font></td>
        </tr>
        <tr id="check_2">
          	<td colspan="6" align="left">
            <input type="checkbox" id="sure" checked/>
         	<font color="red"> * 此次变更与用户仔细核对，所有资料真实，特此向错误工单制作方提出投诉申请</font></td>
        </tr>
        </tbody><!-- 备注结束 -->
		</table>
		</br>
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

function loadVehicleChangeInfo() {
	var id = document.getElementById("id").value;	
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehicleChangeInfoById.json?viewFlag=0&id='+id;
	makeNomalFormCall(url,handleView,'fm');
}

function handleView(json) {
	var vehicleInfo = document.getElementById("vehicleInfo");
	var vehicle = json.map;
	document.getElementById("vin").value = checkNull(vehicle.VIN);
	vehicleInfo.rows[1].cells[1].innerText = checkNull(vehicle.VIN);//VIN
	vehicleInfo.rows[1].cells[3].innerText = checkNull(vehicle.ENGINE_NO);//发动机号
	vehicleInfo.rows[1].cells[5].innerText = checkNull(vehicle.VEHICLE_NO);//牌照号
	vehicleInfo.rows[2].cells[1].innerText = checkNull(vehicle.YIELDLY);//产地
	vehicleInfo.rows[2].cells[3].innerText = checkNull(vehicle.SERIES_NAME);//车系
	vehicleInfo.rows[2].cells[5].innerText = checkNull(vehicle.MODEL_NAME);//车型
	vehicleInfo.rows[3].cells[1].innerText = checkNull(vehicle.PURCHASED_DATE);//购车日期
	vehicleInfo.rows[3].cells[3].innerText = checkNull(vehicle.MILEAGE);//行驶里程
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	vehicleInfo.rows[5].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[6].cells[1].innerText = checkNull(vehicle.ADDRESS);//车主地址
	document.getElementById("freeTimes").value = checkNull(vehicle.FREE_TIMES);//保养次数
	document.getElementById("changeType").value = vehicle.APPLY_ID;
	document.getElementById("changeType2").value = vehicle.APPLY_ID;
	$("changeType2").disabled=true;
	$('myDateShow').innerHTML = checkNull(vehicle.CREATE_DATE);
	//genWrGame(json.wrGames, vehicle);
	if (vehicle.APPLY_ID == 13141001) {
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("cmileage").value = vehicle.APPLY_DATA;
		document.getElementById("errorRoCode").value = checkNull(vehicle.ERROR_RO_CODE);
		document.getElementById("troDealerCode").value = checkNull(vehicle.RO_DEALER_CODE);
		document.getElementById("roDealerCode").innerText = checkNull(vehicle.DN);
		document.getElementById("troMileage").value = checkNull(vehicle.RO_MILEAGE);
		document.getElementById("roInMileage").innerText = checkNull(vehicle.RO_MILEAGE);
		document.getElementById("troFreeTimes").value = checkNull(vehicle.RO_FREE_TIMES);
		document.getElementById("roFreeTimes").innerText = checkNull(vehicle.RO_FREE_TIMES);
		document.getElementById("tcMileage").value = checkNull(vehicle.C_MILEAGE);
		document.getElementById("cInMileage").innerText = checkNull(vehicle.C_MILEAGE);
		document.getElementById("tcFreeTimes").value = checkNull(vehicle.C_FREE_TIMES);
		document.getElementById("cFreeTimes").innerText = checkNull(vehicle.C_FREE_TIMES);
		document.getElementById("MILEAGE").value = checkNull(vehicle.MILEAGE);
	} else if (vehicle.APPLY_ID == 13141002) {
		document.getElementById("freetimes_body").style.display = "inline";
		document.getElementById("ferrorRoCode").value = checkNull(vehicle.ERROR_RO_CODE);
		document.getElementById("cfree_times").value = checkNull(vehicle.APPLY_DATA);
		document.getElementById("ftroDealerCode").value = checkNull(vehicle.RO_DEALER_CODE);
		document.getElementById("froDealerCode").innerText = checkNull(vehicle.DN);
		document.getElementById("troMileage").value = checkNull(vehicle.RO_MILEAGE);
		document.getElementById("roInMileage").innerText = checkNull(vehicle.RO_MILEAGE);
		document.getElementById("troFreeTimes").value = checkNull(vehicle.RO_FREE_TIMES);
		document.getElementById("roFreeTimes").innerText = checkNull(vehicle.RO_FREE_TIMES);
		document.getElementById("tcMileage").value = checkNull(vehicle.C_MILEAGE);
		document.getElementById("cInMileage").innerText = checkNull(vehicle.C_MILEAGE);
		document.getElementById("tcFreeTimes").value = checkNull(vehicle.C_FREE_TIMES);
		document.getElementById("cFreeTimes").innerText = checkNull(vehicle.C_FREE_TIMES);
	} else if (vehicle.APPLY_ID == 13141003) {
		document.getElementById("purchase_date_chg").style.display = "inline";
		document.getElementById("c_purchase_date").value = dateformat(vehicle.C_PURCHASE_DATE);
		$('check_2').style.display = 'none' ;
		$('add_file').style.display = 'block' ;
		$('span1').innerHTML = '(必须上传发票照片 ) ' ;
	} else if (vehicle.APPLY_ID == 13141004) {
		document.getElementById("tdwrGames").style.display = "inline";
	} else if (vehicle.APPLY_ID == 13141005) {
		document.getElementById("ctm_info_chg").style.display = "inline";
		$('c_ctm_name').value =  vehicle.C_CTM_NAME;
		$('c_ctm_phone').value =  vehicle.C_CTM_PHONE;
		$('c_ctm_address').value =  vehicle.C_CTM_ADDRESS;
		$('check_2').style.display = 'none' ;
		$('add_file').style.display = 'block' ;
		$('span1').innerHTML = '(必须上传行车执照或发票照片 ) ' ;
	}
	//document.getElementById("errorDealerCode").value = checkNull(vehicle.DEALER_CODE);
	document.getElementById("remark").value = checkNull(vehicle.APPLY_REMARK);
}
//生成三包策略下拉框
function genWrGame(wrGames, vehicle) {
	var td = document.getElementById("tdwrGames");
	var select = '<select class="short_sel" id="cwrGames" name="cwrGames">';
	for (var i=0;i<wrGames.length;i++) {
		if (vehicle.APPLY_DATA == wrGames[i].gameCode) {
			select += '<option value='+wrGames[i].gameCode+' selected>'+wrGames[i].gameCode+'</option>';
		} else {
			select += '<option value='+wrGames[i].gameCode+'>'+wrGames[i].gameCode+'</option>';
		}
	}
	select += '</select>';
	td.innerHTML = select;
}

function checkNull(value) {
	if (value == null || value == 'null') {
		return '';
	} else {
		return value;
	}
}
function dateformat(str){
	if(str==null || str == ""){
		return "";
	}else{
	return str.substring(0,10);
	}
}
function doCusChange(){
	var selectType = document.getElementById("changeType").value;	
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
//保存
function doSave() {
	if (!validate()) {
		MyAlert("带*为必填项");
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
	var id = document.getElementById("id").value
	
	//fm.action = url;
	//fm.method = "post";
	//fm.submit();
	MyConfirm("是否保存？",confirmAdd2,[id]);
}
function confirmAdd2(id){
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doMod.json?modFlag=0&id='+id;
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
	var id = document.getElementById("id").value
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doMod.json?modFlag=1&id='+id;
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

function openItem() {
	var vin = document.getElementById("vin").value;
	var changeType = $('changeType').value;
	var url = "<%=contextPath%>/jsp/vehicleInfoManage/apply/showOrder.jsp?vin="+vin+"&selectType="+changeType;
	OpenHtmlWindow(url,800,500);
	
}

function validate() {
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
		return false;
	}
	var sure = document.getElementById("sure").checked;//确认checkBox
	if (!sure) {
		return false;
	}
	return true;
}

//根据变更类型判断输入的值是否合法
function validateValue() {
	var selectType = document.getElementById("changeType").value;
	var reg = /^\d+$/;
	//行驶里程变更	
	if (selectType == 13141001) {
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
	else if(selectType == 13141002) {
		var cfree_times = document.getElementById("cfree_times").value;	//变更后的保养次数
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
			if (cfree_times != 0 && cfree_times != 1) {
				MyAlert('保养次数只能填0或1');
				return false;
			}
			return true;
		} else if (cfree_times < 0) {
			MyAlert('保养次数不能小于0');
			return false;
		} 
		var minF = parseInt(freeTimes) - 1;//最小保养次数
		var maxF = parseInt(freeTimes) + 1;//最大保养次数
		if (cfree_times == minF) {//修改保养次数为正负1
			return true;
		} else {
			MyAlert('保养次数只能填' + (parseInt(freeTimes) - 1));
			return false;
		}
	}
	else if(selectType == <%=Constant.VEHICLE_CHANGE_TYPE_03%> || selectType == <%=Constant.VEHICLE_CHANGE_TYPE_05%>){
		var date = $('p_date').innerText ;
		if(date=='' || date=='null' || date.trim() == '' || date == null){
			MyAlert('该车未做实销或已做实销退车，不予做此变更！');
			return false ;
		}
	}
	return true;
}

function showRo(roNo, dealerCode, inMileage, freeTimes,nextInMileage) {
	document.getElementById("errorRoCode").value = roNo;//选定的工单编号
	document.getElementById("roDealerCode").innerText = dealerCode;//经销商代码
	document.getElementById("troDealerCode").value = dealerCode;
	document.getElementById("roInMileage").innerText = inMileage;//单据里程
	document.getElementById("troMileage").value = inMileage;
	document.getElementById("tcMileage").value = nextInMileage;//可变更最小里程
	document.getElementById("cInMileage").innerText =  nextInMileage;
	MyAlert(freeTimes);
	document.getElementById("roFreeTimes").innerText = freeTimes;//保养次数
	
	document.getElementById("troFreeTimes").value = freeTimes;
	/*var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
	*/

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
