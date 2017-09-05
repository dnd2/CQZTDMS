<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
    String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运输方式与计价维护-新增</title>
<script type="text/javascript" >
function add(){ 	
	if(checkData()==true){
		MyConfirm("确认添加该信息！",addTransportValua);
	}
}
function addTransportValua(){ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueAdd.json",addTransportValuaBack,'fm','queryBtn'); 
}
function addTransportValuaBack(json){
	MyAlert("===json.returnValue=="+json.returnValue);
	if(json.returnValue == 1){
		__parent().MyAlert("操作成功！");
		fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueInit.do";
		fm.submit();
	}else if(json.returnValue == 2){//添加失败 
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！该运输方式和计价方式已存在");
	}else{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}
}


function back(){
	fm.action="<%=contextPath%>/parts/baseManager/logisticsManage/TransportAndValueAction/transportAndValueInit.do";
	fm.submit();
}

function checkData(){
	var tvId=document.getElementById("tvId");/**序号*/
	var tvName=document.getElementById("tvName");/**名称*/
	var transportMode = document.getElementById("transportMode");/**运输方式*/
 	var valuationMode = document.getElementById("valuationMode");/**计价方式*/
 	var isStatus = document.getElementById("isStatus");/**是否有效*/
	if(tvName.value==null || tvName.value==""){
		MyAlert("名称不能为空！");
		return  false;
	}
	if(transportMode.value==null || transportMode.value==""){
		MyAlert("运输方式不能为空！");
		return  false;
	}
	if(valuationMode.value==null || valuationMode.value==""){
		MyAlert("计价方式不能为空！");
		return  false;
	}
	if(isStatus.value==null || isStatus.value==""){
		MyAlert("是否有效状态不能为空！");
		return  false;
	}
	return true;
}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" method="post" id="fm">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 运输方式与计价维护(新增)
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 运输方式与计价
				</h2>
				<div class="form-body">
					<table class="table_query" id="subtab">
						<tr>
							<td class="right">序号：</td>
							<td>
								<input id="tvId" name="tvId" type="text" value="${tvId}" readonly="readonly" class="middle_txt" />
							</td>
							<td class="right">名称：</td>
							<td>
								<input id="tvName" name="tvName" type="text" class="middle_txt" />
							</td>
						</tr>
						<tr>
							<td class="right">运输方式：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("transportMode",<%=Constant.TRANSPORT_MODE%>, "", true, "u-select", "", "false", '');
								</script>
							</td>
							<td class="right">计价方式：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("valuationMode",<%=Constant.VALUATION_MODE%>, "", true, "u-select", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">是否有效：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("isStatus",<%=Constant.STATUS%>, "", true, "u-select", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input type="button" name="button1" id="saveButton" class="u-button" onclick="add();" value="保存" />
								<input type="button" name="button2" id="goBack" class="u-button" onclick="back();" value="返回" />

							</td>
						</tr>
					</table>
				</div>
			</div>

			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>

</body>
</html>
