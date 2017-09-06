<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件库存状态变更</title>

<script type="text/javascript" >
$(function(){__extQuery__(1);});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockSettingSearch.json";
var title = null;
var columns = [
			{header: "序号", dataIndex: 'CHANGE_ID', renderer:getIndex,align:'center'},
            {id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
			{header: "变更单号", dataIndex: 'CHANGE_CODE', align:'center'},
			{header: "制单单位", dataIndex: 'CHGORG_CNAME', align:'center'},
			{header: "仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "制单人", dataIndex: 'NAME', align:'center'},
			{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "备注", dataIndex: 'REMARK', style:'text-align: center;'},
			{header: "完成状态", dataIndex: 'STATE',renderer:getState}
	      ];

//设置超链接
function myLink(value,meta,record){
	var changeId = record.data.CHANGE_ID;
	var state = record.data.STATE;
	var unfiState = "1";
	if(unfiState == state)
	{
		return String.format("<a href=\"#\" onclick='viewDetail(\""+changeId+"\",\"view\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='viewDetail(\""+changeId+"\",\"handle\")'>[处理]</a>");
	}
	return String.format("<a href=\"#\" onclick='viewDetail(\""+changeId+"\",\"view\")'>[查看]</a>");
}

function getState(value,meta,record){
	var unfiState = "1";
	if(unfiState == value){
		return String.format("未完成");
	}
	return String.format("<font color='red'>已完成</font>");
	
}

//查看
function viewDetail(parms, option){
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/viewStockDeatilInint.do?changeId=" + parms + "&option=" + option;
	document.fm.target="_self";
	document.fm.submit();
}

//新增
function addNew(){
	var parentOrgId = document.getElementById("parentOrgId").value;
	var companyName = document.getElementById("companyName").value;
	btnDisable();
	var actionURL = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/partStockAddInit.do";
	document.getElementById("actionURL").value = actionURL;
	document.fm.action = actionURL;
	document.fm.target = "_self";
	document.fm.submit();
}
    
//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStaSetManager/partStockSettingAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}
</script>
</head>
<body>
<div class="wbox">
<form method="post" name ="fm" id="fm">
<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
<input type="hidden" name="companyName" id="companyName" value="${companyName }"/>
<input type="hidden" name="actionURL" id="actionURL" value=""/>
<div class="navigation">
	<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件仓库管理  &gt;配件状态变更&gt;配件库存状态变更
</div>
<div class="form-panel">
<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
<div class="form-body">
	<table class="table_query">
		<tr>
			<td class="right">变更单号：</td>
			<td><input class="middle_txt" type="text"
				name="changeCode" id="changeCode" /></td>
			<td class="right">制单日期：</td>
			<td><input id="checkSDate" class="short_txt"
				name="checkSDate" datatype="1,is_date,10" maxlength="10"
				group="checkSDate,checkEDate" style="width:80px;" /> <input class="time_ico"  value=" "
				type="button" /> &nbsp;至&nbsp; <input id="checkEDate"
				class="short_txt" name="checkEDate" datatype="1,is_date,10"
				maxlength="10" group="checkSDate,checkEDate"  style="width:80px;"/> <input
				class="time_ico"  value=" "
				type="button" />
			</td>
			<td class="right">仓库：</td>
			<td>
				<select name="whId" id="whId" class="u-select" onchange="__extQuery__(1)">
					<option value="">-请选择-</option>
					<c:if test="${WHList!=null}">
						<c:forEach items="${WHList}" var="list">
							<option value="${list.WH_ID }">${list.WH_CNAME }</option>
						</c:forEach>
					</c:if>
				</select>
			</td>
		</tr>
		<tr>
			<td class="right">备注：</td>
			<td><input class="middle_txt" type="text" name="remark" id="remark" /></td>
			<td class="right">完成状态：</td>
			<td>
				<select name="isFinish" id="isFinish" class="u-select" onchange="__extQuery__(1)">
					<option value="">-请选择-</option>
					<option value="1" selected="selected">-未完成-</option>
					<option value="0">-已完成-</option>
				</select>
			</td>
			<td class="right">配件编码：</td>
			<td><input class="middle_txt" type="text" name="partOldcode" id="partOldcode" /></td>
		</tr>
		<tr>
			<td class="right">制单人：</td>
			<td><input class="middle_txt" type="text" name="maker" id="maker" value="${maker }" /></td>
			<td class="right"></td>
			<td></td>
			<td class="right"></td>
			<td></td>
		</tr>
		<tr>
			<td class="center" colspan="6">
				<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
				<input class="u-button" type="reset" value="重 置" onclick="reset()" />
				<input class="u-button" type="button" value="新 增" onclick="addNew()" />
				<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
			</td>
		</tr>
	</table>
</div>
</div>

<!-- 查询条件 end -->
<!--分页 begin -->
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</div>
</body>
</html>