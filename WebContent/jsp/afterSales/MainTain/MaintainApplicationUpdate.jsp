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
	var saveUrl = "<%=contextPath%>/OutMaintainAction/saveMaintainApplication.json";
	var checkVinUrl = "<%=contextPath%>/OutMaintainAction/showCustomerInfosByVin.json";
	// 保存信息反馈类型
	function saveMainTain() {
		if($J("#linkMan").val() == null || $J("#linkMan").val() == ''){
			MyAlert("联系人不能为空");
			return;
		}
		if($J("#tel").val() == null || $J("#tel").val() == ''){
			MyAlert("联系电话不能为空");
			return;
		}
		if($J("#mileage").val() == null || $J("#mileage").val() == ''){
			MyAlert("行驶里程不能为空");
			return;
		}
		if($J("#relief_mileage").val() == null || $J("#relief_mileage").val() == ''){
			MyAlert("单程救急里程(公里)不能为空");
			return;
		}
		if($J("#txt1").val() == null || $J("#txt1").val() == ''){
			MyAlert("救急所在省不能为空");
			return;
		}
		if($J("#txt2").val() == null || $J("#txt2").val() == ''){
			MyAlert("救急所在市不能为空");
			return;
		}
		if($J("#txt3").val() == null || $J("#txt3").val() == ''){
			MyAlert("救急所在县(区)不能为空");
			return;
		}
		if($J("#egress_reamrk").val() == null || $J("#egress_reamrk").val() == ''){
			MyAlert("申请内容不能为空");
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
<title>外出救援申请修改</title>
</head>
<body>
<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; 外出救援申请修改</font></div>
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
<%--         <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 发动机号：</td>
        <td align="left" id="engineNo">${info.ENGINE_NO }</td> --%>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车系名称：</td>
        <td align="left" id="seName">${info.SE_NAME }</td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 车型名称：</td>
        <td align="left" id="vehicleName">${info.GROUP_NAME }</td>
      </tr>
      
      <tr >    	
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 配置：</td>
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
         <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">客户名字：</td>
         <td align="left">
        	<input class="middle_txt" title="联系人" maxlength="50" name="linkMan" id="linkMan" value="${info.CUSTOMER_NAME }"/>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 客户电话：</td>
        <td align="left">
        	<input class="middle_txt" title="电话" maxlength="15" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" name="tel" id="tel" value="${info.TELEPHONE }"/>
        	<span style="color: red">*</span>
        </td>   
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 行驶里程：</td>
        <td align="left">
        	<input class="middle_txt" title="行驶里程" maxlength="8" onkeyup="vaildateNum(this)" onblur="vaildateNum(this)" name="mileage" id="mileage" value="${info.MILEAGE }"/>
        	<span style="color: red">*</span>
        </td>
      </tr>
          
      <tr >     	
        
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 单程救急里程(公里)：</td>
        <td align="left">
        	<input class="middle_txt" title="单程救急里程(公里)" maxlength="8" onblur="vaildateNum(this)" name="relief_mileage" id="relief_mileage" value="${info.RELIEF_MILEAGE }"/>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在省：</td>
         <td align="left">
        	<select class="middle_txt" style="width: 137px" id="txt1" name="province" onchange="_genCity(this,'txt2');"> </select>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在市：</td>
         <td align="left">
        	<select class="middle_txt" style="width: 137px" id="txt2" name="city" onchange="_genCity(this,'txt3')"> </select><span style="color: red">*</span>
        </td>
      </tr>
      
      <tr >       
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 救急所在县(区)：</td>
         <td align="left">
        	<select class="middle_txt" style="width: 137px" id="txt3" name="county"></select>
        	<span style="color: red">*</span>
        </td>
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">
				详细地址：</td>
			<td align="left">
				<input class="middle_txt" title="救急所在镇" maxlength="30" name="town" id="town" value="${info.TOWN }"/>
				<span style="color: red">*</span>
			</td>
       </tr>
      
      <tr >
        <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;"> 申请内容：</td>
        <td align="left" colspan="5">
        	<textarea style="width: 95%;height: 80px" name="egress_reamrk" id="egress_reamrk">${info.EGRESS_REAMRK }</textarea>
       		<span style="color:red;">*</span>
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