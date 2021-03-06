<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>库存盘点调整审核</title>
<script type="text/javascript" >
$(function(){__extQuery__(1);});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/invReqCheckAction/invReqCheckSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'RESULT_ID', renderer:getIndex,align:'center'},
			{header: "盘点单号", dataIndex: 'CHANGE_CODE', align:'center'},
			{header: "申请单号", dataIndex: 'RESULT_CODE', align:'center'},
			{header: "盘点类型", dataIndex: 'CHECK_TYPE', align:'center',renderer:getItemValue},
			{header: "盘点仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "导入人", dataIndex: 'IMP_NAME', align:'center'},
			{header: "导入日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "提交人", dataIndex: 'COMM_NAME', align:'center'},
			{header: "提交日期", dataIndex: 'COMMIT_DATE', align:'center'},
			{header: "单据状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var resultId = record.data.RESULT_ID;
	var str = "<a href=\"#\" onclick='viewDetail(\""+resultId+"\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='viewHandle(\""+resultId+"\")'>[审核]</a>"

	return String.format(str);
}

//查看
function viewDetail(parms){
	document.getElementById("resultId").value = parms;
	document.getElementById("optionType").value = "view";
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/invReqCheckAction/viewStockDeatilInit.do";
	document.fm.target="_self";
	document.fm.submit();
}

//审核
function viewHandle(parms){
	document.getElementById("resultId").value = parms;
	document.getElementById("optionType").value = "handle";
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/invReqCheckAction/viewStockDeatilInit.do";
	document.fm.target="_self";
	document.fm.submit();
}
    
//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/invReqCheckAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

</script>

</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件仓库管理 &gt;库存盘点管理&gt;库存盘点调整审核
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="resultId" id="resultId" value="" />
				<input type="hidden" name="optionType" id="optionType" value="" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">盘点单号：</td>
							<td>
								<input class="middle_txt" type="text" name="inveCode" id="inveCode" />
							</td>
							<td class="right">盘点类型：</td>
							<td >
								<script type="text/javascript">
									genSelBoxExp("inveType", <%=Constant.PART_STOCK_INVE_TYPE%>, "", true, "", "", "false", "");
								</script>
							</td>
							<td class="right">盘点仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
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
							<td class="right">申请单号：</td>
							<td>
								<input class="middle_txt" type="text" name="resultCode" id="resultCode" />
							</td>
							<td class="right">提交日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">提交人：</td>
							<td>
								<input class="middle_txt" type="text" name="improterName" id="improterName" />
							</td>
						</tr>
						<c:if test="${oemId eq parentOrgId}">
							<tr>
								<td class="center" colspan="6">
									<input type="radio" name="RADIO_SELECT" value="1" checked />
									本部盘点审核&nbsp;&nbsp;
									<input type="radio" name="RADIO_SELECT" value="2" />
									服务商&供应中心盘点
								</td>
							</tr>
						</c:if>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置">
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