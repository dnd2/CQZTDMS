<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@page import="com.infodms.dms.common.Constant"%>
<head> 
<%  
	String contextPath = request.getContextPath(); 
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="/BQYXDMS/js/jslib/calendar.js"></script>
<title>客户抱怨新增</title>
<script type="text/javascript">
	function getInfo(){
		document.getElementById("main_phone").value = "";
		document.getElementById("ctm_name").value = "";
		document.getElementById("deliverer_adress").value = "";
		document.getElementById("color").value = "";
		document.getElementById("car_use_desc").value = "";
		document.getElementById("guarantee_date").value = "";
		document.getElementById("package_name").value = "";
		document.getElementById("model_name").value = "";
		document.getElementById("product_date").value = "";
		document.getElementById("series_name").value = "";
		document.getElementById("brand_name").value = "";
		var vin=document.getElementById("vin").value;
		if(vin!=""){
			sendAjax('<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/showInfoByVin.json?vin='+vin,backByVin,'fm');
		}
	}	
	function sureInsert(){
		var wrgroup_id=document.getElementById("brand_name").value;
		var package_id=document.getElementById("package_name").value;
		var series_id=document.getElementById("series_name").value;
		var model_id=document.getElementById("model_name").value;
		var vin=document.getElementById("vin").value;
		if(""==wrgroup_id||""==package_id||""==series_id||""==model_id||""==vin){
			MyAlert("提示：请先输入VIN并确认是否信息正确带出！");
			return;
		}
		var create_by=document.getElementById("create_by").value;
		if(""==create_by){
			MyAlert("提示：请先输入登记人姓名！");
			return;
		}
		var createBy_tel=document.getElementById("createBy_tel").value;
		if(""==createBy_tel){
			MyAlert("提示：请先输入登记人电话！");
			return;
		}
		var time=document.getElementById("time").value;
		if(""==time){
			MyAlert("提示：请先输入登记时间！");
			return;
		}
		var address=document.getElementById("address").value;
		if(""==address){
			MyAlert("提示：请先输入地点！");
			return;
		}
		var GUZHANG_MIAOSU=document.getElementById("GUZHANG_MIAOSU").value;
		if(""==GUZHANG_MIAOSU){
			MyAlert("提示：请先输入故障描述！");
			return;
		}
		var url="<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/customerComplainInsert.do";
		fm.action = url;
		fm.submit();
	}
	function genjin(){
		var ss = "";
		var dd = new Date();   
		ss += dd.getYear()+"";   
		var month = dd.getMonth() + 1;
		if(month<10){
			ss += "0"+ month + "";  
		}else{
			ss += month + "";  
		}
		var date = dd.getDate();
		if(date<10){
			ss += "0"+dd.getDate();
		}else{
			ss += dd.getDate();
		}
		
		var time=document.getElementById("time").value;
		var replaceStr = "-";
		var dengji = time.replace(new RegExp(replaceStr,'gm'),'');
		var chuliTime1=document.getElementById("chuliTime1").value;
		var chuli = chuliTime1.replace(new RegExp(replaceStr,'gm'),'');
		if(""==chuliTime1){
			MyAlert("提示：请先输入处理时间！");
			return;
		}
		if(parseInt(chuli)<parseInt(dengji)){
			MyAlert("提示：处理时间不能小于登记时间！");
			return;
		}
		if(parseInt(chuli)>parseInt(ss)){
			MyAlert("提示：处理时间不能大于今天！");
			return;
		}
		var chuliren1=document.getElementById("chuliren1").value;
		if(""==chuliren1){
			MyAlert("提示：请先输入处理人！");
			return;
		}
		var chuli_jilu1=document.getElementById("chuli_jilu1").value;
		if(""==chuli_jilu1){
			MyAlert("提示：请先输入处理问题描述！");
			return;
		}
		var url="<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/genjin.do";
		fm.action = url;
		fm.submit();
	}	
	function bihuan(){
		var ss = "";
		var dd = new Date();   
		ss += dd.getYear()+"";   
		var month = dd.getMonth() + 1;
		if(month<10){
			ss += "0"+ month + "";  
		}else{
			ss += month + "";  
		} 
		var date = dd.getDate();
		if(date<10){
			ss += "0"+dd.getDate();
		}else{
			ss += dd.getDate();
		}
		var time=document.getElementById("time").value;
		var replaceStr = "-";
		var dengji = time.replace(new RegExp(replaceStr,'gm'),'');
		var chuliTime1=document.getElementById("chuliTime1").value;
		var chuli = chuliTime1.replace(new RegExp(replaceStr,'gm'),'');
		if(""==chuliTime1){
			MyAlert("提示：请先输入处理时间！");
			return;
		}
		if(parseInt(chuli)<parseInt(dengji)){
			MyAlert("提示：处理时间不能小于登记时间！");
			return;
		}
		if(parseInt(chuli)>parseInt(ss)){
			MyAlert("提示：处理时间不能大于今天！");
			return;
		}
		var chuliren1=document.getElementById("chuliren1").value;
		if(""==chuliren1){
			MyAlert("提示：请先输入处理人！");
			return;
		}
		var chuli_jilu1=document.getElementById("chuli_jilu1").value;
		if(""==chuli_jilu1){
			MyAlert("提示：请先输入处理问题描述！");
			return;
		}
		var url="<%=contextPath%>/customerRelationships/complaintConsult/CustomerComplain/bihuan.do";
		fm.action = url;
		fm.submit();
	}	
	//维修历史按钮方法
	function maintaimHistory(){
		var vin=document.getElementById("vin").value;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
	}
	//保养历史按钮方法
	function freeMaintainHistory(){
		var vin=document.getElementById("vin").value;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
	}
	//授权历史按钮方法
	function auditingHistory(){
		var vin=document.getElementById("vin").value;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/auditingHistory.do?VIN='+vin);
	}
	function backByVin(json){
			var t=json.info;
			if(json.info!="exist"){
				document.getElementById("main_phone").value = t.MAIN_PHONE;
				document.getElementById("ctm_name").value = t.CTM_NAME;
				document.getElementById("deliverer_adress").value = t.ADDRESS;
				document.getElementById("color").value = t.COLOR;
				document.getElementById("car_use_desc").value = t.CAR_USE_DESC;
				document.getElementById("guarantee_date").value = t.PURCHASED_DATE_ACT;
				document.getElementById("package_name").value = t.PACKAGE_NAME;
				document.getElementById("model_name").value = t.MODEL_NAME;
				document.getElementById("product_date").value = t.PRODUCT_DATE_ACT;
				document.getElementById("series_name").value = t.SERIES_NAME;
				document.getElementById("brand_name").value = t.BRAND_NAME;
			}else{
				MyAlert("该车有未闭环的数据");
			}

	}
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/jsp_new/img/nav.gif" />&nbsp;当前位置：投资咨询管理&gt;客户抱怨登记&gt;<c:if test="${isAdd == true }">新增客户抱怨记录</c:if>
<c:if test="${isCheck == true }">客户抱怨处理记录查看</c:if>
<c:if test="${isAdd == false }">跟进处理客户抱怨记录</c:if>
</div>
<form name="fm" id="fm" method="post">
<input id="id" value="${id}"  name="id" type="hidden"  />
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/jsp_new/img/subNav.gif" />用户信息
	</th>
	<tr>
		<td nowrap="true" width="10%" align="right">VIN:</td>
    	<c:if test="${isAdd == true }"><td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="vin" value="${complainPOS.vin }" onchange="getInfo();"  name="vin" type="text" maxlength="30" /><font color="red">*</font>
    	</td></c:if>
    	<c:if test="${isAdd != true }"><td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="vin" value="${complainPOS.vin }"   name="vin" readonly="readonly" type="text" maxlength="30" />
    	</td></c:if>
		<td nowrap="true" width="10%" align="right">车主姓名:</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="ctm_name" value="${complainPOS.chezhuXingming }" readonly="readonly" name="ctm_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" align="right">车主电话:</td>
    	<td nowrap="true" width="15%" align="left">
    		<input class="middle_txt" id="main_phone" value="${complainPOS.chezhuDianhua }" readonly="readonly" name="main_phone" type="text" maxlength="30" />
    	</td>
	</tr>
	<tr>
		<td nowrap="true" width="10%" align="right">车主地址:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="deliverer_adress" value="${complainPOS.chezhuDizhi }" readonly="readonly" name="deliverer_adress" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">品牌:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="brand_name" value="${complainPOS.pinpai }" readonly="readonly" name="brand_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">车系:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="series_name" value="${complainPOS.chexi }" readonly="readonly" name="series_name" type="text" maxlength="30" />
    	</td>
	</tr>
		<tr>
		<td nowrap="true" width="10%" align="right">车型:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="model_name" value="${complainPOS.chexing }" readonly="readonly" name="model_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">购车日期:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="guarantee_date" value="<fmt:formatDate value="${complainPOS.goucheRiqi }" pattern='yyyy-MM-dd'/>" readonly="readonly" name="guarantee_date" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">生产日期:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="product_date" value="<fmt:formatDate value="${complainPOS.shengchanRiqi }" pattern='yyyy-MM-dd'/>" readonly="readonly" name="product_date" type="text" maxlength="30" />
    	</td>
	</tr>
		<tr>
		<td nowrap="true" width="10%" align="right">配置:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="package_name" value="${complainPOS.peizhi }" readonly="readonly" name="package_name" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">车辆用途:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="car_use_desc" value="${complainPOS.cheliangYongtu }" readonly="readonly" name="car_use_desc" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" align="right">颜色:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<input class="middle_txt"  id="color" value="${complainPOS.color }" readonly="readonly" name="color" type="text" maxlength="30" />
    	</td>
	</tr>	
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/jsp_new/img/subNav.gif" />登记信息
	</th>
			<c:if test="${isAdd == true }"><tr>
				<td class="table_query_label" align="right">抱怨类型:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
					genSelBoxExp("COMPLAINT_TYPE",<%=Constant.COMPLAINT_TYPE%>, "${complainPOS.complaintType}", false,"short_sel", "", "false", '');
				</script><font color="red">*</font></td>
				<td class="table_query_label" align="right">抱怨等级:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
				genSelBoxExp("COMPLAINT_LEVEL",<%=Constant.COMPLAINT_LEVEL%>, "${complainPOS.complaintLevel}", false,"short_sel", "", "false", '');
				</script><font color="red">*</font></td>
			</tr></c:if>
						<c:if test="${isAdd != true }"><tr>
				<td class="table_query_label" align="right">抱怨类型:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
					genSelBoxExp("COMPLAINT_TYPE",<%=Constant.COMPLAINT_TYPE%>, "${complainPOS.complaintType}", false,"short_sel", "disabled", "false", '');
				</script></td>
				<td class="table_query_label" align="right">抱怨等级:</td>
				<td class="table_query_label" align="left"><script type="text/javascript">
				genSelBoxExp("COMPLAINT_LEVEL",<%=Constant.COMPLAINT_LEVEL%>, "${complainPOS.complaintLevel}", false,"short_sel", "disabled", "false", '');
				</script></td>
			</tr></c:if>
			<tr>
		<td nowrap="true" width="10%" align="right">登记人姓名:</td>
    	<td nowrap="true" width="15%" align="left" >
    	<c:if test="${isAdd == true }">
    		<input class="middle_txt"  id="create_by" value="${complainPOS.createBy }"  name="create_by" type="text" maxlength="30" /><font color="red">*</font></c:if>
    	<c:if test="${isAdd != true }">
    		<input class="middle_txt"  id="create_by" value="${complainPOS.createBy }" readonly="readonly" name="create_by" type="text" maxlength="30" /></c:if>
    	</td>
    	<td nowrap="true" width="10%" align="right">登记人电话:</td>
    	<td nowrap="true" width="15%" align="left" >
    		<c:if test="${isAdd == true }"><input class="middle_txt"  id="createBy_tel" value="${complainPOS.createbyTel }"  name="createBy_tel" type="text" maxlength="30" /><font color="red">*</font></c:if>
    		<c:if test="${isAdd != true }"><input class="middle_txt"  id="createBy_tel" value="${complainPOS.createbyTel }" readonly="readonly" name="createBy_tel" type="text" maxlength="30" /></c:if>
    	</td>
    	<td align="right" nowrap="true">登记时间:</td>
		<td align="left" nowrap="true">
		<c:if test="${isAdd == true }"><input class="short_txt" readonly="readonly" value="${now}" id="time" name="time" datatype="1,is_date,10" maxlength="10" /><font color="red">*</font>
		</c:if>
		<c:if test="${isAdd != true }"><input class="short_txt" readonly="readonly" value="<fmt:formatDate value="${complainPOS.time }" pattern='yyyy-MM-dd'/>" id="time" name="time" maxlength="10" /> 
		</c:if>
		</td>
	</tr>
	<tr>
		<td nowrap="true" width="10%" align="right">地点:</td>
		<c:if test="${isAdd == true }"><td colspan="8" nowrap="true" width="30%" align="left"><textarea name="address" id="address" rows="2" style="width: 55.25%;">${complainPOS.address }</textarea><font color="red">*</font></td></c:if>
		<c:if test="${isAdd != true }"><td colspan="8" nowrap="true" width="30%" align="left"><textarea name="address" readonly="readonly" id="address" rows="2" style="width: 55.25%;">${complainPOS.address }</textarea></td></c:if>
	</tr>
	<tr>
	<td nowrap="true" width="10%" align="right">故障描述:</td>
		<c:if test="${isAdd == true }"><td colspan="8" nowrap="true" width="30%" align="left">
			<textarea name="GUZHANG_MIAOSU" id="GUZHANG_MIAOSU" rows="5" style="width: 55.25%;">${complainPOS.guzhangMiaosu }</textarea><font color="red">*</font>
		</td></c:if>
		<c:if test="${isAdd != true }"><td colspan="8" nowrap="true" width="30%" align="left">
			<textarea name="GUZHANG_MIAOSU" readonly="readonly" id="GUZHANG_MIAOSU" rows="5" style="width: 55.25%;">${complainPOS.guzhangMiaosu }</textarea>
		</td></c:if>
	</tr>
	<c:if test="${isAdd == false }">
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/jsp_new/img/subNav.gif" />处理记录
	</th>
	<c:forEach items="${complainRecordPOS}" var="complainRecordPOS">
	<tr>
		<td nowrap="true" width="10%" align="right">处理时间:</td>
		<td align="left" nowrap="true"><input class="short_txt" readonly="readonly" value="<fmt:formatDate value="${complainRecordPOS.time }" pattern='yyyy-MM-dd'/>" id="chuliTime" name="chuliTime" datatype="1,is_date,10"
			maxlength="10" /> <input class="time_ico" value=" " onclick="showcalendar(event, 'chuliTime', false);" type="button" /></td>
		<td nowrap="true" width="10%" align="right">处理人:</td>
		<td nowrap="true" width="15%" align="left"><input class="middle_txt" value="${complainRecordPOS.createBy }" id="chuliren" name="chuliren" type="text" maxlength="30" /></td>
	</tr>
	<tr>
		<td nowrap="true" width="10%" align="right">处理问题描述:</td>
		<td colspan="8" nowrap="true" width="30%" align="left"><textarea name="chuli_jilu" id="chuli_jilu" rows="3" style="width: 55.25%;">${complainRecordPOS.chuliNeirong }</textarea></td>
	</tr>
	</c:forEach>
	<tr>
		<td nowrap="true" width="10%" align="right">处理时间:</td>
		<td align="left" nowrap="true"><input class="short_txt" readonly="readonly" id="chuliTime1" name="chuliTime1" datatype="1,is_date,10"
			maxlength="10" /> <input class="time_ico" value=" " onclick="showcalendar(event, 'chuliTime1', false);" type="button" /><font color="red">*</font></td>
		<td nowrap="true" width="10%" align="right">处理人:</td>
		<td nowrap="true" width="15%" align="left"><input class="middle_txt" id="chuliren1" name="chuliren1" type="text" maxlength="30" /><font color="red">*</font></td>
	</tr>
	<tr>
		<td nowrap="true" width="10%" align="right">处理问题描述:</td>
		<td colspan="8" nowrap="true" width="30%" align="left"><textarea name="chuli_jilu1" id="chuli_jilu1" rows="3" style="width: 55.25%;"></textarea><font color="red">*</font></td>
	</tr>
	</c:if>

	<c:if test="${isCheck == true }">
	<c:if test="${!empty complainRecordPOS}">
	<th colspan="8">
		<img class="nav" src="<%=contextPath%>/jsp_new/img/subNav.gif" />处理记录
	</th></c:if>
	<c:forEach items="${complainRecordPOS}" var="complainRecordPOS">
	<tr>
		<td nowrap="true" width="10%" align="right">处理时间:</td>
		<td align="left" nowrap="true"><input class="short_txt" readonly="readonly" value="<fmt:formatDate value="${complainRecordPOS.time }" pattern='yyyy-MM-dd'/>"  id="chuliTime" name="chuliTime" 
			maxlength="10" /> </td>
		<td nowrap="true" width="10%" align="right">处理人:</td>
		<td nowrap="true" width="15%" align="left"><input class="middle_txt" value="${complainRecordPOS.createBy }" readonly="readonly" id="chuliren" name="chuliren" type="text" maxlength="30" /></td>
	</tr>
	<tr>
		<td nowrap="true" width="10%" align="right">处理问题描述:</td>
		<td colspan="8" nowrap="true" width="30%" align="left"><textarea name="chuli_jilu" id="chuli_jilu" rows="3" readonly="readonly" style="width: 55.25%;">${complainRecordPOS.chuliNeirong }</textarea></td>
	</tr>
	</c:forEach>
	</c:if>
</table>
<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<c:if test="${isAdd == false }">
               	<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;&nbsp;
                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;&nbsp;
                <input class="normal_btn" type="button" id="btn3" value="授权历史" onclick="auditingHistory();"/>&nbsp;&nbsp;
                <input type="button" class="normal_btn" id="sure" onclick="genjin();"  style="width=8%" value="跟进" />&nbsp;&nbsp;
                <input type="button" class="normal_btn" id="sure" onclick="bihuan();"  style="width=8%" value="闭环" />&nbsp;&nbsp;
                </c:if>
                <c:if test="${isCheck == true }">
               	<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;&nbsp;
                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;&nbsp;
                <input class="normal_btn" type="button" id="btn3" value="授权历史" onclick="auditingHistory();"/>&nbsp;&nbsp;
                </c:if>
                <c:if test="${isAdd == true }">
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert();"  style="width=8%" value="确定" />&nbsp;&nbsp;
               	</c:if>
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>