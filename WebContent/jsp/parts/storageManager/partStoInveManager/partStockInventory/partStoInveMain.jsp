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
<title>库存盘点</title>
<script type="text/javascript">
$(function(){__extQuery__(1);});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/stockInventorySearch.json";
var title = null;
var columns = [
			{header: "序号", dataIndex: 'CHANGE_ID', renderer:getIndex,align:'center'},
			{header: "盘点单号", dataIndex: 'CHANGE_CODE', align:'center'},
			{header: "盘点人", dataIndex: 'NAME', align:'center'},
//			{header: "盘点单位", dataIndex: 'CHGORG_CNAME', align:'center'},
			{header: "盘点日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "盘点类型", dataIndex: 'CHECK_TYPE', align:'center',renderer:getItemValue},
			{header: "盘点仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "盘点状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var changeId = record.data.CHANGE_ID;
	var invType = record.data.CHECK_TYPE;
	var state = record.data.STATE;
	var state1 = <%=Constant.PART_STOCK_INVE_STATE_01%>;//已保存
	var state2 = <%=Constant.PART_STOCK_INVE_STATE_02%>;//盘点中
	var state3 = <%=Constant.PART_STOCK_INVE_STATE_03%>;//已盘点
	var str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\",\""+invType+"\")'>[查看]</a>&nbsp;";
	if(state1 == state)
	{
		str = str + "<a href=\"#\" onclick='modifyDetail(\""+changeId+"\")'>[修改]</a>&nbsp;<a href=\"#\" onclick='startInventory(\""+changeId+"\",\""+invType+"\")'>[开始盘点]</a>&nbsp;<a href=\"#\" onclick='endInventory(\""+changeId+"\",\""+invType+"\")'>[结束盘点]</a>&nbsp;";
	}
	else if(state2 == state)
	{
		str = str + "<a href=\"#\" onclick='endInventory(\""+changeId+"\",\""+invType+"\")'>[结束盘点]</a>&nbsp;";
//			str = str + "<a href=\"#\" onclick='endInventory(\""+changeId+"\",\""+invType+"\")'>[结束盘点]</a>&nbsp;<a href=\"#\" onclick='prevInventory(\""+changeId+"\",\""+invType+"\")'>[初盘]</a>&nbsp;<a href=\"#\" onclick='againInventory(\""+changeId+"\",\""+invType+"\")'>[复盘]</a>";
	}
	
	str = str + "<a href=\"#\" onclick='expInvDetail(\""+changeId+"\",\""+invType+"\")'>[明细导出]</a>";
	
	return String.format(str);
}

//查看
function viewDetail(parms,invType){
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/viewStockDeatilInit.do?changeId=" + parms +"&invType=" + invType;
	document.fm.target="_self";
	document.fm.submit();
}

//修改
function modifyDetail(parms){
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/modifyStockDeatilInit.do?changeId=" + parms;
	document.fm.target="_self";
	document.fm.submit();
}

//新增
function addNew(){
	btnDisable();
	var actionURL = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/partStockAddInit.do";
	document.getElementById("actionURL").value = actionURL;
	document.fm.action = actionURL;
	document.fm.target = "_self";
	document.fm.submit();
}
    
//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/exportPartStockStatusExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}
//初盘
function prevInventory(parms, invType){
	var optType = "previous";
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/prevOrAginInveInit.do?changeId=" + parms + "&optType=" + optType +"&invType=" + invType;
	document.fm.target="_self";
	document.fm.submit();
}

//导出盘点明细
function expInvDetail(parms, invType){
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/expAgainInvExcel.do?changeId=" + parms  + "&invType=" + invType;
	document.fm.target="_self";
	document.fm.submit();
}

//复盘
function againInventory(parms, invType){
	var optType = "again";
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/prevOrAginInveInit.do?changeId=" + parms  + "&optType=" + optType + "&invType=" + invType;
	document.fm.target="_self";
	document.fm.submit();
}

//开始盘点
function startInventory(parms, invType)
{
	var optionType = "start";
	MyConfirm("确定开始盘点?",commitOrder,[[parms,optionType,invType]]);
}

//结束盘点
function endInventory(parms, invType)
{
	var optionType = "end";
	MyConfirm("确定结束盘点?",commitOrder,[[parms,optionType,invType]]);
}

function commitOrder(paramArr)
{
	var parms = paramArr[0];
	var optionType = paramArr[1];
	var invType = paramArr[2];
	btnDisable();
	var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/startOrEndStockInve.json?changeId="
				+ parms
				+ "&optionType="
				+ optionType
				+ "&invType="
				+ invType + "&curPage=" + myPage.page;
	sendAjax(url, getResult, 'fm');
}
function getResult(json) {
	btnEnable();
	if (null != json) {
		if (json.errorExist != null && json.errorExist.length > 0) {
			MyAlert(json.errorExist);
			__extQuery__(json.curPage);
		} else if (json.success != null && json.success == "true") {
			MyAlert("操作成功!");
			__extQuery__(json.curPage);
		} else {
			MyAlert("操作失败，请联系管理员!");
		}
	}
}

</script>
</head>
<body>
	<div class="wbox" style="min-width: 992px;">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件仓库管理 &gt;库存盘点管理&gt;库存盘点
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="actionURL" id="actionURL" value="" />
			</div>
			<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td width="10%" class="right">盘点单号：</td>
							<td width="25%">
								<input class="middle_txt" type="text" name="changeCode" id="changeCode" />
							</td>
							<td width="10%" class="right">制单日期：</td>
							<td width="25%">
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width:80px;"/>
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td width="10%" class="right">盘点人：</td>
							<td width="25%">
								<input class="middle_txt" type="text" name="inveName" id="inveName" />
							</td>
						</tr>
						<tr>
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
							<td class="right">盘点状态：</td>
							<td>
								<script type="text/javascript">
		        					genSelBoxExp("inveState",<%=Constant.PART_STOCK_INVE_STATE%>,"",true,"","","false","");
		  						</script>
							</td>
							<td class="right">盘点类型：</td>
							<td>
								<script type="text/javascript">
		        					genSelBoxExp("inveType",<%=Constant.PART_STOCK_INVE_TYPE%>,"",true,"","","false","");
			  					</script>
							</td>
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