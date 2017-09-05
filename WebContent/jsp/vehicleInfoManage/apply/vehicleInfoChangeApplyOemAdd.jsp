<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
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
	<table class="table_edit" id="vehicleInfo">
		<th colspan="6"><img class="nav" src="../../../img/subNav.gif"/> 车辆信息</th>
		<tr>
			<td width="10%" align="right">VIN：</td>
			<td width="22%"><input name="vin" id="vin" type="text" class="middle_txt" onblur="queryVehicle()"/><font color="red">*</font></td>
			<td width="10%" align="right">发动机号：<input type="hidden" id="engineNo" name="engineNo"/></td>
			<td width="22%" align="left"><input name="engineNo1" id="engineNo1" type="text" class="middle_txt"/><font color="red">*</font></td>
			<td width="12%" align="right">牌照号：<input type="hidden" id="vehicleNo" name="vehicleNo"/></td>
			<td width="24%"></td>
		</tr>
		<tr>
			<td align="right">产地：<input type="hidden" id="yieldly" name="yieldly"/></td>
			<td></td>
			<td width="10%" align="right">车系：<input type="hidden" id="seriesCode" name="seriesId"/></td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">车型：<input type="hidden" id="modelCode" name="modelId"/></td>
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
			<td width="10%" align="right">车主姓名：<input type="hidden" id="ctmId" name="ctmId"/><!-- 车主id --></td>
			<td></td>
			<td width="10%" align="right">车主电话：</td>
			<td width="22%" align="left"></td>
			<td width="12%" align="right">三包策略代码：<input type="hidden" id="gameId" name="gameId"/><!-- 策略id --></td>
			<td></td>
		</tr>
		<tr>
			<td width="12%" align="right">三包规则代码：<!-- 三包规则代码 --></td>
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
        			genSelBoxExp("changeType",<%=Constant.VEHICLE_CHANGE_TYPE%>,"",true,"short_sel","","false",'');
        		</script>
		  	</td>
		</tr>
		<tbody id="mileage_body" style="display:none"> <!-- 行驶里程变更 保养次数变更 -->
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
          <td align="right">经销商代码：<input type="hidden" id="troDealerCode" name="troDealerCode"/></td>
          <td align="left" id="roDealerCode"></td>
          <td align="right">单据里程：<input type="hidden" id="troMileage" name="troMileage"/></td>
          <td align="left" id="roInMileage"></td>
          <td align="right">单据保养次数：<input type="hidden" id="troFreeTimes" name="troFreeTimes"/></td>
          <td align="left" id="roFreeTimes"></td>
        </tr>
        <tr>
          <td align="right" nowrap="nowrap">可变更最小里程：<input type="hidden" id="tcMileage" name="tcMileage"/></td>
          <td align="left" id="cInMileage"></td>
          <td align="right" nowrap="nowrap">可变更保养次数：<input type="hidden" id="tcFreeTimes" name="tcFreeTimes"/></td>
          <td align="left" id="cFreeTimes"></td>
          <td></td>
          <td></td>
        </tr>
        </tbody><!-- 行驶里程变更结束 -->
            
        <tbody id="remark_body" style="dispaly:none"><!-- 备注 -->
       	<tr>
          	<td align="right">备注：</td>
 			<td colspan="5" align="left"><textarea id="remark" name="remark" cols="120" rows="3" ></textarea><font color="red">*</font></td>
        </tr>
        <tr>
          	<td colspan="6" align="left">
            <input type="checkbox" id="sure"/>
         	<font color="red"> * 此次变更与用户仔细核对，所有资料真实，特此向错误工单制作方提出投诉申请</font></td>
        </tr>
        </tbody><!-- 备注结束 -->
        
	</table>
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
	document.getElementById("MILEAGE").value = checkNull(vehicle.MILEAGE);
	vehicleInfo.rows[3].cells[5].innerText = checkNull(vehicle.FREE_TIMES);//保养次数
	document.getElementById("freeTimes").value = checkNull(vehicle.FREE_TIMES);
	vehicleInfo.rows[4].cells[1].innerText = checkNull(vehicle.CTM_NAME);//车主姓名
	document.getElementById("ctmId").value = checkNull(vehicle.CTM_ID);//车主ID
	vehicleInfo.rows[4].cells[3].innerText = checkNull(vehicle.MAIN_PHONE);//车主电话
	//document.getElementById("mainPhone").value = checkNull(vehicle.MAIN_PHONE);
	vehicleInfo.rows[4].cells[5].innerText = checkNull(vehicle.GAME_CODE);//三包策略代码
	document.getElementById("gameId").value = checkNull(vehicle.GAME_ID);//三包策略ID
	vehicleInfo.rows[5].cells[1].innerText = checkNull(vehicle.RULE_CODE);//三包规则代码
	vehicleInfo.rows[6].cells[1].innerText = checkNull(vehicle.ADDRESS);//车主地址
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
	if (selectType == '') {
		document.getElementById("mileage_body").style.display = "none";
		document.getElementById("remark_body").style.display = "none";
		clearApplyData();
	}
	//行驶里程变更 保养次数变更
	if (selectType == 13141001 || selectType == 13141002) {
		document.getElementById("mileage_body").style.display = "inline";
		document.getElementById("remark_body").style.display = "inline";
		clearApplyData();
	}
	//购车时间变更
	else if(selectType == 13141003) {
		document.getElementById("tdmileage").style.display = "none";
		document.getElementById("tdfreeTimes").style.display = "none";
		document.getElementById("tdpurchasedDate").style.display = "inline";
		document.getElementById("tdwrGames").style.display = "none";
		clearApplyData();
	}
	//三包策略变更
	else if(selectType == 13141004) {
		document.getElementById("tdmileage").style.display = "none";
		document.getElementById("tdfreeTimes").style.display = "none";
		document.getElementById("tdpurchasedDate").style.display = "none";
		document.getElementById("tdwrGames").style.display = "inline";
		clearApplyData();
	}
}
//onchange下拉框的时候 清空原来的选择
function clearApplyData() {
	document.getElementById("cmileage").value = "";//变更后数据 变更里程和变更保养次数通用
	document.getElementById("errorRoCode").value="";
	document.getElementById("roDealerCode").innerText = '';//经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	document.getElementById("tcFreeTimes").value = "";
	document.getElementById("remark").value = '';//备注
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
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doSave.do?addFlag=0';
	fm.action = url;
	fm.method = "post";
	fm.submit();
	//makeNomalFormCall(url,handleDoSave,'fm');
}
function handleDoSave() {
	MyAlert('保存成功');
}
//上报
function doSubmit() {
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
	var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/doSave.do?addFlag=1';
	fm.action = url;
	fm.method = "post";
	fm.submit();
	//makeNomalFormCall(url,handleDoSave,'fm');
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
	document.getElementById("errorRoCode").value="";
	document.getElementById("roDealerCode").innerText = '';//经销商代码
	document.getElementById("troDealerCode").value = "";
	document.getElementById("roInMileage").innerText = '';//单据里程
	document.getElementById("troMileage").value = "";
	document.getElementById("roFreeTimes").innerText = '';//保养次数
	document.getElementById("troFreeTimes").value = "";
	document.getElementById("cInMileage").innerText = '';//可变最小里程
	document.getElementById("tcMileage").value = "";
	document.getElementById("cFreeTimes").innerText = '';//可变保养次数
	document.getElementById("tcFreeTimes").value = "";
}

