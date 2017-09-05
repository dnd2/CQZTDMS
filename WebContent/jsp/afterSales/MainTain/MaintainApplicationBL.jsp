<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%=FileConstant.regionJsUrl%>"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" >
var $J = jQuery.noConflict();
$J(document).ready(function(){
	loadcalendar();
	genLocSel('txt1','txt2','txt3','${info.PROVINCECODE}','${info.CITYCODE}','${info.COUNTYCODE}'); // 加载省份城市和县 
});
	function setMoreValue(inputId,vin) {
		document.getElementById(inputId).value=vin;
		showCustomerInfos(vin);
	}

	var myPage;
	var btnIds = new Array("userQuery","update");
	var menuUrl = "<%=contextPath%>/OutMaintainAction/MaintainSelect.do";
	var saveUrl = "<%=contextPath%>/OutMaintainAction/saveMaintainApplicationBL.json";
	var checkVinUrl = "<%=contextPath%>/OutMaintainAction/showCustomerInfosByVin.json";
	//截取字符串
	function splitStr(str, s) {
        var newStr = "";
        var strArray = str.split(s);
        for (var i = 0; i < strArray.length; i++) {
            newStr += strArray[i];
        }
        return newStr;
    }

	// 保存信息反馈类型
	function saveMainTain() {
		if($J("#statrDate").val() == null || $J("#statrDate").val() == ''){
			MyAlert("急救开始时间不能为空");
			return;
		}
		if($J("#endDate").val() == null || $J("#endDate").val() == ''){
			MyAlert("急救结束时间不能为空");
			return;
		}
		var beginDate=$J("#statrDate").val();  
		var endDate=$J("#endDate").val();
		var d1 = new Date(beginDate.replace(/\-/g, "\/"));  
		var d2 = new Date(endDate.replace(/\-/g, "\/"));
		 if(d1 > d2){
			MyAlert("开始时间不能大于结束时间");
			return;
		} 
		if($J("#eNum").val() == null || $J("#eNum").val() == ''){
			MyAlert("急救人数不能为空");
			return;
		}
		if($J("#eName").val() == null || $J("#eName").val() == ''){
			MyAlert("急救人姓名不能为空");
			return;
		}
		if($J("#eLicenseNo").val() == null || $J("#eLicenseNo").val() == ''){
			MyAlert("派车车牌号不能为空");
			return;
		}
		MyConfirm("确定保存吗？",EditAsDo);
	}
	function EditAsDo(){
		var tUrl = saveUrl;
		makeNomalFormCall(tUrl, message, 'fm');
	}
	function message(json){
		if(json.message == "success"){
			MyAlertForFun("保存成功！",backInit);
		}else{
			MyAlert("保存失败！");
		}
	}

	function backInit(){
		window.location.href = menuUrl;
	}
	/* 验证数字且只能输，两位小数 */
	function vaildateNum(obj){
		obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
		obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	}


