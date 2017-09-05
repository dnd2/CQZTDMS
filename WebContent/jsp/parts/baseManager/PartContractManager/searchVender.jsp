<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>备件信息</title>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
  <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	  基础信息管理 &gt; 计划相关信息维护 &gt; 供应商管理 &gt; 合同管理 &gt; 供应商签约合同查询</div>
 <form method="post" name = "fm" id="fm">
	 <%--<input type="hidden" name="VENDER_ID" id="VENDER_ID" value="${VENDER_ID}">--%>
	<div class="form-panel">
		<h2><img src="/CQZTDMS/jmstyle/img/search-ico.png" class="panel-query-title">供应商签约合同查询</h2>	
		<div class="form-body">
			<table class="table_query" border="0" >
				<tr>
					<td width="10%" align="right">合同类型：</td>
					<td width="20%">
						<%--<select id="SPCPD_TYPE" name="SPCPD_TYPE" onchange="changeType(this.value);" style="width:153px;">--%>
							<%--<option value='92481001' selected="selected">--请选择--</option>--%>
							<%--<option value='92481001' selected="selected">拆件</option>--%>
							<%--<option value='92481002'>合件</option>--%>
						<%--</select>--%>
						<script type="text/javascript">
							genSelBoxExp("CONTRACT_TYPE", <%=Constant.CONTRACT_TYPE%>, "", true, "short_sel u-select", "onChange=doCusChange(this.value)", "false", '');
						</script>
					</td>
					<td width="10%" align="right">供应商名称：</td>
					<td width="20%"><input class="middle_txt" type="text" name="VENDER_CODE" /></td>
					<td width="10%" align="right">备件名称：</td>
					<td width="20%"><input class="middle_txt" type="text" name="PART_NAME" /></td>
				</tr>
					<tr>
						<td width="10%" align="right">计划员：</td>
						<td width="20%">
							<input class="middle_txt" type="text" name="PLAN_NAME" id="PLAN_NAME" />
						</td>
						<td width="10%" align="right">采购员：</td>
						<td width="20%"><input class="middle_txt" type="text" name="BUY_NAME" id="BUY_NAME" /></td>
						<td width="10%" align="right">供应商类型：</td>
						<td width="20%">
							<script type="text/javascript">
							genSelBoxExp("VENDER_TYPE", <%=Constant.VENDER_TYPE%>, "", true, "short_sel u-select", "", 'false', '');
							</script>
						</td>
					</tr>
				<tr>
					<td class="center" align="center" colspan="6" >
						<input type="button" onclick="__extQuery__(1);" class="u-button u-query"  value="查询" name="BtnQuery" id="queryBtn" />
						<input type="button" onclick="exportPartStockExcel();" class="u-button"  value="导出" name="BtnQuery" id="queryBtn1" />
						<input type="button" value="返 回" class="u-button u-cancel" onclick="goBack();"/>
					</td>
				</tr>
				</table>
		</div>
	</div>  
    
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />  
</form>
</div>
<script type="text/javascript">
	var flag = true ;
	var url=null;

	var title = null;
	var columns=null;
	doCusChange(0);
	function doCusChange(obj){
		if(obj==<%=Constant.CONTRACT_TYPE_01%>){
			url="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/getVenderIsSignSim.json";
			columns = [
				{header: '序号', align: 'center', renderer: getIndex},
				{header: "供应商编码", sortable: false, dataIndex: 'VENDER_CODE', align: 'center'},
				{header: "供应商名称", sortable: false, dataIndex: 'VENDER_NAME', align: 'center',renderer:openPart},
				{header: "合同编号", sortable: false, dataIndex: 'CONTRACT_NUMBER', align: 'center'},
				{header: "合同开始日期", sortable: false, dataIndex: 'CONTRACT_SDATE', align: 'center'},
				{header: "合同结束日期", sortable: false, dataIndex: 'CONTRACT_EDATE', align: 'center'}
			];
		}else {
			url="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/getVenderIsSign.json";
			columns = [
				{header: '序号', align: 'center', renderer: getIndex},
				{header: "供应商编码", sortable: false, dataIndex: 'VENDER_CODE', align: 'center'},
				{header: "供应商名称", sortable: false, dataIndex: 'VENDER_NAME', align: 'center'},
				{header: "备件编码", sortable: false, dataIndex: 'PART_OLDCODE', align: 'center'},
				{header: "备件名称", sortable: false, dataIndex: 'PART_CNAME', align: 'center'},
				{header: "计划员", sortable: false, dataIndex: 'PLANNER_NAME', align: 'center'},
				{header: "采购员", sortable: false, dataIndex: 'BUY_NAME', align: 'center'},
				{header: "合同编号", sortable: false, dataIndex: 'CONTRACT_NUMBER', align: 'center'},
				{header: "合同开始日期", sortable: false, dataIndex: 'CONTRACT_SDATE', align: 'center'},
				{header: "合同结束日期", sortable: false, dataIndex: 'CONTRACT_EDATE', align: 'center'}
			];
		}
	}

	function openPart(value, meta, record){
		var venderId=record.data.VENDER_ID;
		var output = "<a href=\"#\"  onclick='PartDefine(\"" + venderId + "\")' >" + value + "</a>";
		return output;
	}
	function  PartDefine(obj){
		OpenHtmlWindow('<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partInit.do?VENDER_ID='+obj, 1100, 450);
	}

	function goBack(){
		location = '<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/partContractQueryInit.do';
	}

	//下载
	function exportPartStockExcel(){
		disableAllBtn();
		document.fm.action="<%=contextPath%>/parts/baseManager/PartContractManager/PartContractQuery/expPartContractExcel.do";
		document.fm.submit();
	}
	function disableAllBtn() {
		var inputArr = document.getElementsByTagName("input");
		for (var i = 0; i < inputArr.length; i++) {
			if (inputArr[i].type == "button") {
				inputArr[i].disabled = true;
			}
		}
	}
	function enableAllBtn() {
		var inputArr = document.getElementsByTagName("input");
		for (var i = 0; i < inputArr.length; i++) {
			if (inputArr[i].type == "button") {
				inputArr[i].disabled = false;
			}
		}
	}
</script>
</body>
</html>