function validate() {
	var vin = document.getElementById("vin").value;
	if (!vin) {
		return fasle;
	}
	var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");
	if (!engineNo1) {
		return false;
	}
	var errorRoCode = document.getElementById('errorRoCode').valu
    if(''== errorRoCode ||null == errorRoCode) {
        MyAlert('错误单据号不能为空');
        return false;
    }
	var changeType = document.getElementById("changeType").value;
	if (changeType == 13141001) {
		var cmileage = document.getElementById("cmileage").value;
		if (!cmileage) {
			return false;
		}
	} else if (changeType == 13141002) {
		var cfreeTimes = document.getElementById("cmileage").value;//保养次数变更和行驶里程变更是一个
		if (!cfreeTimes) {
			return false;
		}
	} else if (changeType == 13141003) {
		var cpurchasedDate = document.getElementById("cpurchasedDate").value;
		if (!cpurchasedDate) {
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
//比较VIN带出来的发动机号和手动输入的发动机号是否相同
function compareEngineNo() {
	var engineNo1 = document.getElementById("engineNo1").value;//手动输入发动机号
	engineNo1 = (engineNo1.toUpperCase()).replace(/\s+/g, "");//发动机号变成大写 去掉所有空格
	var engineNo = document.getElementById("engineNo").value;//输入VIN带出来的发动机号,隐藏域里面
	if (engineNo1 != engineNo) {
		MyAlert("VIN和发动机号不符");
		return false;
	}
	return true;
}
//根据变更类型判断输入的值是否合法
function validateValue() {
	var selectType = document.getElementById("changeType").value;
	//行驶里程变更	
	if (selectType == 13141001) {
	    var cmileage = document.getElementById("cmileage").value;
		var mileage = document.getElementById('mileage').value;
		var tcMileage = document.getElementById("tcMileage").value;
		if(isNaN(cmileage)){
		 MyAlert('变更后数据必须输入数字');
		 return false;
		}
		//MyAlert('cmileage:'+cmileage);
		//MyAlert('mileage:'+mileage);
		//MyAlert('tcMileage:'+tcMileage);
		if(parseInt(cmileage) < parseInt(tcMileage) || parseInt(cmileage) > parseInt(mileage) ){
		  MyAlert('行驶里程的范围应是'+tcMileage+'-'+mileage+"之间");
		  return false;
		}
	}
	//保养次数变更
	else if(selectType == 13141002) {
		var cmileage = document.getElementById("cmileage").value;	
		if (isNaN(cmileage)) {
			MyAlert('变更后数据必须输入数字');
			return false;
		} 
		var roFreeTimes = document.getElementById("roFreeTimes").innerText;
		if (cmileage < 0 || cmileage < roFreeTimes - 1 || cmileage > parseInt(roFreeTimes) + 1) {
			MyAlert('保养次数超过限制');
			return false;
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
	
	document.getElementById("roFreeTimes").innerText = freeTimes;//保养次数
	document.getElementById("troFreeTimes").value = freeTimes;
	var minFreeTimes = freeTimes - 1 < 0 ? 0 : freeTimes - 1;//最小保养次数
	var maxFreeTimes = parseInt(freeTimes) + 1;//最大保养次数
	var cFreeTimes = minFreeTimes + '-' + maxFreeTimes;
	document.getElementById("cFreeTimes").innerText = cFreeTimes;//可变保养次数 minFreeTimes到maxFreeTimes
	document.getElementById("tcFreeTimes").value = cFreeTimes;
}
</script>