</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>外出救援申请补录</title>
</head>
<body>
<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; 外出救援申请补录</font></div>
<form method="post" name="fm" id="fm">
	<!--隐藏表单域 -->
	<input type="hidden" id="id" name="id" value="${info.ID }"/>
	<table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
      <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单据编码：</td>
        <td align="left">
        	${info.EGRESS_NO }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单人姓名：</td>
        <td align="left">
        	${info.CREATE_BY }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 制单日期：</td>
        <td align="left">
			${info.CREATE_DATE }
         </td>
      </tr>
      <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站编码：</td>
        <td align="left">
        	${info.DEALER_CODE }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站：</td>
        <td align="left">
      	  	${info.DEALER_NAME }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 服务站电话：</td>
        <td align="left">
        	${info.PHONE }
        </td>
      </tr>
     <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> VIN码：</td>
         <td align="left">
        	${info.VIN }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车系名称：</td>
        <td align="left" id="seName">${info.SE_NAME }</td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车型：</td>
        <td align="left" id="vehicleName">${info.GROUP_NAME }</td>
      </tr>
      
      <tr >

        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 配置名称：</td>
        <td align="left" id="pzName">${info.PZ_NAME }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车牌号：</td>
        <td align="left" id="plateNum">${info.LICENSE_NO }
        </td>   
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车主名字：</td>
         <td align="left" id="userName">${info.CTM_NAME }
         </td>
      </tr>
      
      <tr >     	
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 出厂日期：</td>
		<td align="left" id="outDate2">${info.FACTORY_DATE }
		</td>
        
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 购车日期：</td>
        <td align="left" id="buyDate2">${info.PURCHASED_DATE }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 用户地址：</td>
        <td align="left" id="userAddress">${info.ADDRESS }
        </td>
      </tr>
      
      <tr >
         <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户名字：</td>
         <td align="left">
        	${info.CUSTOMER_NAME }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户电话：</td>
        <td align="left">
        	${info.TELEPHONE }
        </td>   
         <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 行驶里程：</td>
        <td align="left">
        	${info.MILEAGE }
        </td>
      </tr>
          
      <tr >     	       
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急里程(公里)：</td>
        <td align="left">
        	${info.RELIEF_MILEAGE }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在省：</td>
         <td align="left">
        	${info.PROVINCE }
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在市：</td>
         <td align="left">
        	${info.CITY }
        </td>
      </tr>
      
      <tr >        
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在县(区)：</td>
         <td align="left">
        	${info.COUNTY}
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">
				详细地址：</td>
			<td align="left" colspan="3">
				${info.TOWN }
			</td>
       </tr>
       <tr >
        <%-- <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急开始时间：</td>
         <td align="left">
        	<input class="middle_txt"  type="text" readonly="readonly" name="statrDate" id="statrDate" value="${info.E_START_DATE }"/>
			<input class="time_ico" type="button" onclick="showcalendar(event, 'statrDate', false);" value="&nbsp;" />
			<span style="COLOR: red;">*</span> --%>
		<td class="table_add_2Col_label_5Letter" style="text-align:right">救急开始时间：</td>
	  		<td align="left">
        	<input id="statrDate" name="statrDate" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.E_START_DATE }"/>
        	<font style="color:red">*</font>
      		</td>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right">救急结束时间：</td>
	  		<td align="left">
        	<input id="endDate" name="endDate" readonly class="Wdate" type="text" onclick="WdatePicker({onpicked:function(dq){changeArrivalDate(dq.cal.getNewDateStr());},dateFmt:'yyyy-MM-dd HH:mm'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;" value="${info.E_END_DATE }"/>
        	<font style="color:red">*</font>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急人数：</td>
         <td align="left">
        	<input class="middle_txt"  type="text" onkeyup="vaildateNum(this)" onblur="vaildateNum(this)" maxlength="8" name="eNum" id="eNum" value="${info.E_NUM }"/>			
			<span style="COLOR: red;">*</span>
        </td>
       </tr>
       <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急人名字：</td>
         <td align="left">
        	<input class="middle_txt"  type="text" name="eName" maxlength="200" id="eName" value="${info.E_NAME }"/>
			<span style="COLOR: red;">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 派车车牌号：</td>
         <td align="left">
        	<input class="middle_txt"  type="text" name="eLicenseNo" maxlength="30" id="eLicenseNo" value="${info.E_LICENSE_NO }"/>
			<span style="COLOR: red;">*</span>
        </td>
       </tr>
      
      <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 申请内容：</td>
        <td align="left" colspan="5">
        	${info.EGRESS_REAMRK }
        </td>
      </tr>
	<tr >
		<td colspan="6" style="text-align: center;">
			<input class="normal_btn" type="button" name="button1"  id="queryBtn" value="保存" onclick="saveMainTain();" />
			<input class="normal_btn" type="button" name="button1"  id="backBtn" value="返回" onclick="backInit();"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>